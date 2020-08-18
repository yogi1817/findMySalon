package com.spj.salon.utils;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Yogesh SHarma
 *
 */
@Component
public class UserContextFilter implements Filter{

	private static final Logger logger = LogManager.getLogger(UserContextFilter.class);
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		
		if(httpServletRequest.getHeader(UserContext.CORRELATION_ID)!=null)
			UserContextHolder.getContext().setCorrelationId(httpServletRequest.getHeader(UserContext.CORRELATION_ID));
		else
			UserContextHolder.getContext().setCorrelationId(UUID.randomUUID().toString());
        //UserContextHolder.getContext().setUserId(httpServletRequest.getHeader(UserContext.USER_ID));
        UserContextHolder.getContext().setAuthToken(httpServletRequest.getHeader(UserContext.AUTH_TOKEN));
        //UserContextHolder.getContext().setOrgId(httpServletRequest.getHeader(UserContext.ORG_ID));

        logger.debug("Find MySalon Service Incoming Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
        httpServletResponse.setHeader(UserContext.CORRELATION_ID, UserContextHolder.getContext().getCorrelationId());
        
        //This is like MDC and is used for logging
        ThreadContext.clearAll();
        ThreadContext.put(UserContext.CORRELATION_ID, UserContextHolder.getContext().getCorrelationId());
        
        chain.doFilter(httpServletRequest, response);
	}
}
