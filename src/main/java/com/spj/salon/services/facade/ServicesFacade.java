package com.spj.salon.services.facade;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spj.salon.services.model.Services;
import com.spj.salon.services.repository.ServicesRepository;
import com.spj.salon.utils.DateUtils;

@Service
public class ServicesFacade implements IServicesFacade {

	@Autowired
	private ServicesRepository servicesRepo;
	
	private static final Logger logger = LogManager.getLogger(ServicesFacade.class.getName());
	
	/**
	 * This method will add new service to service table. These services can be later attached to the barber.
	 */
	@Override
	public boolean addService(Services services) {
		validateServices(services);
		servicesRepo.saveAndFlush(services);
		logger.info("Service saved");
		
		return true;
	}

	private void validateServices(Services services) {
		services.setCreateDate(DateUtils.getTodaysDate());
		services.setModifyDate(DateUtils.getTodaysDate());
	}
}
