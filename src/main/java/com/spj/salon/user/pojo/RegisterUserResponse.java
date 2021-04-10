package com.spj.salon.user.pojo;

import lombok.Data;

@Data
public class RegisterUserResponse {
    private Boolean registered;
    private String email;
}

