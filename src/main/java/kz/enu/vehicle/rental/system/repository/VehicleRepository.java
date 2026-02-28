package kz.enu.vehicle.rental.system.repository;

import kz.enu.vehicle.rental.system.model.Vehicle;
import kz.enu.vehicle.rental.system.model.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByBrandContainingIgnoreCase(String brand);

    List<Vehicle> findByBrandContainingIgnoreCaseAndPricePerDayBetweenAndVehicleClassContainingIgnoreCaseAndYearAndStatus(
            String brand,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String vehicleClass,
            Integer year,
            VehicleStatus status
    );
}
