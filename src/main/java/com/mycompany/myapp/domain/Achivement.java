package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Achivement.
 */
@Entity
@Table(name = "achivement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Achivement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "point_value")
    private Integer pointValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "creditScore", "points", "account", "task", "achivements", "parents" }, allowSetters = true)
    private Child child;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "creditScore", "points", "balances", "task", "children", "achivements" }, allowSetters = true)
    private Parents parents;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Achivement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Achivement name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPointValue() {
        return this.pointValue;
    }

    public Achivement pointValue(Integer pointValue) {
        this.setPointValue(pointValue);
        return this;
    }

    public void setPointValue(Integer pointValue) {
        this.pointValue = pointValue;
    }

    public Child getChild() {
        return this.child;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public Achivement child(Child child) {
        this.setChild(child);
        return this;
    }

    public Parents getParents() {
        return this.parents;
    }

    public void setParents(Parents parents) {
        this.parents = parents;
    }

    public Achivement parents(Parents parents) {
        this.setParents(parents);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Achivement)) {
            return false;
        }
        return getId() != null && getId().equals(((Achivement) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Achivement{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", pointValue=" + getPointValue() +
            "}";
    }
}
