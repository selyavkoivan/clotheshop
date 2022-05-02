package by.bsuir.clotheshop.model.service;

import by.bsuir.clotheshop.model.entities.address.Address;
import by.bsuir.clotheshop.model.entities.dto.OrderFilter;
import by.bsuir.clotheshop.model.entities.dto.ProductDto;
import by.bsuir.clotheshop.model.entities.goods.Order;
import by.bsuir.clotheshop.model.entities.user.Card;
import by.bsuir.clotheshop.model.entities.user.User;
import by.bsuir.clotheshop.model.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Locale;

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
                .stream().filter(pr -> pr.getUser().getUsername().equals(username) && pr.getStatus() == 0).toList();
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
        order.setStatus(1);
        order.setDate(new Date());

        order.setUser(updateUser(order.getUser().getUsername(),
                productInCart.isRememberAddress() ? productInCart.getAddress() : null,
                productInCart.isRememberCard() ? productInCart.getUser().getCard() : null));

        if(productInCart.isRememberAddress()) order.setAddress(order.getUser().getAddress());
        else order.setAddress(createAddress(productInCart.getAddress()));

        order.getSize().setCount(order.getSize().getCount() - order.getCount());

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

    public List<Order> findAllOrders() {
        return ((List<Order>) orderRepository.findAll())
                .stream().filter(pr -> pr.getStatus() != 0).toList();
    }

    public List<Order> findAllOrders(String username) {
        return ((List<Order>) orderRepository.findAll())
                .stream().filter(pr -> pr.getUser().getUsername().equals(username) && pr.getStatus() != 0).toList();
    }

    public List<Order> read(OrderFilter filter, User user) {

        var orders = user == null ? findAllOrders() : findAllOrders(user.getUsername());
        if (!filter.getTextForSearch().equals("") && !filter.isForAllUsers()) orders = orders.stream()
                .filter(g -> g.getProduct().getName().toLowerCase(Locale.ROOT).contains(filter.getTextForSearch().toLowerCase(Locale.ROOT)) ||
                        g.getProduct().getDescription().toLowerCase(Locale.ROOT).contains(filter.getTextForSearch().toLowerCase(Locale.ROOT))).toList();
        else if (!filter.getTextForSearch().equals("")) orders = orders.stream()
                .filter(g -> g.getProduct().getName().toLowerCase(Locale.ROOT).contains(filter.getTextForSearch().toLowerCase(Locale.ROOT)) ||
                        g.getProduct().getDescription().toLowerCase(Locale.ROOT).contains(filter.getTextForSearch().toLowerCase(Locale.ROOT)) ||
                        g.getUser().getUsername().toLowerCase(Locale.ROOT).contains(filter.getTextForSearch().toLowerCase(Locale.ROOT)) ||
                        g.getUser().getEmail().toLowerCase(Locale.ROOT).contains(filter.getTextForSearch().toLowerCase(Locale.ROOT))).toList();
        if (filter.getDelivery() != 1) orders = orders.stream().filter(g -> (filter.getDelivery() == 2) == g.isDelivery()).toList();
        if(filter.getStatus() != 0) orders = orders.stream().filter(o -> o.getStatus() == filter.getStatus()).toList();
        if (filter.getMinPrice() != 0)
            orders = orders.stream().filter(g -> g.getTotalPrice() >= filter.getMinPrice()).toList();
        if (filter.getMaxPrice() != 0)
            orders = orders.stream().filter(g -> g.getTotalPrice() <= filter.getMaxPrice()).toList();
        if (!filter.getType().equals(""))
            orders = orders.stream().filter(g -> g.getProduct().getType().toLowerCase(Locale.ROOT).contains(filter.getType().toLowerCase(Locale.ROOT))).toList();
        if (!filter.getColor().equals(""))
            orders = orders.stream().filter(g -> g.getProduct().getMaterial().getColor().toLowerCase(Locale.ROOT).contains(filter.getColor().toLowerCase(Locale.ROOT))).toList();
        if (!filter.getMaterial().equals(""))
            orders = orders.stream().filter(g -> g.getProduct().getMaterial().getMaterial().toLowerCase(Locale.ROOT).contains(filter.getMaterial().toLowerCase(Locale.ROOT))).toList();
        if (!filter.getSize().equals(""))
            orders = orders.stream().filter(g -> g.getProduct().getSizes().stream().
                    anyMatch(s -> s.getSize().toLowerCase(Locale.ROOT).contains(filter.getSize().toLowerCase(Locale.ROOT)) && s.getCount() != 0)).toList();
        if (filter.isHasPhoto()) orders = orders.stream().filter(g -> g.getProduct().getPhotoUrls().size() != 0).toList();

        return orders;
    }

    public Order findById(int id) {
        return orderRepository.findById(id).get();
    }

    public Order updateOrder(Order order, int id) {
        var oldOrder = orderRepository.findById(id).get();
        oldOrder.setStatus(order.getStatus());
        return orderRepository.save(oldOrder);
    }

    public void finishOrder(int id) {
        var order = orderRepository.findById(id).get();
        order.setStatus(4);
        orderRepository.save(order);
    }
}
