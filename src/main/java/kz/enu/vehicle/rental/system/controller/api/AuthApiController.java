package kz.enu.vehicle.rental.system.controller.api;

import jakarta.validation.Valid;
import kz.enu.vehicle.rental.system.dto.auth.AuthResponse;
import kz.enu.vehicle.rental.system.dto.auth.LoginRequest;
import kz.enu.vehicle.rental.system.dto.auth.RegisterRequest;
import kz.enu.vehicle.rental.system.model.User;
import kz.enu.vehicle.rental.system.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

    private final AuthService authService;

    public AuthApiController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        User user = authService.register(request);
        return new AuthResponse(user.getId(), user.getName(), user.getEmail(), user.getRole().name());
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
