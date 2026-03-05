package com.example.campus.repository;

import com.example.campus.domain.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    Optional<Grade> findByEnrollmentId(Long enrollmentId);
}
