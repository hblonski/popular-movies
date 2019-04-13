package com.popularmovies.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class FormatUtil {

    public final static  String ORIGINAL_DATE_FORMAT = "yyyy-MM-dd";

    public final static  String APP_DATE_FORMAT = "dd/MM/yyyy";

    public static String formatDate (String unformattedDate) throws ParseException {
        if (unformattedDate != null) {
            Date date = new SimpleDateFormat(ORIGINAL_DATE_FORMAT).parse(unformattedDate);
            return new SimpleDateFormat(APP_DATE_FORMAT).format(date);
        }
        return null;
    }
}
