package com.spj.salon.services.facade;

import com.spj.salon.services.model.Services;
import com.spj.salon.services.repository.ServicesRepository;
import com.spj.salon.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ServicesFacade implements IServicesFacade {

    @Autowired
    private ServicesRepository servicesRepo;

    /**
     * This method will add new service to service table. These services can be later attached to the barber.
     */
    @Override
    public boolean addService(Services services) {
        validateServices(services);
        servicesRepo.saveAndFlush(services);
        log.info("Service saved");

        return true;
    }

    private void validateServices(Services services) {
        services.setCreateDate(DateUtils.getTodaysDate());
        services.setModifyDate(DateUtils.getTodaysDate());
    }
}
