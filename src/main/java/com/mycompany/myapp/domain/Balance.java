package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Balance.
 */
@Entity
@Table(name = "balance")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Balance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "credit_card_type")
    private String creditCardType;

    @Column(name = "creditcard_num")
    private Integer creditcardNum;

    @Column(name = "vaild_thru")
    private Instant vaildThru;

    @Column(name = "cvs")
    private Integer cvs;

    @Column(name = "max_limit")
    private Double maxLimit;

    @Column(name = "thrity_precent_limit")
    private Double thrityPrecentLimit;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "account" }, allowSetters = true)
    private Set<Transactions> transactions = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "creditScore", "points", "balances", "task", "children", "achivements" }, allowSetters = true)
    private Parents parents;

    @JsonIgnoreProperties(value = { "creditScore", "points", "account", "task", "achivements", "parents" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "account")
    private Child child;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Balance id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreditCardType() {
        return this.creditCardType;
    }

    public Balance creditCardType(String creditCardType) {
        this.setCreditCardType(creditCardType);
        return this;
    }

    public void setCreditCardType(String creditCardType) {
        this.creditCardType = creditCardType;
    }

    public Integer getCreditcardNum() {
        return this.creditcardNum;
    }

    public Balance creditcardNum(Integer creditcardNum) {
        this.setCreditcardNum(creditcardNum);
        return this;
    }

    public void setCreditcardNum(Integer creditcardNum) {
        this.creditcardNum = creditcardNum;
    }

    public Instant getVaildThru() {
        return this.vaildThru;
    }

    public Balance vaildThru(Instant vaildThru) {
        this.setVaildThru(vaildThru);
        return this;
    }

    public void setVaildThru(Instant vaildThru) {
        this.vaildThru = vaildThru;
    }

    public Integer getCvs() {
        return this.cvs;
    }

    public Balance cvs(Integer cvs) {
        this.setCvs(cvs);
        return this;
    }

    public void setCvs(Integer cvs) {
        this.cvs = cvs;
    }

    public Double getMaxLimit() {
        return this.maxLimit;
    }

    public Balance maxLimit(Double maxLimit) {
        this.setMaxLimit(maxLimit);
        return this;
    }

    public void setMaxLimit(Double maxLimit) {
        this.maxLimit = maxLimit;
    }

    public Double getThrityPrecentLimit() {
        return this.thrityPrecentLimit;
    }

    public Balance thrityPrecentLimit(Double thrityPrecentLimit) {
        this.setThrityPrecentLimit(thrityPrecentLimit);
        return this;
    }

    public void setThrityPrecentLimit(Double thrityPrecentLimit) {
        this.thrityPrecentLimit = thrityPrecentLimit;
    }

    public Set<Transactions> getTransactions() {
        return this.transactions;
    }

    public void setTransactions(Set<Transactions> transactions) {
        if (this.transactions != null) {
            this.transactions.forEach(i -> i.setAccount(null));
        }
        if (transactions != null) {
            transactions.forEach(i -> i.setAccount(this));
        }
        this.transactions = transactions;
    }

    public Balance transactions(Set<Transactions> transactions) {
        this.setTransactions(transactions);
        return this;
    }

    public Balance addTransactions(Transactions transactions) {
        this.transactions.add(transactions);
        transactions.setAccount(this);
        return this;
    }

    public Balance removeTransactions(Transactions transactions) {
        this.transactions.remove(transactions);
        transactions.setAccount(null);
        return this;
    }

    public Parents getParents() {
        return this.parents;
    }

    public void setParents(Parents parents) {
        this.parents = parents;
    }

    public Balance parents(Parents parents) {
        this.setParents(parents);
        return this;
    }

    public Child getChild() {
        return this.child;
    }

    public void setChild(Child child) {
        if (this.child != null) {
            this.child.setAccount(null);
        }
        if (child != null) {
            child.setAccount(this);
        }
        this.child = child;
    }

    public Balance child(Child child) {
        this.setChild(child);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Balance)) {
            return false;
        }
        return getId() != null && getId().equals(((Balance) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Balance{" +
            "id=" + getId() +
            ", creditCardType='" + getCreditCardType() + "'" +
            ", creditcardNum=" + getCreditcardNum() +
            ", vaildThru='" + getVaildThru() + "'" +
            ", cvs=" + getCvs() +
            ", maxLimit=" + getMaxLimit() +
            ", thrityPrecentLimit=" + getThrityPrecentLimit() +
            "}";
    }
}
