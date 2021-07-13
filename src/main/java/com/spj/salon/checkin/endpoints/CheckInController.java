package com.spj.salon.checkin.endpoints;

import com.spj.salon.checkin.adapters.ICheckinFacade;
import com.spj.salon.openapi.endpoint.CheckInApiDelegate;
import com.spj.salon.openapi.resources.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

/**
 * @author Yogesh Sharma
 */
@Controller
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class CheckInController implements CheckInApiDelegate {

    private final ICheckinFacade checkInFacade;

    @Override
    public ResponseEntity<CustomerCheckInResponse> checkInCustomerByCustomer(Optional<Long> barberId, Optional<String> timeZone) {
        log.info("Inside CheckInController checkInByUser service");
        return ResponseEntity.ok(checkInFacade.checkInCustomerByCustomer(barberId, TimeZone.getTimeZone(timeZone.orElse("America/New_York"))));
    }

    @Override
    public ResponseEntity<CustomerCheckInResponse> checkInCustomerByBarber(Long customerId, Optional<String> timeZone) {
        log.info("Inside CheckInController checkInByBarber service");
        return ResponseEntity.ok(checkInFacade.checkInCustomerByBarber(customerId, TimeZone.getTimeZone(timeZone.orElse("America/New_York"))));
    }

    @Override
    public ResponseEntity<BarberWaitTimeResponse> waitTimeEstimateAtBarber(Optional<Long> barberIdOptional, Optional<String> timeZone) {
        log.info("Inside CheckInController waitTimeEstimate service");
        return ResponseEntity.ok(checkInFacade.waitTimeEstimate(barberIdOptional, TimeZone.getTimeZone(timeZone.orElse("America/New_York"))));
    }

    @Override
    public ResponseEntity<CustomerCheckoutResponse> checkOutCustomer(Optional<Long> customerId) {
        log.info("Inside CheckInController checkOut service");
        return ResponseEntity.ok(checkInFacade.checkOut(customerId));
    }

    @Override
    public ResponseEntity<BarbersWaitTimeResponse> findAllBarbersAtZip(BarberWaitTimeRequest barberWaitTimeRequest, Optional<String> timeZone) {
        log.info("Inside CheckInController findBarbersAtZip service");
        return ResponseEntity.ok(checkInFacade.findBarbersAtZip(barberWaitTimeRequest, TimeZone.getTimeZone(timeZone.orElse("America/New_York"))));
    }

    @Override
    public ResponseEntity<BarberWaitTimeResponse> currentWaitTimeEstimateForCustomer(Long barberId) {
        log.info("Inside CheckInController currentWaitTimeEstimateForCustomer service");
        return ResponseEntity.ok(checkInFacade.currentWaitTimeEstimateForCustomer(barberId));
    }

    @Override
    public ResponseEntity<List<UserProfile>> getCheckedInCustomer() {
        log.info("Inside CheckInController getCheckedInCustomer service");
        return ResponseEntity.ok(checkInFacade.getCheckedInCustomer());
    }
}
