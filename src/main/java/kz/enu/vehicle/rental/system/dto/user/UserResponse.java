package kz.enu.vehicle.rental.system.dto.user;

public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String role;
    private String phone;
    private boolean enabled;

    public UserResponse(Long id, String name, String email, String role, String phone, boolean enabled) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.phone = phone;
        this.enabled = enabled;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
