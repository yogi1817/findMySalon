package com.spj.salon.user.messaging;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface UserRegisterSource {
    String OUTPUT = "user-registered-out";

    @Output(OUTPUT)
    MessageChannel output();
}
