package kz.enu.vehicle.rental.system.service;

import kz.enu.vehicle.rental.system.dto.user.UserResponse;
import kz.enu.vehicle.rental.system.exception.ResourceNotFoundException;
import kz.enu.vehicle.rental.system.model.Role;
import kz.enu.vehicle.rental.system.model.User;
import kz.enu.vehicle.rental.system.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAll() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse updateRole(Long id, Role role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found id=" + id));
        user.setRole(role);
        return toResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse toggleEnabled(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found id=" + id));
        user.setEnabled(!user.isEnabled());
        return toResponse(userRepository.save(user));
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                user.getPhone(),
                user.isEnabled()
        );
    }
}
