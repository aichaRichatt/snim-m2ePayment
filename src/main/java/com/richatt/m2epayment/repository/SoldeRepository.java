package com.richatt.m2epayment.repository;

import com.richatt.m2epayment.domain.Solde;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Solde entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SoldeRepository extends JpaRepository<Solde, Long>, JpaSpecificationExecutor<Solde> {}
