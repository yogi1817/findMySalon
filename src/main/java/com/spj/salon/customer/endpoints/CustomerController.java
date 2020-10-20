package com.spj.salon.customer.endpoints;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.spj.salon.customer.ports.in.ICustomerAdapter;
import com.spj.salon.customer.ports.in.IRegisterCustomer;
import com.spj.salon.openapi.endpoint.CustomerApiDelegate;
import com.spj.salon.openapi.resources.AuthenticationRequest;
import com.spj.salon.openapi.resources.AuthenticationResponse;
import com.spj.salon.openapi.resources.CustomerFavouriteBarberResponse;
import com.spj.salon.openapi.resources.RegisterCustomerRequest;
import com.spj.salon.openapi.resources.RegisterCustomerResponse;
import com.spj.salon.openapi.resources.UpdatePasswordRequest;
import com.spj.salon.openapi.resources.UpdatePasswordResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Yogesh Sharma
 */
@Controller
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class CustomerController implements CustomerApiDelegate {

    private final ICustomerAdapter customerAdapter;
    private final IRegisterCustomer registerCustomer;

    @Override
    public ResponseEntity<RegisterCustomerResponse> registerCustomer(RegisterCustomerRequest registerCustomerRequest, Optional<String> clientHost) {
        log.info("Inside CustomerController registerBarber service");
        return ResponseEntity.ok(registerCustomer.registerCustomer(registerCustomerRequest)
                .message("Member registered successfully"));
    }

    @Override
    public ResponseEntity<CustomerFavouriteBarberResponse> customerFavourite(Long barberId) {
        log.info("Inside UserController addFavouriteSalon service");
        return ResponseEntity.ok(customerAdapter.addFavouriteSalon(barberId));
    }

    @Override
    public ResponseEntity<AuthenticationResponse> authenticateCustomer(AuthenticationRequest authenticationRequest, Optional<String> clientHost) {
        log.info("Inside UserController authenticate service");
        return ResponseEntity.ok(customerAdapter.getJwtToken(authenticationRequest, clientHost.orElse(null)));
    }

    @Override
    public ResponseEntity<UpdatePasswordResponse> updatePassword(UpdatePasswordRequest updatePasswordRequest, Optional<String> clientHost) {
        log.info("Inside UserController authenticate service");
        return ResponseEntity.ok(customerAdapter.updatePassword(updatePasswordRequest, clientHost.orElse(null)));
    }
}
