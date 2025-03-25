package org.example.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.utils.Const;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Const.ORDER_CORS)
public class CorsFilter extends HttpFilter {
    @Value("${spring.web.cors.origin}")
    String origin;
    @Value("${spring.web.cors.credentials}")
    String credentials;
    @Value("${spring.web.cors.methods}")
    String methods;

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain){
        response.setHeader("Access-Control-Allow-Origin", this.resolveOrigin(request));
        response.setHeader("Access-Control-Allow-Credentials", credentials);
        response.setHeader("Access-Control-Allow-Methods", resolveMethod());
    }

    private String resolveMethod(){
        return methods.equals("*") ? "GET, HEAD, POST, PUT, DELETE, OPTIONS, TRACE, PATCH" : methods;
    }

    private String resolveOrigin(HttpServletRequest request){
        return origin.equals("*") ? request.getHeader("Origin") : origin;
    }
}
