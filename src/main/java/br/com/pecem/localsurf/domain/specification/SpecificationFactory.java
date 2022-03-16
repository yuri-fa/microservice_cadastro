package br.com.pecem.localsurf.domain.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class SpecificationFactory<T> {

    public Specification<T> isEquals(String key, Object arg){
        GenericSpecificationsBuilder<T> builder = new GenericSpecificationsBuilder<>();
        return builder.with(key,SearchOperation.EQUALITY, Collections.singletonList(arg)).build();
    }

    public Specification<T> isGreatThan(String key, Comparable arg){
        GenericSpecificationsBuilder<T> builder = new GenericSpecificationsBuilder<>();
        return builder.with(key,SearchOperation.GREATER_THAN,Collections.singletonList(arg)).build();
    }

    public Specification<T> isLessThan(String key, Comparable arg){
        GenericSpecificationsBuilder<T> builder = new GenericSpecificationsBuilder<>();
        return builder.with(key,SearchOperation.LESS_THAN,Collections.singletonList(arg)).build();
    }

    public Specification<T> isLike(String key, Comparable arg){
        GenericSpecificationsBuilder<T> builder = new GenericSpecificationsBuilder<>();
        return builder.with(key,SearchOperation.LIKE,Collections.singletonList(arg)).build();
    }
}
