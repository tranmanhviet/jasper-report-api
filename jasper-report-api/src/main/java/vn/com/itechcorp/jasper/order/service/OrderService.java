package vn.com.itechcorp.jasper.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import vn.com.itechcorp.jasper.order.dto.OrderDTOGet;
import vn.com.itechcorp.jasper.order.dto.OrderSummary;
import vn.com.itechcorp.jasper.order.dto.model.Order;
import vn.com.itechcorp.jasper.order.repository.CustomAggregationOperation;
import vn.com.itechcorp.jasper.order.repository.OrderRepository;
import vn.com.itechcorp.jasper.base.service.BaseServiceImpl;

import java.util.List;

@Service
public class OrderService extends BaseServiceImpl<OrderDTOGet, Order, String> {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public OrderDTOGet convert(Order order) {
        return new OrderDTOGet(order);
    }

    @Override
    public MongoRepository<Order, String> getRepository() {
        return orderRepository;
    }

    @Override
    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    @Override
    public Class<Order> clazz() {
        return Order.class;
    }

    public List<OrderSummary> getSummarizeData1() {
        String m1 = "{\n" +
                "    $match: { orderdate: { $gte: new Date(\"2004-01-01\"), $lte: new Date(\"2004-01-31\") } }\n" +
                "  }";

        String g1 = "{\n" +
                "    $group: {\n" +
                "      _id: \"$customerid\",\n" +
                "      totalNetAmount: { $sum: \"$netamount\" },\n" +
                "      totalTax: { $sum: \"$tax\" },\n" +
                "      count: { $sum: 1 }\n" +
                "    }\n" +
                "  }";

        String addField1 = "{\n" +
                "    $addFields: {\n" +
                "      totalAmount: { $add: [\"$totalNetAmount\", \"$totalTax\"] }\n" +
                "    }\n" +
                "  }";

        String p1 = "{\n" +
                "    $project: {\n" +
                "      id: \"$_id\",\n" +
                "      totalAmount: 1,\n" +
                "      count: 1\n" +
                "    }\n" +
                "  }";

        Aggregation aggregation = Aggregation.newAggregation(
//                new CustomAggregationOperation(m1),
                new CustomAggregationOperation(g1),
                new CustomAggregationOperation(addField1),
                new CustomAggregationOperation(p1)
        );
        AggregationResults<OrderSummary> orderSummaries = mongoTemplate.aggregate(aggregation, Order.class, OrderSummary.class);

        return orderSummaries.getMappedResults();
    }
}
