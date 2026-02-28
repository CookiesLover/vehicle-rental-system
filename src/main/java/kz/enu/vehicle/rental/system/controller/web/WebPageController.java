package kz.enu.vehicle.rental.system.controller.web;

import jakarta.validation.Valid;
import kz.enu.vehicle.rental.system.dto.auth.RegisterRequest;
import kz.enu.vehicle.rental.system.dto.car.CarRequest;
import kz.enu.vehicle.rental.system.dto.rental.RentalCreateRequest;
import kz.enu.vehicle.rental.system.model.RentalStatus;
import kz.enu.vehicle.rental.system.model.Role;
import kz.enu.vehicle.rental.system.model.VehicleStatus;
import kz.enu.vehicle.rental.system.service.AuthService;
import kz.enu.vehicle.rental.system.service.RentalService;
import kz.enu.vehicle.rental.system.service.UserService;
import kz.enu.vehicle.rental.system.service.VehicleService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;

@Controller
public class WebPageController {

    private final AuthService authService;
    private final VehicleService vehicleService;
    private final RentalService rentalService;
    private final UserService userService;

    public WebPageController(AuthService authService, VehicleService vehicleService, RentalService rentalService, UserService userService) {
        this.authService = authService;
        this.vehicleService = vehicleService;
        this.rentalService = rentalService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/home";
    }

    @GetMapping("/cars")
    public String oldCarsRedirect() {
        return "redirect:/home";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterRequest registerRequest, RedirectAttributes redirectAttributes) {
        authService.register(registerRequest);
        redirectAttributes.addFlashAttribute("msg", "Р РµРіРёСЃС‚СЂР°С†РёСЏ РїСЂРѕС€Р»Р° СѓСЃРїРµС€РЅРѕ. РўРµРїРµСЂСЊ РІРѕР№РґРёС‚Рµ РІ СЃРёСЃС‚РµРјСѓ.");
        return "redirect:/login";
    }

    @GetMapping("/home")
    public String carsPage(@RequestParam(required = false) String brand,
                           @RequestParam(required = false) BigDecimal minPrice,
                           @RequestParam(required = false) BigDecimal maxPrice,
                           @RequestParam(required = false) String vehicleClass,
                           @RequestParam(required = false) Integer year,
                           @RequestParam(required = false) VehicleStatus status,
                           Authentication authentication,
                           Model model) {
        model.addAttribute("cars", vehicleService.findAll(brand, minPrice, maxPrice, vehicleClass, year, status));
        model.addAttribute("statuses", VehicleStatus.values());
        model.addAttribute("userEmail", authentication.getName());
        model.addAttribute("isAdmin", authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(Role.ROLE_ADMIN.name())));
        return "cars";
    }

    @GetMapping("/home/{id}")
    public String carDetails(@PathVariable Long id, Authentication authentication, Model model) {
        model.addAttribute("car", vehicleService.getById(id));
        model.addAttribute("userEmail", authentication.getName());
        model.addAttribute("isAdmin", authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(Role.ROLE_ADMIN.name())));
        return "car-details";
    }

    @PostMapping("/home/{id}/rent")
    @PreAuthorize("hasRole('USER')")
    public String rentCar(@PathVariable Long id,
                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                          Authentication authentication,
                          RedirectAttributes redirectAttributes) {
        RentalCreateRequest request = new RentalCreateRequest();
        request.setCarId(id);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        rentalService.createRental(authentication.getName(), request);
        redirectAttributes.addFlashAttribute("msg", "Р—Р°СЏРІРєР° РЅР° Р°СЂРµРЅРґСѓ СЃРѕР·РґР°РЅР°");
        return "redirect:/my-rentals";
    }

    @GetMapping("/my-rentals")
    @PreAuthorize("hasRole('USER')")
    public String myRentals(Authentication authentication, Model model) {
        model.addAttribute("rentals", rentalService.myRentals(authentication.getName()));
        return "my-rentals";
    }

    @PostMapping("/my-rentals/{id}/cancel")
    @PreAuthorize("hasRole('USER')")
    public String cancelMyRental(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        rentalService.cancelMyRental(authentication.getName(), id);
        redirectAttributes.addFlashAttribute("msg", "РђСЂРµРЅРґР° РѕС‚РјРµРЅРµРЅР°");
        return "redirect:/my-rentals";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminPage(Model model) {
        model.addAttribute("carsCount", vehicleService.findAll(null, null, null, null, null, null).size());
        model.addAttribute("rentalsCount", rentalService.allRentals().size());
        model.addAttribute("usersCount", userService.getAll().size());
        return "admin";
    }

    @GetMapping("/admin/cars")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminCars(Model model) {
        model.addAttribute("cars", vehicleService.findAll(null, null, null, null, null, null));
        model.addAttribute("carRequest", new CarRequest());
        return "admin-cars";
    }

    @PostMapping("/admin/cars")
    @PreAuthorize("hasRole('ADMIN')")
    public String createCar(@Valid @ModelAttribute CarRequest request, RedirectAttributes redirectAttributes) {
        vehicleService.create(request);
        redirectAttributes.addFlashAttribute("msg", "РђРІС‚РѕРјРѕР±РёР»СЊ РґРѕР±Р°РІР»РµРЅ");
        return "redirect:/admin/cars";
    }

    @PostMapping("/admin/cars/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String editCar(@PathVariable Long id, @Valid @ModelAttribute CarRequest request, RedirectAttributes redirectAttributes) {
        vehicleService.update(id, request);
        redirectAttributes.addFlashAttribute("msg", "РђРІС‚РѕРјРѕР±РёР»СЊ РѕР±РЅРѕРІР»РµРЅ");
        return "redirect:/admin/cars";
    }

    @PostMapping("/admin/cars/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteCar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        vehicleService.delete(id);
        redirectAttributes.addFlashAttribute("msg", "РђРІС‚РѕРјРѕР±РёР»СЊ СѓРґР°Р»РµРЅ");
        return "redirect:/admin/cars";
    }

    @GetMapping("/admin/rentals")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminRentals(Model model) {
        model.addAttribute("rentals", rentalService.allRentals());
        model.addAttribute("statuses", RentalStatus.values());
        return "admin-rentals";
    }

    @PostMapping("/admin/rentals/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateRentalStatus(@PathVariable Long id,
                                     @RequestParam RentalStatus status,
                                     RedirectAttributes redirectAttributes) {
        rentalService.updateStatus(id, status);
        redirectAttributes.addFlashAttribute("msg", "РЎС‚Р°С‚СѓСЃ Р°СЂРµРЅРґС‹ РѕР±РЅРѕРІР»РµРЅ");
        return "redirect:/admin/rentals";
    }

    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminUsers(Model model) {
        model.addAttribute("users", userService.getAll());
        return "admin-users";
    }

    @PostMapping("/admin/users/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateUserRole(@PathVariable Long id, @RequestParam Role role, RedirectAttributes redirectAttributes) {
        userService.updateRole(id, role);
        redirectAttributes.addFlashAttribute("msg", "Р РѕР»СЊ РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ РѕР±РЅРѕРІР»РµРЅР°");
        return "redirect:/admin/users";
    }

    @PostMapping("/admin/users/{id}/toggle")
    @PreAuthorize("hasRole('ADMIN')")
    public String toggleUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.toggleEnabled(id);
        redirectAttributes.addFlashAttribute("msg", "Р”РѕСЃС‚СѓРї РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ РёР·РјРµРЅРµРЅ");
        return "redirect:/admin/users";
    }
}

