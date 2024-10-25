package com.richatt.m2epayment.web.rest;

import static com.richatt.m2epayment.domain.SoldeAsserts.*;
import static com.richatt.m2epayment.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.richatt.m2epayment.IntegrationTest;
import com.richatt.m2epayment.domain.Solde;
import com.richatt.m2epayment.repository.SoldeRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SoldeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SoldeResourceIT {

    private static final String DEFAULT_CLIENT_REF = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_REF = "BBBBBBBBBB";

    private static final String DEFAULT_CLIENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CLIENT_FIRSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_FIRSTNAME = "BBBBBBBBBB";

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;
    private static final Double SMALLER_AMOUNT = 1D - 1D;

    private static final LocalDate DEFAULT_UPDATING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATING_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_UPDATING_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/soldes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SoldeRepository soldeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSoldeMockMvc;

    private Solde solde;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Solde createEntity(EntityManager em) {
        Solde solde = new Solde()
            .clientRef(DEFAULT_CLIENT_REF)
            .clientName(DEFAULT_CLIENT_NAME)
            .clientFirstname(DEFAULT_CLIENT_FIRSTNAME)
            .amount(DEFAULT_AMOUNT)
            .updatingDate(DEFAULT_UPDATING_DATE);
        return solde;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Solde createUpdatedEntity(EntityManager em) {
        Solde solde = new Solde()
            .clientRef(UPDATED_CLIENT_REF)
            .clientName(UPDATED_CLIENT_NAME)
            .clientFirstname(UPDATED_CLIENT_FIRSTNAME)
            .amount(UPDATED_AMOUNT)
            .updatingDate(UPDATED_UPDATING_DATE);
        return solde;
    }

    @BeforeEach
    public void initTest() {
        solde = createEntity(em);
    }

    @Test
    @Transactional
    void createSolde() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Solde
        var returnedSolde = om.readValue(
            restSoldeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(solde)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Solde.class
        );

        // Validate the Solde in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSoldeUpdatableFieldsEquals(returnedSolde, getPersistedSolde(returnedSolde));
    }

    @Test
    @Transactional
    void createSoldeWithExistingId() throws Exception {
        // Create the Solde with an existing ID
        solde.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSoldeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(solde)))
            .andExpect(status().isBadRequest());

        // Validate the Solde in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkClientRefIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        solde.setClientRef(null);

        // Create the Solde, which fails.

        restSoldeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(solde)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSoldes() throws Exception {
        // Initialize the database
        soldeRepository.saveAndFlush(solde);

        // Get all the soldeList
        restSoldeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(solde.getId().intValue())))
            .andExpect(jsonPath("$.[*].clientRef").value(hasItem(DEFAULT_CLIENT_REF)))
            .andExpect(jsonPath("$.[*].clientName").value(hasItem(DEFAULT_CLIENT_NAME)))
            .andExpect(jsonPath("$.[*].clientFirstname").value(hasItem(DEFAULT_CLIENT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].updatingDate").value(hasItem(DEFAULT_UPDATING_DATE.toString())));
    }

    @Test
    @Transactional
    void getSolde() throws Exception {
        // Initialize the database
        soldeRepository.saveAndFlush(solde);

        // Get the solde
        restSoldeMockMvc
            .perform(get(ENTITY_API_URL_ID, solde.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(solde.getId().intValue()))
            .andExpect(jsonPath("$.clientRef").value(DEFAULT_CLIENT_REF))
            .andExpect(jsonPath("$.clientName").value(DEFAULT_CLIENT_NAME))
            .andExpect(jsonPath("$.clientFirstname").value(DEFAULT_CLIENT_FIRSTNAME))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.updatingDate").value(DEFAULT_UPDATING_DATE.toString()));
    }

    @Test
    @Transactional
    void getSoldesByIdFiltering() throws Exception {
        // Initialize the database
        soldeRepository.saveAndFlush(solde);

        Long id = solde.getId();

        defaultSoldeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSoldeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSoldeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSoldesByClientRefIsEqualToSomething() throws Exception {
        // Initialize the database
        soldeRepository.saveAndFlush(solde);

        // Get all the soldeList where clientRef equals to
        defaultSoldeFiltering("clientRef.equals=" + DEFAULT_CLIENT_REF, "clientRef.equals=" + UPDATED_CLIENT_REF);
    }

    @Test
    @Transactional
    void getAllSoldesByClientRefIsInShouldWork() throws Exception {
        // Initialize the database
        soldeRepository.saveAndFlush(solde);

        // Get all the soldeList where clientRef in
        defaultSoldeFiltering("clientRef.in=" + DEFAULT_CLIENT_REF + "," + UPDATED_CLIENT_REF, "clientRef.in=" + UPDATED_CLIENT_REF);
    }

    @Test
    @Transactional
    void getAllSoldesByClientRefIsNullOrNotNull() throws Exception {
        // Initialize the database
        soldeRepository.saveAndFlush(solde);

        // Get all the soldeList where clientRef is not null
        defaultSoldeFiltering("clientRef.specified=true", "clientRef.specified=false");
    }

    @Test
    @Transactional
    void getAllSoldesByClientRefContainsSomething() throws Exception {
        // Initialize the database
        soldeRepository.saveAndFlush(solde);

        // Get all the soldeList where clientRef contains
        defaultSoldeFiltering("clientRef.contains=" + DEFAULT_CLIENT_REF, "clientRef.contains=" + UPDATED_CLIENT_REF);
    }

    @Test
    @Transactional
    void getAllSoldesByClientRefNotContainsSomething() throws Exception {
        // Initialize the database
        soldeRepository.saveAndFlush(solde);

        // Get all the soldeList where clientRef does not contain
        defaultSoldeFiltering("clientRef.doesNotContain=" + UPDATED_CLIENT_REF, "clientRef.doesNotContain=" + DEFAULT_CLIENT_REF);
    }

    @Test
    @Transactional
    void getAllSoldesByClientNameIsEqualToSomething() throws Exception {
        // Initialize the database
        soldeRepository.saveAndFlush(solde);

        // Get all the soldeList where clientName equals to
        defaultSoldeFiltering("clientName.equals=" + DEFAULT_CLIENT_NAME, "clientName.equals=" + UPDATED_CLIENT_NAME);
    }

    @Test
    @Transactional
    void getAllSoldesByClientNameIsInShouldWork() throws Exception {
        // Initialize the database
        soldeRepository.saveAndFlush(solde);

        // Get all the soldeList where clientName in
        defaultSoldeFiltering("clientName.in=" + DEFAULT_CLIENT_NAME + "," + UPDATED_CLIENT_NAME, "clientName.in=" + UPDATED_CLIENT_NAME);
    }

    @Test
    @Transactional
    void getAllSoldesByClientNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        soldeRepository.saveAndFlush(solde);

        // Get all the soldeList where clientName is not null
        defaultSoldeFiltering("clientName.specified=true", "clientName.specified=false");
    }

    @Test
    @Transactional
    void getAllSoldesByClientNameContainsSomething() throws Exception {
        // Initialize the database
        soldeRepository.saveAndFlush(solde);

        // Get all the soldeList where clientName contains
        defaultSoldeFiltering("clientName.contains=" + DEFAULT_CLIENT_NAME, "clientName.contains=" + UPDATED_CLIENT_NAME);
    }

    @Test
    @Transactional
    void getAllSoldesByClientNameNotContainsSomething() throws Exception {
        // Initialize the database
        soldeRepository.saveAndFlush(solde);

        // Get all the soldeList where clientName does not contain
        defaultSoldeFiltering("clientName.doesNotContain=" + UPDATED_CLIENT_NAME, "clientName.doesNotContain=" + DEFAULT_CLIENT_NAME);
    }

    @Test
    @Transactional
    void getAllSoldesByClientFirstnameIsEqualToSomething() throws Exception {
        // Initialize the database
        soldeRepository.saveAndFlush(solde);

        // Get all the soldeList where clientFirstname equals to
        defaultSoldeFiltering("clientFirstname.equals=" + DEFAULT_CLIENT_FIRSTNAME, "clientFirstname.equals=" + UPDATED_CLIENT_FIRSTNAME);
    }

    @Test
    @Transactional
    void getAllSoldesByClientFirstnameIsInShouldWork() throws Exception {
        // Initialize the database
        soldeRepository.saveAndFlush(solde);

        // Get all the soldeList where clientFirstname in
        defaultSoldeFiltering(
            "clientFirstname.in=" + DEFAULT_CLIENT_FIRSTNAME + "," + UPDATED_CLIENT_FIRSTNAME,
            "clientFirstname.in=" + UPDATED_CLIENT_FIRSTNAME
        );
    }

    @Test
    @Transactional
    void getAllSoldesByClientFirstnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        soldeRepository.saveAndFlush(solde);

        // Get all the soldeList where clientFirstname is not null
        defaultSoldeFiltering("clientFirstname.specified=true", "clientFirstname.specified=false");
    }

    @Test
    @Transactional
    void getAllSoldesByClientFirstnameContainsSomething() throws Exception {
        // Initialize the database
        soldeRepository.saveAndFlush(solde);

        // Get all the soldeList where clientFirstname contains
        defaultSoldeFiltering(
            "clientFirstname.contains=" + DEFAULT_CLIENT_FIRSTNAME,
            "clientFirstname.contains=" + UPDATED_CLIENT_FIRSTNAME
        );
    }

    @Test
    @Transactional
    void getAllSoldesByClientFirstnameNotContainsSomething() throws Exception {
        // Initialize the database
        soldeRepository.saveAndFlush(solde);

        // Get all the soldeList where clientFirstname does not contain
        defaultSoldeFiltering(
            "clientFirstname.doesNotContain=" + UPDATED_CLIENT_FIRSTNAME,
            "clientFirstname.doesNotContain=" + DEFAULT_CLIENT_FIRSTNAME
        );
    }

    @Test
    @Transactional
    void getAllSoldesByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        soldeRepository.saveAndFlush(solde);

        // Get all the soldeList where amount equals to
        defaultSoldeFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSoldesByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        soldeRepository.saveAndFlush(solde);

        // Get all the soldeList where amount in
        defaultSoldeFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSoldesByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        soldeRepository.saveAndFlush(solde);

        // Get all the soldeList where amount is not null
        defaultSoldeFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllSoldesByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        soldeRepository.saveAndFlush(solde);

        // Get all the soldeList where amount is greater than or equal to
        defaultSoldeFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSoldesByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        soldeRepository.saveAndFlush(solde);

        // Get all the soldeList where amount is less than or equal to
        defaultSoldeFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSoldesByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        soldeRepository.saveAndFlush(solde);

        // Get all the soldeList where amount is less than
        defaultSoldeFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSoldesByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        soldeRepository.saveAndFlush(solde);

        // Get all the soldeList where amount is greater than
        defaultSoldeFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSoldesByUpdatingDateIsEqualToSomething() throws Exception {
        // Initialize the database
        soldeRepository.saveAndFlush(solde);

        // Get all the soldeList where updatingDate equals to
        defaultSoldeFiltering("updatingDate.equals=" + DEFAULT_UPDATING_DATE, "updatingDate.equals=" + UPDATED_UPDATING_DATE);
    }

    @Test
    @Transactional
    void getAllSoldesByUpdatingDateIsInShouldWork() throws Exception {
        // Initialize the database
        soldeRepository.saveAndFlush(solde);

        // Get all the soldeList where updatingDate in
        defaultSoldeFiltering(
            "updatingDate.in=" + DEFAULT_UPDATING_DATE + "," + UPDATED_UPDATING_DATE,
            "updatingDate.in=" + UPDATED_UPDATING_DATE
        );
    }

    @Test
    @Transactional
    void getAllSoldesByUpdatingDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        soldeRepository.saveAndFlush(solde);

        // Get all the soldeList where updatingDate is not null
        defaultSoldeFiltering("updatingDate.specified=true", "updatingDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSoldesByUpdatingDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        soldeRepository.saveAndFlush(solde);

        // Get all the soldeList where updatingDate is greater than or equal to
        defaultSoldeFiltering(
            "updatingDate.greaterThanOrEqual=" + DEFAULT_UPDATING_DATE,
            "updatingDate.greaterThanOrEqual=" + UPDATED_UPDATING_DATE
        );
    }

    @Test
    @Transactional
    void getAllSoldesByUpdatingDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        soldeRepository.saveAndFlush(solde);

        // Get all the soldeList where updatingDate is less than or equal to
        defaultSoldeFiltering(
            "updatingDate.lessThanOrEqual=" + DEFAULT_UPDATING_DATE,
            "updatingDate.lessThanOrEqual=" + SMALLER_UPDATING_DATE
        );
    }

    @Test
    @Transactional
    void getAllSoldesByUpdatingDateIsLessThanSomething() throws Exception {
        // Initialize the database
        soldeRepository.saveAndFlush(solde);

        // Get all the soldeList where updatingDate is less than
        defaultSoldeFiltering("updatingDate.lessThan=" + UPDATED_UPDATING_DATE, "updatingDate.lessThan=" + DEFAULT_UPDATING_DATE);
    }

    @Test
    @Transactional
    void getAllSoldesByUpdatingDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        soldeRepository.saveAndFlush(solde);

        // Get all the soldeList where updatingDate is greater than
        defaultSoldeFiltering("updatingDate.greaterThan=" + SMALLER_UPDATING_DATE, "updatingDate.greaterThan=" + DEFAULT_UPDATING_DATE);
    }

    private void defaultSoldeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSoldeShouldBeFound(shouldBeFound);
        defaultSoldeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSoldeShouldBeFound(String filter) throws Exception {
        restSoldeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(solde.getId().intValue())))
            .andExpect(jsonPath("$.[*].clientRef").value(hasItem(DEFAULT_CLIENT_REF)))
            .andExpect(jsonPath("$.[*].clientName").value(hasItem(DEFAULT_CLIENT_NAME)))
            .andExpect(jsonPath("$.[*].clientFirstname").value(hasItem(DEFAULT_CLIENT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].updatingDate").value(hasItem(DEFAULT_UPDATING_DATE.toString())));

        // Check, that the count call also returns 1
        restSoldeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSoldeShouldNotBeFound(String filter) throws Exception {
        restSoldeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSoldeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSolde() throws Exception {
        // Get the solde
        restSoldeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSolde() throws Exception {
        // Initialize the database
        soldeRepository.saveAndFlush(solde);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the solde
        Solde updatedSolde = soldeRepository.findById(solde.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSolde are not directly saved in db
        em.detach(updatedSolde);
        updatedSolde
            .clientRef(UPDATED_CLIENT_REF)
            .clientName(UPDATED_CLIENT_NAME)
            .clientFirstname(UPDATED_CLIENT_FIRSTNAME)
            .amount(UPDATED_AMOUNT)
            .updatingDate(UPDATED_UPDATING_DATE);

        restSoldeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSolde.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSolde))
            )
            .andExpect(status().isOk());

        // Validate the Solde in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSoldeToMatchAllProperties(updatedSolde);
    }

    @Test
    @Transactional
    void putNonExistingSolde() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        solde.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSoldeMockMvc
            .perform(put(ENTITY_API_URL_ID, solde.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(solde)))
            .andExpect(status().isBadRequest());

        // Validate the Solde in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSolde() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        solde.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSoldeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(solde))
            )
            .andExpect(status().isBadRequest());

        // Validate the Solde in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSolde() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        solde.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSoldeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(solde)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Solde in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSoldeWithPatch() throws Exception {
        // Initialize the database
        soldeRepository.saveAndFlush(solde);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the solde using partial update
        Solde partialUpdatedSolde = new Solde();
        partialUpdatedSolde.setId(solde.getId());

        partialUpdatedSolde.clientRef(UPDATED_CLIENT_REF).clientName(UPDATED_CLIENT_NAME).clientFirstname(UPDATED_CLIENT_FIRSTNAME);

        restSoldeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSolde.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSolde))
            )
            .andExpect(status().isOk());

        // Validate the Solde in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSoldeUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSolde, solde), getPersistedSolde(solde));
    }

    @Test
    @Transactional
    void fullUpdateSoldeWithPatch() throws Exception {
        // Initialize the database
        soldeRepository.saveAndFlush(solde);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the solde using partial update
        Solde partialUpdatedSolde = new Solde();
        partialUpdatedSolde.setId(solde.getId());

        partialUpdatedSolde
            .clientRef(UPDATED_CLIENT_REF)
            .clientName(UPDATED_CLIENT_NAME)
            .clientFirstname(UPDATED_CLIENT_FIRSTNAME)
            .amount(UPDATED_AMOUNT)
            .updatingDate(UPDATED_UPDATING_DATE);

        restSoldeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSolde.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSolde))
            )
            .andExpect(status().isOk());

        // Validate the Solde in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSoldeUpdatableFieldsEquals(partialUpdatedSolde, getPersistedSolde(partialUpdatedSolde));
    }

    @Test
    @Transactional
    void patchNonExistingSolde() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        solde.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSoldeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, solde.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(solde))
            )
            .andExpect(status().isBadRequest());

        // Validate the Solde in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSolde() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        solde.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSoldeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(solde))
            )
            .andExpect(status().isBadRequest());

        // Validate the Solde in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSolde() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        solde.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSoldeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(solde)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Solde in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSolde() throws Exception {
        // Initialize the database
        soldeRepository.saveAndFlush(solde);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the solde
        restSoldeMockMvc
            .perform(delete(ENTITY_API_URL_ID, solde.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return soldeRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Solde getPersistedSolde(Solde solde) {
        return soldeRepository.findById(solde.getId()).orElseThrow();
    }

    protected void assertPersistedSoldeToMatchAllProperties(Solde expectedSolde) {
        assertSoldeAllPropertiesEquals(expectedSolde, getPersistedSolde(expectedSolde));
    }

    protected void assertPersistedSoldeToMatchUpdatableProperties(Solde expectedSolde) {
        assertSoldeAllUpdatablePropertiesEquals(expectedSolde, getPersistedSolde(expectedSolde));
    }
}
