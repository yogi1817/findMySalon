package com.spj.salon.checkin.adapters;

import com.google.maps.model.GeocodingResult;
import com.spj.salon.barber.entities.Address;
import com.spj.salon.barber.entities.BarberCalendar;
import com.spj.salon.barber.entities.DailyBarbers;
import com.spj.salon.barber.entities.ZipCodeLookup;
import com.spj.salon.barber.repository.AddressRepository;
import com.spj.salon.barber.repository.ZipCodeRepository;
import com.spj.salon.checkin.entities.CheckIn;
import com.spj.salon.checkin.pojo.BarberDayOfWeekWithTime;
import com.spj.salon.checkin.ports.out.GeoCoding;
import com.spj.salon.checkin.repository.CheckInRepository;
import com.spj.salon.exception.DuplicateEntityException;
import com.spj.salon.exception.NotFoundCustomException;
import com.spj.salon.openapi.resources.*;
import com.spj.salon.user.entities.User;
import com.spj.salon.user.repository.UserRepository;
import com.spj.salon.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.naming.ServiceUnavailableException;
import java.io.IOException;
import java.math.BigInteger;
import java.time.DayOfWeek;
import java.time.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

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
    private final CheckInAdapterMapper checkInAdapterMapper;

    private final int waitTimeBetweenCustomers = 15;

    @Override
    public BarberWaitTimeResponse waitTimeEstimate(Optional<Long> barberIdOptional, TimeZone timeZone) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();

        User user = userRepository.findByEmail(email);
        Long barberId;
        if (user.getAuthority().getAuthority().equals("CUSTOMER")) {
            if (barberIdOptional.isEmpty()) {
                if (user.getFavouriteSalonId() == null) {
                    throw new NotFoundCustomException("No Favourite Salon Found", "NULL");
                } else {
                    barberId = user.getFavouriteSalonId();
                }
            } else {
                barberId = barberIdOptional.get();
            }
            //TODO: Test it
            // If someone is already checkin find his remaining time

            CheckIn checkIn = findCheckedInBarberId(user.getUserId());
            if (checkIn != null) {
                return new BarberWaitTimeResponse()
                        .waitTime("" + findTimeLeft(checkIn.getEta(), checkIn.getCheckInTimestamp()))
                        .salonName("Already checked in");
            }
        } else {
            barberId = user.getUserId();
        }
        return waitTimeEstimateAtBarber(barberId, timeZone);
    }

    private BarberWaitTimeResponse waitTimeEstimateAtBarber(Long barberId, TimeZone timeZone) {
        User barber = userRepository.findByUserId(barberId);
        if (barber != null) {
            DailyBarbers todaysBarber = barber.getDailyBarberSet()
                    .stream()
                    .filter(a -> a.getBarbersCount() > 0)
                    .findFirst()
                    .orElse(new DailyBarbers());

            long noOfCheckIns = barber.getCheckInSet()
                    .stream()
                    .filter(a -> a.getCheckInTimestamp().isAfter(OffsetDateTime.now().minusMinutes(90)))
                    .filter(a -> !a.isCheckedOut())
                    .count();

            return new BarberWaitTimeResponse().waitTime(getEstimateWaitTime(barber.getBarberCalendarSet(), todaysBarber, noOfCheckIns, timeZone))
                    .salonName(barber.getStoreName());
        }
        return new BarberWaitTimeResponse().waitTime("Unable to find wait-time");
    }

    @Override
    public CheckIn findCheckedInBarberId(long customerId) {
        List<CheckIn> checkInList = checkInRepository
                .findByUserMappingIdAndCheckedOutAndCreateDate(customerId, false, LocalDate.now());

        if (!CollectionUtils.isEmpty(checkInList)) {
            return checkInList.stream().findFirst().get();
        }

        return null;
    }

    private long findTimeLeft(int eta, OffsetDateTime createTimestamp) {
        OffsetDateTime stop = LocalDateTime.now().atOffset(ZoneOffset.UTC);
        Duration d = Duration.between(createTimestamp, stop).minusMinutes(eta);
        return (d.getSeconds() / 60);
    }

    /**
     * This method will calculate the checkin time available for a given service
     *
     * @param todaysBarberCount
     * @param noOfCheckIns
     * @return
     */
    private String getEstimateWaitTime(Set<BarberCalendar> barberCalendarSet, DailyBarbers todaysBarberCount,
                                       long noOfCheckIns, TimeZone timeZone) {

        if(isBarberClosed(barberCalendarSet, timeZone)){
            return "Barber is either closed or calender not set for today";
        }

        if (todaysBarberCount.getBarbersCount() == 0) {
            return "No Barbers available at this time";
        }

        long waitTime = (noOfCheckIns / todaysBarberCount.getBarbersCount()) * waitTimeBetweenCustomers;
        return waitTime + "";
    }

    /**
     * This method will find out todays working hours by filtering on todays date
     * It will also check if it a special day like 4th July
     */
    private boolean isBarberClosed(Set<BarberCalendar> barberCalendarSet, TimeZone timeZone) {
        BarberCalendar todaysDay = null;
        BarberCalendar todaysDate = null;
        for (BarberCalendar barberCalendar: barberCalendarSet) {
            if (DateUtils.getTodaysDay(timeZone).equals(barberCalendar.getCalendarDay())) {
                todaysDay = barberCalendar;
            }
            else if(DateUtils.isTodayDate(barberCalendar.getCalendarDate(), timeZone)){
                todaysDate = barberCalendar;
            }
        }

        if (todaysDay == null || todaysDate != null) {
            return true;
        }

        Date nowTime = DateUtils.getNowTime1970Format(timeZone);
        if(todaysDay.getSalonOpenTime().after(nowTime)
                || todaysDay.getSalonCloseTime().before(nowTime)){
            return true;
        }

        return false;
    }

    @Override
    public CustomerCheckInResponse checkInCustomerByCustomer(Optional<Long> barberIdOpt, TimeZone timeZone) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();
        User customer = userRepository.findByEmail(email);
        Long barberId;

        if (barberIdOpt.isPresent()) {
            barberId = barberIdOpt.get();
        } else if (customer.getFavouriteSalonId() != null) {
            barberId = customer.getFavouriteSalonId();
        } else {
            return new CustomerCheckInResponse().message("No Favourite Salon Found");
        }

        User barber = userRepository.findByUserId(barberId);
        return checkin(customer, barber, customer.getUserId(), timeZone);
    }

    @Override
    public CustomerCheckInResponse checkInCustomerByBarber(long userId, TimeZone timeZone) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();
        User barber = userRepository.findByEmail(email);

        User customer = userRepository.findByUserId(userId);

        return checkin(customer, barber, barber.getUserId(), timeZone);
    }

    @Override
    public boolean isUserAlreadyCheckedIn(long userId) {
        return checkInRepository.countByUserMappingIdAndCheckedOutAndCreateDate(userId, false, LocalDate.now()) > 0;
    }

    private CustomerCheckInResponse checkin(User customer, User barber, long updatedBy, TimeZone timeZone) {
        String waitTimeEstimate = waitTimeEstimateAtBarber(barber.getUserId(), timeZone).getWaitTime();

        try {
            int waitTime = Integer.parseInt(waitTimeEstimate);
            if (isUserAlreadyCheckedIn(customer.getUserId())) {
                throw new DuplicateEntityException("Customer is already checkedIn", customer.getEmail());
            }

            CheckIn checkIn = new CheckIn(barber.getUserId(), customer.getUserId(), waitTime, updatedBy);
            checkInRepository.saveAndFlush(checkIn);
            return new CustomerCheckInResponse().message("Check in with waitTime " + waitTime)
                    .customerId(customer.getEmail());
        } catch (NumberFormatException ne) {
            return new CustomerCheckInResponse().message("Unable to Checkin")
                    .customerId(customer.getEmail());
        }
    }

    @Override
    public CustomerCheckoutResponse checkOut(Optional<Long> customerIdOptional) {
        Long customerId;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();
        User user = userRepository.findByEmail(email);
        Long checkedOutBy = user.getUserId();

        if (user.getAuthorityId() == 2 && customerIdOptional.isEmpty()) {
            throw new NotFoundCustomException("Please pass customer ID", "Blank customer ID");
        } else if (customerIdOptional.isPresent()) {
            customerId = customerIdOptional.get();
            if (userRepository.findByUserId(customerId) == null) {
                throw new NotFoundCustomException("Customer Id Not Found", "" + customerId);
            }
        } else {
            customerId = user.getUserId();
        }

        checkInRepository.findByUserMappingIdAndCheckedOutAndCreateDate(customerId, false, LocalDate.now())
                .forEach(checkIn -> checkOut(checkIn, checkedOutBy));

        return new CustomerCheckoutResponse().message("Customer has been checked out");
    }

    private void checkOut(CheckIn checkIn, long checkOutBy) {
        checkIn.setCheckedOut(true);
        checkIn.setUpdatedBy(checkOutBy);
        checkIn.setCheckOutTimestamp(OffsetDateTime.now());
        checkInRepository.saveAndFlush(checkIn);
    }

    /**
     * @throws ServiceUnavailableException
     */
    @Override
    public BarbersWaitTimeResponse findBarbersAtZip(BarberWaitTimeRequest barberCheckInRequest, TimeZone timeZone) {
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

        return findBarbersWithinXMiles(longitude, latitude, barberCheckInRequest.getDistance(), timeZone);
    }

    /**
     * 1° of latitude ~= 69 miles
     * 1° of longitude ~= cos(latitude)*69
     *
     * @param longitude
     * @param latitude
     * @param distance
     */
    private BarbersWaitTimeResponse findBarbersWithinXMiles(double longitude, double latitude,
                                                            Double distance, TimeZone timeZone) {
        BarberDetails barberDetails;
        BarbersWaitTimeResponse barbersWaitTimeResponse = new BarbersWaitTimeResponse();

        double multiplier  = distance / Math.abs(Math.cos(Math.toRadians(latitude)) * 69);

        double long1 = longitude - multiplier;
        double long2 = longitude + multiplier;
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
                Optional<User> userOpt = userRepository.findById(barbersAddress.get().getUserId());
                User user = userOpt.get();
                BarberDetailsCalendar barberDetailsCalendar = getUserCalendar(user.getBarberCalendarSet());

                barberDetails = new BarberDetails()
                        .address(checkInAdapterMapper.toResponse(barbersAddress.get()))
                        .email(user.getEmail())
                        .barberId(user.getUserId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .middleName(user.getMiddleName())
                        .phone(user.getPhone())
                        .storeName(user.getStoreName())
                        .distance((Double) map.get("distance"))
                        .waitTime(waitTimeEstimateAtBarber(barbersAddress.get().getUserId(), timeZone).getWaitTime())
                        .calendar(barberDetailsCalendar)
                        .isShopOpen(!isBarberClosed(user.getBarberCalendarSet(), timeZone))
                        .storeCloseIn1HourFlag(isStoreClosingIn1Hour(user.getBarberCalendarSet(), timeZone));

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

    private Boolean isStoreClosingIn1Hour(Set<BarberCalendar> barberCalendarSet, TimeZone timeZone) {
        BarberCalendar todaysDay = null;
        for (BarberCalendar barberCalendar: barberCalendarSet) {
            if (DateUtils.getTodaysDay(timeZone).equals(barberCalendar.getCalendarDay())) {
                todaysDay = barberCalendar;
                break;
            }
        }

        Date nowTimePlus1Hour = DateUtils.getNowTimePlus60Mins1970(timeZone);
        if(todaysDay.getSalonCloseTime().before(nowTimePlus1Hour)){
            return true;
        }
        return false;
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

    public BarberDetailsCalendar getUserCalendar(Set<BarberCalendar> barberCalendarSet) {
        List<BarberDayOfWeekWithTime> barberDayOfWeekWithTimeList = new ArrayList<>();
        List<Date> holidays = new ArrayList<>();
        barberCalendarSet.forEach(barberCalendar -> {
            if (barberCalendar.getCalendarDate() == null) {
                barberDayOfWeekWithTimeList.add(BarberDayOfWeekWithTime.builder()
                        .salonOpenTime(DateUtils.getFormattedDateInString(barberCalendar.getSalonOpenTime(), "hh:mm aa"))
                        .salonCloseTime(DateUtils.getFormattedDateInString(barberCalendar.getSalonCloseTime(), "hh:mm aa"))
                        .dayOfWeek(DayOfWeek.valueOf(barberCalendar.getCalendarDay().toUpperCase()))
                        .build());
            } else {
                holidays.add(barberCalendar.getCalendarDate());
            }
        });
        Collections.sort(barberDayOfWeekWithTimeList);

        return new BarberDetailsCalendar()
                .holidays(holidays)
                .weeklySchedule(checkInAdapterMapper.toResponseList(barberDayOfWeekWithTimeList));
    }

    @Override
    public BarberWaitTimeResponse currentWaitTimeEstimateForCustomerAtBarber(long customerId, long barberId) {
        User user = userRepository.findByUserId(customerId);
        return calculateWaitTime(user, barberId);
    }

    @Override
    public BarberWaitTimeResponse currentWaitTimeEstimateForCustomer(long barberId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();
        User user = userRepository.findByEmail(email);

        return calculateWaitTime(user, barberId);
    }

    private BarberWaitTimeResponse calculateWaitTime(User user, long barberId) {
        long waitTime;
        //Find barber count at current time
        int currentBarberCount = userRepository.findByUserId(barberId).getDailyBarberSet()
                .stream()
                .filter(a -> a.getBarbersCount() > 0)
                .findFirst()
                .orElse(null).getBarbersCount();

        List<CheckIn> checkInList = checkInRepository
                .findByBarberMappingIdAndCreateDateOrderByCheckInTimestampAsc(barberId, LocalDate.now());

        CheckIn lastCheckoutUser = checkInList.stream()
                .filter(CheckIn::isCheckedOut)
                .reduce((first, second) -> second).orElse(null);

        OffsetDateTime checkOutTimestamp = null;
        if (lastCheckoutUser != null) {
            checkOutTimestamp = lastCheckoutUser.getCheckOutTimestamp();
        }

        AtomicInteger counter = new AtomicInteger(0);

        //Set correct rank only for the one that matches the user mapping ID
        //This will help later to filter to the record that has rank and for current User
        //Get the CheckIn data for user in context
        CheckIn userCheckInRecord = checkInList.stream()
                .filter(checkIn -> !checkIn.isCheckedOut())
                .map(checkIn -> setRank(checkIn, user, counter))
                .filter(checkIn -> checkIn.getRank() != -1)
                .findFirst().orElse(null);

        //Rank multiplier is the field that is calculated based on number of barbers ex:
        //if there are 3 barbers then until 3 customer the wait time should be 0
        int rankMultiplier = 0;
        if (userCheckInRecord.getRank() != -1) {
            rankMultiplier = (userCheckInRecord.getRank() - 1) / currentBarberCount;
        }

        if (checkOutTimestamp == null || checkOutTimestamp.plusMinutes(waitTimeBetweenCustomers)
                .isBefore(userCheckInRecord.getCheckInTimestamp())) {
            waitTime = Duration.between(OffsetDateTime.now(),
                    userCheckInRecord.getCheckInTimestamp()
                            .plusMinutes((long) rankMultiplier * waitTimeBetweenCustomers)).toMinutes();
        } else {
            waitTime = Duration.between(OffsetDateTime.now(),
                    checkOutTimestamp
                            .plusMinutes((long) rankMultiplier * waitTimeBetweenCustomers)).toMinutes();
        }

        return new BarberWaitTimeResponse().waitTime(waitTime < 0 ? "0" : waitTime + "");
    }

    private CheckIn setRank(CheckIn checkIn, User user, AtomicInteger counter) {

        int count = counter.incrementAndGet();
        if (Objects.equals(checkIn.getUserMappingId(), user.getUserId())) {
            checkIn.setRank(count);
            return checkIn;
        }
        checkIn.setRank(-1);
        return checkIn;
    }

    @Override
    public List<UserProfile> getCheckedInCustomer() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();

        User barber = userRepository.findByEmail(email);
        List<CheckIn> checkedInUsers = checkInRepository.findByBarberMappingIdAndCreateDateAndCheckedOutOrderByCheckInTimestampAsc(
                barber.getUserId(), LocalDate.now(), false);

        List<UserProfile> userprofileList = new ArrayList<>();
        checkedInUsers.forEach(checkedInUser -> userprofileList.add(
                checkInAdapterMapper.toUserProfile(userRepository.findByUserId(checkedInUser.getUserMappingId()))));
        return userprofileList;
    }
}
