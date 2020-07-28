package com.spj.salon.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spj.salon.barber.facade.IBarberFacade;
import com.spj.salon.client.OAuthClient;
import com.spj.salon.security.pojo.UserType;
import com.spj.salon.user.facade.IUserFacade;
import com.spj.salon.user.model.User;

/**
 * 
 * @author ipecy
 *
 */
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "user",
					produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

	@Autowired
	private IUserFacade userFacade;
	
	@Autowired
	private IBarberFacade barberFacade;
	
	@Autowired
	private OAuthClient oAuthClient;
	
	@PostMapping(value = "register", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> registerBarber(@RequestBody User user, @RequestHeader Map<String, String> headers){
		return new ResponseEntity<>(barberFacade.register(user, UserType.USER, headers.get("clienthost")), HttpStatus.OK);
	}
	
	@PostMapping(value = "favourite", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<Boolean> addFavouriteSalon( 
			@RequestParam Long barberId){
		return new ResponseEntity<>(userFacade.addFavouriteSalon(barberId), HttpStatus.OK);
	}
	
	@PostMapping(value = "authenticate", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> authenticate(@RequestBody User user, @RequestHeader Map<String, String> headers) {
		return new ResponseEntity<>(oAuthClient.getJwtToken(user, headers.get("clienthost")), HttpStatus.OK);
	}
}
