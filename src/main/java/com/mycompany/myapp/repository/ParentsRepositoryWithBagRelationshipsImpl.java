package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Parents;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ParentsRepositoryWithBagRelationshipsImpl implements ParentsRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Parents> fetchBagRelationships(Optional<Parents> parents) {
        return parents.map(this::fetchChildren);
    }

    @Override
    public Page<Parents> fetchBagRelationships(Page<Parents> parents) {
        return new PageImpl<>(fetchBagRelationships(parents.getContent()), parents.getPageable(), parents.getTotalElements());
    }

    @Override
    public List<Parents> fetchBagRelationships(List<Parents> parents) {
        return Optional.of(parents).map(this::fetchChildren).orElse(Collections.emptyList());
    }

    Parents fetchChildren(Parents result) {
        return entityManager
            .createQuery("select parents from Parents parents left join fetch parents.children where parents.id = :id", Parents.class)
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Parents> fetchChildren(List<Parents> parents) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, parents.size()).forEach(index -> order.put(parents.get(index).getId(), index));
        List<Parents> result = entityManager
            .createQuery("select parents from Parents parents left join fetch parents.children where parents in :parents", Parents.class)
            .setParameter("parents", parents)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
