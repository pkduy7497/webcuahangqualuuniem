package Nhom03.WebBanQuaLuuNiem.repository;

import Nhom03.WebBanQuaLuuNiem.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}