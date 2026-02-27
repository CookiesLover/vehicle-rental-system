package kz.enu.vehicle.rental.system;

import kz.enu.vehicle.rental.system.model.Customer;
import kz.enu.vehicle.rental.system.model.Vehicle;
import kz.enu.vehicle.rental.system.service.RentalService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VehicleRentalSystemApplication implements CommandLineRunner {

	private final RentalService rentalService;

	public VehicleRentalSystemApplication(RentalService rentalService) {
		this.rentalService = rentalService;
	}

	public static void main(String[] args) {
		SpringApplication.run(VehicleRentalSystemApplication.class, args);
	}

	@Override
	public void run(String... args) {
		// Клиент с id=1 обязателен, потому что login "user/user" присваивает customerId=1
		rentalService.addCustomer(new Customer(1, "Клиент (user)", "+7-777-000-00-00"));
		rentalService.addCustomer(new Customer(2, "Второй клиент", "+7-700-111-11-11"));

		// Автомобили
		rentalService.addVehicle(new Vehicle(1, "Toyota", "Camry", "Sedan", 20000,
				"https://images.unsplash.com/photo-1549924231-f129b911e442?auto=format&fit=crop&w=1200&q=60"));
		rentalService.addVehicle(new Vehicle(2, "Hyundai", "Elantra", "Sedan", 15000,
				"https://images.unsplash.com/photo-1550355291-bbee04a92027?auto=format&fit=crop&w=1200&q=60"));
		rentalService.addVehicle(new Vehicle(3, "Kia", "Sportage", "SUV", 25000,
				"https://images.unsplash.com/photo-1619767886558-efdc259cde1a?auto=format&fit=crop&w=1200&q=60"));
	}
}