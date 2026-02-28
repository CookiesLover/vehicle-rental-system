package kz.enu.vehicle.rental.system.config;

import kz.enu.vehicle.rental.system.model.Role;
import kz.enu.vehicle.rental.system.model.User;
import kz.enu.vehicle.rental.system.model.Vehicle;
import kz.enu.vehicle.rental.system.model.VehicleStatus;
import kz.enu.vehicle.rental.system.repository.UserRepository;
import kz.enu.vehicle.rental.system.repository.VehicleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(UserRepository userRepository, VehicleRepository vehicleRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                User admin = new User();
                admin.setName("System Admin");
                admin.setEmail("admin@rent.local");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(Role.ROLE_ADMIN);
                admin.setPhone("+1-000-000-0001");
                admin.setEnabled(true);

                User user = new User();
                user.setName("Demo User");
                user.setEmail("user@rent.local");
                user.setPassword(passwordEncoder.encode("user123"));
                user.setRole(Role.ROLE_USER);
                user.setPhone("+1-000-000-0002");
                user.setEnabled(true);

                userRepository.saveAll(List.of(admin, user));
            }

            if (vehicleRepository.count() == 0) {
                Vehicle car1 = new Vehicle();
                car1.setBrand("Toyota");
                car1.setModel("Camry");
                car1.setYear(2022);
                car1.setVehicleClass("Sedan");
                car1.setTransmission("Automatic");
                car1.setFuel("Petrol");
                car1.setSeats(5);
                car1.setPricePerDay(new BigDecimal("79.99"));
                car1.setStatus(VehicleStatus.AVAILABLE);
                car1.setImageUrl("https://images.unsplash.com/photo-1549924231-f129b911e442?auto=format&fit=crop&w=1200&q=60");

                Vehicle car2 = new Vehicle();
                car2.setBrand("Hyundai");
                car2.setModel("Tucson");
                car2.setYear(2023);
                car2.setVehicleClass("SUV");
                car2.setTransmission("Automatic");
                car2.setFuel("Hybrid");
                car2.setSeats(5);
                car2.setPricePerDay(new BigDecimal("99.99"));
                car2.setStatus(VehicleStatus.AVAILABLE);
                car2.setImageUrl("https://images.unsplash.com/photo-1552519507-da3b142c6e3d?auto=format&fit=crop&w=1200&q=60");

                Vehicle car3 = new Vehicle();
                car3.setBrand("Kia");
                car3.setModel("Rio");
                car3.setYear(2021);
                car3.setVehicleClass("Economy");
                car3.setTransmission("Manual");
                car3.setFuel("Petrol");
                car3.setSeats(5);
                car3.setPricePerDay(new BigDecimal("54.99"));
                car3.setStatus(VehicleStatus.MAINTENANCE);
                car3.setImageUrl("https://images.unsplash.com/photo-1494976388531-d1058494cdd8?auto=format&fit=crop&w=1200&q=60");

                vehicleRepository.saveAll(List.of(car1, car2, car3));
            }
        };
    }
}
