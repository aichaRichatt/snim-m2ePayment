package com.richatt.m2epayment.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Payment.
 */
@Entity
@Table(name = "payment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 30)
    @Column(name = "transaction_id", length = 30, nullable = false)
    private String transactionId;

    @Column(name = "paid_amount")
    private Double paidAmount;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "pay_date")
    private LocalDate payDate;

    @Size(max = 20)
    @Column(name = "client_ref", length = 20)
    private String clientRef;

    @Size(max = 255)
    @Column(name = "wallet_message", length = 255)
    private String walletMessage;

    @Size(max = 255)
    @Column(name = "sap_message", length = 255)
    private String sapMessage;

    @Size(max = 64)
    @Column(name = "pay_wallet", length = 64)
    private String payWallet;

    @Size(max = 64)
    @Column(name = "pay_wallet_status", length = 64)
    private String payWalletStatus;

    @Size(max = 64)
    @Column(name = "pay_sap_status", length = 64)
    private String paySapStatus;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Payment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionId() {
        return this.transactionId;
    }

    public Payment transactionId(String transactionId) {
        this.setTransactionId(transactionId);
        return this;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Double getPaidAmount() {
        return this.paidAmount;
    }

    public Payment paidAmount(Double paidAmount) {
        this.setPaidAmount(paidAmount);
        return this;
    }

    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Payment phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getPayDate() {
        return this.payDate;
    }

    public Payment payDate(LocalDate payDate) {
        this.setPayDate(payDate);
        return this;
    }

    public void setPayDate(LocalDate payDate) {
        this.payDate = payDate;
    }

    public String getClientRef() {
        return this.clientRef;
    }

    public Payment clientRef(String clientRef) {
        this.setClientRef(clientRef);
        return this;
    }

    public void setClientRef(String clientRef) {
        this.clientRef = clientRef;
    }

    public String getWalletMessage() {
        return this.walletMessage;
    }

    public Payment walletMessage(String walletMessage) {
        this.setWalletMessage(walletMessage);
        return this;
    }

    public void setWalletMessage(String walletMessage) {
        this.walletMessage = walletMessage;
    }

    public String getSapMessage() {
        return this.sapMessage;
    }

    public Payment sapMessage(String sapMessage) {
        this.setSapMessage(sapMessage);
        return this;
    }

    public void setSapMessage(String sapMessage) {
        this.sapMessage = sapMessage;
    }

    public String getPayWallet() {
        return this.payWallet;
    }

    public Payment payWallet(String payWallet) {
        this.setPayWallet(payWallet);
        return this;
    }

    public void setPayWallet(String payWallet) {
        this.payWallet = payWallet;
    }

    public String getPayWalletStatus() {
        return this.payWalletStatus;
    }

    public Payment payWalletStatus(String payWalletStatus) {
        this.setPayWalletStatus(payWalletStatus);
        return this;
    }

    public void setPayWalletStatus(String payWalletStatus) {
        this.payWalletStatus = payWalletStatus;
    }

    public String getPaySapStatus() {
        return this.paySapStatus;
    }

    public Payment paySapStatus(String paySapStatus) {
        this.setPaySapStatus(paySapStatus);
        return this;
    }

    public void setPaySapStatus(String paySapStatus) {
        this.paySapStatus = paySapStatus;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payment)) {
            return false;
        }
        return getId() != null && getId().equals(((Payment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Payment{" +
            "id=" + getId() +
            ", transactionId='" + getTransactionId() + "'" +
            ", paidAmount=" + getPaidAmount() +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", payDate='" + getPayDate() + "'" +
            ", clientRef='" + getClientRef() + "'" +
            ", walletMessage='" + getWalletMessage() + "'" +
            ", sapMessage='" + getSapMessage() + "'" +
            ", payWallet='" + getPayWallet() + "'" +
            ", payWalletStatus='" + getPayWalletStatus() + "'" +
            ", paySapStatus='" + getPaySapStatus() + "'" +
            "}";
    }
}
