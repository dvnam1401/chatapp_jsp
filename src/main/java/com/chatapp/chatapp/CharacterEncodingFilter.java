package com.chatapp.chatapp;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class CharacterEncodingFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Set encoding for request and response
        httpRequest.setCharacterEncoding("UTF-8");
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("text/html; charset=UTF-8");
        
        chain.doFilter(request, response);
    }
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("CharacterEncodingFilter initialized");
    }
    
    @Override
    public void destroy() {
        System.out.println("CharacterEncodingFilter destroyed");
    }
} 