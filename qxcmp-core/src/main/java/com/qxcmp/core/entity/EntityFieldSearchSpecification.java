package com.qxcmp.core.entity;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 实体字段过滤器
 *
 * @author Aaric
 */
@RequiredArgsConstructor
public class EntityFieldSearchSpecification implements Specification {

    public static final String ENTITY_SEARCH_FILED_PARA = "field";
    public static final String ENTITY_SEARCH_OPERATION_PARA = "operation";
    public static final String ENTITY_SEARCH_CONTENT_PARA = "search";

    public static final String OPERATION_GREAT_THAN = ">";
    public static final String OPERATION_GREAT_THAN_OR_EQUAL_TO = ">=";
    public static final String OPERATION_LESS_THAN = "<";
    public static final String OPERATION_LESS_THAN_OR_EQUAL_TO = "<=";
    public static final String OPERATION_EQUAL = "=";
    public static final String OPERATION_CONTAINS = ":";
    public static final String OPERATION_NOT_EQUAL = "!=";
    public static final String OPERATION_NOT_CONTAIN = "!:";

    private final HttpServletRequest request;
    private final Class<?> tClass;

    @Override
    public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
        String searchField = request.getParameter(ENTITY_SEARCH_FILED_PARA);
        String searchOperation = request.getParameter(ENTITY_SEARCH_OPERATION_PARA);
        String searchContent = request.getParameter(ENTITY_SEARCH_CONTENT_PARA);

        boolean matchAllField = StringUtils.isBlank(searchField);
        List<String> keys = Arrays.stream(tClass.getDeclaredFields())
                .map(field -> {
                    if (matchAllField || StringUtils.equals(field.getName(), searchField)) {
                        return field.getName();
                    } else {
                        return "";
                    }
                }).filter(StringUtils::isNotBlank).collect(Collectors.toList());

        List<Predicate> restrictions = Lists.newArrayList();
        keys.forEach(s -> {
            try {
                restrictions.add(getRestriction(s, searchOperation, searchContent, root, criteriaBuilder));
            } catch (Exception ignored) {

            }
        });
        return criteriaBuilder.or(restrictions.toArray(new Predicate[restrictions.size()]));
    }

    @SuppressWarnings("unchecked")
    private Predicate getRestriction(String s, String searchOperation, String searchContent, Root root, CriteriaBuilder criteriaBuilder) {
        switch (searchOperation) {
            case OPERATION_GREAT_THAN:
                return criteriaBuilder.greaterThan(root.get(s), searchContent);
            case OPERATION_GREAT_THAN_OR_EQUAL_TO:
                return criteriaBuilder.greaterThanOrEqualTo(root.get(s), searchContent);
            case OPERATION_LESS_THAN:
                return criteriaBuilder.lessThan(root.get(s), searchContent);
            case OPERATION_LESS_THAN_OR_EQUAL_TO:
                return criteriaBuilder.lessThanOrEqualTo(root.get(s), searchContent);
            case OPERATION_EQUAL:
                return criteriaBuilder.equal(root.get(s), searchContent);
            case OPERATION_CONTAINS:
                return criteriaBuilder.like(root.get(s), "%" + searchContent + "%");
            case OPERATION_NOT_EQUAL:
                return criteriaBuilder.notEqual(root.get(s), searchContent);
            case OPERATION_NOT_CONTAIN:
                return criteriaBuilder.notLike(root.get(s), "%" + searchContent + "%");
            default:
                return criteriaBuilder.equal(root.get(s), searchContent);
        }
    }
}
