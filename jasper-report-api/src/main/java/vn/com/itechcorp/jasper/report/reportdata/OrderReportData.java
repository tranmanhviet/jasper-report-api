package vn.com.itechcorp.jasper.report.reportdata;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import vn.com.itechcorp.jasper.order.dto.OrderDTOGet;
import vn.com.itechcorp.jasper.order.dto.filter.OrderFilter;
import vn.com.itechcorp.jasper.order.service.OrderService;
import vn.com.itechcorp.jasper.util.DateUtils;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Component
@Lazy
public class OrderReportData implements ReportData{

    private final Logger logger = LoggerFactory.getLogger(OrderReportData.class);

    public OrderReportData() {
        logger.info("Create bean with name: orderReportData");
    }

    @Autowired
    private OrderService orderService;

    @Override
    public JRDataSource prepareAllData(Map<String, Object> parameters, Map<String, Object> filter) throws ParseException {
        OrderFilter orderFilter = new OrderFilter();

        // validate filter
        String startDate = (String) filter.get("startDate");
        if (startDate != null && !startDate.isEmpty()) {
            orderFilter.setStartDate(DateUtils.convertYYYYMMDDToDate(startDate));
        }

        String endDate = (String) filter.get("endDate");
        if (endDate != null && !endDate.isEmpty()) {
            orderFilter.setEndDate(DateUtils.convertYYYYMMDDToDate(endDate));
        }

        List<OrderDTOGet> orders = orderService.getAll(orderFilter);

        return new JRBeanCollectionDataSource(orders);
    }
}
