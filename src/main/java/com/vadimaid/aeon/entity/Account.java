package com.vadimaid.aeon.entity;

import com.vadimaid.aeon.entity.base.TimedEntity;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true, exclude = "user")

@Entity
@Table(name = "accounts")
public class Account extends TimedEntity {

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "balance")
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
    private List<AccountHistory> accountHistoryList;
}
