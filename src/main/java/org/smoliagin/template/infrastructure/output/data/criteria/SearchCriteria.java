package org.smoliagin.template.infrastructure.output.data.criteria;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchCriteria {
    private final String key;
    private final String operation;
    private final Object value;

    public static class SearchCriteriaBuilder {

        public SearchCriteriaBuilder notEqualOperation() {
            this.operation = "!:";
            return this;
        }
    }
}