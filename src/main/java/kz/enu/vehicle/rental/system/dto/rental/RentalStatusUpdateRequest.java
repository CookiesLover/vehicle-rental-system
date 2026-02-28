package kz.enu.vehicle.rental.system.dto.rental;

import jakarta.validation.constraints.NotNull;
import kz.enu.vehicle.rental.system.model.RentalStatus;

public class RentalStatusUpdateRequest {
    @NotNull
    private RentalStatus status;

    public RentalStatus getStatus() {
        return status;
    }

    public void setStatus(RentalStatus status) {
        this.status = status;
    }
}
