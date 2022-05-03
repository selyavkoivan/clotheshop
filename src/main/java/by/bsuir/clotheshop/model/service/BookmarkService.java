package by.bsuir.clotheshop.model.service;

import by.bsuir.clotheshop.model.entities.dto.ProductFilter;
import by.bsuir.clotheshop.model.entities.goods.Bookmark;
import by.bsuir.clotheshop.model.entities.goods.Product;
import by.bsuir.clotheshop.model.repository.BookmarkRepository;
import by.bsuir.clotheshop.model.repository.ProductRepository;
import by.bsuir.clotheshop.model.repository.UserRepository;
import by.bsuir.clotheshop.model.service.crud.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class BookmarkService {
    @Autowired
    BookmarkRepository repository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    public Bookmark find(String username, int productId) {
        return ((List<Bookmark>)repository.findAll()).stream().filter(b -> b.getProduct().getProductId() == productId
                && b.getUser().getUsername().equals(username)).findFirst().orElse(null);
    }

    public void changeMark(String username, int id) {
        var bookmark = find(username, id);
        if(bookmark == null) {
            bookmark = new Bookmark();
            bookmark.setProduct( productRepository.findById(id).get());
            bookmark.setUser(userRepository.findByUsername(username));
            repository.save(bookmark);
        } else {
            repository.deleteById(bookmark.getBookmarkId());
        }
    }
}
