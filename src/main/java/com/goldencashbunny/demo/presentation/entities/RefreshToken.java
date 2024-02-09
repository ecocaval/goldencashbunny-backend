package com.goldencashbunny.demo.presentation.entities;

import com.goldencashbunny.demo.core.data.requests.CreateAccountRequest;
import com.goldencashbunny.demo.core.data.requests.UpdateAccountRequest;
import com.goldencashbunny.demo.presentation.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@ToString
@SQLDelete(sql = "UPDATE refresh_token SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
public class RefreshToken extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    public static RefreshToken fromAccount(Account account) {
        return RefreshToken.builder()
                .account(account)
                .creationDate(LocalDateTime.now())
                .build();
    }

}
