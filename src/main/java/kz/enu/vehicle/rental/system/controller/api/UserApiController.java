package kz.enu.vehicle.rental.system.controller.api;

import jakarta.validation.Valid;
import kz.enu.vehicle.rental.system.dto.user.UserRoleUpdateRequest;
import kz.enu.vehicle.rental.system.dto.user.UserResponse;
import kz.enu.vehicle.rental.system.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserApiController {

    private final UserService userService;

    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponse> getUsers() {
        return userService.getAll();
    }

    @PatchMapping("/{id}/role")
    public UserResponse updateRole(@PathVariable Long id, @Valid @RequestBody UserRoleUpdateRequest request) {
        return userService.updateRole(id, request.getRole());
    }

    @PatchMapping("/{id}/toggle-enabled")
    public UserResponse toggleEnabled(@PathVariable Long id) {
        return userService.toggleEnabled(id);
    }
}
