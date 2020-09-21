package com.spj.salon.controller;

import java.util.Map;

import javax.naming.ServiceUnavailableException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spj.salon.barber.dto.BarberCheckInRequest;
import com.spj.salon.barber.dto.BarberCheckInResponse;
import com.spj.salon.checkin.facade.ICheckinFacade;
import com.spj.salon.utils.UserContextHolder;

import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "checkin", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CheckInController {

	private final ICheckinFacade checkInFacade;

	private static final Logger logger = LogManager.getLogger(CheckInController.class.getName());
	
	//User is checking in at barber
	@PostMapping(value = "barber/{barberId}/time/{time}", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<String> checkInByUser(@PathVariable long barberId, @PathVariable String time) {
		logger.info("Inside CheckInController checkInByUser service");
		return new ResponseEntity<>(checkInFacade.checkInUser(barberId, time), HttpStatus.OK);
	}
	
	//Barber is checking in for user
	@PostMapping(value = "user/{userId}/time/{time}", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<String> checkInByBarber(@PathVariable long userId, @PathVariable String time) {
		logger.info("Inside CheckInController checkInByBarber service");
		return new ResponseEntity<>(checkInFacade.checkInBarber(userId, time), HttpStatus.OK);
	}
	
	@GetMapping(value = "barber/{barberId}/waittime")
	public ResponseEntity<String> waitTimeEstimate(@PathVariable long barberId) {
		logger.info("Inside CheckInController waitTimeEstimate service");
		return new ResponseEntity<>(checkInFacade.waitTimeEstimate(barberId), HttpStatus.OK);
	}
	
	@PostMapping(value = "user/checkout", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<Boolean> checkOut(@RequestHeader Map<String, String> headers) {
		logger.info("Inside CheckInController checkOut service");
		return new ResponseEntity<>(checkInFacade.checkOut(Long.parseLong(headers.get("userid"))), HttpStatus.OK);
	}
	
	@PostMapping(value = "barbers/waittime/forlocation", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BarberCheckInResponse> findBarbersAtZip(@RequestBody BarberCheckInRequest barberCheckInRequest, 
			@RequestHeader Map<String,String> headers) throws ServiceUnavailableException {
		logger.info("Inside CheckInController findBarbersAtZip service");
		UserContextHolder.getContext().setHost(headers.get("hostname"));
		return new ResponseEntity<>(checkInFacade.findBarbersAtZip(barberCheckInRequest), HttpStatus.OK);
	}
}