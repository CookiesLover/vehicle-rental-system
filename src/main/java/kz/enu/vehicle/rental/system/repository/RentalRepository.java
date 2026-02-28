package kz.enu.vehicle.rental.system.repository;

import kz.enu.vehicle.rental.system.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Rental> findAllByOrderByCreatedAtDesc();
}
