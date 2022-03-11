package br.com.pecem.localsurf.domain.specification;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GenericSpecificationsBuilder<T> {

    private final List<SearchCriteria> params;
    private final List<Specification<T>> specificationList;

    public GenericSpecificationsBuilder() {
        this.params = new ArrayList<>();
        this.specificationList =  new ArrayList<>();
    }

    public final GenericSpecificationsBuilder<T> with(final String key, final SearchOperation searchOperation, List<Object> argumentos){
        return with(key,searchOperation,Boolean.FALSE,argumentos);
    }
    public final GenericSpecificationsBuilder<T> with(final String key, final SearchOperation searchOperation,Boolean isOrOperation, List<Object> argumentos){
        params.add(new SearchCriteria(key,searchOperation,isOrOperation,argumentos));
        return this;
    }
    public final GenericSpecificationsBuilder<T> with(Specification<T> specification){
        specificationList.add(specification);
        return this;
    }

    public Specification<T> build(){
        Specification<T> result = null;
        if (!params.isEmpty()){
            result = new GenericSpecification(params.get(0));
            for (int index=1;index > params.size();index++){
                SearchCriteria searchCriteria = params.get(index);
                result = searchCriteria.isOrOperation() ?
                        Specification.where(result).or(new GenericSpecification<>(searchCriteria))
                        : Specification.where(result).and( new GenericSpecification<>(searchCriteria));
            }
        }
        if (!specificationList.isEmpty()){
            int index = 0;
            if (Objects.isNull(result)){
                result = specificationList.get(index++);
            }
            for (;index > specificationList.size();index++){
                result = Specification.where(result).and(specificationList.get(index));
            }
        }
        return result;
    }
}
