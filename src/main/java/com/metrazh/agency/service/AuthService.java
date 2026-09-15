package com.metrazh.agency.service;

import com.metrazh.agency.entity.AdminUser;
import com.metrazh.agency.entity.Client;
import com.metrazh.agency.repository.AdminUserRepository;
import com.metrazh.agency.repository.ClientRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final ClientRepository clientRepository;
    private final AdminUserRepository adminUserRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(ClientRepository clientRepository, AdminUserRepository adminUserRepository) {
        this.clientRepository = clientRepository;
        this.adminUserRepository = adminUserRepository;
    }

    public String hashPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public boolean verifyPassword(String rawPassword, String passwordHash) {
        return passwordEncoder.matches(rawPassword, passwordHash);
    }

    /** Результат реєстрації: або створений клієнт, або повідомлення про помилку. */
    public record RegistrationResult(boolean success, Client client, String errorMessage) {
        public static RegistrationResult ok(Client client) {
            return new RegistrationResult(true, client, null);
        }

        public static RegistrationResult error(String message) {
            return new RegistrationResult(false, null, message);
        }
    }

    public RegistrationResult registerClient(String email, String password, String fullName, String phone) {
        if (clientRepository.findByEmail(email).isPresent()) {
            return RegistrationResult.error("Користувач з таким email вже існує");
        }
        if (password == null || password.length() < 6) {
            return RegistrationResult.error("Пароль має бути не коротше 6 символів");
        }

        Client client = new Client();
        client.setEmail(email);
        client.setPasswordHash(hashPassword(password));
        client.setFullName(fullName);
        client.setPhone(phone);
        client = clientRepository.save(client);

        return RegistrationResult.ok(client);
    }

    public Optional<Client> loginClient(String email, String password) {
        return clientRepository.findByEmail(email)
                .filter(c -> verifyPassword(password, c.getPasswordHash()));
    }

    public Optional<AdminUser> loginAdmin(String username, String password) {
        return adminUserRepository.findByUsername(username)
                .filter(u -> verifyPassword(password, u.getPasswordHash()));
    }
}
