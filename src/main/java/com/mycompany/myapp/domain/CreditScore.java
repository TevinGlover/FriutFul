package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CreditScore.
 */
@Entity
@Table(name = "credit_score")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CreditScore implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "month")
    private Instant month;

    @Column(name = "score_number")
    private Integer scoreNumber;

    @JsonIgnoreProperties(value = { "creditScore", "points", "balances", "task", "children", "achivements" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "creditScore")
    private Parents parents;

    @JsonIgnoreProperties(value = { "creditScore", "points", "account", "task", "achivements", "parents" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "creditScore")
    private Child child;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CreditScore id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getMonth() {
        return this.month;
    }

    public CreditScore month(Instant month) {
        this.setMonth(month);
        return this;
    }

    public void setMonth(Instant month) {
        this.month = month;
    }

    public Integer getScoreNumber() {
        return this.scoreNumber;
    }

    public CreditScore scoreNumber(Integer scoreNumber) {
        this.setScoreNumber(scoreNumber);
        return this;
    }

    public void setScoreNumber(Integer scoreNumber) {
        this.scoreNumber = scoreNumber;
    }

    public Parents getParents() {
        return this.parents;
    }

    public void setParents(Parents parents) {
        if (this.parents != null) {
            this.parents.setCreditScore(null);
        }
        if (parents != null) {
            parents.setCreditScore(this);
        }
        this.parents = parents;
    }

    public CreditScore parents(Parents parents) {
        this.setParents(parents);
        return this;
    }

    public Child getChild() {
        return this.child;
    }

    public void setChild(Child child) {
        if (this.child != null) {
            this.child.setCreditScore(null);
        }
        if (child != null) {
            child.setCreditScore(this);
        }
        this.child = child;
    }

    public CreditScore child(Child child) {
        this.setChild(child);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CreditScore)) {
            return false;
        }
        return getId() != null && getId().equals(((CreditScore) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CreditScore{" +
            "id=" + getId() +
            ", month='" + getMonth() + "'" +
            ", scoreNumber=" + getScoreNumber() +
            "}";
    }
}
