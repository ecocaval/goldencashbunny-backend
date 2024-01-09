package com.goldencashbunny.demo.infra.security;

import com.goldencashbunny.demo.core.usecases.AccountUseCase;
import com.goldencashbunny.demo.presentation.entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountUseCase accountUseCase;

    // @Lazy annotation used for breaking the cycle between securityConfig,
    // and accountUseCase, removing it will break the application!
    @Autowired
    public CustomUserDetailsService(@Lazy AccountUseCase accountUseCase) {
        this.accountUseCase = accountUseCase;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        var account = this.accountUseCase.findByLogin(login);

        return new User(login, account.getPassword(), this.mapRolesToAuthorities(account.getRoles()));
    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
        return roles.stream().map(role ->  new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
