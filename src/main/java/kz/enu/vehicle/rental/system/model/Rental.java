package kz.enu.vehicle.rental.system.model;

import java.time.LocalDateTime;

/**
 * Аренда (одна запись аренды).
 */
public class Rental {
    private int id;
    private int vehicleId;
    private int customerId;
    private LocalDateTime rentedAt;
    private LocalDateTime returnedAt; // null если активная аренда

    public Rental(int id, int vehicleId, int customerId) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.customerId = customerId;
        this.rentedAt = LocalDateTime.now();
        this.returnedAt = null;
    }

    public int getId() { return id; }
    public int getVehicleId() { return vehicleId; }
    public int getCustomerId() { return customerId; }
    public LocalDateTime getRentedAt() { return rentedAt; }
    public LocalDateTime getReturnedAt() { return returnedAt; }

    public boolean isActive() {
        return returnedAt == null;
    }

    public void markReturned() {
        this.returnedAt = LocalDateTime.now();
    }
}