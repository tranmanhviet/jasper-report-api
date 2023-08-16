package vn.com.itechcorp.jasper.order.dto;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class OrderSummary {

    private Long id;

    private Long count;

    private Long totalAmount;

    @Override
    public String toString() {
        return "OrderSummary{" +
                "customerID=" + id +
                ", count=" + count +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
