package by.bsuir.clotheshop.controller;

import by.bsuir.clotheshop.model.entities.dto.ProductDto;
import by.bsuir.clotheshop.model.entities.goods.Product;
import by.bsuir.clotheshop.model.service.ProductService;

import by.bsuir.clotheshop.model.service.cloudinary.PhotoUploader;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Controller
@RequestMapping(value = "")
public class GoodsController {

    @Autowired
    ProductService service;

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
}