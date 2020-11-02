package com.spj.salon.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class TestHaversine {

    /*
     * @Autowired private TIMESTAMPTZClient client;
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

        Assertions.assertThat(distance == 19.006413168511905);
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

        Assertions.assertThat(distance == 14.258648941728783);
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

        Assertions.assertThat(distance == 5.102813311062466);
    }
}
