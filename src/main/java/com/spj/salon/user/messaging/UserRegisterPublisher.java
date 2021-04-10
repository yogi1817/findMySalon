package com.spj.salon.user.messaging;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class UserRegisterPublisher {
    private final UserRegisterSource userRegisterSource;

    public void sendUserRegisterDetails(UserRegisterPayload userRegisterPayload) {
        userRegisterSource.output().send(MessageBuilder.withPayload(userRegisterPayload).build());
    }
}
