package com.spj.salon.controller;

import java.util.Map;

import javax.naming.ServiceUnavailableException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spj.salon.barber.facade.IBarberFacade;
import com.spj.salon.barber.model.Address;
import com.spj.salon.barber.model.BarberCalendar;
import com.spj.salon.barber.model.DailyBarbers;
import com.spj.salon.security.pojo.UserType;
import com.spj.salon.user.model.User;
import com.spj.salon.utils.UserContextHolder;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "barber", produces = MediaType.APPLICATION_JSON_VALUE)
public class BarberController {

	private static final Logger logger = LogManager.getLogger(BarberController.class.getName());

	@Autowired
	private Source source;
	
	@Autowired
	private IBarberFacade barberFacade;

	@PostMapping(value = "register", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> registerBarber(@RequestBody User barber, @RequestHeader Map<String, String> headers) {
		logger.info("Inside BarberController registerBarber service");
		return new ResponseEntity<>(barberFacade.register(barber, UserType.BARBER, headers.get("clienthost")),
				HttpStatus.OK);
	}

	@PostMapping(value = "barbersCount", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> addBarbersCountToday(@RequestBody DailyBarbers dailyBarbers) {
		logger.info("Inside BarberController addBarbersCountToday service");
		return new ResponseEntity<>(barberFacade.addBarbersCountToday(dailyBarbers), HttpStatus.OK);
	}

	@PostMapping(value = "services/{serviceId}/cost/{cost}/time/{time}", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<Boolean> addServices(@PathVariable long serviceId, @PathVariable int cost,
			@PathVariable int time) {
		logger.info("Inside BarberController addServices service");
		return new ResponseEntity<>(barberFacade.addServices(serviceId, cost, time), HttpStatus.OK);
	}

	@PostMapping(value = "calendar", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> addBarberCalendar(@RequestBody BarberCalendar barberCalendar) {
		logger.info("Inside BarberController calendar service");
		return new ResponseEntity<>(barberFacade.addBarberCalendar(barberCalendar), HttpStatus.OK);
	}

	@PostMapping(value = "/address", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> addBarberAddress(@RequestBody Address address,
			@RequestHeader Map<String, String> headers) throws ServiceUnavailableException {
		// adding hostname to call google api on Heroku
		logger.info("Inside BarberController addBarberAddress service");
		UserContextHolder.getContext().setHost(headers.get("hostname"));
		return new ResponseEntity<>(barberFacade.addBarberAddress(address), HttpStatus.OK);
	}

	@GetMapping("/validate/prime-number")
	public String isNumberPrime(@RequestParam("number") String number) {
		return Integer.parseInt(number) % 2 == 0 ? "Even" : "Odd";
	}
	
	@PostMapping("/message")
	public void message() {
		Address address = new Address();
		address.setCity("Pittsburgh");
		source.output().send(MessageBuilder.withPayload(address).build());
	}
}
