package com.spj.salon.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "services", 
		produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ServicesController {

	private final IServicesFacade servicesFacade;
	
	private static final Logger logger = LogManager.getLogger(ServicesController.class.getName());
	
	@PostMapping(value = "register", 
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> addService(@RequestBody Services services){
		logger.info("Inside ServicesController addService service");
		return new ResponseEntity<>(servicesFacade.addService(services), HttpStatus.OK);
	}
}
