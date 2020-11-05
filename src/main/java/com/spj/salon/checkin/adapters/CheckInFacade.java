package com.spj.salon.checkin.adapters;

import java.io.IOException;
import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.naming.ServiceUnavailableException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.maps.model.GeocodingResult;
import com.spj.salon.barber.entities.Address;
import com.spj.salon.barber.entities.BarberCalendar;
import com.spj.salon.barber.entities.DailyBarbers;
import com.spj.salon.barber.entities.ZipCodeLookup;
import com.spj.salon.barber.repository.AddressRepository;
import com.spj.salon.barber.repository.ZipCodeRepository;
import com.spj.salon.checkin.entities.CheckIn;
import com.spj.salon.checkin.ports.out.GeoCoding;
import com.spj.salon.checkin.repository.CheckInRepository;
import com.spj.salon.customer.entities.User;
import com.spj.salon.customer.repository.UserRepository;
import com.spj.salon.openapi.resources.BarberDetails;
import com.spj.salon.openapi.resources.BarberWaitTimeRequest;
import com.spj.salon.openapi.resources.BarberWaitTimeResponse;
import com.spj.salon.openapi.resources.BarbersWaitTimeResponse;
import com.spj.salon.openapi.resources.CustomerCheckInResponse;
import com.spj.salon.openapi.resources.CustomerCheckoutResponse;
import com.spj.salon.utils.DateUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Yogesh Sharma
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CheckInFacade implements ICheckinFacade {

    private final UserRepository userRepository;
    private final CheckInRepository checkInRepository;
    private final ZipCodeRepository zipCodeRepo;
    //private GeoApiContext context;
    private final AddressRepository addressRepo;
    private final GeoCoding googleGeoCodingClient;

    @Override
    public BarberWaitTimeResponse waitTimeEstimate(long barberId) {
        User barber = userRepository.findByUserId(barberId);
        if(barber!=null){
            List<BarberCalendar> todaysCal = barber.getBarberCalendarSet()
                    .stream()
                    .filter(this::getTodaysBarberCal)
                    .collect(Collectors.toList());

            DailyBarbers todaysBarber = barber.getDailyBarberSet()
                    .stream()
                    .filter(a -> a.getBarbersCount() > 0)
                    .findFirst()
                    .orElse(new DailyBarbers());

            long noOfCheckIns = barber.getCheckInSet()
                    .stream()
                    .filter(a -> !a.isCheckedOut())
                    .count();

            return new BarberWaitTimeResponse().waitTime(getEstimateWaitTime(todaysCal, todaysBarber, noOfCheckIns))
                    .salonName(barber.getStoreName());
        }
        return new BarberWaitTimeResponse().waitTime("Unable to find wait-time");
    }

    @Override
    public BarberWaitTimeResponse waitTimeEstimateAtBarberForCustomerInOauthHeader() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();

        User user = userRepository.findByEmail(email);
        if(user.getFavouriteSalonId()==null){
            return new BarberWaitTimeResponse().salonName("No Favourite Salon Found");
        }

        List<CheckIn> checkInList = checkInRepository
                                        .findByUserMappingIdAndCheckedOut(user.getUserId(), false);
        //TODO: Test it
        // If someone is already checkin find his remaining time
        if(!CollectionUtils.isEmpty(checkInList)){
            CheckIn checkIn = checkInList.stream().findFirst().get();
            return new BarberWaitTimeResponse()
                    .waitTime(""+findTimeLeft(checkIn.getEta(), checkIn.getCreateTimestamp()))
                    .salonName("Already checked in");
        }
        
        return waitTimeEstimate(userRepository.findByEmail(email).getFavouriteSalonId());
    }

    private long findTimeLeft(int eta, OffsetDateTime createTimestamp) {
        OffsetDateTime stop = LocalDateTime.now().atOffset( ZoneOffset.UTC );
        Duration d = Duration.between(createTimestamp, stop).minusMinutes(eta);
        return (d.getSeconds()/60);
    }

    /**
     * This method will calculate the checkin time available for a given service
     *
     * @param todaysCal
     * @param todaysBarberCount
     * @param noOfCheckIns
     * @return
     */
    private String getEstimateWaitTime(List<BarberCalendar> todaysCal, DailyBarbers todaysBarberCount,
                                       long noOfCheckIns) {

        if (CollectionUtils.isEmpty(todaysCal)) {
            return "Barber Calender not set for today";
        }
        //If any record for today date exists then its a holiday
        else if (todaysCal.stream().anyMatch(a -> DateUtils.isTodayDate(a.getCalendarDate()))) {
            return "Barber closed";
        }

        if (todaysBarberCount.getBarbersCount() == 0) {
            return "No Barbers available at this time";
        }

        long waitTime = (noOfCheckIns / todaysBarberCount.getBarbersCount()) * 15;
        return waitTime + "";
    }

    /**
     * This method will find out todays working hours  y filtering on todays date
     * It will also check if it a special day like 4th July
     *
     * @param barberCalendar
     * @return
     */
    private boolean getTodaysBarberCal(BarberCalendar barberCalendar) {
        if (DateUtils.getTodaysDay().equals(barberCalendar.getCalendarDay())) {
            return true;
        }
        return DateUtils.isTodayDate(barberCalendar.getCalendarDate());
    }

    @Override
    public CustomerCheckInResponse checkInCustomerByCustomer(Optional<Long> barberIdOpt) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();
        User customer = userRepository.findByEmail(email);
        Long barberId = null;

        if(barberIdOpt.isPresent()){
            barberId = barberIdOpt.get();
        }else if(customer.getFavouriteSalonId()!=null){
            barberId = customer.getFavouriteSalonId();
        }else{
            return new CustomerCheckInResponse().message("No Favourite Salon Found");
        }

        User barber = userRepository.findByUserId(barberId);
        return checkin(customer, barber);
    }

    @Override
    public CustomerCheckInResponse checkInCustomerByBarber(long userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();
        User barber = userRepository.findByEmail(email);

        User customer = userRepository.findByUserId(userId);

        return checkin(customer, barber);
    }

    private CustomerCheckInResponse checkin(User customer, User barber) {
        String waitTimeEstimate = waitTimeEstimate(barber.getUserId()).getWaitTime();

        try {
            int waitTime = Integer.parseInt(waitTimeEstimate);
            if (checkInRepository.countByUserMappingId(customer.getUserId()) > 0) {
                return new CustomerCheckInResponse().message("Customer is already checkedIn")
                        .customerId(customer.getEmail());
            }

            CheckIn checkIn = new CheckIn(barber.getUserId(), customer.getUserId(), waitTime);
            checkInRepository.saveAndFlush(checkIn);
            return new CustomerCheckInResponse().message("Check in with waitTime " + waitTime)
                    .customerId(customer.getEmail());
        } catch (NumberFormatException ne) {
            return new CustomerCheckInResponse().message("Unable to Checkin")
                    .customerId(customer.getEmail());
        }
    }

    @Override
    public CustomerCheckoutResponse checkOut(long userId) {
        checkInRepository.findByUserMappingIdAndCheckedOut(userId, false)
                .stream()
                .peek(this::checkOut);

        return new CustomerCheckoutResponse().message("Customer has been checked out");
    }

    private void checkOut(CheckIn checkIn) {
        checkIn.setCheckedOut(true);
        checkInRepository.saveAndFlush(checkIn);
    }

    /**
     * @throws ServiceUnavailableException
     */
    @Override
    public BarbersWaitTimeResponse findBarbersAtZip(BarberWaitTimeRequest barberCheckInRequest) {
        double longitude;
        double latitude;

        if (barberCheckInRequest.getZipCode() == null &&
                barberCheckInRequest.getLatitude() != null &&
                barberCheckInRequest.getLongitude() != null) {

            longitude = barberCheckInRequest.getLongitude();
            latitude = barberCheckInRequest.getLatitude();
        } else {
            longitude = 0;
            latitude = 0;

            ZipCodeLookup zipCodeLookUp = zipCodeRepo.findByZipCode(Long.parseLong(barberCheckInRequest.getZipCode()));
            if (zipCodeLookUp != null) {
                longitude = zipCodeLookUp.getLongitude();
                latitude = zipCodeLookUp.getLatitude();
            }
        }

        if (longitude == 0 || latitude == 0) {
			/*try {
				//This is how we do it using goodle api
				GeocodingResult[] results = GeocodingApi.geocode(context, zipCode).await();
				*/
            try {
                GeocodingResult[] results = googleGeoCodingClient.findGeocodingResult(barberCheckInRequest.getZipCode());
                longitude = results[0].geometry.location.lng;
                latitude = results[0].geometry.location.lat;

                //This will update zip code table async
                CompletableFuture.runAsync(() -> saveZipCode(barberCheckInRequest.getZipCode(), results));
            } catch (IOException e) {
                e.printStackTrace();
                new BarbersWaitTimeResponse().message("Google service unavailable");
            }

            log.debug("longitude " + longitude);
            log.debug("latitude " + latitude);
        }

        return findBarbersWithinXMiles(longitude, latitude, barberCheckInRequest.getDistance());
    }

    /**
     * 1° of latitude ~= 69 miles
     * 1° of longitude ~= cos(latitude)*69
     *
     * @param longitude
     * @param latitude
     * @param distance
     */
    private BarbersWaitTimeResponse findBarbersWithinXMiles(double longitude, double latitude, Double distance) {
        BarberDetails barberDetails;
        BarbersWaitTimeResponse barbersWaitTimeResponse = new BarbersWaitTimeResponse();

        double long1 = longitude - distance / Math.abs(Math.cos(Math.toRadians(latitude)) * 69);
        double long2 = longitude + distance / Math.abs(Math.cos(Math.toRadians(latitude)) * 69);
        double lat1 = latitude - (distance / 69);
        double lat2 = latitude + (distance / 69);

        log.debug("long1 --> {}, long2 --> {}, lat1 --> {}. lat2 --> {}", long1, long2, lat1, lat2);

        log.debug("longitude --> {}, latitude --> {}", longitude, latitude);
        List<Map<String, Object>> addressIds =
                addressRepo.getBarbersId(longitude, latitude, distance, long1, long2, lat1, lat2);

        for (Map<String, Object> map : addressIds) {
            Optional<Address> barbersAddress
                    = addressRepo.findById(((BigInteger) map.get("address_id")).longValue());
            if (barbersAddress.isPresent()) {
                Optional<User> user = userRepository.findById(barbersAddress.get().getUserId());
                barberDetails = new BarberDetails()
                        .addressLineOne(barbersAddress.get().getAddressLineOne())
                        .addressLineTwo(barbersAddress.get().getAddressLineTwo())
                        .city(barbersAddress.get().getCity())
                        .state(barbersAddress.get().getState())
                        .zip(barbersAddress.get().getZip())
                        .email(user.get().getEmail())
                        .firstName(user.get().getFirstName())
                        .lastName(user.get().getLastName())
                        .middleName(user.get().getMiddleName())
                        .phone(user.get().getPhone())
                        .storeName(user.get().getStoreName())
                        .distance((Double) map.get("distance"))
                        .longitude(longitude)
                        .latitude(latitude)
                        .waitTime(waitTimeEstimate(barbersAddress.get().getUserId()).getWaitTime());

                barbersWaitTimeResponse.addBarberDetailsItem(barberDetails);
            }
        }

        if (CollectionUtils.isEmpty(barbersWaitTimeResponse.getBarberDetails())) {
            barbersWaitTimeResponse.setMessage("No Barbers Found within the range");
        } else {
            barbersWaitTimeResponse.setMessage(barbersWaitTimeResponse.getBarberDetails().size() + " Barbers Found");
        }
        return barbersWaitTimeResponse;
    }

    /**
     * Save zip code from google api to lookuptable
     *
     * @param results
     */
    private void saveZipCode(String zipCode, GeocodingResult[] results) {
        ZipCodeLookup zipCodeLookup = new ZipCodeLookup();
        zipCodeLookup.setZipCode(Long.parseLong(zipCode));
        zipCodeLookup.setLatitude(results[0].geometry.location.lat);
        zipCodeLookup.setLongitude(results[0].geometry.location.lng);

        zipCodeRepo.saveAndFlush(zipCodeLookup);
    }


}
