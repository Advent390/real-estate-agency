package com.metrazh.agency.controller;

import com.metrazh.agency.repository.ClientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/** Аналог admin_clients() з app.py. */
@Controller
public class AdminClientController {

    private final ClientRepository clientRepository;

    public AdminClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping("/admin/clients")
    public String list(Model model) {
        model.addAttribute("clients", clientRepository.findAllByOrderByCreatedAtDesc());
        return "admin/clients";
    }
}
