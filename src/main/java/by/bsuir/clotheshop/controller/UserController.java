package by.bsuir.clotheshop.controller;

import by.bsuir.clotheshop.model.entities.user.UserForm;
import by.bsuir.clotheshop.model.entities.user.UserLogin;
import by.bsuir.clotheshop.model.entities.user.gender.Gender;
import by.bsuir.clotheshop.model.entities.user.role.Role;
import by.bsuir.clotheshop.model.service.cloudinary.PhotoUploader;
import by.bsuir.clotheshop.model.status.UserStatus;
import by.bsuir.clotheshop.model.entities.user.User;
import by.bsuir.clotheshop.model.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;


@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    UserService service;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping( "/sign-up")
    public String SignUp(Model model) {

        model.addAttribute("userForm", new UserForm());
        return "user/sign-up";
    }

    @PostMapping( "/login")
    public String Login(@ModelAttribute("userForm") UserForm userForm) {
        var user = service.findByUsername(userForm.getUsername());
        if(user.getGender() == null)  return "redirect:user/" + userForm.getUsername() + "/edit";
        return "redirect:user/" + userForm.getUsername() + "/";
    }

    @PostMapping( "/sign-up")
    public String SignUp(@ModelAttribute("userForm") UserForm userForm, Model model) {
        model.addAttribute("userForm", userForm);
        if (!userForm.getPassword().equals(userForm.getRepeatPassword())) {
            model.addAttribute("passwordError", "Повторите пароль верно");
            return "user/sign-up";
        }
        userForm.setPassword(passwordEncoder.encode(userForm.getPassword()));
        var user = new User(userForm);
        var addStatus = service.create(user);
        if (addStatus == UserStatus.NO_ERROR) {
            return "redirect:" + user.getUsername() + "/edit";
        }
        if (addStatus == UserStatus.EMAIL_AND_USERNAME_IS_EXIST) {
            model.addAttribute("emailExist", "Электронная почта " + user.getEmail() + " уже используется");
            model.addAttribute("usernameExist", "Имя пользователя " + user.getUsername() + " уже используется");
        } else if (addStatus == UserStatus.EMAIL_IS_EXIST)
            model.addAttribute("emailExist", "Электронная почта " + user.getEmail() + " уже используется");
        else if (addStatus == UserStatus.USERNAME_IS_EXIST)
            model.addAttribute("usernameExist", "Имя пользователя " + user.getUsername() + " уже используется");

        return "user/sign-up";
    }

    @GetMapping("/{username}/edit")
    public String SaveData(@PathVariable String username, Model model) {
        var user = service.findByUsername(username);
        if (user == null) {
            //usernotfoundpage
        }
        model.addAttribute("user", user);
        return "user/input-user-data";
    }

    @GetMapping( "/{username}/")
    public String ShowUser(@PathVariable String username, Model model) {
        var user = service.findByUsername(username);
        if (user == null) {
            //usernotfoundpage
        }

        model.addAttribute("user", user);
        return "user/profile";
    }

    @PostMapping( "/save")
    public String saveUserAfterSignUp(@ModelAttribute("user") User user) {

        user.setGender(Gender.NoData);
        service.saveUserData(user);
        return "redirect:/user/" + user.getUsername() + "/";
    }

    @GetMapping("/login-error")
    public String loginError(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        String errorMessage = null;
        if (session != null) {
            AuthenticationException ex = (AuthenticationException) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
                if (ex.getMessage().contains("locked"))
                    errorMessage = "Учетная запись пользователя заблокирована";
                else
                    errorMessage = "Неверный логин или пароль";
            }
        }

        model.addAttribute("Error", errorMessage);
        return "/user/sign-in";
    }

    @PostMapping( "/{username}/")
    public String saveUserData(@ModelAttribute("user") User editUser, @PathVariable String username, Model model, @AuthenticationPrincipal User user) {
        var answer = service.saveUserData(editUser);
        if (answer.get(0) == UserStatus.EMAIL_AND_USERNAME_IS_EXIST) {
            model.addAttribute("emailExist", "Электронная почта " + editUser.getEmail() + " уже используется");
            model.addAttribute("usernameExist", "Имя пользователя " + editUser.getUsername() + " уже используется");
        } else if (answer.get(0) == UserStatus.EMAIL_IS_EXIST)
            model.addAttribute("emailExist", "Электронная почта " + editUser.getEmail() + " уже используется");
        else if (answer.get(0) == UserStatus.USERNAME_IS_EXIST)
            model.addAttribute("usernameExist", "Имя пользователя " + editUser.getUsername() + " уже используется");
        if(username == user.getUsername()) user.setUsername(editUser.getUsername());
        return "redirect:/user/" + ((User) answer.get(1)).getUsername() + "/";

    }

    @GetMapping( "/sign-in")
    public String SignIn(Model model) {
        model.addAttribute("user", new UserLogin());
        return "user/sign-in";
    }

    @PostMapping("/sign-in")
    public String SignIn(@ModelAttribute("user") UserLogin userLogin, Model model) {

        var userStatusAndUser = service.login(userLogin);
        if (userStatusAndUser.get(1) != null) {
            var user = (User) userStatusAndUser.get(1);

            model.addAttribute("user", user);
            return "redirect:" + user.getUsername() + "/edit";
        }
        if (userStatusAndUser.get(0) == UserStatus.USER_NOT_FOUND)
            model.addAttribute("userNotFound", "Пользователь не найден");
        else if (userStatusAndUser.get(0) == UserStatus.INCORRECT_PASSWORD)
            model.addAttribute("incorrectPassword", "Неверно введен пароль");
        return "user/sign-in";
    }

    @PostMapping("/{username}/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, @PathVariable String username, @AuthenticationPrincipal User user) throws IOException {

        var url = PhotoUploader.uploadImage(file);
        var editAvatarUser = service.findByUsername(username);
        editAvatarUser.setAvatarUrl(url);
        if(username.equals(user.getUsername())) user.setAvatarUrl(editAvatarUser.getAvatarUrl());
        service.saveUserData(editAvatarUser);

        return "redirect:/user/" + username + "/";
    }

    @PostMapping("/{username}/lock")
    public String setLockStatus(@RequestParam("status") boolean status, @PathVariable String username) {

        var user = service.findByUsername(username);
        user.setLocked(status);
        service.saveUserData(user);

        return "redirect:/user/" + username + "/";
    }
}