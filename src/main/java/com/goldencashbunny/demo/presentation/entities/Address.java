package com.goldencashbunny.demo.presentation.entities;

import com.goldencashbunny.demo.presentation.entities.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class Address extends BaseEntity {

    @Column(length = 8, nullable = false)
    private String zipCode;

    @Column(length = 100, nullable = false)
    private String address;

    @Column(length = 6)
    private String number;

    @Column(length = 50, nullable = false)
    private String neighborhood;

    @Column(length = 50)
    private String complement;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @OneToOne(optional = false)
    private Customer customer;
}
