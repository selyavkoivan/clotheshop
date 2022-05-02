package by.bsuir.clotheshop.model.service;


import by.bsuir.clotheshop.model.entities.user.User;

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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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
        user.setRoles(noChangeUser.getRoles());
        user.setLocked(noChangeUser.isLocked());
        if(noChangeUser.getCard() != null) user.setCard(noChangeUser.getCard());
        repository.save(user);
        return Arrays.asList(UserStatus.NO_ERROR, user);
    }

    public User findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username);
    }

    public List<User> findAll() {
        return (List<User>)repository.findAll();
    }

    public List<User> findAllByUsername(String username) {
        return ((List<User>)repository.findAll()).stream().filter(u-> u.getUsername().contains(username) || u.getEmail().contains(username)).toList();
    }
}
