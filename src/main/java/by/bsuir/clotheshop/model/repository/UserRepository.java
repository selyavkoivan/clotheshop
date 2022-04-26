package by.bsuir.clotheshop.model.repository;

import by.bsuir.clotheshop.model.entities.user.User;
import by.bsuir.clotheshop.model.entities.user.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByUsername(String username);
    User findByEmail(String email);
}
