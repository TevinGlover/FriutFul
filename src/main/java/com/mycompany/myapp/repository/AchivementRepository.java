package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Achivement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Achivement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AchivementRepository extends JpaRepository<Achivement, Long> {}
