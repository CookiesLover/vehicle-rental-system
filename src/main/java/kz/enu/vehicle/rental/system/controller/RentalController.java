package kz.enu.vehicle.rental.system.controller;

import kz.enu.vehicle.rental.system.model.Customer;
import kz.enu.vehicle.rental.system.model.Vehicle;
import kz.enu.vehicle.rental.system.service.RentalService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API — как у учителя (GET/POST).
 */
@RestController
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    // как в примере: тест что работает
    @GetMapping("/index")
    public String hello() {
        return "Hello World";
    }

    @GetMapping("/api/vehicles")
    public List<Vehicle> vehicles() {
        return rentalService.getVehicles();
    }

    @PostMapping("/api/vehicles")
    public String addVehicle(@RequestParam int id,
                             @RequestParam String brand,
                             @RequestParam String model) {
        new Vehicle(id, brand, model, "Sedan", 15000);
        return "OK: Vehicle added";
    }

    @GetMapping("/api/customers")
    public List<Customer> customers() {
        return rentalService.getCustomers();
    }

    @PostMapping("/api/customers")
    public String addCustomer(@RequestParam int id,
                              @RequestParam String fullName,
                              @RequestParam String phone) {
        rentalService.addCustomer(new Customer(id, fullName, phone));
        return "OK: Customer added";
    }

    @PostMapping("/api/rent")
    public String rent(@RequestParam int vehicleId,
                       @RequestParam int customerId) {
        return rentalService.rentVehicle(vehicleId, customerId);
    }


}