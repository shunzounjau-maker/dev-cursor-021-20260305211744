package com.example.campus.payload;

import com.example.campus.domain.Student;
import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class StudentGradeDTO {
    private Student student;
    private Integer score;
}
