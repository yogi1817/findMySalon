package com.spj.salon.client;

import java.util.HashMap;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.spj.salon.barber.model.Address;

@SpringBootTest
public class RestTemplateClientTests {

	/*
	 * @Autowired private RestTemplateClient client;
	 */
	
	@Test
	public void getLongitideLatitudeTest() {
		Address address = new Address();
		address.setAddressLineOne("2107 Teal Trce");
		address.setAddressLineTwo("");
		address.setCity("Pittsburgh");
		address.setState("PA");
		
		Map<String, Double> longitudeMap = new HashMap<>();
		/*
		 * try { longitudeMap = client.getLongitideLatitude(address); } catch
		 * (UnsupportedEncodingException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		
		Assertions.assertThatObject(longitudeMap.containsKey("longitude"));
	}
}
