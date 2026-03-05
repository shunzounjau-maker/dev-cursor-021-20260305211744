package com.example.campus.payload;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String name;
    private String email;
    private String password; // Optional
}
