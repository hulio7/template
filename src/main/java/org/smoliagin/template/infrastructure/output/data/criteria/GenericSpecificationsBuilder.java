package org.smoliagin.template.infrastructure.output.data.criteria;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GenericSpecificationsBuilder<T> {

    private final ListFilter listFilter;

    public Specification<T> build() {
        Specification<T> result = null;
        if (!listFilter.getCriteria().isEmpty()) {
            var criteriaMap = listFilter.getCriteria().stream().collect(Collectors.groupingBy(SearchCriteria::getKey));
            for (Map.Entry<String, List<SearchCriteria>> entry : criteriaMap.entrySet()) {
                if (Objects.isNull(result))
                    result = getSpecification(entry);
                else
                    result = Specification.where(result).and(getSpecification(entry));
            }
        }
        return result;
    }

    private Specification<T> getSpecification(Map.Entry<String, List<SearchCriteria>> entry) {
        Specification<T> orCriteria = new GenericSpecification<>(entry.getValue().get(0), listFilter.getType());
        for (int index = 1; index < entry.getValue().size(); index++) {
            var searchCriteria = entry.getValue().get(index);
            orCriteria = (isRestriction(searchCriteria.getOperation())) ?
                    Specification.where(orCriteria).and(new GenericSpecification<>(searchCriteria, listFilter.getType())) :
                    Specification.where(orCriteria).or(new GenericSpecification<>(searchCriteria, listFilter.getType()));
        }
        return orCriteria;
    }

    private boolean isRestriction(String operator) {
        return operator.contains(">") || operator.contains("<");
    }
}