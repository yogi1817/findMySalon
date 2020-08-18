package findMySalon;

import findMySalon.BaseClass;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.Test;
import org.junit.Rule;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import io.restassured.response.ResponseOptions;

import static org.springframework.cloud.contract.verifier.assertion.SpringCloudContractAssertions.assertThat;
import static org.springframework.cloud.contract.verifier.util.ContractVerifierUtil.*;
import static com.toomuchcoding.jsonassert.JsonAssertion.assertThatJson;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;

@SuppressWarnings("rawtypes")
public class FindMySalonTest extends BaseClass {

	@Test
	public void validate_find_barber_by_zip_miles() throws Exception {
		// given:
			MockMvcRequestSpecification request = given();


		// when:
			ResponseOptions response = given().spec(request)
					.get("/checkin/barbers/zip/15237/distance/25");

		// then:
			assertThat(response.statusCode()).isEqualTo(200);
			assertThat(response.header("Content-Type")).matches("application/json.*");

		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
			assertThatJson(parsedJson).array("['barberAddressDTO']").contains("['firstName']").isEqualTo("Yogi");
			assertThatJson(parsedJson).array("['barberAddressDTO']").contains("['lastName']").isEqualTo("Sharma");
			assertThatJson(parsedJson).array("['barberAddressDTO']").contains("['middleName']").isNull();
			assertThatJson(parsedJson).array("['barberAddressDTO']").contains("['storeName']").isEqualTo("Yogi the barber");
			assertThatJson(parsedJson).array("['barberAddressDTO']").contains("['email']").isEqualTo("ipec.yogesh@gmail.com");
			assertThatJson(parsedJson).array("['barberAddressDTO']").contains("['phone']").isEqualTo("+1-412-4787824");
			assertThatJson(parsedJson).array("['barberAddressDTO']").contains("['addressLineOne']").isEqualTo("2107 Teal Trce");
			assertThatJson(parsedJson).array("['barberAddressDTO']").contains("['addressLineTwo']").isEqualTo("");
			assertThatJson(parsedJson).array("['barberAddressDTO']").contains("['city']").isEqualTo("Pittsburgh");
			assertThatJson(parsedJson).array("['barberAddressDTO']").contains("['state']").isEqualTo("PA");
			assertThatJson(parsedJson).array("['barberAddressDTO']").contains("['zip']").isEqualTo("15237");
			assertThatJson(parsedJson).array("['barberAddressDTO']").contains("['distance']").isEqualTo(2.1913932781340915);
			assertThatJson(parsedJson).array("['barberAddressDTO']").contains("['waitTime']").isEqualTo(0);
			assertThatJson(parsedJson).array("['barberAddressDTO']").contains("['firstName']").isEqualTo("Peter");
			assertThatJson(parsedJson).array("['barberAddressDTO']").contains("['storeName']").isEqualTo("peter the barber");
			assertThatJson(parsedJson).array("['barberAddressDTO']").contains("['email']").isEqualTo("peter.yogesh1234@gmail.com");
			assertThatJson(parsedJson).array("['barberAddressDTO']").contains("['addressLineOne']").isEqualTo("1743 Royal Oak Drive");
			assertThatJson(parsedJson).array("['barberAddressDTO']").contains("['zip']").isEqualTo("15220");
			assertThatJson(parsedJson).array("['barberAddressDTO']").contains("['distance']").isEqualTo(14.293933548528882);
			assertThatJson(parsedJson).field("['message']").isEqualTo("2 Barbers Found");
	}

}
