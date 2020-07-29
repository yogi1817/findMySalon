package com.spj.salon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spj.salon.services.facade.IServicesFacade;
import com.spj.salon.services.model.Services;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "services", 
		produces = MediaType.APPLICATION_JSON_VALUE)
public class ServicesController {

	@Autowired
	private IServicesFacade servicesFacade;
	
	@PostMapping(value = "register", 
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> addService(@RequestBody Services services){
		return new ResponseEntity<>(servicesFacade.addService(services), HttpStatus.OK);
	}
}
