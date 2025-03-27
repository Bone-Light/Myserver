package org.example.config;

import jakarta.annotation.Resource;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.DAO.entity.DTO.Account;
import org.example.DAO.entity.RestBean;
import org.example.DAO.entity.VO.responst.AuthorizeVO;
import org.example.DAO.service.AccountService;
import org.example.filter.JwtAuthenticationFilter;
import org.example.filter.RequestLogFilter;
import org.example.utils.Const;
import org.example.utils.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class SecurityConfiguration {
    @Resource
    JwtAuthenticationFilter  jwtAuthenticationFilter;

    @Resource
    RequestLogFilter requestLogFilter;

    @Resource
    JwtUtils jwtUtils;

    @Resource
    AccountService accountService;

    @Bean
    public SecurityFilterChain securityWebFilterChain(
            HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(conf -> conf
                        .requestMatchers(
                                "/terminal/**",
                                "api/auth/**",
                                "/error",
                                "monitor/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .requestMatchers("/api/user/sub/**").hasRole(Const.ROLE_ADMIN)
                        .anyRequest().hasAnyRole(Const.ROLE_ADMIN, Const.ROLE_NORMAL)
                )
                .formLogin(conf -> conf
                        .loginProcessingUrl("/api/auth/login")
                        .failureHandler(this::handleProcess)
                        .successHandler(this::handleProcess)
                        .permitAll()
                )
                .logout(conf ->conf
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler(this::handleProcess)
                )
                .exceptionHandling(conf -> conf
                        .accessDeniedHandler(this::handleProcess)
                        .authenticationEntryPoint(this::handleProcess)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(conf -> conf
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(requestLogFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, RequestLogFilter.class)
                .build();
    }

    private void handleProcess(HttpServletRequest request, HttpServletResponse response, Object exceptionOrAuthentication) throws IOException, ServletException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter writer = response.getWriter();
        if(exceptionOrAuthentication instanceof AccessDeniedException exception){
            writer.write(RestBean
                    .forbidden(exception.getMessage()).asJsonString());
        } else if(exceptionOrAuthentication instanceof Exception exception){
              writer.write(RestBean
                      .unauthorized(exception.getMessage()).asJsonString());
        } else if(exceptionOrAuthentication instanceof Authentication authentication){
            User user = (User) authentication.getPrincipal();
            Account account = accountService.findAccountByNameOrEmail(user.getUsername());
            String jwt = jwtUtils.createJWT(user, account.getUsername(), account.getId());
            if(jwt == null){
                writer.write(RestBean.forbidden("登录验证频繁,请稍后再试").asJsonString());
            } else {
                AuthorizeVO vo = account.asViewObject(AuthorizeVO.class, o -> o.setToken(jwt));
                vo.setExpires(jwtUtils.expireTime());
                writer.write(RestBean.success(vo).asJsonString());
            }
        }
    }

    private void ouLogoutSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        String authorization = request.getHeader("Authorization");
        if(jwtUtils.invalidateJwt(authorization)){
            writer.write(RestBean.success("退出登录成功").asJsonString());
            return;
        }
        writer.write(RestBean.failure(400, "退出登录失败").asJsonString());
    }
}
