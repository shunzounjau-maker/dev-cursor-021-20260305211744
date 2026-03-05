package com.example.campus.controller;

import com.example.campus.domain.*;
import com.example.campus.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/student")
@PreAuthorize("hasRole('STUDENT')")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseClassRepository courseClassRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private UserRepository userRepository;

    private Student getCurrentStudent(UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return studentRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    @GetMapping("/classes")
    public List<CourseClass> getAvailableClasses() {
        // In a real app, filter by semester and exclude already enrolled
        return courseClassRepository.findAll();
    }

    @PostMapping("/enroll/{classId}")
    public ResponseEntity<?> enrollClass(@PathVariable Long classId, @AuthenticationPrincipal UserDetails userDetails) {
        Student student = getCurrentStudent(userDetails);
        CourseClass courseClass = courseClassRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found"));

        // Check if already enrolled
        if (enrollmentRepository.findByStudentIdAndCourseClassId(student.getId(), classId).isPresent()) {
            return ResponseEntity.badRequest().body("Already enrolled in this class");
        }

        // Check capacity
        long enrolledCount = enrollmentRepository.countByCourseClassId(classId);
        if (enrolledCount >= courseClass.getCapacity()) {
            return ResponseEntity.badRequest().body("Class is full");
        }

        // Check credit limit (simplified: assume 30 max)
        List<Enrollment> myEnrollments = enrollmentRepository.findByStudentId(student.getId());
        int currentCredits = myEnrollments.stream()
                .mapToInt(e -> e.getCourseClass().getCourse().getCredits())
                .sum();
        
        if (currentCredits + courseClass.getCourse().getCredits() > 30) {
            return ResponseEntity.badRequest().body("Credit limit exceeded");
        }

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .courseClass(courseClass)
                .build();
        
        return ResponseEntity.ok(enrollmentRepository.save(enrollment));
    }

    @DeleteMapping("/drop/{classId}")
    public ResponseEntity<?> dropClass(@PathVariable Long classId, @AuthenticationPrincipal UserDetails userDetails) {
        Student student = getCurrentStudent(userDetails);
        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseClassId(student.getId(), classId)
                .orElseThrow(() -> new RuntimeException("Not enrolled in this class"));

        // Check if grade exists (cannot drop if graded)
        if (gradeRepository.findByEnrollmentId(enrollment.getId()).isPresent()) {
            return ResponseEntity.badRequest().body("Cannot drop class with grade");
        }

        enrollmentRepository.delete(enrollment);
        return ResponseEntity.ok("Dropped successfully");
    }

    @GetMapping("/schedule")
    public List<Enrollment> getMySchedule(@AuthenticationPrincipal UserDetails userDetails) {
        Student student = getCurrentStudent(userDetails);
        return enrollmentRepository.findByStudentId(student.getId());
    }

    @GetMapping("/grades")
    public List<Grade> getMyGrades(@AuthenticationPrincipal UserDetails userDetails) {
        Student student = getCurrentStudent(userDetails);
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(student.getId());
        
        return enrollments.stream()
                .map(e -> gradeRepository.findByEnrollmentId(e.getId()).orElse(null))
                .filter(g -> g != null)
                .collect(Collectors.toList());
    }
}
