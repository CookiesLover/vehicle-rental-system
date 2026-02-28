package kz.enu.vehicle.rental.system.service;

import kz.enu.vehicle.rental.system.dto.car.CarRequest;
import kz.enu.vehicle.rental.system.exception.ResourceNotFoundException;
import kz.enu.vehicle.rental.system.model.Vehicle;
import kz.enu.vehicle.rental.system.model.VehicleStatus;
import kz.enu.vehicle.rental.system.repository.VehicleRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<Vehicle> findAll(String brand, BigDecimal minPrice, BigDecimal maxPrice, String vehicleClass, Integer year, VehicleStatus status) {
        return vehicleRepository.findAll().stream()
                .filter(v -> brand == null || brand.isBlank() || v.getBrand().toLowerCase().contains(brand.toLowerCase()))
                .filter(v -> minPrice == null || v.getPricePerDay().compareTo(minPrice) >= 0)
                .filter(v -> maxPrice == null || v.getPricePerDay().compareTo(maxPrice) <= 0)
                .filter(v -> vehicleClass == null || vehicleClass.isBlank() || v.getVehicleClass().equalsIgnoreCase(vehicleClass))
                .filter(v -> year == null || v.getYear().equals(year))
                .filter(v -> status == null || v.getStatus() == status)
                .toList();
    }

    public Vehicle getById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found id=" + id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Vehicle create(CarRequest request) {
        Vehicle vehicle = new Vehicle();
        fillVehicle(vehicle, request);
        vehicle.setStatus(VehicleStatus.AVAILABLE);
        return vehicleRepository.save(vehicle);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Vehicle update(Long id, CarRequest request) {
        Vehicle vehicle = getById(id);
        fillVehicle(vehicle, request);
        return vehicleRepository.save(vehicle);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(Long id) {
        Vehicle vehicle = getById(id);
        vehicleRepository.delete(vehicle);
    }

    private void fillVehicle(Vehicle vehicle, CarRequest request) {
        vehicle.setBrand(request.getBrand().trim());
        vehicle.setModel(request.getModel().trim());
        vehicle.setYear(request.getYear());
        vehicle.setVehicleClass(request.getVehicleClass().trim());
        vehicle.setTransmission(request.getTransmission().trim());
        vehicle.setFuel(request.getFuel().trim());
        vehicle.setSeats(request.getSeats());
        vehicle.setPricePerDay(request.getPricePerDay());
        vehicle.setImageUrl(request.getImageUrl().trim());
    }
}
