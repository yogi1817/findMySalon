package com.spj.salon.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

/**
 * 
 * @author Yogesh SHarma
 *
 */
public class UserContextInterceptor implements ClientHttpRequestInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(UserContextInterceptor.class.getName());
	
	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		//HttpHeaders headers = request.getHeaders();
		//headers.add(UserContext.CORRELATION_ID, UserContextHolder.getContext().getCorrelationId());
        //headers.add(UserContext.AUTH_TOKEN, UserContextHolder.getContext().getAuthToken());
        ClientHttpResponse response = execution.execute(request, body);
        InputStreamReader isr = new InputStreamReader(response.getBody(), StandardCharsets.UTF_8);
        String responseBody = new BufferedReader(isr)
          .lines()
          .collect(Collectors.joining("\n"));
        logger.debug("Response body: {}", responseBody);
             
        return response;
	}
}
