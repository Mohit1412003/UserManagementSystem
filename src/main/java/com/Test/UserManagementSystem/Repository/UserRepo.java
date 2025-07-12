package com.Test.UserManagementSystem.Repository;

import com.Test.UserManagementSystem.model.Users;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<Users, Integer> {
    Users findByEmail(String username);
}
