package kz.enu.vehicle.rental.system.service;

import org.springframework.stereotype.Service;

/**
 * Учебная авторизация:
 * admin/admin -> АДМИН
 * user/user   -> КЛИЕНТ
 */
@Service
public class AuthService {

    public String login(String username, String password) {
        if ("admin".equals(username) && "admin".equals(password)) return "АДМИН";
        if ("user".equals(username) && "user".equals(password)) return "КЛИЕНТ";
        return null;
    }
}