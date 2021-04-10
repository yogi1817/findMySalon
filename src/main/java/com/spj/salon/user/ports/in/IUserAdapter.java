package com.spj.salon.user.ports.in;

import com.spj.salon.openapi.resources.*;
import com.spj.salon.user.entities.User;

import java.util.Optional;

public interface IUserAdapter {
    UserProfile getUserProfile(Optional<Long> barberId, Optional<Long> customerId);

    UserAddressResponse addBarberAddress(UserAddressRequest userAddressRequest);

    User getLoggedInUserDetails();

    AuthenticationResponse getJwtToken(AuthenticationRequest authenticationRequest, String clientHeader);

    AuthenticationResponse getRefreshToken(RefreshRequest refreshRequest, String clientHeader);

    UpdatePasswordResponse updatePassword(UpdatePasswordRequest updatePasswordRequest);
}
