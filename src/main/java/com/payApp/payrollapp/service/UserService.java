package com.payApp.payrollapp.service;

import com.payApp.payrollapp.entity.Users;
import com.payApp.payrollapp.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /* It is to handle the user login failed attempts
     * handleFailedLogin will check for login failed attempts
     * and increase upto 3 if more than 3
     * account will locked for 15 minutes
     */

    public void handleFailedLogin(Users user){
        int failedAttempts = user.getFailedLoginAttempts();

        user.setFailedLoginAttempts(failedAttempts + 1);
        user.setAccountLockTime(LocalDateTime.now());

        if (failedAttempts + 1 >= 3) {  // Lock account after 3 failed attempts
            user.setAccountLocked(true);
            user.setAccountLockTime(LocalDateTime.now());  // Set lock time
        }
        userRepository.save(user);
    }

    /* This method is to check for last lock time
     * if the lock time is more then 15 minutes
     * of current time automatically it will unlock
     * and allow the user to login back
     */

    public boolean isAccountUnlocked(Users user) {
        user.unlockAccountIfNeeded();

        if (!user.isAccountLocked()) {
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public void onLoginSuccess(Users user) {
        if(user.getFailedLoginAttempts()>0){
            user.setFailedLoginAttempts(0);
            user.setAccountLockTime(LocalDateTime.now());
        }
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
