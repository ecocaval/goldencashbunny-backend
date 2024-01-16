package com.goldencashbunny.demo.presentation.entities;

import com.goldencashbunny.demo.core.data.enums.AccountRole;
import com.goldencashbunny.demo.presentation.entities.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@ToString
public class Role extends BaseEntity {

    @Column(length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountRole name;

    public String getName() {
        return this.name.getRole();
    }
}
