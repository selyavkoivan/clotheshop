package by.bsuir.clotheshop;

import by.bsuir.clotheshop.model.entities.goods.Material;
import by.bsuir.clotheshop.model.entities.goods.Product;
import by.bsuir.clotheshop.model.entities.goods.Size;
import by.bsuir.clotheshop.model.entities.user.User;
import by.bsuir.clotheshop.model.entities.user.gender.Gender;
import by.bsuir.clotheshop.model.entities.user.role.Role;
import by.bsuir.clotheshop.model.repository.ProductRepository;
import by.bsuir.clotheshop.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;

@SpringBootApplication
public class ClotheshopApplication implements CommandLineRunner {

	@Autowired
	UserRepository userRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(ClotheshopApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (userRepository.findByUsername("admin") == null) {
			User admin = new User();
			admin.setEmail("admin@gmail.com");
			admin.setRoles(Collections.singleton(Role.Admin));
			admin.setUsername("admin");
			admin.setPassword(passwordEncoder.encode("admin"));
			admin.setAvatarUrl("https://avatarko.ru/img/kartinka/1/avatarko_anonim.jpg");
			userRepository.save(admin);
		}
	}
}
