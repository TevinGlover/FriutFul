package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Parents.
 */
@Entity
@Table(name = "parents")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Parents implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "parents_frist_name")
    private String parentsFristName;

    @Column(name = "parents_last_name")
    private String parentsLastName;

    @JsonIgnoreProperties(value = { "parents", "child" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private CreditScore creditScore;

    @JsonIgnoreProperties(value = { "parents", "child" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Points points;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parents")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "transactions", "parents", "child" }, allowSetters = true)
    private Set<Balance> balances = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "children", "parents" }, allowSetters = true)
    private Task task;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_parents__child",
        joinColumns = @JoinColumn(name = "parents_id"),
        inverseJoinColumns = @JoinColumn(name = "child_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "creditScore", "points", "account", "task", "achivements", "parents" }, allowSetters = true)
    private Set<Child> children = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parents")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "child", "parents" }, allowSetters = true)
    private Set<Achivement> achivements = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Parents id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParentsFristName() {
        return this.parentsFristName;
    }

    public Parents parentsFristName(String parentsFristName) {
        this.setParentsFristName(parentsFristName);
        return this;
    }

    public void setParentsFristName(String parentsFristName) {
        this.parentsFristName = parentsFristName;
    }

    public String getParentsLastName() {
        return this.parentsLastName;
    }

    public Parents parentsLastName(String parentsLastName) {
        this.setParentsLastName(parentsLastName);
        return this;
    }

    public void setParentsLastName(String parentsLastName) {
        this.parentsLastName = parentsLastName;
    }

    public CreditScore getCreditScore() {
        return this.creditScore;
    }

    public void setCreditScore(CreditScore creditScore) {
        this.creditScore = creditScore;
    }

    public Parents creditScore(CreditScore creditScore) {
        this.setCreditScore(creditScore);
        return this;
    }

    public Points getPoints() {
        return this.points;
    }

    public void setPoints(Points points) {
        this.points = points;
    }

    public Parents points(Points points) {
        this.setPoints(points);
        return this;
    }

    public Set<Balance> getBalances() {
        return this.balances;
    }

    public void setBalances(Set<Balance> balances) {
        if (this.balances != null) {
            this.balances.forEach(i -> i.setParents(null));
        }
        if (balances != null) {
            balances.forEach(i -> i.setParents(this));
        }
        this.balances = balances;
    }

    public Parents balances(Set<Balance> balances) {
        this.setBalances(balances);
        return this;
    }

    public Parents addBalance(Balance balance) {
        this.balances.add(balance);
        balance.setParents(this);
        return this;
    }

    public Parents removeBalance(Balance balance) {
        this.balances.remove(balance);
        balance.setParents(null);
        return this;
    }

    public Task getTask() {
        return this.task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Parents task(Task task) {
        this.setTask(task);
        return this;
    }

    public Set<Child> getChildren() {
        return this.children;
    }

    public void setChildren(Set<Child> children) {
        this.children = children;
    }

    public Parents children(Set<Child> children) {
        this.setChildren(children);
        return this;
    }

    public Parents addChild(Child child) {
        this.children.add(child);
        return this;
    }

    public Parents removeChild(Child child) {
        this.children.remove(child);
        return this;
    }

    public Set<Achivement> getAchivements() {
        return this.achivements;
    }

    public void setAchivements(Set<Achivement> achivements) {
        if (this.achivements != null) {
            this.achivements.forEach(i -> i.setParents(null));
        }
        if (achivements != null) {
            achivements.forEach(i -> i.setParents(this));
        }
        this.achivements = achivements;
    }

    public Parents achivements(Set<Achivement> achivements) {
        this.setAchivements(achivements);
        return this;
    }

    public Parents addAchivement(Achivement achivement) {
        this.achivements.add(achivement);
        achivement.setParents(this);
        return this;
    }

    public Parents removeAchivement(Achivement achivement) {
        this.achivements.remove(achivement);
        achivement.setParents(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Parents)) {
            return false;
        }
        return getId() != null && getId().equals(((Parents) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Parents{" +
            "id=" + getId() +
            ", parentsFristName='" + getParentsFristName() + "'" +
            ", parentsLastName='" + getParentsLastName() + "'" +
            "}";
    }
}
