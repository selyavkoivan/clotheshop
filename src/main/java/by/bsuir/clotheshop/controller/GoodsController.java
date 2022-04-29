package by.bsuir.clotheshop.controller;

import by.bsuir.clotheshop.model.entities.goods.Product;
import by.bsuir.clotheshop.model.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping(value = "")
public class GoodsController {

    @Autowired
    ProductService service;

    @GetMapping("/goods/add")
    @PreAuthorize("hasAnyAuthority('Admin')")
    public String addProduct(Model model)
    {
        model.addAttribute("product", new Product());
        return "goods/inputProduct";
    }

    @PostMapping("/goods/input-data")
    @PreAuthorize("hasAnyAuthority('Admin')")
    public String saveProduct(Model model, @ModelAttribute("product") Product product)
    {
        model.addAttribute("product", product.getProductId() == 0 ? service.create(product) : service.update(product));
        return "goods/product";
    }

    @GetMapping("/goods/{id}/edit")
    @PreAuthorize("hasAnyAuthority('Admin')")
    public String editProduct(Model model, @PathVariable int id)
    {
        model.addAttribute("product", service.getById(id));
        return "goods/inputProduct";
    }

    @GetMapping("/goods/{id}")
    public String addProduct(Model model, @PathVariable int id)
    {
        model.addAttribute("product", service.getById(id));
        return "goods/product";
    }
}

