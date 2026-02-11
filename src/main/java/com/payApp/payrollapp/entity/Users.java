package com.payApp.payrollapp.entity;

import com.payApp.payrollapp.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.payApp.payrollapp.entity.Employee;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class Users implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private int failedLoginAttempts;
    private boolean isAccountLocked;
    private LocalDateTime accountLockTime;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isAccountLocked;
    }

    // this method is used to unlock the account if required
    public void unlockAccountIfNeeded() {
        if (isAccountLocked && accountLockTime != null) {
            long lockDuration = Duration.between(accountLockTime, LocalDateTime.now()).toMinutes();

            if (lockDuration > 15) {
                this.isAccountLocked = false;
                this.failedLoginAttempts = 0;
                this.accountLockTime = null;
            }
        }
    }

}
