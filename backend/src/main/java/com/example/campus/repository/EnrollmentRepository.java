package com.example.campus.repository;

import com.example.campus.domain.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudentId(Long studentId);
    List<Enrollment> findByCourseClassId(Long courseClassId);
    long countByCourseClassId(Long courseClassId);
    Optional<Enrollment> findByStudentIdAndCourseClassId(Long studentId, Long courseClassId);
}
