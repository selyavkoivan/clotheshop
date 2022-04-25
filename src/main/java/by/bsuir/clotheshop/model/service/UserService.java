package by.bsuir.clotheshop.model.service;


import by.bsuir.clotheshop.model.entities.user.User;
import by.bsuir.clotheshop.model.entities.user.UserLogin;
import by.bsuir.clotheshop.model.entities.user.role.Role;
import by.bsuir.clotheshop.model.status.UserStatus;
import by.bsuir.clotheshop.model.repository.UserRepository;
import by.bsuir.clotheshop.model.service.crud.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
public class UserService implements CrudService<User, UserStatus>, UserDetailsService {
    @Autowired
    UserRepository repository;

    @Override
    public UserStatus create(User user) {
        try {
            user.setAvatarUrl("https://avatarko.ru/img/kartinka/1/avatarko_anonim.jpg");
            user.setRoles(new HashSet<>());
            user.getRoles().add(Role.User);
            repository.save(user);
        } catch (Exception e) {
            var usernameUser = repository.findByUsername(user.getUsername());
            var emailUser = repository.findByEmail(user.getEmail());

            if (emailUser != null && usernameUser != null) return UserStatus.EMAIL_AND_USERNAME_IS_EXIST;
            if (emailUser != null) return UserStatus.EMAIL_IS_EXIST;
            if (usernameUser != null) return UserStatus.USERNAME_IS_EXIST;
        }
        return UserStatus.NO_ERROR;
    }

    @Override
    public Iterable<User> read() {
        return repository.findAll();
    }

    @Override
    public UserStatus update(User user) {
        return null;
    }

    @Override
    public UserStatus delete(User user) {
        return null;
    }

    public List<Object> saveUserData(User user) {
        var noChangeUser = repository.findById(user.getUserId()).get();

        var usernameUser = repository.findByUsername(user.getUsername());
        var emailUser = repository.findByEmail(user.getEmail());

        if (emailUser != null && !user.getEmail().equals(noChangeUser.getEmail()) && usernameUser != null
                && !user.getUsername().equals(noChangeUser.getUsername()))
            return Arrays.asList(UserStatus.EMAIL_AND_USERNAME_IS_EXIST, noChangeUser);
        if (emailUser != null && !user.getEmail().equals(noChangeUser.getEmail()))
            return Arrays.asList(UserStatus.EMAIL_IS_EXIST, noChangeUser);
        if (usernameUser != null && !user.getUsername().equals(noChangeUser.getUsername()))
            return Arrays.asList(UserStatus.USERNAME_IS_EXIST, noChangeUser);


        user.setPassword(noChangeUser.getPassword());
        user.setAvatarUrl(noChangeUser.getAvatarUrl());
        repository.save(user);
        return Arrays.asList(UserStatus.NO_ERROR, user);
    }

    public User findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public List<Object> login(UserLogin userLogin) {
        var user = repository.findByEmail(userLogin.getUsernameOrEmail());
        if (user == null) user = repository.findByUsername(userLogin.getUsernameOrEmail());
        if (user == null) return Arrays.asList(UserStatus.USER_NOT_FOUND, null);
        if (!user.getPassword().equals(userLogin.getPassword()))
            return Arrays.asList(UserStatus.INCORRECT_PASSWORD, null);
        return Arrays.asList(UserStatus.USER_NOT_FOUND, user);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username);
    }
}
