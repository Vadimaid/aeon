package com.vadimaid.aeon.entity;

import com.vadimaid.aeon.entity.base.TimedEntity;
import lombok.*;

import javax.persistence.*;
import java.security.Principal;

@Data
@EqualsAndHashCode(callSuper = true)

@Entity
@Table(name = "users")
public class User extends TimedEntity implements Principal {

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_blocked")
    private Boolean isBlocked;

    @Column(name = "fail_attempts")
    private Integer failAttempts;

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "user_id")
    private Account account;

    @Override
    public String getName() {
        return this.username;
    }
}
