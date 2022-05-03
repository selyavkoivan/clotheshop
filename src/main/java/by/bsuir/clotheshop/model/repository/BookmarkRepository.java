package by.bsuir.clotheshop.model.repository;

import by.bsuir.clotheshop.model.entities.goods.Bookmark;
import by.bsuir.clotheshop.model.entities.goods.Product;
import org.springframework.data.repository.CrudRepository;

public interface BookmarkRepository extends CrudRepository<Bookmark, Integer> {
}
