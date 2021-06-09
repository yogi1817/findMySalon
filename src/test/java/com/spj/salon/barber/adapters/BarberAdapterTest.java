package com.spj.salon.barber.adapters;

import com.spj.salon.barber.entities.Services;
import com.spj.salon.barber.repository.ServicesRepository;
import com.spj.salon.openapi.resources.BarberCalendarRequest;
import com.spj.salon.openapi.resources.BarberServicesRequest;
import com.spj.salon.openapi.resources.DailyBarbersRequest;
import com.spj.salon.user.entities.User;
import com.spj.salon.user.ports.out.OAuthClient;
import com.spj.salon.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class BarberAdapterTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ServicesRepository serviceRepo;
    private BarberAdapterMapper facadeMapper = new com.spj.salon.barber.adapters.BarberAdapterMapperImpl();
    @Mock
    private ServicesRepository servicesRepo;
    @Mock
    private OAuthClient oAuthClient;

    final User barber = User.builder()
            .authorityId(2)
            .firstName("barber")
            .lastName("secret")
            .email("barber@barber.com")
            .storeName("barberthebarber")
            .build();

    private BarberAdapter testSubject;

    @BeforeEach
    void setUp() {
        testSubject = new BarberAdapter(userRepository, serviceRepo, facadeMapper, servicesRepo, oAuthClient);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(barber.getEmail(), "encryptedPassword"));
    }

    @Test
    void addBarbersCountToday() {
        DailyBarbersRequest dailyBarbersRequest = new DailyBarbersRequest()
                .barbersCount(5)
                .barbersDescription("five");

        Mockito.doReturn(barber)
                .when(userRepository)
                .findByEmail(barber.getEmail());

        Mockito.doReturn(barber)
                .when(userRepository)
                .saveAndFlush(barber);

        testSubject.addBarbersCountToday(dailyBarbersRequest);

        Mockito.verify(userRepository, Mockito.times(1))
                .findByEmail(barber.getEmail());

        Mockito.verify(userRepository, Mockito.times(1))
                .saveAndFlush(barber);

        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    void addServices() {
        Mockito.doReturn(barber)
                .when(userRepository)
                .findByEmail(barber.getEmail());

        Mockito.doReturn(Optional.of(Services.builder().serviceId(1L).serviceDescription("haircut").build()))
                .when(serviceRepo)
                .findById(1L);

        Mockito.doReturn(barber)
                .when(userRepository)
                .saveAndFlush(barber);

        testSubject.addServices(1L, 15, 15);

        Mockito.verify(userRepository, Mockito.times(1))
                .saveAndFlush(barber);

        Mockito.verify(serviceRepo, Mockito.times(1))
                .findById(1L);

        Mockito.verify(userRepository, Mockito.times(1))
                .findByEmail(barber.getEmail());

        Mockito.verifyNoMoreInteractions(userRepository);
        Mockito.verifyNoMoreInteractions(serviceRepo);
    }

    @Test
    void addBarberCalendar() {
        BarberCalendarRequest barberCalendarRequest = new BarberCalendarRequest()
                .calendarDay(BarberCalendarRequest.CalendarDayEnum.MONDAY)
                .salonClosesAt("11:00 PM")
                .salonOpensAt("11:00 AM");

        Mockito.doReturn(barber)
                .when(userRepository)
                .findByEmail(barber.getEmail());

        Mockito.doReturn(barber)
                .when(userRepository)
                .saveAndFlush(barber);

        testSubject.addBarberCalendar(barberCalendarRequest);

        Mockito.verify(userRepository, Mockito.times(1))
                .findByEmail(barber.getEmail());

        Mockito.verify(userRepository, Mockito.times(1))
                .saveAndFlush(barber);

        Mockito.verifyNoMoreInteractions(userRepository);
    }

    /*@Test
    void addBarberAddress() throws IOException {
        BarberAddressRequest barberAddressRequest = new BarberAddressRequest()
                .addressLineOne("one")
                .city("pitts")
                .state("pa")
                .country("usa")
                .zip(15237);

        StringBuilder address = new StringBuilder()
                .append("one")
                .append(" ").append("pitts")
                .append(" ").append("pa")
                .append(" ").append(15237);

        Address address1 = Address.builder()
                .addressLineOne("one")
                .city("pitts")
                .state("pa")
                .country("usa")
                .zip("15237")
                .longitude(12345)
                .latitude(12345)
                .build();

        Set<Address> addressSet=  new HashSet<>();
        addressSet.add(address1);

        User barberSave = User.builder()
                .authorityId(2)
                .firstName("barber")
                .lastName("secret")
                .email("barber@barber.com")
                .storeName("barberthebarber")
                .addressSet(addressSet)
                .build();

        GeocodingResult geocodingResult = new GeocodingResult();
        Geometry geometry = new Geometry();
        LatLng latLng = new LatLng(12345, 12345);
        geocodingResult.geometry = new Geometry();

        geometry.location = latLng;
        geocodingResult.geometry = geometry;

        GeocodingResult[] array = {geocodingResult};

        Mockito.doReturn(barber)
                .when(userRepository)
                .findByEmail(barber.getEmail());

        Mockito.doReturn(array)
                .when(googleGeoCodingAdapter)
                .findGeocodingResult(URLEncoder.encode(address.toString(), StandardCharsets.UTF_8));

        Mockito.doReturn(barber)
                .when(userRepository)
                .saveAndFlush(barberSave);

        testSubject.addBarberAddress(barberAddressRequest);

        Mockito.verify(userRepository, Mockito.times(1))
                .findByEmail(barber.getEmail());

        Mockito.verify(googleGeoCodingAdapter, Mockito.times(1))
                .findGeocodingResult(URLEncoder.encode(address.toString(), StandardCharsets.UTF_8));

        Mockito.verify(userRepository, Mockito.times(1))
                .saveAndFlush(barberSave);

        Mockito.verifyNoMoreInteractions(userRepository);
        Mockito.verifyNoMoreInteractions(googleGeoCodingAdapter);
    }*/

    @Test
    void addService() {
        BarberServicesRequest barberServicesRequest = new BarberServicesRequest()
                .serviceDescription("haircut").serviceName("haircut");

        Services services = Services.builder().serviceDescription("haircut").serviceName("haircut").build();
        Mockito.doReturn(null)
                .when(servicesRepo)
                .saveAndFlush(services);

        testSubject.addService(barberServicesRequest);

        Mockito.verify(servicesRepo, Mockito.times(1))
                .saveAndFlush(services);

        Mockito.verifyNoMoreInteractions(servicesRepo);
    }
}