package com.spj.salon.customer.pojo;

import lombok.Data;

@Data
public class RegisterUserResponse {
  private Boolean registered;
  private String email;
}

