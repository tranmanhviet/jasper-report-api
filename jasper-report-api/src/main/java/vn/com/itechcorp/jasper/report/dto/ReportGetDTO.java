package vn.com.itechcorp.jasper.report.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.com.itechcorp.base.service.dto.Dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

@NoArgsConstructor
@Getter @Setter
public class ReportGetDTO extends Dto {

    @NotNull @NotEmpty
    private String template;

    private Map<String, Object> filter;
}
