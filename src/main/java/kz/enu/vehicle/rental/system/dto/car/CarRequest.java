package kz.enu.vehicle.rental.system.dto.car;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class CarRequest {
    @NotBlank
    private String brand;

    @NotBlank
    private String model;

    @NotNull
    @Min(1980)
    private Integer year;

    @NotBlank
    private String vehicleClass;

    @NotBlank
    private String transmission;

    @NotBlank
    private String fuel;

    @NotNull
    @Min(1)
    private Integer seats;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal pricePerDay;

    @NotBlank
    private String imageUrl;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getVehicleClass() {
        return vehicleClass;
    }

    public void setVehicleClass(String vehicleClass) {
        this.vehicleClass = vehicleClass;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    public BigDecimal getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(BigDecimal pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
