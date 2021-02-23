package com.vadimaid.aeon.component;

import com.vadimaid.aeon.entity.User;
import com.vadimaid.aeon.exception.ApiException;
import com.vadimaid.aeon.security.JwtAuthenticationToken;
import com.vadimaid.aeon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    private final TokenHelper tokenHelper;
    private final UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {
            final User user = userService.getUserByUsername(tokenHelper.extractUsername(request));
            if (!user.getIsActive()) {
                throw new ApiException(HttpStatus.UNAUTHORIZED, "unauthorized", "User is unauthorized!");
            }
            SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(user));
        } catch (ApiException ex) {
            throw ex;
        }

        return super.preHandle(request, response, handler);
    }
}
