package com.vadimaid.aeon.controller;

import com.vadimaid.aeon.dto.AccountDto;
import com.vadimaid.aeon.dto.CreateUserDto;
import com.vadimaid.aeon.dto.UpdateUserDto;
import com.vadimaid.aeon.dto.UserDto;
import com.vadimaid.aeon.entity.User;
import com.vadimaid.aeon.service.AccountService;
import com.vadimaid.aeon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor

@RestController
@RequestMapping(value = "/api/users")
public class UserController {

    private final UserService userService;
    private final AccountService accountService;

    @PostMapping(value = "/create")
    public ResponseEntity<UserDto> createUser(
            @RequestBody CreateUserDto createUserDto
    ) {
        return ResponseEntity.ok(userService.create(createUserDto));
    }

    @PutMapping(value = "/update")
    public ResponseEntity<UserDto> updateUser(
            @AuthenticationPrincipal User user,
            @RequestBody UpdateUserDto updateUserDto
    ) {
        return ResponseEntity.ok(userService.update(user, updateUserDto));
    }

    @PostMapping(value = "/payment")
    public ResponseEntity<AccountDto> makePayment() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(accountService.makePayment(user));
    }
}
