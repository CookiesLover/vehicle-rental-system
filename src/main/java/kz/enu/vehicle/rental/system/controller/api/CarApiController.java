package kz.enu.vehicle.rental.system.controller.api;

import jakarta.validation.Valid;
import kz.enu.vehicle.rental.system.dto.car.CarRequest;
import kz.enu.vehicle.rental.system.model.Vehicle;
import kz.enu.vehicle.rental.system.model.VehicleStatus;
import kz.enu.vehicle.rental.system.service.VehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarApiController {

    private final VehicleService vehicleService;

    public CarApiController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public List<Vehicle> getCars(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String vehicleClass,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) VehicleStatus status
    ) {
        return vehicleService.findAll(brand, minPrice, maxPrice, vehicleClass, year, status);
    }

    @GetMapping("/{id}")
    public Vehicle getCar(@PathVariable Long id) {
        return vehicleService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public Vehicle createCar(@Valid @RequestBody CarRequest request) {
        return vehicleService.create(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Vehicle updateCar(@PathVariable Long id, @Valid @RequestBody CarRequest request) {
        return vehicleService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCar(@PathVariable Long id) {
        vehicleService.delete(id);
    }
}
