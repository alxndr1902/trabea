package com.bc46.trabea.auth;

import com.bc46.trabea.user.User;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@RequiredArgsConstructor
@Builder
public class AuthUserDetails implements UserDetails {
    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().name()))
                .toList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getWorkEmail();
    }

//    @Override
//    public String getWorkEmail() {
//        return user.getWorkEmail();
//    }

//    @Override
//    public boolean isEnabled() {
//        return !user.getDeactivated();
//    }
//
//    public String getEmail() {
//        return user.getEmail();
//    }
}
