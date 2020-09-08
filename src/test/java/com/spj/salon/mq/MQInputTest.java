package com.spj.salon.mq;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.test.context.junit4.SpringRunner;

import com.spj.salon.barber.model.Address;

@SpringBootTest
@RunWith(SpringRunner.class)
class MQInputTest {

	/*@Autowired
	private RabbitMQListener listener;*/
	
	@Autowired
	private Sink sink;
	
	@Test
	public void test_consume_address_message() throws Throwable{
		Address address = new Address();
		address.setCity("Pittsburgh");
		
		Message<Address> msg = MessageBuilder.withPayload(address).build();
		
		SubscribableChannel input = sink.input();
		input.send(msg);
		
		//BDDAssertions.then(this.listener.getCity()).isEqualTo(address.getCity());
	}
}
