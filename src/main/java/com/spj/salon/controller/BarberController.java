package com.spj.salon.controller;

import java.util.Map;

import javax.naming.ServiceUnavailableException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spj.salon.barber.facade.IBarberFacade;
import com.spj.salon.barber.model.Address;
import com.spj.salon.barber.model.Barber;
import com.spj.salon.barber.model.BarberCalendar;
import com.spj.salon.barber.model.DailyBarbers;
import com.spj.salon.utils.UserContextHolder;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@RestController
@CrossOrigin
@RequestMapping(value = "barber", produces = MediaType.APPLICATION_JSON_VALUE)
public class BarberController {

	@Autowired
	private IBarberFacade barberFacade;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@PostMapping(value = "register", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> registerBarber(@RequestBody Barber barber) {
		barber.setPassword(passwordEncoder.encode(barber.getPassword()));
		return new ResponseEntity<>(barberFacade.registerBarber(barber), HttpStatus.OK);
	}

	@PostMapping(value = "{id}/barbersCount", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> addBarbersCountToday(@PathVariable long id, @RequestBody DailyBarbers dailyBarbers) {
		return new ResponseEntity<>(barberFacade.addBarbersCountToday(id, dailyBarbers), HttpStatus.OK);
	}

	@PostMapping(value = "{barberId}/services/{serviceId}/cost/{cost}/time/{time}", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<Boolean> addServices(@PathVariable long barberId, @PathVariable long serviceId,
			@PathVariable int cost, @PathVariable int time) {
		return new ResponseEntity<>(barberFacade.addServices(barberId, serviceId, cost, time), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_BARBER')")
	@PostMapping(value = "{barberId}/calendar", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> addBarberCalendar(@PathVariable long barberId,
			@RequestBody BarberCalendar barberCalendar) {
		return new ResponseEntity<>(barberFacade.addBarberCalendar(barberId, barberCalendar), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_BARBER')")
	@PostMapping(value = "{barberId}/address", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> addBarberAddress(@PathVariable long barberId, @RequestBody Address address,
			@RequestHeader Map<String, String> headers) throws ServiceUnavailableException {
		// adding hostname to call google api on Heroku
		UserContextHolder.getContext().setHost(headers.get("hostname"));
		return new ResponseEntity<>(barberFacade.addBarberAddress(barberId, address), HttpStatus.OK);
	}
}
