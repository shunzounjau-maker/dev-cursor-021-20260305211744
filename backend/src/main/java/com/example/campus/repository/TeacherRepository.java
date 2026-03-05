package com.example.campus.repository;

import com.example.campus.domain.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByUserId(Long userId);
    Optional<Teacher> findByStaffNo(String staffNo);
}
