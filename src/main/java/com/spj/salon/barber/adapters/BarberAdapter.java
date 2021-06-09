package com.spj.salon.barber.adapters;

import com.spj.salon.barber.entities.BarberCalendar;
import com.spj.salon.barber.entities.BarberServicesMapping;
import com.spj.salon.barber.entities.DailyBarbers;
import com.spj.salon.barber.entities.Services;
import com.spj.salon.barber.ports.in.IBarberAdapter;
import com.spj.salon.barber.repository.ServicesRepository;
import com.spj.salon.openapi.resources.*;
import com.spj.salon.user.entities.User;
import com.spj.salon.user.ports.out.OAuthClient;
import com.spj.salon.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

/**
 * @author Yogesh Sharma
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BarberAdapter implements IBarberAdapter {

    private final UserRepository userRepository;
    private final ServicesRepository serviceRepo;
    private final BarberAdapterMapper facadeMapper;
    private final ServicesRepository servicesRepo;
    private final OAuthClient oAuthClient;

    /**
     * This method will be called every morning by Barber to provide how many barbers are available today.
     * This can be called again if at the later time some barbers are unavailable,
     * the code will read the latest record of barber count available on the current day
     */
    @Override
    public DailyBarbersResponse addBarbersCountToday(DailyBarbersRequest dailyBarbersRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();

        DailyBarbers dalBarbers = facadeMapper.fromRequest(dailyBarbersRequest);
        log.info("Barber found in jwt with email {}", email);
        User user = userRepository.findByEmail(email);
        if (user != null) {
            log.info("Barber found for email {}", email);
            user.getDailyBarberSet().add(dalBarbers);
            userRepository.saveAndFlush(user);
            log.info("Barber count saved into DB {}", email);
            return new DailyBarbersResponse()
                    .barbersCount(dalBarbers.getBarbersCount())
                    .actionResult("Barbers count added");
        }
        log.error("No Barber found for email {}", email);
        return new DailyBarbersResponse()
                .barbersCount(0)
                .actionResult("No Barbers added");
    }

    /**
     * This method add services provide by barbers like haircut etc.
     */
    @Override
    public BarberServicesResponse addServices(long serviceId, int cost, int time) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();
        log.info("Barber found in jwt with email {}", email);

        User user = userRepository.findByEmail(email);

        Optional<Services> servicesOpt = serviceRepo.findById(serviceId);
        if (servicesOpt.isPresent()) {
            log.info("Services found in DB with email {}", email);
            user.getBarberServicesMappingSet()
                    .add(BarberServicesMapping.builder()
                            .userId(user.getUserId())
                            .serviceId(serviceId)
                            .serviceCharges(cost)
                            .timeToPerform(time)
                            .build());

            log.info("Adding new service DB for barber with email {}", email);

            userRepository.saveAndFlush(user);
            return new BarberServicesResponse().serviceName(servicesOpt.get().getServiceName())
                    .message("Service added to account -> " + user.getEmail());
        }

        return new BarberServicesResponse().message("Data is not saved");
    }

    /**
     * This method will add barber calendar. The barber can send his daily calendar like wednesday from 10 to 4.
     * He can also send calendar with date and mentions his hours.
     */
    @Override
    public BarberCalendarResponse addBarberCalendar(BarberCalendarRequest barberCalendarRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();
        log.info("Barber found in jwt with email {}", email);

        BarberCalendar barberCalendar = facadeMapper.toDomain(barberCalendarRequest);
        User user = userRepository.findByEmail(email);

        if (user != null) {
            Set<BarberCalendar> barberCalendarSet = user.getBarberCalendarSet();
            barberCalendar.setUserId(user.getUserId());
            barberCalendarSet.add(barberCalendar);

            userRepository.saveAndFlush(user);
            log.info("Barber cal is updated with email {}", email);
            return new BarberCalendarResponse().email(email).message("Calendar entry added to database");
        }

        log.error("No Barber found to update cal with email {}", email);
        return new BarberCalendarResponse().email(email).message("Calendar entry failed to save");
    }

    @Override
    public BarberServicesResponse addService(BarberServicesRequest barberServicesRequest) {
        Services services = facadeMapper.toDomain(barberServicesRequest);
        servicesRepo.saveAndFlush(services);
        log.info("Service saved");

        return new BarberServicesResponse().message("service added");
    }

    @Override
    public AuthenticationResponse getJwtToken(AuthenticationRequest authenticationRequest, String clientHeader) {
        return oAuthClient.getAuthenticationData(authenticationRequest.getEmail().toLowerCase(),
                authenticationRequest.getPassword(), clientHeader, true);
    }

    @Override
    public AuthenticationResponse getRefreshToken(RefreshRequest refreshRequest, String clientHeader) {
        return oAuthClient.getRefreshToken(refreshRequest.getRefreshToken(), refreshRequest.getEmail(), clientHeader, true);
    }
}
