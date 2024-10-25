package com.richatt.m2epayment.service;

import com.richatt.m2epayment.domain.*; // for static metamodels
import com.richatt.m2epayment.domain.Payment;
import com.richatt.m2epayment.repository.PaymentRepository;
import com.richatt.m2epayment.service.criteria.PaymentCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Payment} entities in the database.
 * The main input is a {@link PaymentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Payment} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaymentQueryService extends QueryService<Payment> {

    private final Logger log = LoggerFactory.getLogger(PaymentQueryService.class);

    private final PaymentRepository paymentRepository;

    public PaymentQueryService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    /**
     * Return a {@link Page} of {@link Payment} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Payment> findByCriteria(PaymentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Payment> specification = createSpecification(criteria);
        return paymentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaymentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Payment> specification = createSpecification(criteria);
        return paymentRepository.count(specification);
    }

    /**
     * Function to convert {@link PaymentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Payment> createSpecification(PaymentCriteria criteria) {
        Specification<Payment> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Payment_.id));
            }
            if (criteria.getTransactionId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTransactionId(), Payment_.transactionId));
            }
            if (criteria.getPaidAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaidAmount(), Payment_.paidAmount));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), Payment_.phoneNumber));
            }
            if (criteria.getPayDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPayDate(), Payment_.payDate));
            }
            if (criteria.getClientRef() != null) {
                specification = specification.and(buildStringSpecification(criteria.getClientRef(), Payment_.clientRef));
            }
            if (criteria.getWalletMessage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWalletMessage(), Payment_.walletMessage));
            }
            if (criteria.getSapMessage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSapMessage(), Payment_.sapMessage));
            }
            if (criteria.getPayWallet() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPayWallet(), Payment_.payWallet));
            }
            if (criteria.getPayWalletStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPayWalletStatus(), Payment_.payWalletStatus));
            }
            if (criteria.getPaySapStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPaySapStatus(), Payment_.paySapStatus));
            }
        }
        return specification;
    }
}
