package by.bsuir.clotheshop.model.service;

import by.bsuir.clotheshop.model.entities.address.Address;
import by.bsuir.clotheshop.model.entities.dto.ProductDto;
import by.bsuir.clotheshop.model.entities.goods.Order;
import by.bsuir.clotheshop.model.entities.user.Card;
import by.bsuir.clotheshop.model.entities.user.User;
import by.bsuir.clotheshop.model.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ProductRepository productRepository;

    public Order createProductInOrder(ProductDto productDto, User user){
        var product = productRepository.findById(productDto.getProductId()).get();
        var size = product.getSizes().stream().filter(s -> s.getSizeId() == productDto.getSize().getSizeId()).findFirst().get();
        var productInOrder = new Order();
        productInOrder.setProduct(product);
        productInOrder.setSize(size);
        productInOrder.setCount(productDto.getSize().getCount());
        productInOrder.setUser(user);
        return orderRepository.save(productInOrder);
    }

    public List<Order> findCartByUsername(String username) {
        return ((List<Order>) orderRepository.findAll())
                .stream().filter(pr -> pr.getUser().getUsername().equals(username) && !pr.isStatus()).toList();
    }

    public Order findProductById(int id) {
        return orderRepository.findById(id).get();
    }

    public Order updateProductInCart(Order productInCart) {
        var product = orderRepository.findById(productInCart.getProductInCartId()).get();
        product.setCount(productInCart.getCount());
        return orderRepository.save(product);
    }

    public void delete(Order productInCart) {
        orderRepository.delete(productInCart);
    }

    public void createOrder(Order productInCart, int id) {

        var order = orderRepository.findById(id).get();
        order.setDelivery(productInCart.isDelivery());
        order.setStatus(true);
        order.setDate(new Date());

        order.setUser(updateUser(order.getUser().getUsername(),
                productInCart.isRememberAddress() ? productInCart.getAddress() : null,
                productInCart.isRememberCard() ? productInCart.getUser().getCard() : null));

        if(productInCart.isRememberAddress()) order.setAddress(order.getUser().getAddress());
        else order.setAddress(createAddress(productInCart.getAddress()));

        orderRepository.save(order);
    }

    private Address createAddress(Address address) {
        return addressRepository.save(address);
    }

    private User updateUser(String username, Address address, Card card)
    {
        var user = userRepository.findByUsername(username);
        if(address != null) {
            var id = user.getAddress().getAddressId();
            user.setAddress(address);
            user.getAddress().setAddressId(id);
        }
        if(card != null) {
            var id = user.getCard().getCardId();
            user.setCard(card);
            user.getCard().setCardId(id);
        }
        return userRepository.save(user);
    }
}
