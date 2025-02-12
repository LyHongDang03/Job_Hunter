package com.example.Job_Hunter.config;

import com.example.Job_Hunter.service.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component("userDetailsService")
public class UserDetailCustom implements UserDetailsService {
    private final UserService userService;

    public UserDetailCustom(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.example.Job_Hunter.domain.Entity.User user = userService.getUserByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
