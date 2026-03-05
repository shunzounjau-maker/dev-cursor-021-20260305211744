package com.example.campus.repository;

import com.example.campus.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUserId(Long userId);
    Optional<Student> findByStudentNo(String studentNo);
}
