package com.vadimaid.aeon.controller;

import com.vadimaid.aeon.dto.LoginRequest;
import com.vadimaid.aeon.entity.User;
import com.vadimaid.aeon.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor

@RestController
@RequestMapping(value = "/api/auth")
public class LoginController {

    private final LoginService loginService;

    @PostMapping(value = "/login")
    public ResponseEntity<String> login(
            @RequestBody LoginRequest loginRequest
    ) {
        return ResponseEntity.ok(loginService.login(loginRequest));
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<Boolean> logout() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        loginService.logout(user.getUsername());
        return ResponseEntity.ok(Boolean.TRUE);
    }

}
