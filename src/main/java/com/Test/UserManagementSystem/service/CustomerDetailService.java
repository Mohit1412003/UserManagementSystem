package com.Test.UserManagementSystem.service;

import com.Test.UserManagementSystem.Repository.UserRepo;
import com.Test.UserManagementSystem.model.CustomerDetails;
import com.Test.UserManagementSystem.model.Users;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class CustomerDetailService implements UserDetailsService {

    private final UserRepo repo;

    public CustomerDetailService(UserRepo repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user=repo.findByEmail(username);
        if(user!=null){
            System.out.println("User not found");
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomerDetails(user);

    }
}
