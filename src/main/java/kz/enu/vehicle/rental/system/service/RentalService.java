package kz.enu.vehicle.rental.system.service;

import kz.enu.vehicle.rental.system.dto.rental.RentalCreateRequest;
import kz.enu.vehicle.rental.system.exception.ConflictException;
import kz.enu.vehicle.rental.system.exception.ResourceNotFoundException;
import kz.enu.vehicle.rental.system.model.*;
import kz.enu.vehicle.rental.system.repository.RentalRepository;
import kz.enu.vehicle.rental.system.repository.UserRepository;
import kz.enu.vehicle.rental.system.repository.VehicleRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    public RentalService(RentalRepository rentalRepository, UserRepository userRepository, VehicleRepository vehicleRepository) {
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @PreAuthorize("hasRole('USER')")
    public Rental createRental(String userEmail, RentalCreateRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Vehicle vehicle = vehicleRepository.findById(request.getCarId())
                .orElseThrow(() -> new ResourceNotFoundException("Car not found id=" + request.getCarId()));

        if (vehicle.getStatus() != VehicleStatus.AVAILABLE) {
            throw new ConflictException("Car is not available");
        }

        if (!request.getEndDate().isAfter(request.getStartDate())) {
            throw new IllegalArgumentException("endDate must be after startDate");
        }

        long days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate());
        BigDecimal totalPrice = vehicle.getPricePerDay().multiply(BigDecimal.valueOf(days));

        Rental rental = new Rental();
        rental.setUser(user);
        rental.setVehicle(vehicle);
        rental.setStartDate(request.getStartDate());
        rental.setEndDate(request.getEndDate());
        rental.setDays((int) days);
        rental.setTotalPrice(totalPrice);
        rental.setStatus(RentalStatus.NEW);

        return rentalRepository.save(rental);
    }

    @PreAuthorize("hasRole('USER')")
    public List<Rental> myRentals(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return rentalRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<Rental> allRentals() {
        return rentalRepository.findAllByOrderByCreatedAtDesc();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Rental updateStatus(Long rentalId, RentalStatus targetStatus) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new ResourceNotFoundException("Rental not found id=" + rentalId));

        Vehicle vehicle = rental.getVehicle();
        RentalStatus current = rental.getStatus();

        if (current == RentalStatus.CANCELLED || current == RentalStatus.FINISHED || current == RentalStatus.REJECTED) {
            throw new ConflictException("Cannot change final rental status");
        }

        if (targetStatus == RentalStatus.APPROVED || targetStatus == RentalStatus.ACTIVE) {
            if (vehicle.getStatus() == VehicleStatus.AVAILABLE) {
                vehicle.setStatus(VehicleStatus.RENTED);
            } else if (vehicle.getStatus() == VehicleStatus.RENTED
                    && (current == RentalStatus.APPROVED || current == RentalStatus.ACTIVE)) {
                // Allowed transition for the same active rental lifecycle.
            } else {
                throw new ConflictException("Car cannot be moved to this status because it is not available");
            }
        }

        if (targetStatus == RentalStatus.REJECTED || targetStatus == RentalStatus.CANCELLED || targetStatus == RentalStatus.FINISHED) {
            vehicle.setStatus(VehicleStatus.AVAILABLE);
        }

        rental.setStatus(targetStatus);
        vehicleRepository.save(vehicle);
        return rentalRepository.save(rental);
    }

    @PreAuthorize("hasRole('USER')")
    public Rental cancelMyRental(String userEmail, Long rentalId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new ResourceNotFoundException("Rental not found id=" + rentalId));

        if (!rental.getUser().getId().equals(user.getId())) {
            throw new ConflictException("Cannot cancel another user's rental");
        }

        if (rental.getStatus() == RentalStatus.FINISHED || rental.getStatus() == RentalStatus.CANCELLED || rental.getStatus() == RentalStatus.REJECTED) {
            throw new ConflictException("Rental cannot be cancelled in this status");
        }

        rental.setStatus(RentalStatus.CANCELLED);
        if (rental.getVehicle().getStatus() == VehicleStatus.RENTED) {
            rental.getVehicle().setStatus(VehicleStatus.AVAILABLE);
            vehicleRepository.save(rental.getVehicle());
        }

        return rentalRepository.save(rental);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Rental finishRental(Long rentalId) {
        return updateStatus(rentalId, RentalStatus.FINISHED);
    }
}
