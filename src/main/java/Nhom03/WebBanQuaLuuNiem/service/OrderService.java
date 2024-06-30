package Nhom03.WebBanQuaLuuNiem.service;

import Nhom03.WebBanQuaLuuNiem.model.CartItem;
import Nhom03.WebBanQuaLuuNiem.model.Order;
import Nhom03.WebBanQuaLuuNiem.model.OrderDetail;
import Nhom03.WebBanQuaLuuNiem.repository.OrderDetailRepository;
import Nhom03.WebBanQuaLuuNiem.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private CartService cartService;

    @Transactional
    public void createOrder(Order order, List<CartItem> cartItems) {
        order.setOrderDate(new Date());
        order = orderRepository.save(order);
        for (CartItem item : cartItems) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(item.getProduct());
            detail.setQuantity(item.getQuantity());
            orderDetailRepository.save(detail);
        }
        cartService.clearCart();
    }
}