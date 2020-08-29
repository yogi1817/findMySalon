package com.spj.salon.mq;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;

import com.spj.salon.barber.model.Address;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@Data
public class RabbitMQListener {

	private String city = null;
	
	@StreamListener(Sink.INPUT)
	public void onAddressListener(Address address) {
		this.city = address.getCity();
		log.info("Inside RabbitMQListener --> Address: {}", address);
	}
}