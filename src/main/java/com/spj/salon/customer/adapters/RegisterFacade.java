package com.spj.salon.customer.adapters;

import com.spj.salon.barber.entities.Authorities;
import com.spj.salon.barber.ports.in.IRegisterBarber;
import com.spj.salon.barber.repository.AuthoritiesRepository;
import com.spj.salon.customer.dao.IUserDao;
import com.spj.salon.customer.entities.User;
import com.spj.salon.customer.messaging.UserRegisterPublisher;
import com.spj.salon.customer.messaging.UserRegisterPayload;
import com.spj.salon.customer.ports.in.IRegisterCustomer;
import com.spj.salon.customer.repository.UserRepository;
import com.spj.salon.openapi.resources.RegisterBarberRequest;
import com.spj.salon.openapi.resources.RegisterBarberResponse;
import com.spj.salon.openapi.resources.RegisterCustomerRequest;
import com.spj.salon.openapi.resources.RegisterCustomerResponse;
import com.spj.salon.otp.adapters.MyEmailAdapter;
import com.spj.salon.security.pojo.UserType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegisterFacade implements IRegisterCustomer, IRegisterBarber {

    private final IUserDao userDao;
    private final AuthoritiesRepository authoritiesRepository;
    private final UserRegisterPublisher userRegisterPublisher;
    private final UserRepository userRepository;
    private final MyEmailAdapter myEmailAdapter;
    private final RegisterMapper registerMapper;

    /**
     * This method registers all types of users available in userType
     */
    private User register(User user, UserType userType, String password) {
        if (CollectionUtils.isEmpty(userDao.searchUserWithEmailAndAuthority(user.getEmail(), userType.getResponse()))) {

            Authorities auth = authoritiesRepository.findByAuthority(userType.getResponse());
            user.setAuthorityId(auth.getAuthorityId());

            userRegisterPublisher.sendUserRegisterDetails(UserRegisterPayload.builder()
                    .email(user.getEmail())
                    .password(password)
                    .authorityId(auth.getAuthorityId())
                    .updatePasswordRequest(false)
                    .build());

            userRepository.saveAndFlush(user);

            log.debug("User registered sucessfully {}", user.getEmail());
            CompletableFuture.runAsync(() -> myEmailAdapter.sendOtpMessage(user.getEmail()));

            //user.setJwtToken(oAuthClient.getJwtToken(userCopy.getEmail(),userCopy.getPassword(), clientHost));

            log.info("Call completed successfully for user {}", user.getEmail());
            return user;
        }

        log.error("User Already Exists for email {}", user.getEmail());
        throw new DuplicateKeyException("User already Exists");
    }

    @Override
    public RegisterCustomerResponse registerCustomer(RegisterCustomerRequest registerCustomerRequest) {
        return registerMapper.toResponse(
                register(registerMapper.toEntity(registerCustomerRequest),
                        UserType.CUSTOMER, registerCustomerRequest.getPassword()));
    }

    @Override
    public RegisterBarberResponse registerBarber(RegisterBarberRequest registerBarberRequest) {
        return registerMapper.toBarberResponse(
                register(registerMapper.toEntity(registerBarberRequest),
                        UserType.BARBER, registerBarberRequest.getPassword()));
    }
}
