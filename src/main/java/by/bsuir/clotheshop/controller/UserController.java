package by.bsuir.clotheshop.controller;

import by.bsuir.clotheshop.model.entities.user.UserForm;
import by.bsuir.clotheshop.model.entities.user.UserLogin;
import by.bsuir.clotheshop.model.status.UserStatus;
import by.bsuir.clotheshop.model.entities.user.User;
import by.bsuir.clotheshop.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    UserService service;

    @RequestMapping(value = "/sign-up", method = RequestMethod.GET)
    public String SignUp(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "user/sign-up";
    }

    @RequestMapping(value = "/sign-up", method = RequestMethod.POST)
    public String SignUp(@ModelAttribute("userForm") UserForm userForm, BindingResult bindingResult, Model model) {
        model.addAttribute("userForm", userForm);
        if (!userForm.getPassword().equals(userForm.getRepeatPassword())) {
            model.addAttribute("passwordError", "Повторите пароль верно");
            return "user/sign-up";
        }

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

    @RequestMapping(value = "/{username}/edit", method = RequestMethod.GET)
    public String SaveData(@PathVariable String username, Model model) {
        var user = service.findByUsername(username);
        if (user == null) {
            //usernotfoundpage
        }
        model.addAttribute("user", user);
        return "user/input-user-data";
    }

    @RequestMapping(value = "/save-data", method = RequestMethod.POST)
    public String saveUserData(@ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        service.saveUserData(user);

        model.addAttribute("user", user);
        return "user/" + user.getUsername();

    }

    @RequestMapping(value = "/sign-in", method = RequestMethod.GET)
    public String SignIn(Model model) {
        model.addAttribute("user", new UserLogin());
        return "user/sign-in";
    }

    @RequestMapping(value = "/sign-in", method = RequestMethod.POST)
    public String SignIn(@ModelAttribute("user") UserLogin userLogin, BindingResult bindingResult, Model model) {

        var userStatusAndUser = service.login(userLogin);
        if(userStatusAndUser.get(1) != null)
        {
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
}