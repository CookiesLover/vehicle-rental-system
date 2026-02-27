
 package kz.enu.vehicle.rental.system.controller;
 
 import kz.enu.vehicle.rental.system.model.Customer;
 import kz.enu.vehicle.rental.system.model.Vehicle;
 import kz.enu.vehicle.rental.system.service.AuthService;
 import kz.enu.vehicle.rental.system.service.RentalService;
 import jakarta.servlet.http.HttpSession;
 import org.springframework.stereotype.Controller;
 import org.springframework.ui.Model;
 import org.springframework.web.bind.annotation.*;
 

 @Controller
 public class WebController {
 
     private final RentalService rentalService;
     private final AuthService authService;
 
     public WebController(RentalService rentalService, AuthService authService) {
         this.rentalService = rentalService;
         this.authService = authService;
     }
 

     @GetMapping("/")
     public String root(HttpSession session) {
         if (session.getAttribute("role") == null) return "redirect:/login";
         return "redirect:/vehicles";
     }
 
     @GetMapping("/login")
     public String loginPage(Model model, @RequestParam(required = false) String error) {
         model.addAttribute("error", error);
         return "login";
     }
 
     @PostMapping("/login")
     public String doLogin(@RequestParam String username,
                           @RequestParam String password,
                           HttpSession session) {
 
         String role = authService.login(username, password);
         if (role == null) {
             return "redirect:/login?error=Неверный%20логин%20или%20пароль";
         }
 
         session.setAttribute("role", role);
 

             session.setAttribute("customerId", 1);
         }
 
         return "redirect:/vehicles";
     }
 
     @GetMapping("/logout")
     public String logout(HttpSession session) {
         session.invalidate();
         return "redirect:/login";
     }
 
     private boolean isLogged(HttpSession session) {
         return session.getAttribute("role") != null;
     }
 
     private boolean isAdmin(HttpSession session) {
         return "АДМИН".equals(session.getAttribute("role"));
     }
 
     private boolean isClient(HttpSession session) {
         return "КЛИЕНТ".equals(session.getAttribute("role"));
     }
 
     // ---------------- USER PAGES ----------------
 
     @GetMapping("/vehicles")
     public String vehiclesPage(HttpSession session,
                                @RequestParam(required = false) String type,
                                @RequestParam(required = false) Double maxPrice,
                                @RequestParam(required = false) String msg,
                                Model model) {
 
         if (!isLogged(session)) return "redirect:/login";
 
         model.addAttribute("role", session.getAttribute("role"));
         model.addAttribute("type", type == null ? "ALL" : type);
         model.addAttribute("maxPrice", maxPrice);
         model.addAttribute("msg", msg);
 
         model.addAttribute("vehicles", rentalService.filterVehicles(type, maxPrice));
         return "vehicles";
     }
 
  @GetMapping("/rent")
  public String rentPage(HttpSession session,
                         @RequestParam(required = false) String msg,
                        Model model) {
     if (!isLogged(session)) return "redirect:/login";
    if (!isClient(session)) return "redirect:/vehicles?msg=Страница%20доступна%20только%20клиенту";

        model.addAttribute("role", session.getAttribute("role"));
        model.addAttribute("msg", msg);
        model.addAttribute("vehicles", rentalService.getVehicles());
        return "rent";
    }

    @GetMapping("/return")
    public String returnPage(HttpSession session,
                             @RequestParam(required = false) String msg,
                             Model model) {
        if (!isLogged(session)) return "redirect:/login";
        if (!isClient(session)) return "redirect:/vehicles?msg=Страница%20доступна%20только%20клиенту";

        Integer customerId = (Integer) session.getAttribute("customerId");
        model.addAttribute("role", session.getAttribute("role"));
        model.addAttribute("msg", msg);
        model.addAttribute("myVehicles", rentalService.getActiveRentalsForCustomer(customerId));
        return "return";
    }

     @PostMapping("/rent")
     public String rent(@RequestParam int vehicleId, HttpSession session) {
         if (!isLogged(session)) return "redirect:/login";
         if (!isClient(session)) return "redirect:/vehicles?msg=Только%20клиент%20может%20арендовать";
 
         Integer customerId = (Integer) session.getAttribute("customerId");
         if (customerId == null) return "redirect:/login?error=Нет%20ID%20клиента";
 
         String result = rentalService.rentVehicle(vehicleId, customerId);

       return "redirect:/rent?msg=" + urlEncode(result);
     }
 
     @PostMapping("/return")
     public String ret(@RequestParam int vehicleId, HttpSession session) {
         if (!isLogged(session)) return "redirect:/login";
         if (!isClient(session)) return "redirect:/vehicles?msg=Только%20клиент%20может%20возвращать";
 
         Integer customerId = (Integer) session.getAttribute("customerId");
         if (customerId == null) return "redirect:/login?error=Нет%20ID%20клиента";
 

       return "redirect:/return?msg=" + urlEncode(result);
     }
 
     @GetMapping("/my-rentals")
     public String myRentals(HttpSession session,
                             @RequestParam(required = false) String msg,
                             Model model) {
         if (!isLogged(session)) return "redirect:/login";
         if (!isClient(session)) return "redirect:/vehicles";
 
         Integer customerId = (Integer) session.getAttribute("customerId");
         model.addAttribute("role", session.getAttribute("role"));
         model.addAttribute("msg", msg);
         model.addAttribute("myVehicles", rentalService.getActiveRentalsForCustomer(customerId));
 
         return "my_rentals";
     }
 
     // ---------------- ADMIN PAGES ----------------
 
     @GetMapping("/admin")
     public String adminPage(HttpSession session,
                             @RequestParam(required = false) String msg,
                             Model model) {
         if (!isAdmin(session)) return "redirect:/vehicles?msg=Нет%20доступа";
 
    @ public class WebController {
         return "redirect:/admin?msg=ОК:%20Клиент%20добавлен";
     }
 
     @PostMapping("/admin/customer/delete")
     public String adminDeleteCustomer(@RequestParam int id, HttpSession session) {
         if (!isAdmin(session)) return "redirect:/vehicles?msg=Нет%20доступа";
 
         String result = rentalService.deleteCustomer(id);
         return "redirect:/admin?msg=" + urlEncode(result);
     }
 
     @GetMapping("/admin/rentals")
     public String adminRentals(HttpSession session, Model model) {
         if (!isAdmin(session)) return "redirect:/vehicles?msg=Нет%20доступа";
 
         model.addAttribute("role", session.getAttribute("role"));
         model.addAttribute("rentals", rentalService.getAllRentals());
         model.addAttribute("service", rentalService);
 
         return "admin_rentals";
     }
 
     private String urlEncode(String s) {
         return s.replace(" ", "%20");
     }

}
 
