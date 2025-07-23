package org.smoliagin.template.infrastructure.output.data.criteria;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public abstract class FilteringAndSortingAdapter<T> {

    protected Pageable getPageable(ListFilter filter) {
        return PageRequest.of(filter.getPage(), filter.getSize(), Sort.Direction.valueOf(filter.getDirection()), getSortedBy(filter));
    }

    protected Specification<T> getSpecification(ListFilter filter) {
        GenericSpecificationsBuilder<T> builder = new GenericSpecificationsBuilder<>(filter);
        return builder.build();
    }

    private String getSortedBy(ListFilter filter) {
        try {
            if (filter.getType().getDeclaredField(filter.getSortBy()).getType().isEnum()) {
                return filter.getSortBy() + "Id";
            }
        } catch (Exception ignored) {}
        return filter.getSortBy();
    }
}