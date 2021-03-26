package com.spj.salon.user.endpoints;

import com.spj.salon.openapi.endpoint.UserApiDelegate;
import com.spj.salon.openapi.resources.*;
import com.spj.salon.user.ports.in.IUserAdapter;
import lombok.RequiredArgsConstructor;
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
@Slf4j
@RequiredArgsConstructor
public class UserController implements UserApiDelegate {
    private final IUserAdapter userAdapter;

    @Override
    public ResponseEntity<UserProfile> getUserProfile(Optional<Long> barberId, Optional<Long> customerId) {
        return ResponseEntity.ok(userAdapter.getUserProfile(barberId, customerId));
    }

    @Override
    public ResponseEntity<UserAddressResponse> addUserAddress(UserAddressRequest userAddressRequest) {
        return ResponseEntity.ok(userAdapter.addBarberAddress(userAddressRequest));
    }

    @Override
    public ResponseEntity<AuthenticationResponse> authenticateUser(AuthenticationRequest authenticationRequest, Optional<String> clientHost) {
        return ResponseEntity.ok(userAdapter.getJwtToken(authenticationRequest, clientHost.orElse(null)));
    }

    @Override
    public ResponseEntity<AuthenticationResponse> refreshUser(RefreshRequest refreshRequest, Optional<String> clientHost) {
        return ResponseEntity.ok(userAdapter.getRefreshToken(refreshRequest, clientHost.orElse(null)));
    }
}
