package com.spj.salon.barber.endpoints;

import com.spj.salon.barber.ports.in.IBarberAdapter;
import com.spj.salon.openapi.endpoint.BarberApiDelegate;
import com.spj.salon.openapi.resources.*;
import com.spj.salon.user.ports.in.IRegisterBarber;
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
public class BarberController implements BarberApiDelegate {
    private final IBarberAdapter barberAdapter;
    private final IRegisterBarber registerBarber;

    @Override
    public ResponseEntity<RegisterBarberResponse> registerBarber(RegisterBarberRequest registerBarberRequest) {
        log.info("Inside BarberController registerBarber service");
        return ResponseEntity.ok(registerBarber.registerBarber(registerBarberRequest));
    }

    @Override
    public ResponseEntity<DailyBarbersResponse> addBarberCountToday(DailyBarbersRequest dailyBarbersRequest) {
        log.info("Inside BarberController addBarbersCountToday service");
        return ResponseEntity.ok(barberAdapter.addBarbersCountToday(dailyBarbersRequest));
    }

    @Override
    public ResponseEntity<BarberServicesResponse> addServices(BarberServicesRequest barberServicesRequest) {
        log.info("Inside ServicesController addService service");
        return ResponseEntity.ok(barberAdapter.addService(barberServicesRequest));
    }

    @Override
    public ResponseEntity<BarberServicesResponse> addServicesForBarber(Long serviceId, Integer cost, Integer time) {
        log.info("Inside BarberController addServices service");
        return ResponseEntity.ok(barberAdapter.addServices(serviceId, cost, time));
    }

    @Override
    public ResponseEntity<BarberCalendarResponse> addBarberCalendar(BarberCalendarRequest barberCalendarRequest) {
        log.info("Inside BarberController calendar service");
        return ResponseEntity.ok(barberAdapter.addBarberCalendar(barberCalendarRequest));
    }

    @Override
    public ResponseEntity<AuthenticationResponse> authenticateBarber(AuthenticationRequest authenticationRequest, Optional<String> clientHeader) {
        return ResponseEntity.ok(barberAdapter.getJwtToken(authenticationRequest, clientHeader.orElse(null)));
    }

    @Override
    public ResponseEntity<AuthenticationResponse> refreshBarber(RefreshRequest refreshRequest, Optional<String> clientHost) {
        return ResponseEntity.ok(barberAdapter.getRefreshToken(refreshRequest, clientHost.orElse(null)));
    }
}
