package Nhom03.WebBanQuaLuuNiem.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customerName;
    private String shipAddress;
    private String phone;
    private String note;
    private Date orderDate;
    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails;
}