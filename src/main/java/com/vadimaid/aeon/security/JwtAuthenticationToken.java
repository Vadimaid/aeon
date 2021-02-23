package com.vadimaid.aeon.security;

import com.vadimaid.aeon.entity.User;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private User user;

    public JwtAuthenticationToken(User user) {
        super(Collections.singletonList(new SimpleGrantedAuthority("ALL")));
        this.user = user;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return user.getUsername();
    }

    @Override
    public Object getPrincipal() {
        return this.user;
    }
}
