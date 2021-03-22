package com.spj.salon.checkin.endpoints;

import com.spj.salon.checkin.adapters.ICheckinFacade;
import com.spj.salon.openapi.endpoint.CheckInApiDelegate;
import com.spj.salon.openapi.resources.*;
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
@RequiredArgsConstructor
@Slf4j
public class CheckInController implements CheckInApiDelegate {

    private final ICheckinFacade checkInFacade;

    @Override
    public ResponseEntity<CustomerCheckInResponse> checkInCustomerByCustomer(Optional<Long> barberId) {
        log.info("Inside CheckInController checkInByUser service");
        return ResponseEntity.ok(checkInFacade.checkInCustomerByCustomer(barberId));
    }

    @Override
    public ResponseEntity<CustomerCheckInResponse> checkInCustomerByBarber(Long customerId) {
        log.info("Inside CheckInController checkInByBarber service");
        return ResponseEntity.ok(checkInFacade.checkInCustomerByBarber(customerId));
    }

    @Override
    public ResponseEntity<BarberWaitTimeResponse> waitTimeEstimateAtBarber(Long barberId) {
        log.info("Inside CheckInController waitTimeEstimate service");
        return ResponseEntity.ok(checkInFacade.waitTimeEstimate(barberId));
    }

    @Override
    public ResponseEntity<CustomerCheckoutResponse> checkOutCustomer(Optional<Long> customerId) {
        log.info("Inside CheckInController checkOut service");
        return ResponseEntity.ok(checkInFacade.checkOut(customerId));
    }

    @Override
    public ResponseEntity<BarbersWaitTimeResponse> findAllBarbersAtZip(BarberWaitTimeRequest barberWaitTimeRequest) {
        log.info("Inside CheckInController findBarbersAtZip service");
        return ResponseEntity.ok(checkInFacade.findBarbersAtZip(barberWaitTimeRequest));
    }

    @Override
    public ResponseEntity<BarberWaitTimeResponse> waitTimeEstimateAtBarberForCustomerInOauthHeader() {
        return ResponseEntity.ok(checkInFacade.waitTimeEstimateAtBarberForCustomerInOauthHeader());
    }
}