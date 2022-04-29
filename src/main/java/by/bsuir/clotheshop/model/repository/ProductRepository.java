package by.bsuir.clotheshop.model.repository;

import by.bsuir.clotheshop.model.entities.goods.Product;
import by.bsuir.clotheshop.model.entities.user.User;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Integer> {
    void deleteByName(String name);

    Product findByName(String name);
}
