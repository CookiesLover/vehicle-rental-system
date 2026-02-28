package kz.enu.vehicle.rental.system.service;

import kz.enu.vehicle.rental.system.dto.auth.AuthResponse;
import kz.enu.vehicle.rental.system.dto.auth.LoginRequest;
import kz.enu.vehicle.rental.system.dto.auth.RegisterRequest;
import kz.enu.vehicle.rental.system.exception.ConflictException;
import kz.enu.vehicle.rental.system.exception.ResourceNotFoundException;
import kz.enu.vehicle.rental.system.model.Role;
import kz.enu.vehicle.rental.system.model.User;
import kz.enu.vehicle.rental.system.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email already exists");
        }

        User user = new User();
        user.setName(request.getName().trim());
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone().trim());
        user.setRole(Role.ROLE_USER);
        user.setEnabled(true);

        return userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return new AuthResponse(user.getId(), user.getName(), user.getEmail(), user.getRole().name());
    }
}
