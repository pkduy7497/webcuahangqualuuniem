package Nhom03.WebBanQuaLuuNiem.repository;

import Nhom03.WebBanQuaLuuNiem.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

}