package com.Test.UserManagementSystem.service;

import com.Test.UserManagementSystem.Repository.UserRepo;
import com.Test.UserManagementSystem.model.Users;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepo repo;
    private final PasswordEncoder encoder;

    public UserService(UserRepo repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public List<Users> getAllUsers() {
        return repo.findAll();
    }

    public Users addUser(Users users) {
        users.setPassword(encoder.encode(users.getPassword())); 
        return repo.save(users);
    }

    public Users getUserById(int userId) {
        return repo.findById(userId).orElse(null);
    }

    public Users updateUser(Users user) {
       return  repo.save(user);
    }

    public void deleteById(int id) {
        Users user=getUserById(id);
        repo.deleteById(id);
    }

    public Users findByEmail(String email) {
        return repo.findByEmail(email);
    }
}
