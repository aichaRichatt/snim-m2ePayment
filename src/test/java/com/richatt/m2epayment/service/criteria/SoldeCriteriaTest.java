package com.richatt.m2epayment.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SoldeCriteriaTest {

    @Test
    void newSoldeCriteriaHasAllFiltersNullTest() {
        var soldeCriteria = new SoldeCriteria();
        assertThat(soldeCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void soldeCriteriaFluentMethodsCreatesFiltersTest() {
        var soldeCriteria = new SoldeCriteria();

        setAllFilters(soldeCriteria);

        assertThat(soldeCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void soldeCriteriaCopyCreatesNullFilterTest() {
        var soldeCriteria = new SoldeCriteria();
        var copy = soldeCriteria.copy();

        assertThat(soldeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(soldeCriteria)
        );
    }

    @Test
    void soldeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var soldeCriteria = new SoldeCriteria();
        setAllFilters(soldeCriteria);

        var copy = soldeCriteria.copy();

        assertThat(soldeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(soldeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var soldeCriteria = new SoldeCriteria();

        assertThat(soldeCriteria).hasToString("SoldeCriteria{}");
    }

    private static void setAllFilters(SoldeCriteria soldeCriteria) {
        soldeCriteria.id();
        soldeCriteria.clientRef();
        soldeCriteria.clientName();
        soldeCriteria.clientFirstname();
        soldeCriteria.amount();
        soldeCriteria.updatingDate();
        soldeCriteria.distinct();
    }

    private static Condition<SoldeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getClientRef()) &&
                condition.apply(criteria.getClientName()) &&
                condition.apply(criteria.getClientFirstname()) &&
                condition.apply(criteria.getAmount()) &&
                condition.apply(criteria.getUpdatingDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SoldeCriteria> copyFiltersAre(SoldeCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getClientRef(), copy.getClientRef()) &&
                condition.apply(criteria.getClientName(), copy.getClientName()) &&
                condition.apply(criteria.getClientFirstname(), copy.getClientFirstname()) &&
                condition.apply(criteria.getAmount(), copy.getAmount()) &&
                condition.apply(criteria.getUpdatingDate(), copy.getUpdatingDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
