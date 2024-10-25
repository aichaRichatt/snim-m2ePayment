package com.richatt.m2epayment.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Solde.
 */
@Entity
@Table(name = "solde")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Solde implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "client_ref", length = 20, nullable = false)
    private String clientRef;

    @Size(max = 20)
    @Column(name = "client_name", length = 20)
    private String clientName;

    @Size(max = 20)
    @Column(name = "client_firstname", length = 20)
    private String clientFirstname;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "updating_date")
    private LocalDate updatingDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Solde id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientRef() {
        return this.clientRef;
    }

    public Solde clientRef(String clientRef) {
        this.setClientRef(clientRef);
        return this;
    }

    public void setClientRef(String clientRef) {
        this.clientRef = clientRef;
    }

    public String getClientName() {
        return this.clientName;
    }

    public Solde clientName(String clientName) {
        this.setClientName(clientName);
        return this;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientFirstname() {
        return this.clientFirstname;
    }

    public Solde clientFirstname(String clientFirstname) {
        this.setClientFirstname(clientFirstname);
        return this;
    }

    public void setClientFirstname(String clientFirstname) {
        this.clientFirstname = clientFirstname;
    }

    public Double getAmount() {
        return this.amount;
    }

    public Solde amount(Double amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getUpdatingDate() {
        return this.updatingDate;
    }

    public Solde updatingDate(LocalDate updatingDate) {
        this.setUpdatingDate(updatingDate);
        return this;
    }

    public void setUpdatingDate(LocalDate updatingDate) {
        this.updatingDate = updatingDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Solde)) {
            return false;
        }
        return getId() != null && getId().equals(((Solde) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Solde{" +
            "id=" + getId() +
            ", clientRef='" + getClientRef() + "'" +
            ", clientName='" + getClientName() + "'" +
            ", clientFirstname='" + getClientFirstname() + "'" +
            ", amount=" + getAmount() +
            ", updatingDate='" + getUpdatingDate() + "'" +
            "}";
    }
}
