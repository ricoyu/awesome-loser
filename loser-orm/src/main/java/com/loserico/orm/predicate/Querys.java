package com.loserico.orm.predicate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.loserico.orm.predicate.CompareMode.GT;
import static com.loserico.orm.predicate.DateMatchMode.LATER_THAN;

/**
 * 查询条件构建器
 */
public final class Querys {

    public static final class QueryBuilder {
        private List<Predicate> predicates = new ArrayList<>();

        public QueryBuilder add(Predicate predicate) {
            predicates.add(predicate);
            return this;
        }

        public List<Predicate> predicates() {
            return predicates;
        }


        public QueryBuilder eq(String propertyName, Object propertyValue) {
            if (propertyValue == null) {
                StringPredicate predicate = new StringPredicate(propertyName, null);
                add(predicate);
                return this;
            }

            if (propertyValue instanceof String) {
                Predicate predicate = stringPredicate(propertyName, (String) propertyValue);
                add(predicate);
                return this;
            }

            if (propertyValue instanceof Integer) {
                Predicate predicate = integerPredicate(propertyName, (Integer) propertyValue);
                add(predicate);
                return this;
            }

            if (propertyValue instanceof Long) {
                Predicate predicate = longPredicate(propertyName, (Long) propertyValue);
                add(predicate);
                return this;
            }

            if (propertyValue instanceof Boolean) {
                Predicate predicate = booleanPredicate(propertyName, (Boolean) propertyValue);
                add(predicate);
                return this;
            }

            if (propertyValue instanceof LocalDate) {
                Predicate predicate = localDatePredicate(propertyName, (LocalDate) propertyValue);
                add(predicate);
                return this;
            }

            Predicate predicate = basicPredicate(propertyName, propertyValue);
            add(predicate);
            return this;
        }

        public QueryBuilder gt(String propertyName, Object propertyValue) {
            if (propertyValue == null) {
                StringPredicate predicate = new StringPredicate(propertyName, null, GT);
                add(predicate);
                return this;
            }

            if (propertyValue instanceof String) {
                Predicate predicate = stringPredicate(propertyName, (String) propertyValue, GT);
                add(predicate);
                return this;
            }

            if (propertyValue instanceof Integer) {
                Predicate predicate = integerPredicate(propertyName, (Integer) propertyValue, GT);
                add(predicate);
                return this;
            }

            if (propertyValue instanceof Long) {
                Predicate predicate = longPredicate(propertyName, (Long) propertyValue, GT);
                add(predicate);
                return this;
            }

            if (propertyValue instanceof Boolean) {
                Predicate predicate = booleanPredicate(propertyName, (Boolean) propertyValue, GT);
                add(predicate);
                return this;
            }

            if (propertyValue instanceof LocalDate) {
                Predicate predicate = localDatePredicate(propertyName, (LocalDate) propertyValue, LATER_THAN);
                add(predicate);
                return this;
            }

            //这个只支持等于不等于
            Predicate predicate = basicPredicate(propertyName, propertyValue);
            add(predicate);
            return this;
        }

    }

    public static QueryBuilder eq(String propertyName, Object propertyValue) {
        QueryBuilder builder = new QueryBuilder();
        return builder.eq(propertyName, propertyValue);
    }

    public static QueryBuilder gt(String propertyName, Object propertyValue) {
        QueryBuilder builder = new QueryBuilder();
        return builder.gt(propertyName, propertyValue);
    }



    private static Predicate stringPredicate(String propertyName, String propertyValue) {
        return new StringPredicate(propertyName, propertyValue);
    }

    private static Predicate stringPredicate(String propertyName, String propertyValue, CompareMode compareMode) {
        return new StringPredicate(propertyName, propertyValue, compareMode);
    }

    private static Predicate integerPredicate(String propertyName, Integer propertyValue) {
        return new IntegerPredicate(propertyName, propertyValue);
    }

    private static Predicate integerPredicate(String propertyName, Integer propertyValue, CompareMode compareMode) {
        return new IntegerPredicate(propertyName, propertyValue, compareMode);
    }

    private static Predicate longPredicate(String propertyName, long propertyValue) {
        return new LongPredicate(propertyName, propertyValue);
    }

    private static Predicate longPredicate(String propertyName, long propertyValue, CompareMode compareMode) {
        return new LongPredicate(propertyName, propertyValue, compareMode);
    }

    private static Predicate booleanPredicate(String propertyName, boolean propertyValue) {
        return new BooleanPredicate(propertyName, propertyValue);
    }

    private static Predicate booleanPredicate(String propertyName, boolean propertyValue, CompareMode compareMode) {
        return new BooleanPredicate(propertyName, propertyValue, compareMode);
    }

    private static Predicate localDatePredicate(String propertyName, LocalDate propertyValue) {
        return new LocalDatePredicate(propertyName, propertyValue);
    }

    private static Predicate localDatePredicate(String propertyName, LocalDate propertyValue, DateMatchMode dateMatchMode) {
        return new LocalDatePredicate(propertyName, propertyValue, dateMatchMode);
    }

    private static Predicate inPredicate(String propertyName, Object propertyValue) {
        return new InPredicate(propertyName, propertyValue);
    }

    private static Predicate notInPredicate(String propertyName, Object propertyValue) {
        return new InPredicate(propertyName, propertyValue, CompareMode.NOTIN);
    }

    private static Predicate basicPredicate(String propertyName, Object propertyValue) {
        return new BasicPredicate(propertyName, propertyValue);
    }
}
