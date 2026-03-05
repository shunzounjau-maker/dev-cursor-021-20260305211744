package com.example.campus.controller;

import com.example.campus.domain.*;
import com.example.campus.payload.GradeRequest;
import com.example.campus.payload.StudentGradeDTO;
import com.example.campus.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/teacher")
@PreAuthorize("hasRole('TEACHER')")
public class TeacherController {

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private CourseClassRepository courseClassRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private UserRepository userRepository;

    private Teacher getCurrentTeacher(UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return teacherRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
    }

    @GetMapping("/classes")
    public List<CourseClass> getMyClasses(@AuthenticationPrincipal UserDetails userDetails) {
        Teacher teacher = getCurrentTeacher(userDetails);
        return courseClassRepository.findByTeacherId(teacher.getId());
    }

    @GetMapping("/classes/{classId}/students")
    public ResponseEntity<?> getClassStudents(@PathVariable Long classId, @AuthenticationPrincipal UserDetails userDetails) {
        Teacher teacher = getCurrentTeacher(userDetails);
        CourseClass courseClass = courseClassRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found"));

        if (!courseClass.getTeacher().getId().equals(teacher.getId())) {
            return ResponseEntity.status(403).body("Not authorized to view this class");
        }

        List<Enrollment> enrollments = enrollmentRepository.findByCourseClassId(classId);
        
        List<StudentGradeDTO> dtos = enrollments.stream().map(e -> {
            Integer score = gradeRepository.findByEnrollmentId(e.getId())
                    .map(Grade::getScore)
                    .orElse(null);
            return StudentGradeDTO.builder()
                    .student(e.getStudent())
                    .score(score)
                    .build();
        }).collect(java.util.stream.Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/classes/{classId}/grades")
    public ResponseEntity<?> enterGrade(@PathVariable Long classId, @RequestBody GradeRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        Teacher teacher = getCurrentTeacher(userDetails);
        CourseClass courseClass = courseClassRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found"));

        if (!courseClass.getTeacher().getId().equals(teacher.getId())) {
            return ResponseEntity.status(403).body("Not authorized to grade this class");
        }

        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseClassId(request.getStudentId(), classId)
                .orElseThrow(() -> new RuntimeException("Student not enrolled in this class"));

        Grade grade = gradeRepository.findByEnrollmentId(enrollment.getId())
                .orElse(Grade.builder().enrollment(enrollment).build());
        
        grade.setScore(request.getScore());
        return ResponseEntity.ok(gradeRepository.save(grade));
    }

    @PutMapping("/grades/{gradeId}")
    public ResponseEntity<?> updateGrade(@PathVariable Long gradeId, @RequestBody Map<String, Integer> request, @AuthenticationPrincipal UserDetails userDetails) {
        Teacher teacher = getCurrentTeacher(userDetails);
        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new RuntimeException("Grade not found"));
        
        CourseClass courseClass = grade.getEnrollment().getCourseClass();
        if (!courseClass.getTeacher().getId().equals(teacher.getId())) {
            return ResponseEntity.status(403).body("Not authorized to update this grade");
        }

        grade.setScore(request.get("score"));
        return ResponseEntity.ok(gradeRepository.save(grade));
    }
}
