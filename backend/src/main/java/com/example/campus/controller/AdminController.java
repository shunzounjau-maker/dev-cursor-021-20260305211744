package com.example.campus.controller;

import com.example.campus.domain.*;
import com.example.campus.payload.*;
import com.example.campus.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseClassRepository courseClassRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // --- Teachers ---

    @GetMapping("/teachers")
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    @PostMapping("/teachers")
    public ResponseEntity<?> createTeacher(@RequestBody TeacherRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(Role.TEACHER)
                .name(request.getName())
                .email(request.getEmail())
                .build();
        
        Teacher teacher = Teacher.builder()
                .user(user)
                .staffNo(request.getStaffNo())
                .department(request.getDepartment())
                .build();

        return ResponseEntity.ok(teacherRepository.save(teacher));
    }

    // --- Students ---

    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @PostMapping("/students")
    public ResponseEntity<?> createStudent(@RequestBody StudentRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(Role.STUDENT)
                .name(request.getName())
                .email(request.getEmail())
                .build();
        
        Student student = Student.builder()
                .user(user)
                .studentNo(request.getStudentNo())
                .grade(request.getGrade())
                .className(request.getClassName())
                .build();

        return ResponseEntity.ok(studentRepository.save(student));
    }

    // --- Courses ---

    @GetMapping("/courses")
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @PostMapping("/courses")
    public ResponseEntity<?> createCourse(@RequestBody CourseRequest request) {
        if (courseRepository.findByCode(request.getCode()).isPresent()) {
            return ResponseEntity.badRequest().body("Course code already exists");
        }

        Course course = Course.builder()
                .code(request.getCode())
                .name(request.getName())
                .credits(request.getCredits())
                .description(request.getDescription())
                .build();

        return ResponseEntity.ok(courseRepository.save(course));
    }

    // --- Classes ---

    @GetMapping("/classes")
    public List<CourseClass> getAllClasses() {
        return courseClassRepository.findAll();
    }

    @PostMapping("/classes")
    public ResponseEntity<?> createClass(@RequestBody ClassRequest request) {
        Course course = courseRepository.findByCode(request.getCourseCode())
                .orElseThrow(() -> new RuntimeException("Course not found"));
        
        Teacher teacher = teacherRepository.findByStaffNo(request.getTeacherStaffNo())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        CourseClass courseClass = CourseClass.builder()
                .course(course)
                .teacher(teacher)
                .semester(request.getSemester())
                .schedule(request.getSchedule())
                .capacity(request.getCapacity())
                .build();

        return ResponseEntity.ok(courseClassRepository.save(courseClass));
    }

    // --- Dashboard ---

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboardStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("studentCount", studentRepository.count());
        stats.put("teacherCount", teacherRepository.count());
        stats.put("courseCount", courseRepository.count());
        stats.put("classCount", courseClassRepository.count());
        return ResponseEntity.ok(stats);
    }
}
