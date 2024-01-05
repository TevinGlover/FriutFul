package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Parents;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ParentsRepositoryWithBagRelationships {
    Optional<Parents> fetchBagRelationships(Optional<Parents> parents);

    List<Parents> fetchBagRelationships(List<Parents> parents);

    Page<Parents> fetchBagRelationships(Page<Parents> parents);
}
