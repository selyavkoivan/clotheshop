package by.bsuir.clotheshop.controller;

import by.bsuir.clotheshop.model.entities.user.User;
import by.bsuir.clotheshop.model.entities.user.UserForm;
import by.bsuir.clotheshop.model.entities.user.role.Role;
import by.bsuir.clotheshop.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/users")
@PreAuthorize("hasAnyAuthority('Admin')")
public class UsersController {

    @Autowired
    UserService service;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping( "")
    public String ShowAllUsers(Model model, @AuthenticationPrincipal User user) {

        var users = service.findAll();
        var admins = users.stream().filter(u-> u.getRoles().stream().anyMatch(role -> role == Role.Admin)).collect(Collectors.toList());
        users = users.stream().filter(u-> u.getRoles().stream().noneMatch(role -> role == Role.Admin)).collect(Collectors.toList());

        model.addAttribute("admins", admins);
        model.addAttribute("users", users);
        return "user/users";
    }
}

