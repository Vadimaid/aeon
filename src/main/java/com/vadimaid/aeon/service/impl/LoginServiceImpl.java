package com.vadimaid.aeon.service.impl;

import com.vadimaid.aeon.component.TokenHelper;
import com.vadimaid.aeon.dto.LoginRequest;
import com.vadimaid.aeon.entity.User;
import com.vadimaid.aeon.exception.ApiException;
import com.vadimaid.aeon.service.LoginService;
import com.vadimaid.aeon.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor

@Service
public class LoginServiceImpl implements LoginService {

    private static final Integer MAX_FAIL_ATTEMPTS = 10;

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenHelper tokenHelper;

    @SneakyThrows
    @Override
    public String login(LoginRequest loginRequest) {
        User user = userService.getUserByUsername(loginRequest.getLogin());

        if (user.getIsBlocked()) {
            throw new ApiException(HttpStatus.FORBIDDEN, "user_blocked", "User is blocked!");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            Integer failAttempts = userService.increaseFailAttempts(loginRequest.getLogin());
            if (failAttempts.equals(MAX_FAIL_ATTEMPTS)) {
                userService.renewFailAttempts(loginRequest.getLogin());
                userService.blockUser(loginRequest.getLogin());
            }
            throw new ApiException(HttpStatus.FORBIDDEN, "incorrect_password", "Password is incorrect!");
        }

        userService.renewFailAttempts(loginRequest.getLogin());
        userService.loginUser(loginRequest.getLogin());
        return tokenHelper.generateToken(user.getUsername());
    }

    @Override
    public void logout(String username) {
        userService.logoutUser(username);
    }
}
