package findMySalon;

import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.naming.ServiceUnavailableException;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.spj.salon.FindMySalonApplication;
import com.spj.salon.barber.dto.BarberCheckInRequest;
import com.spj.salon.barber.dto.BarberCheckInResponse;
import com.spj.salon.checkin.facade.CheckInFacade;
import com.spj.salon.controller.BarberController;
import com.spj.salon.controller.CheckInController;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = FindMySalonApplication.class)
@AutoConfigureMessageVerifier
//@ActiveProfiles("test")
public abstract class BaseClass {

	@Autowired
	private BarberController barberController;

	@Autowired
	private CheckInController checkInController;

	@MockBean
	private CheckInFacade checkInFacade;

	@Autowired
	private Sink sink;
	
	@Before
	public void setup() {
		StandaloneMockMvcBuilder standaloneMockMvcBuilder = MockMvcBuilders.standaloneSetup(barberController,
				checkInController);

		RestAssuredMockMvc.standaloneSetup(standaloneMockMvcBuilder);

		//The below code is to get the test case return json object when checkInFacade is called
		Gson gson = new Gson();
		BarberCheckInRequest request = new BarberCheckInRequest("15237", null, null, 25.0);
		try {
			BarberCheckInResponse response = gson.fromJson(new FileReader(
					"C:\\Users\\ipecy\\workspaces\\findMySalon\\src\\test\\resources\\contracts\\findMySalon\\response.json"),
					BarberCheckInResponse.class);
			
			Mockito.when(checkInFacade.findBarbersAtZip(request)).thenReturn(response);
		} catch (ServiceUnavailableException | JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void triggerMessage() {
		this.barberController.message();
	}
}
