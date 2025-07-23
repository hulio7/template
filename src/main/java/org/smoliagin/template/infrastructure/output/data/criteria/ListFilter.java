package org.smoliagin.template.infrastructure.output.data.criteria;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ListFilter {
    private static final String ASC = "ASC";
    private static final String DESC = "DESC";

    Integer page;
    Integer size;
    String sortBy;
    String direction;
    List<SearchCriteria> criteria;
    Class<?> type;

    public static String getDirection(String sort) {
        var sortValues = sort.split(",");
        return sortValues.length > 1 && sortValues[1].equalsIgnoreCase(ASC) ? ASC : DESC;
    }

    public static String getSortBy(String sort) {
        var sortValues = sort.split(",");
        return sortValues[0];
    }
}