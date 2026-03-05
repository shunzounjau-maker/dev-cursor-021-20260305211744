package com.example.campus.payload;

import lombok.Data;

@Data
public class GradeRequest {
    private Long studentId;
    private Integer score;
}
