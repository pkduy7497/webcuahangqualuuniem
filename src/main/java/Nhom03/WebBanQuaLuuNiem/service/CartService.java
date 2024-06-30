package Nhom03.WebBanQuaLuuNiem.service;

import Nhom03.WebBanQuaLuuNiem.model.CartItem;
import Nhom03.WebBanQuaLuuNiem.model.Product;
import Nhom03.WebBanQuaLuuNiem.repository.ProductRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    @Autowired
    private ProductRepository productRepository;

    @Getter
    private final List<CartItem> cartItems = new ArrayList<>();

    public void addToCart(Long productId, int quantity) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + productId));
        cartItems.add(new CartItem(product, quantity));
    }

    public void removeFromCart(Long productId) {
        cartItems.removeIf(item -> item.getProduct().getId().equals(productId));
    }

    public void clearCart() {
        cartItems.clear();
    }
}