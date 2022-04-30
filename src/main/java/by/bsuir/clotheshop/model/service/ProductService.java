package by.bsuir.clotheshop.model.service;

import by.bsuir.clotheshop.model.entities.goods.Product;
import by.bsuir.clotheshop.model.entities.user.User;
import by.bsuir.clotheshop.model.repository.MaterialRepository;
import by.bsuir.clotheshop.model.repository.ProductRepository;
import by.bsuir.clotheshop.model.repository.UserRepository;
import by.bsuir.clotheshop.model.service.crud.CrudService;
import by.bsuir.clotheshop.model.status.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class ProductService implements CrudService<Product, Product> {
    @Autowired
    ProductRepository repository;

    @Override
    public Product create(Product product) {
        try {
            return repository.save(product);
        } catch (Exception e){
            return null;
        }
    }

    public Product getById(int id)
    {
        return repository.findById(id).get();
    }

    @Override
    public Iterable<Product> read() {
     return repository.findAll();
    }

    @Override
    public Product update(Product product) {
        var oldProduct = repository.findById(product.getProductId()).get();
        product.setPhotoUrls(oldProduct.getPhotoUrls());
        product.setMainPhoto(oldProduct.getMainPhoto());
        return repository.save(product);
    }

    @Override
    public Product delete(Product user) {
        return null;
    }
}
