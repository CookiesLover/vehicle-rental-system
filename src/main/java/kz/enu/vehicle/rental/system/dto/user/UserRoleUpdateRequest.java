package kz.enu.vehicle.rental.system.dto.user;

import jakarta.validation.constraints.NotNull;
import kz.enu.vehicle.rental.system.model.Role;

public class UserRoleUpdateRequest {
    @NotNull
    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
