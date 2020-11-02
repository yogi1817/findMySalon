package com.spj.salon.customer.messaging;

import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding(value = {UserRegisterSource.class})
public class UserRegisterBinder {
}
