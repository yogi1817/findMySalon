package com.spj.salon.user.messaging;

import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding(value = {UserRegisterSource.class})
public class UserRegisterBinder {
}
