package com.example.campus.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private CourseClass courseClass;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime enrolledAt;
}
