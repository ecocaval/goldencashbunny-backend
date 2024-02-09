package com.goldencashbunny.demo.presentation.repositories;

import com.goldencashbunny.demo.presentation.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    Optional<Account> findByUserName(String email);

    Optional<Account> findByEmail(String email);

    Optional<Account> findByCpf(String cpf);

    Optional<Account> findByCnpjAndDeletedFalse(String cnpj);

    boolean existsByUserNameAndUserNameIsNotNull(String userName);

    boolean existsByEmailAndEmailIsNotNull(String email);

    boolean existsByCpfAndCpfIsNotNull(String cpf);

    boolean existsByCnpjAndCnpjIsNotNull(String cnpj);
}
