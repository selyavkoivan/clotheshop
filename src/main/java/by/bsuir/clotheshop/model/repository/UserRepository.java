package by.bsuir.clotheshop.model.repository;

import by.bsuir.clotheshop.model.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.username = ?1")
    User findUserByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.email = ?1")
    User findUserByEmail(String email);
}
