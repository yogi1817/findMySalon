package com.spj.salon.controller;

import java.util.List;
import java.util.Map;

import javax.naming.ServiceUnavailableException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spj.salon.barber.dto.BarberAddressDTO;
import com.spj.salon.checkin.facade.ICheckinFacade;
import com.spj.salon.utils.UserContextHolder;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "checkin", produces = MediaType.APPLICATION_JSON_VALUE)
public class CheckInController {

	@Autowired
	private ICheckinFacade checkInFacade;

	//User is checking in at barber
	@PostMapping(value = "barber/{barberId}/time/{time}", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<String> checkInByUser(@PathVariable long barberId, @PathVariable String time) {
		return new ResponseEntity<>(checkInFacade.checkInUser(barberId, time), HttpStatus.OK);
	}
	
	//Barber is checking in for user
	@PostMapping(value = "user/{userId}/time/{time}", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<String> checkInByBarber(@PathVariable long userId, @PathVariable String time) {
		return new ResponseEntity<>(checkInFacade.checkInBarber(userId, time), HttpStatus.OK);
	}
	
	@GetMapping(value = "barber/{barberId}/waittime")
	public ResponseEntity<String> waitTimeEstimate(@PathVariable long barberId) {
		return new ResponseEntity<>(checkInFacade.waitTimeEstimate(barberId), HttpStatus.OK);
	}
	
	@PostMapping(value = "user/checkout", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<Boolean> checkOut(@RequestHeader Map<String, String> headers) {
		return new ResponseEntity<>(checkInFacade.checkOut(Long.parseLong(headers.get("userid"))), HttpStatus.OK);
	}
	
	@GetMapping(value = "barbers/zip/{zipCode}/distance/{distance}")
	public ResponseEntity<List<BarberAddressDTO>> findBarbersAtZip(@PathVariable String zipCode, 
						@PathVariable String distance, @RequestHeader Map<String,String> headers) throws ServiceUnavailableException {
		UserContextHolder.getContext().setHost(headers.get("hostname"));
		return new ResponseEntity<>(checkInFacade.findBarbersAtZip(zipCode, distance), HttpStatus.OK);
	}
}