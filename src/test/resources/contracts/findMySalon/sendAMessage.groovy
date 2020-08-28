package contracts.findMySalon

import org.springframework.cloud.contract.spec.Contract

Contract.make{
	description "should send a message"
	label "triggerMessage"
	input {
		triggeredBy("triggerMessage()")
	}
	outputMessage {
		sentTo 'spring-boot-exchange'
		body('''{ "city" : "Pittsburgh" }''')
		headers {
			messagingContentType(applicationJson())
		}
	}
}