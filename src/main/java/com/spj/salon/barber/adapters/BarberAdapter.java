package com.spj.salon.barber.adapters;

import com.google.maps.model.GeocodingResult;
import com.spj.salon.barber.entities.Address;
import com.spj.salon.barber.entities.BarberCalendar;
import com.spj.salon.barber.entities.BarberServicesMapping;
import com.spj.salon.barber.entities.DailyBarbers;
import com.spj.salon.barber.ports.in.IBarberAdapter;
import com.spj.salon.client.GoogleGeoCodingClient;
import com.spj.salon.customer.model.User;
import com.spj.salon.customer.repository.UserRepository;
import com.spj.salon.exception.NotFoundCustomException;
import com.spj.salon.openapi.resources.*;
import com.spj.salon.services.model.Services;
import com.spj.salon.services.repository.ServicesRepository;
import com.spj.salon.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
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
    private final GoogleGeoCodingClient googleGeoCodingClient;
    private final BarberAdapterMapper facadeMapper;

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
        BarberServicesMapping barberServicesMapping = null;
        if (servicesOpt.isPresent()) {
            log.info("Services found in DB with email {}", email);
            Set<BarberServicesMapping> barberServicesMappingsSet = user.getBarberServicesMappingSet();
            barberServicesMapping = new BarberServicesMapping();
            barberServicesMapping.setUserId(user.getUserId());
            barberServicesMapping.setServiceId(serviceId);
            barberServicesMapping.setServiceCharges(cost);
            barberServicesMapping.setTimeToPerform(time);
            barberServicesMapping.setCreateDate(DateUtils.getTodaysDate());

            log.info("Adding new service DB for barber with email {} and services {}", email, barberServicesMapping);
            barberServicesMappingsSet.add(barberServicesMapping);
            user.setBarberServicesMappingSet(barberServicesMappingsSet);

            userRepository.saveAndFlush(user);

            return new BarberServicesResponse().serviceName("Service with service id " + serviceId)
                    .message("Data saved");
        }

        return new BarberServicesResponse().message("Data is not saved");
    }

    /**
     * This method will add barber calendar. The barber can send his daily calander like wednesday from 10 to 4.
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

    /**
     * This method add the barber address in address table. It also add longi and lati into the table using geolocation api
     */
    @Override
    public BarberAddressResponse addBarberAddress(BarberAddressRequest barberAddressRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();
        log.info("Barber found in jwt with email {}", email);

        User barber = userRepository.findByEmail(email);

        if (barber == null) {
            log.error("Barber not found in DB with email {}", email);
            throw new NotFoundCustomException("Barber not found", email);
        }

        Address address = facadeMapper.toDomain(barberAddressRequest);
        try {
            /*
             * GeocodingResult[] results = GeocodingApi.geocode(context,
             * address.getAddress()).await();
             */

            GeocodingResult[] results = googleGeoCodingClient
                    .findGeocodingResult(URLEncoder.encode(address.getAddress(), "UTF-8"));

            validateAddress(address, barber.getUserId(), results);
            Set<Address> barberAddresses = barber.getAddressSet();
            barberAddresses.add(address);
            barber.setAddressSet(barberAddresses);

            userRepository.saveAndFlush(barber);

            return new BarberAddressResponse().email(email).message("Address saved to DB");
        } catch (IOException e1) {
            return new BarberAddressResponse().email(email).message(e1.getMessage());
        }
    }

    /**
     * @param address
     * @param barberId
     * @param results
     */
    private void validateAddress(Address address, Long barberId, GeocodingResult[] results) {
        address.setUserId(barberId);
        address.setCreateDate(DateUtils.getTodaysDate());
        address.setModifyDate(DateUtils.getTodaysDate());
        log.debug("longitude " + results[0].geometry.location.lng);
        log.debug("latitude " + results[0].geometry.location.lat);
        address.setLongitude(results[0].geometry.location.lng);
        address.setLatitude(results[0].geometry.location.lat);
    }
}