package com.spj.salon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spj.salon.user.facade.IUserFacade;
import com.spj.salon.user.model.User;

/**
 * 
 * @author ipecy
 *
 */
@RestController
@CrossOrigin
@RequestMapping(value = "user",
					produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

	@Autowired
	private IUserFacade userFacade;
	
	@PostMapping(value = "register", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> registerBarber(@RequestBody User user){
		return new ResponseEntity<>(userFacade.registerUser(user), HttpStatus.OK);
	}
	
	@PostMapping(value = "favourite", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<Boolean> addFavouriteSalon(@RequestParam Long userId, 
			@RequestParam Long barberId){
		return new ResponseEntity<>(userFacade.addFavouriteSalon(userId, barberId), HttpStatus.OK);
	}
}
