package com.helpdeskpro.user;

import com.helpdeskpro.security.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthController(UserRepository userRepository, JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody Map<String, String> loginRequest) {
        String userId = loginRequest.get("userId"); // Using ID as username for simplicity in this project
        String password = loginRequest.get("password");

        Optional<User> userOptional = userRepository.findById(userId);
        
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Since we used a hardcoded hash in User.java constructor for backwards compatibility:
            if (passwordEncoder.matches(password, user.getPassword())) {
                String jwt = tokenProvider.generateToken(user.getId());
                return ResponseEntity.ok(Map.of(
                        "accessToken", jwt,
                        "userId", user.getId(),
                        "name", user.getName(),
                        "role", user.getRole().name()
                ));
            }
        }
        
        return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
    }
}
