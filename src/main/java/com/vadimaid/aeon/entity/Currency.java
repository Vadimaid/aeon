package com.vadimaid.aeon.entity;

import com.vadimaid.aeon.entity.base.TimedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)

@Entity
@Table(name = "currency")
public class Currency extends TimedEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;
}
