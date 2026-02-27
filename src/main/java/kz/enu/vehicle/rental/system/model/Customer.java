package kz.enu.vehicle.rental.system.model;

/**
 * Клиент.
 */
public class Customer {
    private int id;
    private String fullName;
    private String phone;

    public Customer() { }

    public Customer(int id, String fullName, String phone) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
    }

    public int getId() { return id; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }

    public void setId(int id) { this.id = id; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setPhone(String phone) { this.phone = phone; }
}