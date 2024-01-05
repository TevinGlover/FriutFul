package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Points.
 */
@Entity
@Table(name = "points")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Points implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "used")
    private Integer used;

    @JsonIgnoreProperties(value = { "creditScore", "points", "balances", "task", "children", "achivements" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "points")
    private Parents parents;

    @JsonIgnoreProperties(value = { "creditScore", "points", "account", "task", "achivements", "parents" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "points")
    private Child child;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Points id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAmount() {
        return this.amount;
    }

    public Points amount(Integer amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getUsed() {
        return this.used;
    }

    public Points used(Integer used) {
        this.setUsed(used);
        return this;
    }

    public void setUsed(Integer used) {
        this.used = used;
    }

    public Parents getParents() {
        return this.parents;
    }

    public void setParents(Parents parents) {
        if (this.parents != null) {
            this.parents.setPoints(null);
        }
        if (parents != null) {
            parents.setPoints(this);
        }
        this.parents = parents;
    }

    public Points parents(Parents parents) {
        this.setParents(parents);
        return this;
    }

    public Child getChild() {
        return this.child;
    }

    public void setChild(Child child) {
        if (this.child != null) {
            this.child.setPoints(null);
        }
        if (child != null) {
            child.setPoints(this);
        }
        this.child = child;
    }

    public Points child(Child child) {
        this.setChild(child);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Points)) {
            return false;
        }
        return getId() != null && getId().equals(((Points) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Points{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", used=" + getUsed() +
            "}";
    }
}
