package org.smoliagin.template.infrastructure.output.data.criteria;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Value
@SuperBuilder
public class GetFilteredAndSortedUserListCommand extends GetFilteredAndSortedListCommand{
}
