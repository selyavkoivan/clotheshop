package by.bsuir.clotheshop.model.repository;

import by.bsuir.clotheshop.model.entities.goods.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Integer> {
}
