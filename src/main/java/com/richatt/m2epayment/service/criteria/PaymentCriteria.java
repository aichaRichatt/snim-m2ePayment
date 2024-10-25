package com.richatt.m2epayment.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.richatt.m2epayment.domain.Payment} entity. This class is used
 * in {@link com.richatt.m2epayment.web.rest.PaymentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /payments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter transactionId;

    private DoubleFilter paidAmount;

    private StringFilter phoneNumber;

    private LocalDateFilter payDate;

    private StringFilter clientRef;

    private StringFilter walletMessage;

    private StringFilter sapMessage;

    private StringFilter payWallet;

    private StringFilter payWalletStatus;

    private StringFilter paySapStatus;

    private Boolean distinct;

    public PaymentCriteria() {}

    public PaymentCriteria(PaymentCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.transactionId = other.optionalTransactionId().map(StringFilter::copy).orElse(null);
        this.paidAmount = other.optionalPaidAmount().map(DoubleFilter::copy).orElse(null);
        this.phoneNumber = other.optionalPhoneNumber().map(StringFilter::copy).orElse(null);
        this.payDate = other.optionalPayDate().map(LocalDateFilter::copy).orElse(null);
        this.clientRef = other.optionalClientRef().map(StringFilter::copy).orElse(null);
        this.walletMessage = other.optionalWalletMessage().map(StringFilter::copy).orElse(null);
        this.sapMessage = other.optionalSapMessage().map(StringFilter::copy).orElse(null);
        this.payWallet = other.optionalPayWallet().map(StringFilter::copy).orElse(null);
        this.payWalletStatus = other.optionalPayWalletStatus().map(StringFilter::copy).orElse(null);
        this.paySapStatus = other.optionalPaySapStatus().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PaymentCriteria copy() {
        return new PaymentCriteria(this);
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

    public StringFilter getTransactionId() {
        return transactionId;
    }

    public Optional<StringFilter> optionalTransactionId() {
        return Optional.ofNullable(transactionId);
    }

    public StringFilter transactionId() {
        if (transactionId == null) {
            setTransactionId(new StringFilter());
        }
        return transactionId;
    }

    public void setTransactionId(StringFilter transactionId) {
        this.transactionId = transactionId;
    }

    public DoubleFilter getPaidAmount() {
        return paidAmount;
    }

    public Optional<DoubleFilter> optionalPaidAmount() {
        return Optional.ofNullable(paidAmount);
    }

    public DoubleFilter paidAmount() {
        if (paidAmount == null) {
            setPaidAmount(new DoubleFilter());
        }
        return paidAmount;
    }

    public void setPaidAmount(DoubleFilter paidAmount) {
        this.paidAmount = paidAmount;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public Optional<StringFilter> optionalPhoneNumber() {
        return Optional.ofNullable(phoneNumber);
    }

    public StringFilter phoneNumber() {
        if (phoneNumber == null) {
            setPhoneNumber(new StringFilter());
        }
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDateFilter getPayDate() {
        return payDate;
    }

    public Optional<LocalDateFilter> optionalPayDate() {
        return Optional.ofNullable(payDate);
    }

    public LocalDateFilter payDate() {
        if (payDate == null) {
            setPayDate(new LocalDateFilter());
        }
        return payDate;
    }

    public void setPayDate(LocalDateFilter payDate) {
        this.payDate = payDate;
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

    public StringFilter getWalletMessage() {
        return walletMessage;
    }

    public Optional<StringFilter> optionalWalletMessage() {
        return Optional.ofNullable(walletMessage);
    }

    public StringFilter walletMessage() {
        if (walletMessage == null) {
            setWalletMessage(new StringFilter());
        }
        return walletMessage;
    }

    public void setWalletMessage(StringFilter walletMessage) {
        this.walletMessage = walletMessage;
    }

    public StringFilter getSapMessage() {
        return sapMessage;
    }

    public Optional<StringFilter> optionalSapMessage() {
        return Optional.ofNullable(sapMessage);
    }

    public StringFilter sapMessage() {
        if (sapMessage == null) {
            setSapMessage(new StringFilter());
        }
        return sapMessage;
    }

    public void setSapMessage(StringFilter sapMessage) {
        this.sapMessage = sapMessage;
    }

    public StringFilter getPayWallet() {
        return payWallet;
    }

    public Optional<StringFilter> optionalPayWallet() {
        return Optional.ofNullable(payWallet);
    }

    public StringFilter payWallet() {
        if (payWallet == null) {
            setPayWallet(new StringFilter());
        }
        return payWallet;
    }

    public void setPayWallet(StringFilter payWallet) {
        this.payWallet = payWallet;
    }

    public StringFilter getPayWalletStatus() {
        return payWalletStatus;
    }

    public Optional<StringFilter> optionalPayWalletStatus() {
        return Optional.ofNullable(payWalletStatus);
    }

    public StringFilter payWalletStatus() {
        if (payWalletStatus == null) {
            setPayWalletStatus(new StringFilter());
        }
        return payWalletStatus;
    }

    public void setPayWalletStatus(StringFilter payWalletStatus) {
        this.payWalletStatus = payWalletStatus;
    }

    public StringFilter getPaySapStatus() {
        return paySapStatus;
    }

    public Optional<StringFilter> optionalPaySapStatus() {
        return Optional.ofNullable(paySapStatus);
    }

    public StringFilter paySapStatus() {
        if (paySapStatus == null) {
            setPaySapStatus(new StringFilter());
        }
        return paySapStatus;
    }

    public void setPaySapStatus(StringFilter paySapStatus) {
        this.paySapStatus = paySapStatus;
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
        final PaymentCriteria that = (PaymentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(transactionId, that.transactionId) &&
            Objects.equals(paidAmount, that.paidAmount) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(payDate, that.payDate) &&
            Objects.equals(clientRef, that.clientRef) &&
            Objects.equals(walletMessage, that.walletMessage) &&
            Objects.equals(sapMessage, that.sapMessage) &&
            Objects.equals(payWallet, that.payWallet) &&
            Objects.equals(payWalletStatus, that.payWalletStatus) &&
            Objects.equals(paySapStatus, that.paySapStatus) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            transactionId,
            paidAmount,
            phoneNumber,
            payDate,
            clientRef,
            walletMessage,
            sapMessage,
            payWallet,
            payWalletStatus,
            paySapStatus,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTransactionId().map(f -> "transactionId=" + f + ", ").orElse("") +
            optionalPaidAmount().map(f -> "paidAmount=" + f + ", ").orElse("") +
            optionalPhoneNumber().map(f -> "phoneNumber=" + f + ", ").orElse("") +
            optionalPayDate().map(f -> "payDate=" + f + ", ").orElse("") +
            optionalClientRef().map(f -> "clientRef=" + f + ", ").orElse("") +
            optionalWalletMessage().map(f -> "walletMessage=" + f + ", ").orElse("") +
            optionalSapMessage().map(f -> "sapMessage=" + f + ", ").orElse("") +
            optionalPayWallet().map(f -> "payWallet=" + f + ", ").orElse("") +
            optionalPayWalletStatus().map(f -> "payWalletStatus=" + f + ", ").orElse("") +
            optionalPaySapStatus().map(f -> "paySapStatus=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
