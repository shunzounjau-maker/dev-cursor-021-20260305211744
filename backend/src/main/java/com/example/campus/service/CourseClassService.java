package com.example.campus.service;

import com.example.campus.domain.CourseClass;
import com.example.campus.repository.CourseClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseClassService {

    @Autowired
    private CourseClassRepository courseClassRepository;

    public List<CourseClass> getAllClasses() {
        return courseClassRepository.findAll();
    }

    public Optional<CourseClass> getClassById(Long id) {
        return courseClassRepository.findById(id);
    }

    public CourseClass saveClass(CourseClass courseClass) {
        return courseClassRepository.save(courseClass);
    }

    public void deleteClass(Long id) {
        courseClassRepository.deleteById(id);
    }
}
