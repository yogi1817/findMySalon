package com.spj.salon.services.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spj.salon.services.model.Services;
import com.spj.salon.services.repository.ServicesRepository;
import com.spj.salon.utils.DateUtils;

@Service
public class ServicesFacade implements IServicesFacade {

	@Autowired
	private ServicesRepository servicesRepo;
	
	@Override
	public boolean addService(Services services) {
		validateServices(services);
		servicesRepo.saveAndFlush(services);
		return true;
	}

	private void validateServices(Services services) {
		services.setCreateDate(DateUtils.getTodaysDate());
		services.setModifyDate(DateUtils.getTodaysDate());
	}
}
