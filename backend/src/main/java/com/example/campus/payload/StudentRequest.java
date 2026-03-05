package com.example.campus.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StudentRequest {
    private String username;
    private String password;
    private String name;
    private String email;
    @JsonProperty("student_no")
    private String studentNo;
    private String grade;
    @JsonProperty("class_name")
    private String className;
}
