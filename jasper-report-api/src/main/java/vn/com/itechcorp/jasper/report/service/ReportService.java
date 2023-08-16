package vn.com.itechcorp.jasper.report.service;

import net.sf.jasperreports.engine.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import vn.com.itechcorp.jasper.report.dto.ReportExportType;
import vn.com.itechcorp.jasper.report.dto.ReportGetDTO;
import vn.com.itechcorp.jasper.report.reportdata.ReportData;
import vn.com.itechcorp.jasper.util.ExportUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReportService {

    private final Logger logger = LoggerFactory.getLogger(ReportData.class);

    @Autowired
    private ApplicationContext applicationContext;

    public byte[] generateReport(ReportGetDTO reportGetDTO, ReportExportType exportType) throws Exception {
        // validate report
        JasperReport jasperReport = validate(reportGetDTO);

        // lấy dữ liệu tương ứng với template
        String dataSourcePath = jasperReport.getProperty("dataSourcePath");
        if (dataSourcePath == null || dataSourcePath.isEmpty()) {
            String errorMsg = "Report template missing {dataSourcePath} property!";
            logger.error(errorMsg);
            throw new Exception(errorMsg);
        }

        ReportData reportData = applicationContext.getBean(dataSourcePath, ReportData.class);

        // lưu các parameter cho báo cáo
        Map<String, Object> parameters = new HashMap<>();

        // chuẩn bị data source + parameter
        JRDataSource dataSource = reportData.prepareAllData(parameters, reportGetDTO.getFilter());

        // điền dữ liệu từ parameters và datasource vào đối tượng JasperReport
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // mặc định xuất báo cáo dưới dạng pdf
//        return JasperExportManager.exportReportToPdf(jasperPrint);
        return ExportUtil.exportReport(jasperPrint, exportType);
    }

    // validate template, filter
    private JasperReport validate(ReportGetDTO reportGetDTO) throws Exception {
        if (reportGetDTO.getTemplate() == null || reportGetDTO.getTemplate().isEmpty()) {
            logger.error("Template is null!");
            throw new Exception("Template is null!");
        }

        String template = new String(Base64.getDecoder().decode(reportGetDTO.getTemplate()));

        try (InputStream inputStream = new ByteArrayInputStream(template.getBytes())) {
            long start = System.currentTimeMillis();
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
            long compileTime = System.currentTimeMillis() - start;
            logger.info("Compile template time : " + compileTime);


            // TODO : validate template

            return jasperReport;
        }
    }
}
