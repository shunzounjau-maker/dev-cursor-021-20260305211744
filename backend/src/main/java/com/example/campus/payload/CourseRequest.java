package com.example.campus.payload;

import lombok.Data;

@Data
public class CourseRequest {
    private String code;
    private String name;
    private Integer credits;
    private String description;
}
