package com.example.campus.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TeacherRequest {
    private String username;
    private String password;
    private String name;
    private String email;
    @JsonProperty("staff_no")
    private String staffNo;
    private String department;
}
