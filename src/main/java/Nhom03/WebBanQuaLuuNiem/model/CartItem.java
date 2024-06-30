package Nhom03.WebBanQuaLuuNiem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class CartItem {
    private Product product;
    private int quantity;
}