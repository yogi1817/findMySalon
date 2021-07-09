package com.spj.salon.user.ports.in;

import com.spj.salon.openapi.resources.RegisterBarberRequest;
import com.spj.salon.openapi.resources.RegisterBarberResponse;
import org.apache.http.client.HttpResponseException;

public interface IRegisterBarber {
    RegisterBarberResponse registerBarber(RegisterBarberRequest registerBarberRequest) throws HttpResponseException;
}
