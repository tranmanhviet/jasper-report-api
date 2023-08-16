package vn.com.itechcorp.jasper.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static String convertDateToYYYYMMDDString(Date date) {
        return dateFormat.format(date);
    }

    public static Date convertYYYYMMDDToDate(String yyyyMMdd) throws ParseException {
        return dateFormat.parse(yyyyMMdd);
    }
}
