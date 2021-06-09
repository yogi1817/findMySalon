package com.spj.salon.customer.adapters;

import com.spj.salon.customer.ports.in.ICustomerAdapter;
import com.spj.salon.openapi.resources.AuthenticationRequest;
import com.spj.salon.openapi.resources.AuthenticationResponse;
import com.spj.salon.openapi.resources.CustomerFavouriteBarberResponse;
import com.spj.salon.openapi.resources.RefreshRequest;
import com.spj.salon.user.entities.User;
import com.spj.salon.user.ports.out.OAuthClient;
import com.spj.salon.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * @author Yogesh Sharma
 */
@Service
@Slf4j
@AllArgsConstructor
public class CustomerAdapter implements ICustomerAdapter {
    private final UserRepository userRepository;
    private final OAuthClient oAuthClient;

    @Override
    public CustomerFavouriteBarberResponse addFavouriteSalon(Long barberId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();

        log.info("User found in jwt with email {}", email);
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setFavouriteSalonId(barberId);
            //TODO: Add org.postgresql.util.PSQLException exception
            userRepository.saveAndFlush(user);
            return new CustomerFavouriteBarberResponse().name(user.getFirstName()).
                    message("Favourite barber record added");
        }

        log.error("User not found in DB with email {}", email);
        return new CustomerFavouriteBarberResponse().message("Unable to save data");
    }

    @Override
    public AuthenticationResponse getJwtToken(AuthenticationRequest authenticationRequest, String clientHeader) {
        return oAuthClient.getAuthenticationData(authenticationRequest.getEmail().toLowerCase(),
                authenticationRequest.getPassword(), clientHeader, false);
    }

    @Override
    public AuthenticationResponse getRefreshToken(RefreshRequest refreshRequest, String clientHeader) {
        return oAuthClient.getRefreshToken(refreshRequest.getRefreshToken(), refreshRequest.getEmail(), clientHeader, false);
    }
}
