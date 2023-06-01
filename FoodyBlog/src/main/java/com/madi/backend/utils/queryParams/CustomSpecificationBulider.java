package com.madi.backend.utils.queryParams;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

public class CustomSpecificationBulider {
    private List<SearchCriteria> params = new ArrayList<SearchCriteria>();

    public CustomSpecificationBulider with(String key, String operation, String value) {

        SearchOperation op = SearchOperation.getSimpleOperation(operation.charAt(0));
        if (op != null) {
            SearchCriteria searchCriteria = new SearchCriteria();
            searchCriteria.setKey(key);
            searchCriteria.setOperation(operation);
            searchCriteria.setValue(value);

            params.add(searchCriteria);
        }
        return this;
    }

    public Specification build() {
        if (params.size() == 0) {
            return null;
        }

        Specification result = new CustomSpecification(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            result = params.get(i).isOrPredicate()
                    ? Specification.where(result).or(new CustomSpecification(params.get(i)))
                    : Specification.where(result).and(new CustomSpecification(params.get(i)));
        }

        return result;
    }
}
