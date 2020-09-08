package com.spj.salon.mq;

import com.spj.salon.barber.model.Address;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Data
public class RabbitMQListener {
	
	public static void onAddressListener(Address address) {
		log.info("Inside RabbitMQListener --> Address: {}", address);
	}
}