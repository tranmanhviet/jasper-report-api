package vn.com.itechcorp.jasper.order.dto;

import lombok.Getter;
import lombok.Setter;
import vn.com.itechcorp.base.service.dto.DtoGet;
import vn.com.itechcorp.jasper.order.dto.model.Order;

import java.util.Date;

@Getter @Setter
public class OrderDTOGet extends DtoGet<Order, String> {

    public OrderDTOGet(Order order){
        super(order);
    }

    private Long orderID;

    private Date orderDate;

    private Long customerID;

    private Double netAmount;

    private Double tax;

    @Override
    public void parse(Order order) {
        this.customerID = order.getCustomerID();
        this.orderID = order.getOrderID();
        this.tax = order.getTax();
        this.orderDate = order.getOrderDate();
        this.netAmount = order.getNetAmount();
    }
}
