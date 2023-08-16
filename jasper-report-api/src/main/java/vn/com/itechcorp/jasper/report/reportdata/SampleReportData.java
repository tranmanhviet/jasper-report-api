package vn.com.itechcorp.jasper.report.reportdata;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Lazy
public class SampleReportData implements ReportData{

    @Override
    public JRDataSource prepareAllData(Map<String, Object> parameters, Map<String, Object> filter) throws Exception {
        if (filter.get("dataSourceName") == null || filter.get("dataSourceName").toString().isEmpty()) {
            return new JREmptyDataSource(1);
        }

        switch (filter.get("dataSourceName").toString()) {
            case "AllAccounts.jrxml":
                List<Map<String, Object>> datas = new ArrayList<>();

                Map<String, Object> row1 = new HashMap<>();
                row1.put("name", "Viet");
                row1.put("phone_office", "Viet");
                row1.put("billing_address_city", "Viet");
                row1.put("billing_address_street", "Viet");
                row1.put("billing_address_country", "Viet");

                Map<String, Object> row2 = new HashMap<>();
                row2.put("name", "Vu Thang");
                row2.put("phone_office", "vt");
                row2.put("billing_address_city", "vt");
                row2.put("billing_address_street", "vt");
                row2.put("billing_address_country", "vt");

                datas.add(row1);
                datas.add(row2);

                parameters.put("Dataset1", new JRBeanCollectionDataSource(datas));

                return new JREmptyDataSource();
            default:
                return new JREmptyDataSource(1);
        }
    }
}
