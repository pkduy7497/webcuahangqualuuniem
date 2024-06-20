package Nhom03.WebBanQuaLuuNiem.model;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public int Quantity;

    public float ImportPrice;

    public float SalesPrice;

    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}