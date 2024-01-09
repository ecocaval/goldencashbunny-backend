package com.goldencashbunny.demo.presentation.entities;

import com.goldencashbunny.demo.core.data.enums.AccountRole;
import com.goldencashbunny.demo.core.data.requests.CreateAccountRequest;
import com.goldencashbunny.demo.core.data.requests.UpdateAccountRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class Role extends BaseEntity {

    @Column(length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountRole name;

    public String getName() {
        return this.name.getRole();
    }
}
