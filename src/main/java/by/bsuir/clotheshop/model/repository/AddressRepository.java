package by.bsuir.clotheshop.model.repository;

import by.bsuir.clotheshop.model.entities.address.Address;
import by.bsuir.clotheshop.model.entities.user.Card;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Integer> {
}
