package contracts.findMySalon

import org.springframework.cloud.contract.spec.Contract

Contract.make {
  description "should return barbers by zip and miles"

  request {
    url "/checkin/barbers/waittime/forlocation"
    method POST()
	body(file("request.json"))
	headers {
		contentType(applicationJson())
	}
  }

  response {
    status OK()
    headers {
      contentType applicationJson()
    }
    body (file("response.json"))
	status 200
  }
}