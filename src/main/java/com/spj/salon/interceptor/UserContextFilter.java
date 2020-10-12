package com.spj.salon.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Yogesh SHarma
 */
@Component
@Slf4j
public class UserContextFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if (httpServletRequest.getHeader(UserContext.CORRELATION_ID) != null)
            UserContextHolder.getContext().setCorrelationId(httpServletRequest.getHeader(UserContext.CORRELATION_ID));
        else
            UserContextHolder.getContext().setCorrelationId(UUID.randomUUID().toString());
        //UserContextHolder.getContext().setUserId(httpServletRequest.getHeader(UserContext.USER_ID));
        UserContextHolder.getContext().setAuthToken(httpServletRequest.getHeader(UserContext.AUTH_TOKEN));
        //UserContextHolder.getContext().setOrgId(httpServletRequest.getHeader(UserContext.ORG_ID));

        log.debug("Find MySalon Service Incoming Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
        httpServletResponse.setHeader(UserContext.CORRELATION_ID, UserContextHolder.getContext().getCorrelationId());

        //This is like MDC and is used for logging
        MDC.clear();
        MDC.put(UserContext.CORRELATION_ID, UserContextHolder.getContext().getCorrelationId());

        chain.doFilter(httpServletRequest, response);
    }
}
