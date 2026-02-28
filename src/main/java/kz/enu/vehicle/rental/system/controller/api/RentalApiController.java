package kz.enu.vehicle.rental.system.controller.api;

import jakarta.validation.Valid;
import kz.enu.vehicle.rental.system.dto.rental.RentalCreateRequest;
import kz.enu.vehicle.rental.system.dto.rental.RentalStatusUpdateRequest;
import kz.enu.vehicle.rental.system.model.Rental;
import kz.enu.vehicle.rental.system.service.RentalService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalApiController {

    private final RentalService rentalService;

    public RentalApiController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public Rental create(@Valid @RequestBody RentalCreateRequest request, Authentication authentication) {
        return rentalService.createRental(authentication.getName(), request);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public List<Rental> my(Authentication authentication) {
        return rentalService.myRentals(authentication.getName());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Rental> all() {
        return rentalService.allRentals();
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public Rental updateStatus(@PathVariable Long id, @Valid @RequestBody RentalStatusUpdateRequest request) {
        return rentalService.updateStatus(id, request.getStatus());
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('USER')")
    public Rental cancel(@PathVariable Long id, Authentication authentication) {
        return rentalService.cancelMyRental(authentication.getName(), id);
    }

    @PatchMapping("/{id}/finish")
    @PreAuthorize("hasRole('ADMIN')")
    public Rental finish(@PathVariable Long id) {
        return rentalService.finishRental(id);
    }
}
