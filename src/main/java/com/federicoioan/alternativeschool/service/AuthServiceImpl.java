package com.federicoioan.alternativeschool.service;

import com.federicoioan.alternativeschool.model.ERole;
import com.federicoioan.alternativeschool.model.Role;
import com.federicoioan.alternativeschool.model.User;
import com.federicoioan.alternativeschool.model.dto.JwtDto;
import com.federicoioan.alternativeschool.model.dto.UserLoginDto;
import com.federicoioan.alternativeschool.model.dto.UserRegistrationDto;
import com.federicoioan.alternativeschool.repository.RoleRepository;
import com.federicoioan.alternativeschool.repository.UserRepository;
import com.federicoioan.alternativeschool.service.IService.AuthService;
import com.federicoioan.alternativeschool.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    public JwtDto authenticateUser(UserLoginDto loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        User user = userRepository.findOneWithAuthoritiesByUsername(loginRequest.getUsername()).get();

        return new JwtDto(jwt, user.getId(), user.getUsername(), user.getEmail(), user.getRoles());

    }

    public User registerUser(UserRegistrationDto signUpRequest) {

        // Check if username already exists
        if (userRepository.existsByUsername(signUpRequest.getUsername()))
            throw new RuntimeException("Username already exists");

        // Check if email already exists
        if (userRepository.existsByEmail(signUpRequest.getEmail()))
            throw new RuntimeException("Email already exists");

        // Create new user's account
        User user = new User(signUpRequest.getName(),
                signUpRequest.getSurname(),
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null)
            throw new RuntimeException("User must have at least one role");

        // Check if roles are valid
        strRoles.forEach(role -> {
            switch (role) {
                case "admin":
                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(adminRole);

                    break;
                case "tutor":
                    Role modRole = roleRepository.findByName(ERole.ROLE_TUTOR)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(modRole);

                    break;
                case "student":
                    Role userRole = roleRepository.findByName(ERole.ROLE_STUDENT)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(userRole);

                    break;
                default:
                    throw new RuntimeException("Error: Role is not valid.");
            }
        });

        user.setRoles(roles);
        userRepository.save(user);

        return user;
    }
}