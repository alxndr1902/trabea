package com.bc46.trabea.auth;


import com.bc46.trabea.auth.dto.AuthPartTimeRegisterRequest;
import com.bc46.trabea.auth.dto.AuthPartTimeRegisterResponse;
import com.bc46.trabea.auth.dto.AuthLoginRequest;
import com.bc46.trabea.auth.dto.AuthLoginResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PreAuthorize("#username == authentication.name")
    @GetMapping("{username}")
    public ResponseEntity<Authentication> getByUsername(@PathVariable String username, Authentication authentication){
        return ResponseEntity.ok(authentication);
    }

    @PostMapping("/employee/part-time/register")
    public ResponseEntity<AuthPartTimeRegisterResponse> register(@Valid @RequestBody AuthPartTimeRegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerPartTimeEmployee(request));
    }

    @PostMapping("login")
    public ResponseEntity<AuthLoginResponse> createToken(@RequestBody AuthLoginRequest request) {

        return ResponseEntity.ok(authService.login(request));
    }
}
