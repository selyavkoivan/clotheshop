package by.bsuir.clotheshop.controller;

import by.bsuir.clotheshop.model.entities.dto.OrderFilter;
import by.bsuir.clotheshop.model.entities.goods.Order;
import by.bsuir.clotheshop.model.entities.user.User;
import by.bsuir.clotheshop.model.entities.user.role.Role;
import by.bsuir.clotheshop.model.service.OrderService;
import by.bsuir.clotheshop.model.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping(value = "")
public class OrdersController {

    @Autowired
    ProductService service;

    @Autowired
    OrderService orderService;

    @GetMapping("/order/")
    @PreAuthorize("hasAnyAuthority('Admin')")
    public String showOrders(Model model) {
        model.addAttribute("orders", orderService.findAllOrders());
        model.addAttribute("filter", new OrderFilter(true));
        return "user/orders";
    }

    @PostMapping("/order/search")
    public String searchOrders(Model model, @ModelAttribute("filter") OrderFilter filter) {
        model.addAttribute("orders", orderService.read(filter, null));
        model.addAttribute("filter", filter);
        return "user/orders";
    }

    @GetMapping("/order/my-order/")
    public String showOrders(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("orders", orderService.findAllOrders(user.getUsername()));
        model.addAttribute("filter", new OrderFilter(false));
        return "user/orders";
    }

    @PostMapping("/order/my-order/search")
    public String searchMyOrders(Model model, @ModelAttribute("filter") OrderFilter filter, @AuthenticationPrincipal User user) {
        model.addAttribute("orders", orderService.read(filter, user));
        model.addAttribute("filter", filter);
        return "user/orders";
    }

    @GetMapping("/order/{id}/")
    public String showOrder(Model model,@PathVariable int id, @AuthenticationPrincipal User user) {
        var order = orderService.findById(id);
        if((!user.getRoles().contains(Role.Admin) && user.getUserId() != order.getUser().getUserId()) || order.getStatus() == 0) return "redirect:/";
        model.addAttribute("order",order);
        return "user/order";
    }

    @PostMapping("/order/{id}/finish")
    public String finishOrder(@PathVariable int id) {

        orderService.finishOrder(id);
        return "";
    }

    @PostMapping("/order/{id}/")
    public String setOrderStatus(Model model, @ModelAttribute("order") Order order, @PathVariable int id) {
        model.addAttribute("order", orderService.updateOrder(order, id));

        return "/user/order";
    }
}