package com.goldencashbunny.demo.presentation.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
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
