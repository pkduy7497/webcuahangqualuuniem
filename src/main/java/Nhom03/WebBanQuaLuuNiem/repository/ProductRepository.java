package Nhom03.WebBanQuaLuuNiem.repository;

import Nhom03.WebBanQuaLuuNiem.model.Product;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
// ...
}
