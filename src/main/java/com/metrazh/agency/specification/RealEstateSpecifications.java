package com.metrazh.agency.specification;

import com.metrazh.agency.dto.SearchFilters;
import com.metrazh.agency.entity.RealEstate;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Побудова динамічного WHERE для пошуку/фільтрації нерухомості.
 * Java/JPA-еквівалент функції search_real_estate(filters) з оригінального db.py,
 * де SQL-рядок збирався вручну додаванням "AND ...".
 *
 * ВАЖЛИВО: тут (і в soldBetween нижче) предикати для необов'язкових
 * дат/фільтрів додаються в WHERE, лише якщо значення не null. Такий підхід
 * (замість "CAST(:param AS timestamp) IS NULL OR ...") — навмисний: коли
 * Hibernate 6 біндить у PostgreSQL параметр зі значенням null всередині
 * CAST(...), драйвер іноді не може визначити тип параметра і падає з
 * "cannot cast type bytea to timestamp". Якщо предикат просто не додається
 * в дерево запиту, null ніколи не потрапляє в SQL — і проблема не виникає.
 */
public final class RealEstateSpecifications {

    private RealEstateSpecifications() {
    }

    public static Specification<RealEstate> fromFilters(SearchFilters f) {
        return (root, query, cb) -> {
            // LEFT JOIN до трьох таблиць специфіки — потрібні для фільтра по кімнатах
            var apartmentJoin = root.join("apartment", JoinType.LEFT);
            var houseJoin = root.join("house", JoinType.LEFT);
            var officeJoin = root.join("office", JoinType.LEFT);

            List<Predicate> predicates = new ArrayList<>();

            if (f.getTypeId() != null) {
                predicates.add(cb.equal(root.get("type").get("typeId"), f.getTypeId()));
            }

            if (f.getDistrictId() != null) {
                predicates.add(cb.equal(root.get("district").get("districtId"), f.getDistrictId()));
            }

            if (f.getStatusId() != null) {
                predicates.add(cb.equal(root.get("status").get("statusId"), f.getStatusId()));
            } else if (!f.isIncludeArchived()) {
                // За замовчуванням — лише активні (статус 1 або 2), як в оригіналі
                predicates.add(root.get("status").get("statusId").in(Arrays.asList(1, 2)));
            }

            if (f.getMinPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), f.getMinPrice()));
            }
            if (f.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), f.getMaxPrice()));
            }

            if (f.getMinArea() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("totalArea"), f.getMinArea()));
            }
            if (f.getMaxArea() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("totalArea"), f.getMaxArea()));
            }

            if (f.getRooms() != null) {
                // Кімнати шукаємо в усіх трьох таблицях (OR), як в оригіналі
                predicates.add(cb.or(
                        cb.equal(apartmentJoin.get("rooms"), f.getRooms()),
                        cb.equal(houseJoin.get("rooms"), f.getRooms()),
                        cb.equal(officeJoin.get("roomsCount"), f.getRooms())
                ));
            }

            query.distinct(true);
            // COUNT-запити (Long.class) не можуть мати ORDER BY по несплюченій колонці
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                query.orderBy(cb.desc(root.get("createdAt")));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Продані об'єкти (status_id = 3) за період створення, згруповані для
     * reports.html. Аналог get_detailed_sold_objects(start_date, end_date) з db.py.
     * start/end можуть бути null — тоді відповідна межа періоду просто не застосовується.
     */
    public static Specification<RealEstate> soldBetween(LocalDateTime start, LocalDateTime end) {
        return (root, query, cb) -> {
            // Eager fetch, щоб уникнути LazyInitializationException/N+1 у шаблоні
            root.fetch("type", JoinType.INNER);
            root.fetch("district", JoinType.INNER);
            root.fetch("apartment", JoinType.LEFT);
            root.fetch("house", JoinType.LEFT);
            root.fetch("office", JoinType.LEFT);

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("status").get("statusId"), 3));

            if (start != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), start));
            }
            if (end != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), end));
            }

            query.distinct(true);
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                query.orderBy(cb.asc(root.get("type").get("name")), cb.desc(root.get("createdAt")));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
