package kz.enu.vehicle.rental.system.service;

import kz.enu.vehicle.rental.system.exception.ConflictException;
import kz.enu.vehicle.rental.system.exception.ResourceNotFoundException;
import kz.enu.vehicle.rental.system.model.Customer;
import kz.enu.vehicle.rental.system.model.Rental;
import kz.enu.vehicle.rental.system.model.Vehicle;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Вся логика системы аренды. Хранение в памяти (ArrayList).
 */
@Service
public class RentalService {

    private final List<Vehicle> vehicles = new ArrayList<>();
    private final List<Customer> customers = new ArrayList<>();
    private final List<Rental> rentals = new ArrayList<>();

    private int rentalIdCounter = 1;

    // ------------------- Vehicles (REST CRUD) -------------------

    public List<Vehicle> getAllVehicles() {
        return vehicles;
    }

    public Vehicle getVehicleById(int id) {
        Vehicle vehicle = findVehicleById(id);
        if (vehicle == null) {
            throw new ResourceNotFoundException("Vehicle not found with id=" + id);
        }
        return vehicle;
    }

    public Vehicle createVehicle(Vehicle vehicle) {
        if (findVehicleById(vehicle.getId()) != null) {
            throw new ConflictException("Vehicle with id=" + vehicle.getId() + " already exists");
        }
        vehicles.add(vehicle);
        return vehicle;
    }

    public Vehicle updateVehicle(int id, Vehicle payload) {
        Vehicle existing = getVehicleById(id);
        existing.setBrand(payload.getBrand());
        existing.setModel(payload.getModel());
        existing.setType(payload.getType());
        existing.setPricePerDay(payload.getPricePerDay());
        existing.setImageUrl(payload.getImageUrl());
        return existing;
    }

    public void deleteVehicleById(int id) {
        Vehicle vehicle = getVehicleById(id);
        if (vehicle.isRented()) {
            throw new ConflictException("Cannot delete vehicle that is currently rented");
        }
        vehicles.remove(vehicle);
    }

    // ------------------- Vehicles (legacy methods for web pages) -------------------

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public Vehicle findVehicleById(int id) {
        for (Vehicle v : vehicles) {
            if (v.getId() == id) return v;
        }
        return null;
    }

    public boolean addVehicle(Vehicle v) {
        if (findVehicleById(v.getId()) != null) return false;
        vehicles.add(v);
        return true;
    }

    public String updateVehicle(int id, String brand, String model, String type, double pricePerDay, String imageUrl) {
        Vehicle v = findVehicleById(id);
        if (v == null) return "ОШИБКА: Авто не найдено (id=" + id + ")";

        v.setBrand(brand);
        v.setModel(model);
        v.setType(type);
        v.setPricePerDay(pricePerDay);
        v.setImageUrl(imageUrl);

        return "ОК: Автомобиль обновлён";
    }

    public String deleteVehicle(int id) {
        Vehicle v = findVehicleById(id);
        if (v == null) return "ОШИБКА: Авто не найдено";
        if (v.isRented()) return "ОШИБКА: Нельзя удалить авто, пока оно в аренде";

        vehicles.remove(v);
        return "ОК: Автомобиль удалён";
    }

    // ------------------- Customers -------------------

    public List<Customer> getCustomers() {
        return customers;
    }

    public Customer findCustomerById(int id) {
        for (Customer c : customers) {
            if (c.getId() == id) return c;
        }
        return null;
    }

    public boolean addCustomer(Customer c) {
        if (findCustomerById(c.getId()) != null) return false;
        customers.add(c);
        return true;
    }

    public String deleteCustomer(int id) {
        Customer c = findCustomerById(id);
        if (c == null) return "ОШИБКА: Клиент не найден";

        for (Vehicle v : vehicles) {
            if (v.isRented() && v.getRentedByCustomerId() != null && v.getRentedByCustomerId() == id) {
                return "ОШИБКА: Нельзя удалить клиента — у него есть активные аренды";
            }
        }

        customers.remove(c);
        return "ОК: Клиент удалён";
    }

    // ------------------- Rent / Return -------------------

    public String rentVehicle(int vehicleId, int customerId) {
        Vehicle v = findVehicleById(vehicleId);
        if (v == null) return "ОШИБКА: Авто не найдено (id=" + vehicleId + ")";

        Customer c = findCustomerById(customerId);
        if (c == null) return "ОШИБКА: Клиент не найден (id=" + customerId + ")";

        if (v.isRented()) return "ОШИБКА: Это авто уже в аренде";

        v.setRented(true);
        v.setRentedByCustomerId(customerId);
        rentals.add(new Rental(rentalIdCounter++, vehicleId, customerId));

        return "ОК: Авто арендовано";
    }

    public String returnVehicle(int vehicleId, int customerId) {
        Vehicle v = findVehicleById(vehicleId);
        if (v == null) return "ОШИБКА: Авто не найдено";
        if (!v.isRented()) return "ОШИБКА: Авто сейчас не в аренде";

        if (v.getRentedByCustomerId() == null || v.getRentedByCustomerId() != customerId) {
            return "ОШИБКА: Вы не можете вернуть чужое авто";
        }

        for (Rental r : rentals) {
            if (r.getVehicleId() == vehicleId && r.isActive()) {
                r.markReturned();
                break;
            }
        }

        v.setRented(false);
        v.setRentedByCustomerId(null);
        return "ОК: Авто возвращено";
    }

    // ------------------- Views -------------------

    public List<Vehicle> getActiveRentalsForCustomer(int customerId) {
        List<Vehicle> result = new ArrayList<>();
        for (Vehicle v : vehicles) {
            if (v.isRented() && v.getRentedByCustomerId() != null && v.getRentedByCustomerId() == customerId) {
                result.add(v);
            }
        }
        return result;
    }

    public List<Rental> getAllRentals() {
        return rentals;
    }

    public List<Vehicle> filterVehicles(String type, Double maxPrice) {
        List<Vehicle> result = new ArrayList<>();
        for (Vehicle v : vehicles) {
            boolean okType = (type == null || type.isBlank() || "ALL".equalsIgnoreCase(type) || v.getType().equalsIgnoreCase(type));
            boolean okPrice = (maxPrice == null || v.getPricePerDay() <= maxPrice);
            if (okType && okPrice) result.add(v);
        }
        return result;
    }

    public String vehicleNameById(int vehicleId) {
        Vehicle v = findVehicleById(vehicleId);
        return v == null ? ("#" + vehicleId) : (v.getBrand() + " " + v.getModel());
    }

    public String customerNameById(int customerId) {
        Customer c = findCustomerById(customerId);
        return c == null ? ("#" + customerId) : c.getFullName();
    }
}
