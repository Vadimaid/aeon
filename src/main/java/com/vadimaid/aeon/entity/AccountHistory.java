package com.vadimaid.aeon.entity;

import com.vadimaid.aeon.entity.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true, exclude = "account")

@Entity
@Table(name = "accounts_history")
public class AccountHistory extends BaseEntity {

    @Column(name = "transaction_time")
    private LocalDateTime transactionTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "spent_amount")
    private BigDecimal spentAmount;

    @Column(name = "left_balance")
    private BigDecimal leftBalance;
}
