package Nhom03.WebBanQuaLuuNiem.controller;

import Nhom03.WebBanQuaLuuNiem.model.CartItem;
import Nhom03.WebBanQuaLuuNiem.model.Order;
import Nhom03.WebBanQuaLuuNiem.service.CartService;
import Nhom03.WebBanQuaLuuNiem.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @GetMapping("/checkout")
    public String checkout(Model model) {
        model.addAttribute("order", new Order());
        return "cart/checkout";
    }

    @PostMapping("/submit")
    public String submitOrder(@Valid Order order) {
        List<CartItem> cartItems = cartService.getCartItems();
        if (cartItems.isEmpty()) {
            return "redirect:/cart";
        }
        orderService.createOrder(order, cartItems);
        return "redirect:@confirmation";
    }

    @GetMapping("/confirmation")
    public String orderConfirmation(Model model) {
        model.addAttribute("message", "Your order has been successfully placed.");
        return "cart/order-confirmation";
    }
}