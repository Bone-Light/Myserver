package org.example.filter;

import jakarta.annotation.Resource;
import org.example.utils.Const;
import org.example.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationFilter {
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    @Resource
    JwtUtils jwtUtils;

    private boolean accessShell(int userId, String userRole, int clientId){
        if(Const.ROLE_ADMIN.equals(userRole.substring(5))){
            return true;
        } else {
            Account account = ....;
            return account.get ....;
        }
    }
}
