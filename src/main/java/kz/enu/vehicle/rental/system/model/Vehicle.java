package kz.enu.vehicle.rental.system.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Автомобиль.
 */
public class Vehicle {
    @Min(value = 1, message = "id must be greater than 0")
    private int id;

    @NotBlank(message = "brand must not be blank")
    private String brand;

    @NotBlank(message = "model must not be blank")
    private String model;

    @NotBlank(message = "type must not be blank")
    private String type;

    @NotNull(message = "pricePerDay is required")
    @Min(value = 1, message = "pricePerDay must be greater than 0")
    private Double pricePerDay;

    @NotBlank(message = "imageUrl must not be blank")
    private String imageUrl;

    private boolean rented;
    private Integer rentedByCustomerId; // null если свободен

    public Vehicle() {
    }

    public Vehicle(int id, String brand, String model) {
        this(id, brand, model, "Sedan", 15000,
                "https://images.unsplash.com/photo-1549924231-f129b911e442?auto=format&fit=crop&w=1200&q=60");
    }

    public Vehicle(int id, String brand, String model, String type, double pricePerDay) {
        this(id, brand, model, type, pricePerDay,
                "https://images.unsplash.com/photo-1549924231-f129b911e442?auto=format&fit=crop&w=1200&q=60");
    }

    public Vehicle(int id, String brand, String model, String type, double pricePerDay, String imageUrl) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.type = type;
        this.pricePerDay = pricePerDay;
        this.imageUrl = imageUrl;
        this.rented = false;
        this.rentedByCustomerId = null;
    }

    public int getId() { return id; }

    public String getBrand() { return brand; }

    public String getModel() { return model; }

    public String getType() { return type; }

    public Double getPricePerDay() { return pricePerDay; }

    public String getImageUrl() { return imageUrl; }

    public boolean isRented() { return rented; }

    public Integer getRentedByCustomerId() { return rentedByCustomerId; }

    public void setId(int id) { this.id = id; }

    public void setBrand(String brand) { this.brand = brand; }

    public void setModel(String model) { this.model = model; }

    public void setType(String type) { this.type = type; }

    public void setPricePerDay(Double pricePerDay) { this.pricePerDay = pricePerDay; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public void setRented(boolean rented) { this.rented = rented; }

    public void setRentedByCustomerId(Integer rentedByCustomerId) { this.rentedByCustomerId = rentedByCustomerId; }
}