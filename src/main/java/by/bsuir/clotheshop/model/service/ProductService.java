package by.bsuir.clotheshop.model.service;

import by.bsuir.clotheshop.model.entities.dto.ProductFilter;
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

import java.util.List;
import java.util.Locale;

@Service
public class ProductService implements CrudService<Product, Product> {
    @Autowired
    ProductRepository repository;

    @Override
    public Product create(Product product) {
        try {
            return repository.save(product);
        } catch (Exception e) {
            return null;
        }
    }

    public Product getById(int id) {
        return repository.findById(id).get();
    }

    @Override
    public Iterable<Product> read() {
        return repository.findAll();
    }

    public List<Product> read(ProductFilter filter) {

        var goods = ((List<Product>) repository.findAll());
        if (!filter.getTextForSearch().equals("")) goods = goods.stream()
                .filter(g -> g.getName().toLowerCase(Locale.ROOT).contains(filter.getTextForSearch().toLowerCase(Locale.ROOT)) ||
                        g.getDescription().toLowerCase(Locale.ROOT).contains(filter.getTextForSearch().toLowerCase(Locale.ROOT))).toList();

        if (filter.getMinPrice() != 0)
            goods = goods.stream().filter(g -> g.getPrice() >= filter.getMinPrice()).toList();
        if (filter.getMaxPrice() != 0)
            goods = goods.stream().filter(g -> g.getPrice() <= filter.getMaxPrice()).toList();
        if (!filter.getType().equals(""))
            goods = goods.stream().filter(g -> g.getType().toLowerCase(Locale.ROOT).contains(filter.getType().toLowerCase(Locale.ROOT))).toList();
        if (!filter.getColor().equals(""))
            goods = goods.stream().filter(g -> g.getMaterial().getColor().toLowerCase(Locale.ROOT).contains(filter.getColor().toLowerCase(Locale.ROOT))).toList();
        if (!filter.getMaterial().equals(""))
            goods = goods.stream().filter(g -> g.getMaterial().getMaterial().toLowerCase(Locale.ROOT).contains(filter.getMaterial().toLowerCase(Locale.ROOT))).toList();
        if (!filter.getSize().equals(""))
            goods = goods.stream().filter(g -> g.getSizes().stream().
                    anyMatch(s -> s.getSize().toLowerCase(Locale.ROOT).contains(filter.getSize().toLowerCase(Locale.ROOT)) && s.getCount() != 0)).toList();
        if (filter.isHasPhoto()) goods = goods.stream().filter(g -> g.getPhotoUrls().size() != 0).toList();

        return goods;
    }

    @Override
    public Product update(Product product) {
        var oldProduct = repository.findById(product.getProductId()).get();

        for (var oldSize : oldProduct.getSizes()) {
            oldSize.setCount(0);
        }
        repository.save(oldProduct);

        oldProduct = repository.findById(product.getProductId()).get();

        product.setPhotoUrls(oldProduct.getPhotoUrls());
        product.setMainPhoto(oldProduct.getMainPhoto());
        return repository.save(product);
    }

    @Override
    public Product delete(Product user) {
        return null;
    }
}
