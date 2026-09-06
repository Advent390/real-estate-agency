package com.metrazh.agency.specification;

import com.metrazh.agency.entity.Viewing;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Заявки на перегляд за період. Аналог get_recent_viewings_report(start, end)
 * з db.py. Предикати додаються лише за наявності значення (start/end != null),
 * щоб уникнути передачі null-параметра в SQL напряму (див. коментар у
 * RealEstateSpecifications щодо PostgreSQL/Hibernate 6 та CAST(... AS timestamp)).
 */
public final class ViewingSpecifications {

    private ViewingSpecifications() {
    }

    public static Specification<Viewing> dateBetween(LocalDateTime start, LocalDateTime end) {
        return (root, query, cb) -> {
            // Eager fetch, щоб уникнути LazyInitializationException/N+1 у шаблоні
            root.fetch("client", JoinType.INNER);
            var reFetch = root.fetch("realEstate", JoinType.INNER);
            reFetch.fetch("type", JoinType.INNER);

            List<Predicate> predicates = new ArrayList<>();
            if (start != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("viewingDate"), start));
            }
            if (end != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("viewingDate"), end));
            }

            query.distinct(true);
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                query.orderBy(cb.desc(root.get("viewingDate")));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
