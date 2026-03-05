package com.example.campus.config;

import com.example.campus.domain.*;
import com.example.campus.repository.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class DataSeeder implements CommandLineRunner {

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

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void run(String... args) throws Exception {
        seedUsers();
        seedTeachers();
        seedStudents();
        seedCourses();
        seedClasses();
    }

    private void seedUsers() throws IOException {
        if (userRepository.count() > 0) return;

        File file = new File("/workspace/data/021/seed/users.json");
        if (!file.exists()) return;

        List<Map<String, String>> users = objectMapper.readValue(file, new TypeReference<>() {});
        for (Map<String, String> u : users) {
            User user = User.builder()
                    .username(u.get("username"))
                    .passwordHash(passwordEncoder.encode(u.get("password")))
                    .role(Role.valueOf(u.get("role").toUpperCase()))
                    .name(u.get("name"))
                    .email(u.get("email"))
                    .build();
            userRepository.save(user);
        }
    }

    private void seedTeachers() throws IOException {
        if (teacherRepository.count() > 0) return;

        File file = new File("/workspace/data/021/seed/teachers.json");
        if (!file.exists()) return;

        List<Map<String, String>> teachers = objectMapper.readValue(file, new TypeReference<>() {});
        for (Map<String, String> t : teachers) {
            User user = userRepository.findByUsername(t.get("username"))
                    .orElseThrow(() -> new RuntimeException("User not found: " + t.get("username")));
            
            Teacher teacher = Teacher.builder()
                    .user(user)
                    .staffNo(t.get("staff_no"))
                    .department(t.get("department"))
                    .build();
            teacherRepository.save(teacher);
        }
    }

    private void seedStudents() throws IOException {
        if (studentRepository.count() > 0) return;

        File file = new File("/workspace/data/021/seed/students.json");
        if (!file.exists()) return;

        List<Map<String, String>> students = objectMapper.readValue(file, new TypeReference<>() {});
        for (Map<String, String> s : students) {
            User user = userRepository.findByUsername(s.get("username"))
                    .orElseThrow(() -> new RuntimeException("User not found: " + s.get("username")));

            Student student = Student.builder()
                    .user(user)
                    .studentNo(s.get("student_no"))
                    .grade(s.get("grade"))
                    .className(s.get("class_name"))
                    .build();
            studentRepository.save(student);
        }
    }

    private void seedCourses() throws IOException {
        if (courseRepository.count() > 0) return;

        File file = new File("/workspace/data/021/seed/courses.json");
        if (!file.exists()) return;

        List<Map<String, Object>> courses = objectMapper.readValue(file, new TypeReference<>() {});
        for (Map<String, Object> c : courses) {
            Course course = Course.builder()
                    .code((String) c.get("code"))
                    .name((String) c.get("name"))
                    .credits((Integer) c.get("credits"))
                    .description((String) c.get("description"))
                    .build();
            courseRepository.save(course);
        }
    }

    private void seedClasses() throws IOException {
        if (courseClassRepository.count() > 0) return;

        File file = new File("/workspace/data/021/seed/classes.json");
        if (!file.exists()) return;

        List<Map<String, Object>> classes = objectMapper.readValue(file, new TypeReference<>() {});
        for (Map<String, Object> c : classes) {
            Course course = courseRepository.findByCode((String) c.get("course_code"))
                    .orElseThrow(() -> new RuntimeException("Course not found: " + c.get("course_code")));
            
            Teacher teacher = teacherRepository.findByStaffNo((String) c.get("teacher_staff_no"))
                    .orElseThrow(() -> new RuntimeException("Teacher not found: " + c.get("teacher_staff_no")));

            CourseClass courseClass = CourseClass.builder()
                    .course(course)
                    .teacher(teacher)
                    .semester((String) c.get("semester"))
                    .schedule((String) c.get("schedule"))
                    .capacity((Integer) c.get("capacity"))
                    .build();
            courseClassRepository.save(courseClass);
        }
    }
}
