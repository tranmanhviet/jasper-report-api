package vn.com.itechcorp.jasper;

import com.lowagie.text.pdf.codec.Base64;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vn.com.itechcorp.jasper.order.dto.OrderDTOGet;
import vn.com.itechcorp.jasper.order.dto.OrderSummary;
import vn.com.itechcorp.jasper.order.dto.filter.OrderFilter;
import vn.com.itechcorp.jasper.order.dto.model.Order;
import vn.com.itechcorp.jasper.order.repository.OrderRepository;
import vn.com.itechcorp.jasper.order.service.OrderService;

import java.io.*;
import java.util.*;

@SpringBootTest
class ReportApplicationTests {

    @Test
    void contextLoads() {
    }

    public static byte[] exportReport(JasperPrint jasperPrint, ExportReportType type) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            switch (type) {
                case XML:
                    JRXmlExporter xmlExporter = new JRXmlExporter();
                    xmlExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                    xmlExporter.setExporterOutput(new SimpleXmlExporterOutput(baos));

                    xmlExporter.exportReport();

                    return baos.toByteArray();
                case CSV:
                    JRCsvExporter csvExporter = new JRCsvExporter();
                    csvExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                    csvExporter.setExporterOutput(new SimpleWriterExporterOutput(baos));

                    csvExporter.exportReport();

                    return baos.toByteArray();
                case XLSX:
                    JRXlsxExporter xlsxExporter = new JRXlsxExporter();
                    xlsxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                    xlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));

                    xlsxExporter.exportReport();

                    return baos.toByteArray();
                case DOCX:
                    JRDocxExporter docxExporter = new JRDocxExporter();
                    docxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                    docxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));

                    docxExporter.exportReport();

                    return baos.toByteArray();
                case PDF:
                    return JasperExportManager.exportReportToPdf(jasperPrint);
                case HTML:
                    HtmlExporter htmlExporter = new HtmlExporter();
                    htmlExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                    // config exporter output
                    SimpleHtmlExporterOutput simpleHtmlExporterOutput = new SimpleHtmlExporterOutput(baos);
                    // hiển thị hình ảnh trong báo cáo dưới dạng base64
                    // mặc định là : lưu ảnh trong báo cáo vào ổ đĩa xong sử dụng
                    // đường dẫn để hiển thị ảnh trong thẻ <img>
                    Map<String, String> base64Images = new HashMap<>();
                    simpleHtmlExporterOutput.setImageHandler(new HtmlResourceHandler() {
                        @Override
                        public void handleResource(String id, byte[] data) {
                            base64Images.put(id, "data:image/jpg;base64," + Base64.encodeBytes(data));
                        }

                        @Override
                        public String getResourcePath(String id) {
                            return base64Images.get(id);
                        }
                    });
                    htmlExporter.setExporterOutput(simpleHtmlExporterOutput);

                    htmlExporter.exportReport();

                    return baos.toByteArray();
                default:
                    return null;
            }
        } catch (IOException | JRException e) {
            throw new RuntimeException(e);
        }
    }

    enum ExportReportType {

        PDF, HTML, CSV, XLSX, DOCX, XML
    }

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void test1() {
        List<Order> orders = orderRepository.findAll();

        orders.forEach(System.out::println);
    }

    @Autowired
    private OrderService orderService;

    @Test
    void test2() throws Exception{
        OrderFilter orderFilter = new OrderFilter();
//        orderFilter.setStartDate(DateUtils.convertYYYYMMDDToDate("2004-01-20"));
//        orderFilter.setEndDate(DateUtils.convertYYYYMMDDToDate("2004-01-30"));

        List<OrderDTOGet> orders = orderService.getAll(orderFilter);

        orders.forEach(System.out::println);
    }

    @Test
    void test3() {
        List<OrderSummary> orderSummaries = orderService.getSummarizeData1();

        orderSummaries.forEach(System.out::println);
    }

    @Test
    void test4() throws JRException {
        JasperCompileManager.compileReport("src/main/resources/templates/test.jrxml");
    }
}
