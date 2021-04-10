package com.spj.salon.user.messaging;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.io.Serial;
import java.io.Serializable;

@Builder(toBuilder = true)
@AllArgsConstructor
@Value
@JsonDeserialize(builder = UserRegisterPayload.UserRegisterPayloadBuilder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRegisterPayload implements Serializable {
    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 1L;
    String email;
    String password;
    Long authorityId;
    boolean updatePasswordRequest;

    @JsonPOJOBuilder(withPrefix = "")
    public static class UserRegisterPayloadBuilder {

    }
}
