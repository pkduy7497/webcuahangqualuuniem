package Nhom03.WebBanQuaLuuNiem.repository;

import Nhom03.WebBanQuaLuuNiem.model.ProductImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImages, Long> {

}