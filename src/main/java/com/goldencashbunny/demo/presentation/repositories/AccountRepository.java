package com.goldencashbunny.demo.presentation.repositories;

import com.goldencashbunny.demo.presentation.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    Optional<Account> findByIdAndDeletedFalse(UUID id);

    Optional<Account> findByUserNameAndDeletedFalse(String email);

    Optional<Account> findByEmailAndDeletedFalse(String email);

    Optional<Account> findByCpfAndDeletedFalse(String cpf);

    Optional<Account> findByCnpjAndDeletedFalse(String cnpj);

    boolean existsByEmailAndEmailIsNotNull(String email);

    boolean existsByCpfAndCpfIsNotNull(String cpf);

    boolean existsByCnpjAndCnpjIsNotNull(String cnpj);
}
