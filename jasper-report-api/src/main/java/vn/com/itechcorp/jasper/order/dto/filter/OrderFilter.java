package vn.com.itechcorp.jasper.order.dto.filter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import vn.com.itechcorp.jasper.base.filter.BaseFilter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter @Setter
public class OrderFilter implements BaseFilter {

    private Date startDate;

    private Date endDate;

    @Override
    public Query toQuery() {
        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();

        if (startDate != null) {
            criteriaList.add(Criteria.where("orderdate").gte(startDate));
        }

        if (endDate != null) {
            criteriaList.add(Criteria.where("orderdate").lte(endDate));
        }

        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }
        return query;
    }
}
