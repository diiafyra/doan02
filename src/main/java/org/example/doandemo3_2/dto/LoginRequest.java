package org.example.doandemo3_2.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {
    private String Email;
    private String password;
}