/*
package com.spj.salon.customer.adapters;

import com.google.gson.Gson;
import com.spj.salon.configs.ServiceConfig;
import com.spj.salon.customer.messaging.OAuthUserSyncPayload;
import com.spj.salon.customer.pojo.RegisterUserResponse;
import com.spj.salon.customer.ports.out.IOAuthSyncUserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class OAuthSyncUserClient implements IOAuthSyncUserClient {

    private final RestTemplate restTemplate;
    private final ServiceConfig serviceConfig;

    @Override
    public boolean syncAuthUser(OAuthUserSyncPayload authUserSync) {
        ResponseEntity<RegisterUserResponse> responseEntityStr = restTemplate.
                exchange(serviceConfig.getOauthRegisterUrl(), HttpMethod.POST, getHeader(authUserSync), RegisterUserResponse.class);

        if(responseEntityStr.getStatusCode().is2xxSuccessful())
            return responseEntityStr.getBody().getRegistered();
        else
            return false;
    }

    @Override
    public boolean updatePassword(OAuthUserSyncPayload authUserSync) {
        ResponseEntity<RegisterUserResponse> responseEntityStr = restTemplate.
                exchange(serviceConfig.getOauthRegisterUrl(), HttpMethod.POST, getHeader(authUserSync), RegisterUserResponse.class);

        if(responseEntityStr.getStatusCode().is2xxSuccessful())
            return responseEntityStr.getBody().getRegistered();
        else
            return false;
    }

    private HttpEntity<String> getHeader(Object objectToJson){
        Gson gson = new Gson();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(gson.toJson(objectToJson), headers);
    }
}
*/
