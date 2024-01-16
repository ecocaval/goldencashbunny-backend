package com.goldencashbunny.demo.presentation.entities;

import com.goldencashbunny.demo.presentation.entities.base.BaseEntity;
import jakarta.persistence.CascadeType;
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
@ToString
public class Customer extends BaseEntity {

    @Column(nullable = false)
    private String firstName;

    @Column
    private String lastName;

    @Column(length = 11)
    private String cpf;

    @Column(length = 14)
    private String cnpj;

    @Column(nullable = false)
    private String companyName;

    @Column
    private String socialCompanyName;

    @Column
    private String email;

    @Column
    private String phone;

    @OneToOne(cascade = CascadeType.ALL)
    private Address Address;

}
