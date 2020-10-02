package com.spj.salon.controller;

import com.spj.salon.services.facade.IServicesFacade;
import com.spj.salon.services.model.Services;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Yogesh Sharma
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "services",
        produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class ServicesController {

    private final IServicesFacade servicesFacade;

    @PostMapping(value = "register",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> addService(@RequestBody Services services) {
        log.info("Inside ServicesController addService service");
        return new ResponseEntity<>(servicesFacade.addService(services), HttpStatus.OK);
    }
}
