package com.wasup.mytasks.model;

import com.wasup.mytasks.model.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class AuthUser implements UserDetails {

    User user;

    private static final String AUTHORITY_PREFIX = "ROLE_";

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> expandedRoles = Role.expand(user.getRoles());
        return expandedRoles.stream()
                .map(Role::name)
                .map(roleName -> new SimpleGrantedAuthority(AUTHORITY_PREFIX + roleName))
                .collect(Collectors.toUnmodifiableSet());

    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
