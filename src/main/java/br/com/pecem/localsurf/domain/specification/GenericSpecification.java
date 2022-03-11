package br.com.pecem.localsurf.domain.specification;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class GenericSpecification<T> implements Specification<T> {

    private SearchCriteria criteria;

    public GenericSpecification(final SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Object> argumentos = criteria.getArguments();
        Object argumento = argumentos.get(0);

        switch (criteria.getSearchOperation()){
            case EQUALITY:
                return criteriaBuilder.equal(root.get(criteria.getKey()),argumento);
            case GREATER_THAN:
                return criteriaBuilder.greaterThan(root.get(criteria.getKey()),(Comparable) argumento);
            case LESS_THAN:
                return criteriaBuilder.lessThan(root.get(criteria.getKey()),(Comparable) argumento);
            case NEGATION:
                return criteriaBuilder.notEqual(root.get(criteria.getKey()),argumento);
            case IN:
                return root.get(criteria.getKey()).in(argumentos);
            default:
                return null;
        }
    }
}
