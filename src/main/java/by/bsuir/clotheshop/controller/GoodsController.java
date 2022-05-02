package by.bsuir.clotheshop.controller;

import by.bsuir.clotheshop.model.entities.dto.ProductDto;
import by.bsuir.clotheshop.model.entities.dto.ProductFilter;
import by.bsuir.clotheshop.model.entities.goods.Product;
import by.bsuir.clotheshop.model.entities.goods.Order;
import by.bsuir.clotheshop.model.entities.user.User;
import by.bsuir.clotheshop.model.service.OrderService;
import by.bsuir.clotheshop.model.service.ProductService;

import by.bsuir.clotheshop.model.service.cloudinary.PhotoUploader;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;


@Controller
@RequestMapping(value = "")
public class GoodsController {

    @Autowired
    ProductService service;

    @Autowired
    OrderService orderService;

    @GetMapping("/goods/add")
    @PreAuthorize("hasAnyAuthority('Admin')")
    public String addProduct(Model model) {
        model.addAttribute("product", new Product());
        return "goods/inputProduct";
    }

    @PostMapping("/goods/input-data")
    @PreAuthorize("hasAnyAuthority('Admin')")
    public String saveProduct(Model model, @ModelAttribute("product") Product product) {
        product = product.getProductId() == 0 ? service.create(product) : service.update(product);
        return "redirect:/goods/" + product.getProductId() + "/";
    }

    @GetMapping("/goods/{id}/edit")
    @PreAuthorize("hasAnyAuthority('Admin')")
    public String editProduct(Model model, @PathVariable int id) {
        model.addAttribute("product", service.getById(id));
        return "goods/inputProduct";
    }

    @GetMapping("/goods/{id}/")
    public String showProduct(Model model, @PathVariable int id) {
        model.addAttribute("product", service.getById(id));
        model.addAttribute("productDto", new ProductDto());
        return "goods/product";
    }

    @PostMapping("/goods/{id}/")
    public String showProduct(Model model, @ModelAttribute("productDto") ProductDto product) {
        return "goods/product";
    }

    @PostMapping("/goods/{id}/upload")
    @PreAuthorize("hasAnyAuthority('Admin')")
    public String uploadFile(@RequestParam("file") MultipartFile file, @PathVariable int id) throws IOException {
        var url = PhotoUploader.uploadImage(file);
        var product = service.getById(id);
        product.addPhotoUrl(url);
        service.update(product);
        return "goods/product";
    }

    @PostMapping("/goods/{id}/setMainPhoto")
    @PreAuthorize("hasAnyAuthority('Admin')")
    public String setMainFile(@RequestParam("photoUrl") String photoUrl, @PathVariable int id) {
        var product = service.getById(id);
        product.setMainPhoto(photoUrl);
        service.update(product);
        return "goods/product";
    }

    @GetMapping("/")
    public String showAllGoods(Model model) {
        model.addAttribute("goods", service.read());
        model.addAttribute("productFilter", new ProductFilter());
        return "goods/goods";
    }

    @PostMapping("/goods/search")
    public String searchGoods(Model model, @ModelAttribute("productFilter") ProductFilter filter) {
        model.addAttribute("goods", service.read(filter));
        model.addAttribute("productFilter", filter);
        return "goods/goods";
    }

    @PostMapping("/goods/{id}/addToCart")
    @PreAuthorize("hasAnyAuthority('User')")
    public String addToCart(Model model, @ModelAttribute("productDto") ProductDto productDto,  @AuthenticationPrincipal User user) {
        orderService.createProductInOrder(productDto, user);
        return "redirect:/goods/" + productDto.getProductId() + "/";
    }

    @GetMapping("/cart/{id}/edit")
    @PreAuthorize("hasAnyAuthority('User')")
    public String showEditPage(Model model, @PathVariable int id,  @AuthenticationPrincipal User user) {
        var productInCart = orderService.findProductById(id);
        if(user.getUserId() != productInCart.getUser().getUserId()) return "redirect:/";
        model.addAttribute("productInCart", productInCart);
        return "goods/productInCart";
    }

    @PostMapping("/cart/{id}/edit")
    @PreAuthorize("hasAnyAuthority('User')")
    public String editProductInCart(Model model, @ModelAttribute("productInCart") Order productInCart) {
        model.addAttribute("productInCart", orderService.updateProductInCart(productInCart));
        return "goods/productInCart";
    }

    @PostMapping("/cart/delete")
    @PreAuthorize("hasAnyAuthority('User')")
    public String deleteProductFromCart(@RequestParam int id, @AuthenticationPrincipal User user) {
        var productInCart = orderService.findProductById(id);
        if(user.getUserId() != productInCart.getUser().getUserId()) return "redirect:/";
        orderService.delete(productInCart);

        return "redirect:/user/" + user.getUsername() + "/";
    }

    @GetMapping("/cart/{id}/createOrder")
    @PreAuthorize("hasAnyAuthority('User')")
    public String createOrder(Model model, @ModelAttribute("productInCart") Order productInCart, @PathVariable int id, @AuthenticationPrincipal User user) {
        var order = orderService.findProductById(id);
        if(user.getUserId() != order.getUser().getUserId()) return "redirect:/";
        order.setDelivery(productInCart.isDelivery());
        order.setCount(productInCart.getCount());
        order.setAddress(order.isDelivery() ? order.getUser().getAddress() : order.generateAddress());
        model.addAttribute("order", order);
        return "goods/createOrder";
    }

    @PostMapping("/cart/{id}/createOrder")
    @PreAuthorize("hasAnyAuthority('User')")
    public String createOrder(@ModelAttribute("productInCart") Order productInCart, @PathVariable int id) {
        orderService.createOrder(productInCart, id);
        return "goods/createOrder";
    }
}