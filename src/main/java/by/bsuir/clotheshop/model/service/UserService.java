package by.bsuir.clotheshop.model.service;


import by.bsuir.clotheshop.model.entities.user.User;
import by.bsuir.clotheshop.model.entities.user.UserLogin;
import by.bsuir.clotheshop.model.status.UserStatus;
import by.bsuir.clotheshop.model.repository.UserRepository;
import by.bsuir.clotheshop.model.service.crud.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService implements CrudService<User, UserStatus> {
    @Autowired
    UserRepository repository;

    @Override
    public UserStatus create(User user)
    {
        try {
            repository.save(user);
        } catch (Exception e) {
            var usernameUser = repository.findUserByUsername(user.getUsername());
            var emailUser = repository.findUserByEmail(user.getEmail());

            if(emailUser != null && usernameUser != null) return UserStatus.EMAIL_AND_USERNAME_IS_EXIST;
            if(emailUser != null) return UserStatus.EMAIL_IS_EXIST;
            if(usernameUser != null) return UserStatus.USERNAME_IS_EXIST;
        }
        return UserStatus.NO_ERROR;
    }

    @Override
    public List<User> read() {
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

    public UserStatus saveUserData(User user)
    {
        var noChangeUser = repository.findById(user.getUserId()).get();
        user.setPassword(noChangeUser.getPassword());
        repository.save(user);
        return UserStatus.NO_ERROR;
    }

    public User findByUsername(String username) {
        return repository.findUserByUsername(username);
    }

    public List<Object> login(UserLogin userLogin) {
        var user = repository.findUserByEmail(userLogin.getUsernameOrEmail());
        if(user == null) user = repository.findUserByUsername(userLogin.getUsernameOrEmail());
        if(user == null) return Arrays.asList(UserStatus.USER_NOT_FOUND, null);
        if(!user.getPassword().equals(userLogin.getPassword())) return Arrays.asList(UserStatus.INCORRECT_PASSWORD, null);
        return Arrays.asList(UserStatus.USER_NOT_FOUND, user);
    }


}
