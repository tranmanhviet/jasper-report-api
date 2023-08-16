package vn.com.itechcorp.jasper.base.filter;

import org.springframework.data.mongodb.core.query.Query;

import java.io.Serializable;

public interface BaseFilter extends Serializable {

    Query toQuery();

}
