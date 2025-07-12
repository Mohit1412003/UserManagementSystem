package com.Test.UserManagementSystem.controller;

import com.Test.UserManagementSystem.model.Users;
import com.Test.UserManagementSystem.model.loginRequest;
import com.Test.UserManagementSystem.service.JWTService;
import com.Test.UserManagementSystem.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class UserController {

    private final  UserService userService;
    private final JWTService jwtService;
    private final PasswordEncoder encoder;



    public UserController(UserService userService, JWTService jwtService, PasswordEncoder encoder) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.encoder = encoder;
    }

    @GetMapping("/auth/users")
    public ResponseEntity<List<Users>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @PostMapping("/auth/user")
    public ResponseEntity<Users> addUser(@RequestBody Users users){
        Users users1=userService.addUser(users);

        if(users1!=null){
            return new ResponseEntity<>(users1,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody loginRequest loginRequest) {
        Users user = userService.findByEmail(loginRequest.getEmail());

        if (user == null || !encoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getEmail()); // Or user.getId(), etc.

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("email", user.getEmail());
        response.put("role", user.getRole());

        return ResponseEntity.ok(response);
    }



    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/user/{userId}")
    public ResponseEntity<String> updateUserDetails(@PathVariable int userId, @RequestBody Users updateUser) {
        try {
            Users user = userService.getUserById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            user.setUsername(updateUser.getUsername());
            user.setEmail(updateUser.getEmail());
            user.setRole(updateUser.getRole());

            userService.updateUser(user);
            return new ResponseEntity<>("User Updated Successfully", HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error Updating user: " + e.getMessage());
        }
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/user/{ProductId}")
    public ResponseEntity<String> deleteUserByID(@PathVariable int id){
            Users user=userService.getUserById(id);

            if(user!=null){
                userService.deleteById(id);
                return new ResponseEntity<>("User is Deleted",HttpStatus.OK);
            }
            return new ResponseEntity<>("No such user with id exists",HttpStatus.EXPECTATION_FAILED);

    }


}
