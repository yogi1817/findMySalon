package com.spj.salon.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestHaversine {

	private static final Logger logger = LoggerFactory.getLogger(TestHaversine.class.getName());
	/*
	 * @Autowired private RestTemplateClient client;
	 */

	//15220 - long --> -80.036726, Lati --> 40.422284
	//2107  - long --> -79.928377, Lati --> 40.423011
	//1743  - long --> -80.034390. Lati --> 40.496168
	// Carriage park and home
	@Test
	public void getLongitideLatitudeTest() {

		double lon1 = -80.034390;
		double lat1 = 40.496168;

		double lon2 = -79.928377;
		double lat2 = 40.233011;
		
		double distance = MathUtils.haversine(lat1, lon1, lat2, lon2);

		Assertions.assertThat(distance==19.006413168511905);
		logger.debug("distance Carriage park and home -->" + distance);
	}

	//15220 - long --> -80.036726, Lati --> 40.422284
	//2107  - long --> -79.928377, Lati --> 40.423011
	//1743  - long --> -80.034390. Lati --> 40.496168
	//15220 and home
	@Test
	public void getLongitideLatitudeTest2() {

		double lon1 = -80.036726;
		double lat1 = 40.422284;

		double lon2 = -79.928377;
		double lat2 = 40.233011;
		
		double distance = MathUtils.haversine(lat1, lon1, lat2, lon2);

		Assertions.assertThat(distance==14.258648941728783);
		logger.debug("distance home and 15220 -->" + distance);
	}

	//15220 - long --> -80.036726, Lati --> 40.422284
	//2107  - long --> -79.928377, Lati --> 40.423011
	//1743  - long --> -80.034390. Lati --> 40.496168
	// Carriage park and 15220
	@Test
	public void getLongitideLatitudeTest3() {

		double lon1 = -80.034390;
		double lat1 = 40.496168;
		
		double lon2 = -80.036726;
		double lat2 = 40.422284;

		double distance = MathUtils.haversine(lat1, lon1, lat2, lon2);

		Assertions.assertThat(distance==5.102813311062466);
		logger.debug("distance Carriage park and 15220 -->" + distance);
	}
}
