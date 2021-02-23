package com.vadimaid.aeon.service.impl;

import com.querydsl.core.BooleanBuilder;
import com.vadimaid.aeon.dto.CreateUserDto;
import com.vadimaid.aeon.dto.UpdateUserDto;
import com.vadimaid.aeon.dto.UserDto;
import com.vadimaid.aeon.entity.QUser;
import com.vadimaid.aeon.entity.User;
import com.vadimaid.aeon.repository.UserRepository;
import com.vadimaid.aeon.service.AccountService;
import com.vadimaid.aeon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountService accountService;

    @Override
    public User getUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            throw new IllegalArgumentException("No user with username " + username);
        }
        return user.get();
    }

    @Transactional
    @Override
    public Integer increaseFailAttempts(String username) {
        User user = getUserByUsername(username);
        user.setFailAttempts(user.getFailAttempts() + 1);
        userRepository.save(user);
        return user.getFailAttempts();
    }

    @Transactional
    @Override
    public void renewFailAttempts(String username) {
        User user = getUserByUsername(username);
        user.setFailAttempts(0);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void blockUser(String username) {
        User user = getUserByUsername(username);
        user.setIsBlocked(Boolean.TRUE);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void loginUser(String username) {
        User user = getUserByUsername(username);
        user.setIsActive(Boolean.TRUE);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void logoutUser(String username) {
        User user = getUserByUsername(username);
        user.setIsActive(Boolean.FALSE);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public UserDto create(CreateUserDto source) {
        BooleanBuilder builder = new BooleanBuilder();
        QUser root = QUser.user;

        builder.and(root.username.eq(source.getUsername()));
        Optional<User> possibleDuplicate = userRepository.findOne(builder.getValue());
        if (possibleDuplicate.isPresent()) {
            throw new IllegalArgumentException("User with username " + source.getUsername() + " already exists");
        }

        User user = new User();
        user.setUsername(source.getUsername());
        user.setPassword(passwordEncoder.encode(source.getPassword()));
        user.setFirstName(source.getFirstName());
        user.setLastName(source.getLastName());
        user.setIsActive(Boolean.TRUE);
        user.setIsBlocked(Boolean.FALSE);
        user.setFailAttempts(0);

        userRepository.save(user);
        accountService.create(user);

        return UserDto
                .builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .isActive(user.getIsActive())
                .isBlocked(user.getIsBlocked())
                .build();
    }

    @Transactional
    @Override
    public UserDto update(User user, UpdateUserDto source) {
        if (Objects.isNull(user)) {
            throw new IllegalArgumentException("User cannot be null to update");
        }

        user.setPassword(passwordEncoder.encode(source.getPassword()));
        user.setFirstName(source.getFirstName());
        user.setLastName(source.getLastName());
        user.setIsActive(source.getIsActive());
        user.setIsBlocked(source.getIsBlocked());
        userRepository.save(user);

        return UserDto
                .builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .isActive(user.getIsActive())
                .isBlocked(user.getIsBlocked())
                .build();
    }
}
