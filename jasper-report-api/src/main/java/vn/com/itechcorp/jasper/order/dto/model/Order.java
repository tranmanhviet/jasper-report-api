package vn.com.itechcorp.jasper.order.dto.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import vn.com.itechcorp.base.persistence.model.interfaces.BaseEntity;
import vn.com.itechcorp.jasper.util.DateUtils;

import javax.persistence.Id;
import java.util.Date;

@Document
@Getter @Setter
public class Order implements BaseEntity<String> {

    @Id
    private String id;

    @Field("orderid")
    private Long orderID;

    @Field("orderdate")
    private Date orderDate;

    @Field("customerid")
    private Long customerID;

    @Field("netamount")
    private Double netAmount;

    @Field("tax")
    private Double tax;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderDate=" + DateUtils.convertDateToYYYYMMDDString(orderDate) +
                ", customerID=" + customerID +
                ", netAmount=" + netAmount +
                ", tax=" + tax +
                '}';
    }
}
