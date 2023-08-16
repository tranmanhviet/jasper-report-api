package vn.com.itechcorp.jasper.report.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.itechcorp.jasper.report.dto.ReportExportType;
import vn.com.itechcorp.jasper.report.dto.ReportGetDTO;
import vn.com.itechcorp.jasper.report.service.ReportService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/rest/api/report")
public class ReportController {

    private final Logger logger = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    private ReportService reportService;

    @PostMapping(value = "/pdf",produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generateReport(@RequestBody @Valid ReportGetDTO reportGetDTO) {
        try {
            return ResponseEntity.ok(reportService.generateReport(reportGetDTO, ReportExportType.PDF));
        } catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
