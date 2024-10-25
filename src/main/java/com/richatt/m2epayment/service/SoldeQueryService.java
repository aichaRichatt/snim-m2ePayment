package com.richatt.m2epayment.service;

import com.richatt.m2epayment.domain.*; // for static metamodels
import com.richatt.m2epayment.domain.Solde;
import com.richatt.m2epayment.repository.SoldeRepository;
import com.richatt.m2epayment.service.criteria.SoldeCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Solde} entities in the database.
 * The main input is a {@link SoldeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Solde} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SoldeQueryService extends QueryService<Solde> {

    private final Logger log = LoggerFactory.getLogger(SoldeQueryService.class);

    private final SoldeRepository soldeRepository;

    public SoldeQueryService(SoldeRepository soldeRepository) {
        this.soldeRepository = soldeRepository;
    }

    /**
     * Return a {@link Page} of {@link Solde} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Solde> findByCriteria(SoldeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Solde> specification = createSpecification(criteria);
        return soldeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SoldeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Solde> specification = createSpecification(criteria);
        return soldeRepository.count(specification);
    }

    /**
     * Function to convert {@link SoldeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Solde> createSpecification(SoldeCriteria criteria) {
        Specification<Solde> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Solde_.id));
            }
            if (criteria.getClientRef() != null) {
                specification = specification.and(buildStringSpecification(criteria.getClientRef(), Solde_.clientRef));
            }
            if (criteria.getClientName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getClientName(), Solde_.clientName));
            }
            if (criteria.getClientFirstname() != null) {
                specification = specification.and(buildStringSpecification(criteria.getClientFirstname(), Solde_.clientFirstname));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), Solde_.amount));
            }
            if (criteria.getUpdatingDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatingDate(), Solde_.updatingDate));
            }
        }
        return specification;
    }
}
