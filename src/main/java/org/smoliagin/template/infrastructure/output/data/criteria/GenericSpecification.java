package org.smoliagin.template.infrastructure.output.data.criteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unchecked")
public class GenericSpecification<T> implements Specification<T> {

    private SearchCriteria criteria;
    private Class<?> type;

    public GenericSpecification(final SearchCriteria criteria, final Class<?> type) {
        super();
        this.criteria = criteria;
        this.type = type;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        var operation = criteria.getOperation();
        switch (operation) {
            case ">":
                return criteriaBuilder.greaterThan(getPath(root), getValue());
            case ">:":
                return criteriaBuilder.greaterThanOrEqualTo(getPath(root), getValue());
            case "<":
                return criteriaBuilder.lessThan(getPath(root), getValue());
            case "<:":
                return criteriaBuilder.lessThanOrEqualTo(getPath(root), getValue());
            case "!:":
                if (getPath(root).getJavaType() == String.class) {
                    return criteriaBuilder.notLike(criteriaBuilder.lower(getPath(root)), "%" + getValue().toString().toLowerCase() + "%");
                } else if (getValue().toString().equals("null")) {
                    return getPath(root).getJavaType() == List.class || getPath(root).getJavaType() == Set.class ?
                            criteriaBuilder.isNotEmpty(getPath(root)) :
                            criteriaBuilder.isNotNull(getPath(root));
                } else {
                    return criteriaBuilder.notEqual(getPath(root), getValue());
                }
            case ":":
                if (getPath(root).getJavaType() == String.class) {
                    return criteriaBuilder.like(criteriaBuilder.lower(getPath(root)), "%" + getValue().toString().toLowerCase() + "%");
                } else if (getValue().toString().equals("null")) {
                    return getPath(root).getJavaType() == List.class || getPath(root).getJavaType() == Set.class ?
                            criteriaBuilder.isEmpty(getPath(root)) :
                            criteriaBuilder.isNull(getPath(root));
                } else {
                    return criteriaBuilder.equal(getPath(root), getValue());
                }
            default:
                return null;
        }
    }

    private Comparable getValue() {
        var value = (Comparable) criteria.getValue();
        var field = criteria.getKey();
        try {
            var fieldType = searchField(field).getType();
            if (fieldType.isEnum()) {
                var enumValue = Enum.valueOf((Class<Enum>) fieldType, String.valueOf(value));
                value = (Comparable) enumValue.getClass().getMethod("getId").invoke(enumValue);
            }
        } catch (Exception ignored) {
        }
        return value;
    }

    private Expression getPath(Root<T> root) {
        var field = criteria.getKey();
        try {
            if (searchField(field).getType().isEnum()) {
                field = field + "Id";
            }
        } catch (Exception ignored) {
        }
        if (field.contains(".")) {
            return root.join(field.split("\\.")[0]).get(field.split("\\.")[1]);
        }
        var expr = root.get(field);
        return root.get(field);
    }

    private Field searchField(String name) {
        var fields = name.split("\\.");
        Field field = null;
        Class<?> fieldType = type;
        for (String s : fields) {
            field = searchField(s, fieldType);
            fieldType = field.getType();
        }
        return field;
    }

    private Field searchField(String name, Class<?> type) {
        var allFields = getAllFields(type);
        for (Field field : allFields) {
            if (field.getName().equals(name)) {
                return field;
            }
        }
        return null;
    }

    List<Field> getAllFields(Class<?> type) {
        if (type == null) {
            return new ArrayList<>();
        }

        List<Field> result = getAllFields(type.getSuperclass());
        result.addAll(List.of(type.getDeclaredFields()));
        return result;
    }
}