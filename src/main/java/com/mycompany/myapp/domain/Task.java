package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Task.
 */
@Entity
@Table(name = "task")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "point_value")
    private Integer pointValue;

    @Column(name = "completed")
    private Boolean completed;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "task")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "creditScore", "points", "account", "task", "achivements", "parents" }, allowSetters = true)
    private Set<Child> children = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "task")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "creditScore", "points", "balances", "task", "children", "achivements" }, allowSetters = true)
    private Set<Parents> parents = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Task id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Task name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Task description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPointValue() {
        return this.pointValue;
    }

    public Task pointValue(Integer pointValue) {
        this.setPointValue(pointValue);
        return this;
    }

    public void setPointValue(Integer pointValue) {
        this.pointValue = pointValue;
    }

    public Boolean getCompleted() {
        return this.completed;
    }

    public Task completed(Boolean completed) {
        this.setCompleted(completed);
        return this;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Set<Child> getChildren() {
        return this.children;
    }

    public void setChildren(Set<Child> children) {
        if (this.children != null) {
            this.children.forEach(i -> i.setTask(null));
        }
        if (children != null) {
            children.forEach(i -> i.setTask(this));
        }
        this.children = children;
    }

    public Task children(Set<Child> children) {
        this.setChildren(children);
        return this;
    }

    public Task addChild(Child child) {
        this.children.add(child);
        child.setTask(this);
        return this;
    }

    public Task removeChild(Child child) {
        this.children.remove(child);
        child.setTask(null);
        return this;
    }

    public Set<Parents> getParents() {
        return this.parents;
    }

    public void setParents(Set<Parents> parents) {
        if (this.parents != null) {
            this.parents.forEach(i -> i.setTask(null));
        }
        if (parents != null) {
            parents.forEach(i -> i.setTask(this));
        }
        this.parents = parents;
    }

    public Task parents(Set<Parents> parents) {
        this.setParents(parents);
        return this;
    }

    public Task addParents(Parents parents) {
        this.parents.add(parents);
        parents.setTask(this);
        return this;
    }

    public Task removeParents(Parents parents) {
        this.parents.remove(parents);
        parents.setTask(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Task)) {
            return false;
        }
        return getId() != null && getId().equals(((Task) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Task{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", pointValue=" + getPointValue() +
            ", completed='" + getCompleted() + "'" +
            "}";
    }
}
