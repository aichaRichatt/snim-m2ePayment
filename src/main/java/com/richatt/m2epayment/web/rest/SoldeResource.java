package com.richatt.m2epayment.web.rest;

import com.richatt.m2epayment.domain.Solde;
import com.richatt.m2epayment.repository.SoldeRepository;
import com.richatt.m2epayment.service.SoldeQueryService;
import com.richatt.m2epayment.service.SoldeService;
import com.richatt.m2epayment.service.criteria.SoldeCriteria;
import com.richatt.m2epayment.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.richatt.m2epayment.domain.Solde}.
 */
@RestController
@RequestMapping("/api/soldes")
public class SoldeResource {

    private final Logger log = LoggerFactory.getLogger(SoldeResource.class);

    private static final String ENTITY_NAME = "solde";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SoldeService soldeService;

    private final SoldeRepository soldeRepository;

    private final SoldeQueryService soldeQueryService;

    public SoldeResource(SoldeService soldeService, SoldeRepository soldeRepository, SoldeQueryService soldeQueryService) {
        this.soldeService = soldeService;
        this.soldeRepository = soldeRepository;
        this.soldeQueryService = soldeQueryService;
    }

    /**
     * {@code POST  /soldes} : Create a new solde.
     *
     * @param solde the solde to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new solde, or with status {@code 400 (Bad Request)} if the solde has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Solde> createSolde(@Valid @RequestBody Solde solde) throws URISyntaxException {
        log.debug("REST request to save Solde : {}", solde);
        if (solde.getId() != null) {
            throw new BadRequestAlertException("A new solde cannot already have an ID", ENTITY_NAME, "idexists");
        }
        solde = soldeService.save(solde);
        return ResponseEntity.created(new URI("/api/soldes/" + solde.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, solde.getId().toString()))
            .body(solde);
    }

    /**
     * {@code PUT  /soldes/:id} : Updates an existing solde.
     *
     * @param id the id of the solde to save.
     * @param solde the solde to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated solde,
     * or with status {@code 400 (Bad Request)} if the solde is not valid,
     * or with status {@code 500 (Internal Server Error)} if the solde couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Solde> updateSolde(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Solde solde)
        throws URISyntaxException {
        log.debug("REST request to update Solde : {}, {}", id, solde);
        if (solde.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, solde.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!soldeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        solde = soldeService.update(solde);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, solde.getId().toString()))
            .body(solde);
    }

    /**
     * {@code PATCH  /soldes/:id} : Partial updates given fields of an existing solde, field will ignore if it is null
     *
     * @param id the id of the solde to save.
     * @param solde the solde to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated solde,
     * or with status {@code 400 (Bad Request)} if the solde is not valid,
     * or with status {@code 404 (Not Found)} if the solde is not found,
     * or with status {@code 500 (Internal Server Error)} if the solde couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Solde> partialUpdateSolde(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Solde solde
    ) throws URISyntaxException {
        log.debug("REST request to partial update Solde partially : {}, {}", id, solde);
        if (solde.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, solde.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!soldeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Solde> result = soldeService.partialUpdate(solde);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, solde.getId().toString())
        );
    }

    /**
     * {@code GET  /soldes} : get all the soldes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of soldes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Solde>> getAllSoldes(
        SoldeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Soldes by criteria: {}", criteria);

        Page<Solde> page = soldeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /soldes/count} : count all the soldes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSoldes(SoldeCriteria criteria) {
        log.debug("REST request to count Soldes by criteria: {}", criteria);
        return ResponseEntity.ok().body(soldeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /soldes/:id} : get the "id" solde.
     *
     * @param id the id of the solde to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the solde, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Solde> getSolde(@PathVariable("id") Long id) {
        log.debug("REST request to get Solde : {}", id);
        Optional<Solde> solde = soldeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(solde);
    }

    /**
     * {@code DELETE  /soldes/:id} : delete the "id" solde.
     *
     * @param id the id of the solde to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSolde(@PathVariable("id") Long id) {
        log.debug("REST request to delete Solde : {}", id);
        soldeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
