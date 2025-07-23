package org.smoliagin.template.infrastructure.output.data.criteria;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class WebUtils {
    private static final String STRING_PATTERN = "([\\w\\-.]?|([а-яА-ЯёЁ0-9_\\-.]?))+?";
    private static final String LOCAL_DATE_TIME_PATTERN = "(\\d{4})-(\\d{2})-(\\d{2})T(\\d{2}):(\\d{2}):(\\d{2})";
    private static final String LOCAL_DATE_PATTERN = "(\\d{4})-(\\d{2})-(\\d{2})";
    private static final String EMAIL_PATTERN = "(?:[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[\\w]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    public static Set<SearchCriteria> getCriteria(String search) {
        var pattern = Pattern.compile("(\\w+?\\.?\\w+?)(:|>|>:|<|<:|!:)("
                + STRING_PATTERN + "|"
                + EMAIL_PATTERN + "|"
                + LOCAL_DATE_PATTERN + "|"
                + LOCAL_DATE_TIME_PATTERN + "),");
        var matcher = pattern.matcher(search + ",");
        var criteria = new HashSet<SearchCriteria>();
        while (matcher.find()) {
            criteria.add(SearchCriteria.builder()
                    .key(matcher.group(1))
                    .operation(matcher.group(2))
                    .value(getValue(matcher.group(3)))
                    .build());
        }
        return criteria;
    }

    private static Object getValue(String group) {
        var localDateTimePattern = Pattern.compile(LOCAL_DATE_TIME_PATTERN);
        var localDatePattern = Pattern.compile(LOCAL_DATE_PATTERN);
        if (localDateTimePattern.matcher(group).matches())
            return LocalDateTime.parse(group);
        if (localDatePattern.matcher(group).matches())
            return LocalDate.parse(group);
        return group;
    }

    public static HttpHeaders getHeaders(String name) {
        var contentDisposition = ContentDisposition.builder("inline")
                .filename(name)
                .build();
        var headers = new HttpHeaders();
        headers.setContentDisposition(contentDisposition);
        return headers;
    }
}