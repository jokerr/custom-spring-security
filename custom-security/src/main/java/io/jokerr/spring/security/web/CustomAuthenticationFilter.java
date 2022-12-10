package io.jokerr.spring.security.web;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationFilter extends GenericFilterBean {

    private final AuthenticationManager authenticaitonManager;
    private final AuthenticationConverter authenticationConverter;
    protected AuthenticationSuccessHandler successHandler = new CustomAuthenticationSuccessHandler();
    protected AuthenticationFailureHandler failureHandler = new CustomAuthenticationFailureHandler();

    public CustomAuthenticationFilter(AuthenticationManager authenticaitonManager, AuthenticationConverter authenticationConverter) {
        this.authenticaitonManager = authenticaitonManager;
        this.authenticationConverter = authenticationConverter;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        try {
            Authentication authentication = authenticationConverter.convert(request);
            if (authentication != null) {
                authentication = authenticaitonManager.authenticate(authentication);
                if (successHandler != null) {
                    successHandler.onAuthenticationSuccess(request, response, authentication);
                }
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            chain.doFilter(request, response);
        }
        catch (InternalAuthenticationServiceException e) {
            logger.error("An internal error occurred while trying to authenticate the user.", e);
            unsuccessfulAuthentication(request, response, e);
        }
        catch (AuthenticationException e) {
            unsuccessfulAuthentication(request, response, e);
        }
    }

    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException exception) throws IOException, ServletException {

        SecurityContextHolder.clearContext();
        if(logger.isDebugEnabled()) {
            //cool logging here
        }
        failureHandler.onAuthenticationFailure(request, response, exception);
    }
}
