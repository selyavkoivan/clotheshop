package by.bsuir.clotheshop.model.entities.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLogin {
    private String usernameOrEmail;
    private String password;
}
