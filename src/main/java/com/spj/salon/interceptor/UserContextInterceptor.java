package com.spj.salon.interceptor;

import java.io.IOException;

import org.jetbrains.annotations.NotNull;
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

	//private static final Logger logger = LogManager.getLogger(UserContextInterceptor.class.getName());
	
	@NotNull
    @Override
	public ClientHttpResponse intercept(@NotNull HttpRequest request, @NotNull byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		//HttpHeaders headers = request.getHeaders();
		//headers.add(UserContext.CORRELATION_ID, UserContextHolder.getContext().getCorrelationId());
        //headers.add(UserContext.AUTH_TOKEN, UserContextHolder.getContext().getAuthToken());
        /*
		 * InputStreamReader isr = new InputStreamReader(response.getBody(),
		 * StandardCharsets.UTF_8); String responseBody = new BufferedReader(isr)
		 * .lines() .collect(Collectors.joining("\n"));
		 * logger.debug("Response body: {}", responseBody);
		 */
             
        return execution.execute(request, body);
	}
}
