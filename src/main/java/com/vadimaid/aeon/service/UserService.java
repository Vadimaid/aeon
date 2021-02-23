package com.vadimaid.aeon.service;

import com.vadimaid.aeon.dto.CreateUserDto;
import com.vadimaid.aeon.dto.UpdateUserDto;
import com.vadimaid.aeon.dto.UserDto;
import com.vadimaid.aeon.entity.User;

public interface UserService {

    User getUserByUsername(String username);

    Integer increaseFailAttempts(String username);

    void renewFailAttempts(String username);

    void blockUser(String username);

    void loginUser(String username);

    void logoutUser(String username);

    UserDto create(CreateUserDto source);

    UserDto update(User user, UpdateUserDto source);

}
