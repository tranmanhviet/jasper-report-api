package vn.com.itechcorp.jasper.order.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.com.itechcorp.jasper.order.dto.model.Order;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

    List<Order> findByOrderDateBetweenOrderByOrderIDDesc(Date start, Date end);
}
