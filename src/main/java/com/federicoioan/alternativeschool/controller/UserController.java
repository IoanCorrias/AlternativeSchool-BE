package com.federicoioan.alternativeschool.controller;

import com.federicoioan.alternativeschool.model.User;
import com.federicoioan.alternativeschool.model.dto.NewPasswordDto;
import com.federicoioan.alternativeschool.model.dto.UserRegistrationDto;
import com.federicoioan.alternativeschool.service.AuthServiceImpl;
import com.federicoioan.alternativeschool.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    private AuthServiceImpl authService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRegistrationDto user) {
        try {
            User registeredUser = authService.registerUser(user);
            return ResponseEntity.ok(registeredUser);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getUser() {
        try {
            List<User> users = userService.findUsers();
            return ResponseEntity.ok(users);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable Long userId) {
        try {
            User user = userService.findUser(userId);
            return ResponseEntity.ok(user);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{userId}/update-password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updatePwd(@PathVariable Long userId, @RequestBody NewPasswordDto password) {
        try {
            User updatedUser = userService.updatePwd(userId, password);
            return ResponseEntity.ok(updatedUser);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        try {
			User deleteUser = userService.deleteUser(userId);
			return ResponseEntity.ok(deleteUser);

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
    }

    @GetMapping("/identify")
    public ResponseEntity<?> getActualUser() {
        try {
            return ResponseEntity.ok(userService.getUserWithAuthorities()
                    .orElseThrow(() -> new IllegalArgumentException("User not found")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
