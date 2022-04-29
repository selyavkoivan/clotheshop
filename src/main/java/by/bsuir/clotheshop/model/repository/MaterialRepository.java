package by.bsuir.clotheshop.model.repository;

import by.bsuir.clotheshop.model.entities.goods.Material;
import by.bsuir.clotheshop.model.entities.goods.Product;
import org.springframework.data.repository.CrudRepository;

public interface MaterialRepository extends CrudRepository<Material, Integer> {
}
