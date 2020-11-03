package com.spj.salon.customer.messaging;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

@Builder(toBuilder = true)
@AllArgsConstructor
@Value
@JsonDeserialize(builder = UserRegisterPayload.UserRegisterPayloadBuilder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRegisterPayload implements Serializable {
    String email;
    String password;
    Long authorityId;
    boolean updatePasswordRequest;

    @JsonPOJOBuilder(withPrefix = "")
    public static class UserRegisterPayloadBuilder{

    }
}
