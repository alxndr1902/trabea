package com.bc46.trabea.auth;


import com.bc46.trabea.auth.dto.AuthPartTimeRegisterRequest;
import com.bc46.trabea.auth.dto.AuthPartTimeRegisterResponse;
import com.bc46.trabea.auth.dto.AuthLoginRequest;
import com.bc46.trabea.auth.dto.AuthLoginResponse;
import com.bc46.trabea.error.exception.ConflictException;
import com.bc46.trabea.error.exception.ResourceNotFoundException;
import com.bc46.trabea.error.exception.RoleDenyException;
import com.bc46.trabea.error.exception.WorkEmailAndPasswordIncorrectException;
import com.bc46.trabea.jwt.JwtService;
import com.bc46.trabea.parttimeemployee.PartTimeEmployee;
import com.bc46.trabea.parttimeemployee.PartTimeEmployeeRepository;
import com.bc46.trabea.role.Role;
import com.bc46.trabea.role.RoleName;
import com.bc46.trabea.role.RoleRepository;
import com.bc46.trabea.user.User;
import com.bc46.trabea.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PartTimeEmployeeRepository  partTimeEmployeeRepository;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;

    public AuthPartTimeRegisterResponse registerPartTimeEmployee(AuthPartTimeRegisterRequest request) {
        if (partTimeEmployeeRepository.existsByPersonalEmail(request.getPersonalEmail())) {
            throw new ConflictException("Email is unavailable");
        }
        if (partTimeEmployeeRepository.existsByPersonalPhoneNumber(request.getPersonalPhoneNumber())) {
            throw new ConflictException("Phone Number is unavailable");
        }

        User user = createUser(request);
        User savedUser = userRepository.save(user);

        PartTimeEmployee partTimeEmployee = createPartTimeEmployee(request, savedUser);
        PartTimeEmployee savedPartTimeEmployee = partTimeEmployeeRepository.save(partTimeEmployee);

        return AuthPartTimeRegisterResponse.builder()
                .workEmail(savedUser.getWorkEmail())
                .firstName(savedPartTimeEmployee.getFirstName())
                .lastName(savedPartTimeEmployee.getLastName())
                .build();
    }

    public AuthLoginResponse login(AuthLoginRequest request) {
        var user = userRepository.findByWorkEmail(request.getWorkEmail())
                .orElseThrow(() -> new WorkEmailAndPasswordIncorrectException("Work Email or Password is incorrect"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new WorkEmailAndPasswordIncorrectException("Work Email or password is incorrect");
        }

        if (!hasRole(user, request.getRole())) {
            throw new RoleDenyException("You are not authrorized to login with this role!");
        }

        RoleName role = request.getRole();

        return AuthLoginResponse.builder()
                .token(jwtService.generateToken(user, role))
                .build();
    }

    private boolean hasRole(User user, RoleName roleRequest) {
        return user.getRoles().stream()
                .map(Role::getName)
                .anyMatch(roleRequest::equals);
//              .anyMatch(roleName -> roleName.equals(request.getRole()));
    }

    private User createUser(AuthPartTimeRegisterRequest request) {
        User user = new User();
        user.setWorkEmail(request.getFirstName() + "." + request.getLastName() + "@trabea.co.id");
        user.setPassword(passwordEncoder.encode("Trabea123"));
        Role role = roleRepository.findByName(RoleName.PARTTIMER)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        user.addRole(role);
        return user;
    }

    private PartTimeEmployee createPartTimeEmployee(AuthPartTimeRegisterRequest request, User savedUser) {
        PartTimeEmployee partTimeEmployee = authMapper.toPartTimeEmployee(request);
        partTimeEmployee.setUser(savedUser);
        partTimeEmployee.setJoinDate(LocalDateTime.now());
        return partTimeEmployee;
    }
}
