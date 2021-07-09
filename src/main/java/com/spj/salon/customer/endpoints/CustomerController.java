package com.spj.salon.customer.endpoints;

import com.spj.salon.customer.ports.in.ICustomerAdapter;
import com.spj.salon.openapi.endpoint.CustomerApiDelegate;
import com.spj.salon.openapi.resources.*;
import com.spj.salon.user.ports.in.IRegisterCustomer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

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

    @SneakyThrows
    @Override
    public ResponseEntity<RegisterCustomerResponse> registerCustomer(RegisterCustomerRequest registerCustomerRequest) {
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
        return ResponseEntity.ok(customerAdapter.getJwtToken(authenticationRequest, clientHost.orElse(null)));
    }

    @Override
    public ResponseEntity<AuthenticationResponse> refreshCustomer(RefreshRequest refreshRequest, Optional<String> clientHost) {
        return ResponseEntity.ok(customerAdapter.getRefreshToken(refreshRequest, clientHost.orElse(null)));
    }
}
