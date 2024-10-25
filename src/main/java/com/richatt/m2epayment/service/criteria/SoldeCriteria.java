package com.richatt.m2epayment.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.richatt.m2epayment.domain.Solde} entity. This class is used
 * in {@link com.richatt.m2epayment.web.rest.SoldeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /soldes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SoldeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter clientRef;

    private StringFilter clientName;

    private StringFilter clientFirstname;

    private DoubleFilter amount;

    private LocalDateFilter updatingDate;

    private Boolean distinct;

    public SoldeCriteria() {}

    public SoldeCriteria(SoldeCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.clientRef = other.optionalClientRef().map(StringFilter::copy).orElse(null);
        this.clientName = other.optionalClientName().map(StringFilter::copy).orElse(null);
        this.clientFirstname = other.optionalClientFirstname().map(StringFilter::copy).orElse(null);
        this.amount = other.optionalAmount().map(DoubleFilter::copy).orElse(null);
        this.updatingDate = other.optionalUpdatingDate().map(LocalDateFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SoldeCriteria copy() {
        return new SoldeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getClientRef() {
        return clientRef;
    }

    public Optional<StringFilter> optionalClientRef() {
        return Optional.ofNullable(clientRef);
    }

    public StringFilter clientRef() {
        if (clientRef == null) {
            setClientRef(new StringFilter());
        }
        return clientRef;
    }

    public void setClientRef(StringFilter clientRef) {
        this.clientRef = clientRef;
    }

    public StringFilter getClientName() {
        return clientName;
    }

    public Optional<StringFilter> optionalClientName() {
        return Optional.ofNullable(clientName);
    }

    public StringFilter clientName() {
        if (clientName == null) {
            setClientName(new StringFilter());
        }
        return clientName;
    }

    public void setClientName(StringFilter clientName) {
        this.clientName = clientName;
    }

    public StringFilter getClientFirstname() {
        return clientFirstname;
    }

    public Optional<StringFilter> optionalClientFirstname() {
        return Optional.ofNullable(clientFirstname);
    }

    public StringFilter clientFirstname() {
        if (clientFirstname == null) {
            setClientFirstname(new StringFilter());
        }
        return clientFirstname;
    }

    public void setClientFirstname(StringFilter clientFirstname) {
        this.clientFirstname = clientFirstname;
    }

    public DoubleFilter getAmount() {
        return amount;
    }

    public Optional<DoubleFilter> optionalAmount() {
        return Optional.ofNullable(amount);
    }

    public DoubleFilter amount() {
        if (amount == null) {
            setAmount(new DoubleFilter());
        }
        return amount;
    }

    public void setAmount(DoubleFilter amount) {
        this.amount = amount;
    }

    public LocalDateFilter getUpdatingDate() {
        return updatingDate;
    }

    public Optional<LocalDateFilter> optionalUpdatingDate() {
        return Optional.ofNullable(updatingDate);
    }

    public LocalDateFilter updatingDate() {
        if (updatingDate == null) {
            setUpdatingDate(new LocalDateFilter());
        }
        return updatingDate;
    }

    public void setUpdatingDate(LocalDateFilter updatingDate) {
        this.updatingDate = updatingDate;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SoldeCriteria that = (SoldeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(clientRef, that.clientRef) &&
            Objects.equals(clientName, that.clientName) &&
            Objects.equals(clientFirstname, that.clientFirstname) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(updatingDate, that.updatingDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientRef, clientName, clientFirstname, amount, updatingDate, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SoldeCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalClientRef().map(f -> "clientRef=" + f + ", ").orElse("") +
            optionalClientName().map(f -> "clientName=" + f + ", ").orElse("") +
            optionalClientFirstname().map(f -> "clientFirstname=" + f + ", ").orElse("") +
            optionalAmount().map(f -> "amount=" + f + ", ").orElse("") +
            optionalUpdatingDate().map(f -> "updatingDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
