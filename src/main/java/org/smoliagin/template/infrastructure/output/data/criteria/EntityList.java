package org.smoliagin.template.infrastructure.output.data.criteria;

import lombok.Value;

import java.util.List;

@Value
public class EntityList<T> {

    long totalItems;
    List<T> items;
}