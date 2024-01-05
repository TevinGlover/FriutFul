package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Child.
 */
@Entity
@Table(name = "child")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Child implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_frist_name")
    private String userFristName;

    @Column(name = "user_last_name")
    private String userLastName;

    @JsonIgnoreProperties(value = { "parents", "child" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private CreditScore creditScore;

    @JsonIgnoreProperties(value = { "parents", "child" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Points points;

    @JsonIgnoreProperties(value = { "transactions", "parents", "child" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Balance account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "children", "parents" }, allowSetters = true)
    private Task task;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "child")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "child", "parents" }, allowSetters = true)
    private Set<Achivement> achivements = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "children")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "creditScore", "points", "balances", "task", "children", "achivements" }, allowSetters = true)
    private Set<Parents> parents = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Child id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserFristName() {
        return this.userFristName;
    }

    public Child userFristName(String userFristName) {
        this.setUserFristName(userFristName);
        return this;
    }

    public void setUserFristName(String userFristName) {
        this.userFristName = userFristName;
    }

    public String getUserLastName() {
        return this.userLastName;
    }

    public Child userLastName(String userLastName) {
        this.setUserLastName(userLastName);
        return this;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public CreditScore getCreditScore() {
        return this.creditScore;
    }

    public void setCreditScore(CreditScore creditScore) {
        this.creditScore = creditScore;
    }

    public Child creditScore(CreditScore creditScore) {
        this.setCreditScore(creditScore);
        return this;
    }

    public Points getPoints() {
        return this.points;
    }

    public void setPoints(Points points) {
        this.points = points;
    }

    public Child points(Points points) {
        this.setPoints(points);
        return this;
    }

    public Balance getAccount() {
        return this.account;
    }

    public void setAccount(Balance balance) {
        this.account = balance;
    }

    public Child account(Balance balance) {
        this.setAccount(balance);
        return this;
    }

    public Task getTask() {
        return this.task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Child task(Task task) {
        this.setTask(task);
        return this;
    }

    public Set<Achivement> getAchivements() {
        return this.achivements;
    }

    public void setAchivements(Set<Achivement> achivements) {
        if (this.achivements != null) {
            this.achivements.forEach(i -> i.setChild(null));
        }
        if (achivements != null) {
            achivements.forEach(i -> i.setChild(this));
        }
        this.achivements = achivements;
    }

    public Child achivements(Set<Achivement> achivements) {
        this.setAchivements(achivements);
        return this;
    }

    public Child addAchivement(Achivement achivement) {
        this.achivements.add(achivement);
        achivement.setChild(this);
        return this;
    }

    public Child removeAchivement(Achivement achivement) {
        this.achivements.remove(achivement);
        achivement.setChild(null);
        return this;
    }

    public Set<Parents> getParents() {
        return this.parents;
    }

    public void setParents(Set<Parents> parents) {
        if (this.parents != null) {
            this.parents.forEach(i -> i.removeChild(this));
        }
        if (parents != null) {
            parents.forEach(i -> i.addChild(this));
        }
        this.parents = parents;
    }

    public Child parents(Set<Parents> parents) {
        this.setParents(parents);
        return this;
    }

    public Child addParents(Parents parents) {
        this.parents.add(parents);
        parents.getChildren().add(this);
        return this;
    }

    public Child removeParents(Parents parents) {
        this.parents.remove(parents);
        parents.getChildren().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Child)) {
            return false;
        }
        return getId() != null && getId().equals(((Child) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Child{" +
            "id=" + getId() +
            ", userFristName='" + getUserFristName() + "'" +
            ", userLastName='" + getUserLastName() + "'" +
            "}";
    }
}
