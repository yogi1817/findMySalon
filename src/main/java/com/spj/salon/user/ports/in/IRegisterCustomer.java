package com.spj.salon.user.ports.in;

import com.spj.salon.openapi.resources.RegisterCustomerRequest;
import com.spj.salon.openapi.resources.RegisterCustomerResponse;
import org.apache.http.client.HttpResponseException;

public interface IRegisterCustomer {
    RegisterCustomerResponse registerCustomer(RegisterCustomerRequest registerBarberRequest) throws HttpResponseException;
}
