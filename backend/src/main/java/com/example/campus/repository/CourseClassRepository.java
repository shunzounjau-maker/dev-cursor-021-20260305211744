package com.example.campus.repository;

import com.example.campus.domain.CourseClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseClassRepository extends JpaRepository<CourseClass, Long> {
    List<CourseClass> findByTeacherId(Long teacherId);
    List<CourseClass> findBySemester(String semester);
}
