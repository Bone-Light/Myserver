package org.example.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.DAO.entity.DTO.Client;
import org.example.DAO.entity.RestBean;
import org.example.DAO.service.AccountService;
import org.example.DAO.service.ClientService;
import org.example.entity.RestBean;
import org.example.utils.Const;
import org.example.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    @Resource
    JwtUtils jwtUtils;

    @Resource
    ClientService clientService;
    @Resource
    AccountService accountService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        String url = request.getRequestURI();
        if(url.startsWith("/monitor")){
            if(!url.endsWith("/register")){
                // 自建
                Client client = clientService.findCilentByToken(authorization);
                if(client != null){
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(RestBean.failure(401, "未注册").asJsonString());
                    return;
                } else {
                    request.setAttribute(Const.ATTR_CLIENT, client);
                }
            }
        } else {
            DecodedJWT jwt = jwtUtils.resolveToken(authorization);
            if(jwt != null){
                UserDetails userDetails = jwtUtils.toUser(jwt);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                request.setAttribute(Const.ATTR_USER_ID, jwtUtils.toId(jwt));
                request.setAttribute(Const.ATTR_USER_ROLE, new ArrayList<>(userDetails.getAuthorities()).getFirst().getAuthority());

                if(request.getRequestURI().startsWith("/terminal") && !accessShell(
                        (int) request.getAttribute(Const.ATTR_USER_ID),
                        (String) request.getAttribute(Const.ATTR_USER_ROLE),
                        (int) request.getAttribute(Const.ATTR_CLIENT)
                )){
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(RestBean.failure(401, "权限不足").asJsonString());
                    return;
                }
            }
        }

        chain.doFilter(request,response);
    }

    private boolean accessShell(int userId, String userRole, int clientId){
        if(Const.ROLE_ADMIN.equals(userRole.substring(5))){
            return true;
        } else {
            Account account = accountService.getById(userId);
            return account.getClientList().contains(clientId);
        }
    }
}
