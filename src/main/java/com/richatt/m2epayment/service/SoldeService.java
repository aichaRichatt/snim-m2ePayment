package com.richatt.m2epayment.service;

import com.richatt.m2epayment.domain.Solde;
import com.richatt.m2epayment.repository.SoldeRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.richatt.m2epayment.domain.Solde}.
 */
@Service
@Transactional
public class SoldeService {

    private final Logger log = LoggerFactory.getLogger(SoldeService.class);

    private final SoldeRepository soldeRepository;

    public SoldeService(SoldeRepository soldeRepository) {
        this.soldeRepository = soldeRepository;
    }

    /**
     * Save a solde.
     *
     * @param solde the entity to save.
     * @return the persisted entity.
     */
    public Solde save(Solde solde) {
        log.debug("Request to save Solde : {}", solde);
        return soldeRepository.save(solde);
    }

    /**
     * Update a solde.
     *
     * @param solde the entity to save.
     * @return the persisted entity.
     */
    public Solde update(Solde solde) {
        log.debug("Request to update Solde : {}", solde);
        return soldeRepository.save(solde);
    }

    /**
     * Partially update a solde.
     *
     * @param solde the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Solde> partialUpdate(Solde solde) {
        log.debug("Request to partially update Solde : {}", solde);

        return soldeRepository
            .findById(solde.getId())
            .map(existingSolde -> {
                if (solde.getClientRef() != null) {
                    existingSolde.setClientRef(solde.getClientRef());
                }
                if (solde.getClientName() != null) {
                    existingSolde.setClientName(solde.getClientName());
                }
                if (solde.getClientFirstname() != null) {
                    existingSolde.setClientFirstname(solde.getClientFirstname());
                }
                if (solde.getAmount() != null) {
                    existingSolde.setAmount(solde.getAmount());
                }
                if (solde.getUpdatingDate() != null) {
                    existingSolde.setUpdatingDate(solde.getUpdatingDate());
                }

                return existingSolde;
            })
            .map(soldeRepository::save);
    }

    /**
     * Get one solde by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Solde> findOne(Long id) {
        log.debug("Request to get Solde : {}", id);
        return soldeRepository.findById(id);
    }

    /**
     * Delete the solde by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Solde : {}", id);
        soldeRepository.deleteById(id);
    }
}
