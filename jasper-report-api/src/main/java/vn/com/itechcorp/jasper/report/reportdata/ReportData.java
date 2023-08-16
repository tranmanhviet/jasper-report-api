package vn.com.itechcorp.jasper.report.reportdata;

import net.sf.jasperreports.engine.JRDataSource;

import java.util.Map;

public interface ReportData {

    JRDataSource prepareAllData(Map<String, Object> parameters, Map<String, Object> filter) throws Exception;
}
