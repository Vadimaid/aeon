package com.vadimaid.aeon.service;

import com.vadimaid.aeon.dto.LoginRequest;

public interface LoginService {

    String login(LoginRequest loginRequest);

    void logout(String username);
}
