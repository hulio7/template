package org.smoliagin.template.infrastructure.output.data.criteria;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@SuperBuilder
public abstract class GetFilteredAndSortedListCommand {
    Integer page;
    Integer size;
    String sort;
    Set<SearchCriteria> criteria;
}