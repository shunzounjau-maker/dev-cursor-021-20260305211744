package com.example.campus.payload;

import lombok.Data;

@Data
public class ClassRequest {
    private String courseCode;
    private String teacherStaffNo;
    private String semester;
    private String schedule;
    private Integer capacity;
}
