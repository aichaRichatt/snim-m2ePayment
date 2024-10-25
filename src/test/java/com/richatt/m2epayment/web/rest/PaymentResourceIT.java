package com.richatt.m2epayment.web.rest;

import static com.richatt.m2epayment.domain.PaymentAsserts.*;
import static com.richatt.m2epayment.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.richatt.m2epayment.IntegrationTest;
import com.richatt.m2epayment.domain.Payment;
import com.richatt.m2epayment.repository.PaymentRepository;
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
 * Integration tests for the {@link PaymentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaymentResourceIT {

    private static final String DEFAULT_TRANSACTION_ID = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_ID = "BBBBBBBBBB";

    private static final Double DEFAULT_PAID_AMOUNT = 1D;
    private static final Double UPDATED_PAID_AMOUNT = 2D;
    private static final Double SMALLER_PAID_AMOUNT = 1D - 1D;

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PAY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PAY_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PAY_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_CLIENT_REF = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_REF = "BBBBBBBBBB";

    private static final String DEFAULT_WALLET_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_WALLET_MESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_SAP_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_SAP_MESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_PAY_WALLET = "AAAAAAAAAA";
    private static final String UPDATED_PAY_WALLET = "BBBBBBBBBB";

    private static final String DEFAULT_PAY_WALLET_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_PAY_WALLET_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_PAY_SAP_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_PAY_SAP_STATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/payments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentMockMvc;

    private Payment payment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payment createEntity(EntityManager em) {
        Payment payment = new Payment()
            .transactionId(DEFAULT_TRANSACTION_ID)
            .paidAmount(DEFAULT_PAID_AMOUNT)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .payDate(DEFAULT_PAY_DATE)
            .clientRef(DEFAULT_CLIENT_REF)
            .walletMessage(DEFAULT_WALLET_MESSAGE)
            .sapMessage(DEFAULT_SAP_MESSAGE)
            .payWallet(DEFAULT_PAY_WALLET)
            .payWalletStatus(DEFAULT_PAY_WALLET_STATUS)
            .paySapStatus(DEFAULT_PAY_SAP_STATUS);
        return payment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payment createUpdatedEntity(EntityManager em) {
        Payment payment = new Payment()
            .transactionId(UPDATED_TRANSACTION_ID)
            .paidAmount(UPDATED_PAID_AMOUNT)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .payDate(UPDATED_PAY_DATE)
            .clientRef(UPDATED_CLIENT_REF)
            .walletMessage(UPDATED_WALLET_MESSAGE)
            .sapMessage(UPDATED_SAP_MESSAGE)
            .payWallet(UPDATED_PAY_WALLET)
            .payWalletStatus(UPDATED_PAY_WALLET_STATUS)
            .paySapStatus(UPDATED_PAY_SAP_STATUS);
        return payment;
    }

    @BeforeEach
    public void initTest() {
        payment = createEntity(em);
    }

    @Test
    @Transactional
    void createPayment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Payment
        var returnedPayment = om.readValue(
            restPaymentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(payment)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Payment.class
        );

        // Validate the Payment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPaymentUpdatableFieldsEquals(returnedPayment, getPersistedPayment(returnedPayment));
    }

    @Test
    @Transactional
    void createPaymentWithExistingId() throws Exception {
        // Create the Payment with an existing ID
        payment.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(payment)))
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTransactionIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        payment.setTransactionId(null);

        // Create the Payment, which fails.

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(payment)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPayments() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionId").value(hasItem(DEFAULT_TRANSACTION_ID)))
            .andExpect(jsonPath("$.[*].paidAmount").value(hasItem(DEFAULT_PAID_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].payDate").value(hasItem(DEFAULT_PAY_DATE.toString())))
            .andExpect(jsonPath("$.[*].clientRef").value(hasItem(DEFAULT_CLIENT_REF)))
            .andExpect(jsonPath("$.[*].walletMessage").value(hasItem(DEFAULT_WALLET_MESSAGE)))
            .andExpect(jsonPath("$.[*].sapMessage").value(hasItem(DEFAULT_SAP_MESSAGE)))
            .andExpect(jsonPath("$.[*].payWallet").value(hasItem(DEFAULT_PAY_WALLET)))
            .andExpect(jsonPath("$.[*].payWalletStatus").value(hasItem(DEFAULT_PAY_WALLET_STATUS)))
            .andExpect(jsonPath("$.[*].paySapStatus").value(hasItem(DEFAULT_PAY_SAP_STATUS)));
    }

    @Test
    @Transactional
    void getPayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get the payment
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL_ID, payment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(payment.getId().intValue()))
            .andExpect(jsonPath("$.transactionId").value(DEFAULT_TRANSACTION_ID))
            .andExpect(jsonPath("$.paidAmount").value(DEFAULT_PAID_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.payDate").value(DEFAULT_PAY_DATE.toString()))
            .andExpect(jsonPath("$.clientRef").value(DEFAULT_CLIENT_REF))
            .andExpect(jsonPath("$.walletMessage").value(DEFAULT_WALLET_MESSAGE))
            .andExpect(jsonPath("$.sapMessage").value(DEFAULT_SAP_MESSAGE))
            .andExpect(jsonPath("$.payWallet").value(DEFAULT_PAY_WALLET))
            .andExpect(jsonPath("$.payWalletStatus").value(DEFAULT_PAY_WALLET_STATUS))
            .andExpect(jsonPath("$.paySapStatus").value(DEFAULT_PAY_SAP_STATUS));
    }

    @Test
    @Transactional
    void getPaymentsByIdFiltering() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        Long id = payment.getId();

        defaultPaymentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPaymentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPaymentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPaymentsByTransactionIdIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where transactionId equals to
        defaultPaymentFiltering("transactionId.equals=" + DEFAULT_TRANSACTION_ID, "transactionId.equals=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void getAllPaymentsByTransactionIdIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where transactionId in
        defaultPaymentFiltering(
            "transactionId.in=" + DEFAULT_TRANSACTION_ID + "," + UPDATED_TRANSACTION_ID,
            "transactionId.in=" + UPDATED_TRANSACTION_ID
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByTransactionIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where transactionId is not null
        defaultPaymentFiltering("transactionId.specified=true", "transactionId.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByTransactionIdContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where transactionId contains
        defaultPaymentFiltering("transactionId.contains=" + DEFAULT_TRANSACTION_ID, "transactionId.contains=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void getAllPaymentsByTransactionIdNotContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where transactionId does not contain
        defaultPaymentFiltering(
            "transactionId.doesNotContain=" + UPDATED_TRANSACTION_ID,
            "transactionId.doesNotContain=" + DEFAULT_TRANSACTION_ID
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByPaidAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paidAmount equals to
        defaultPaymentFiltering("paidAmount.equals=" + DEFAULT_PAID_AMOUNT, "paidAmount.equals=" + UPDATED_PAID_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaidAmountIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paidAmount in
        defaultPaymentFiltering("paidAmount.in=" + DEFAULT_PAID_AMOUNT + "," + UPDATED_PAID_AMOUNT, "paidAmount.in=" + UPDATED_PAID_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaidAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paidAmount is not null
        defaultPaymentFiltering("paidAmount.specified=true", "paidAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByPaidAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paidAmount is greater than or equal to
        defaultPaymentFiltering(
            "paidAmount.greaterThanOrEqual=" + DEFAULT_PAID_AMOUNT,
            "paidAmount.greaterThanOrEqual=" + UPDATED_PAID_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByPaidAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paidAmount is less than or equal to
        defaultPaymentFiltering("paidAmount.lessThanOrEqual=" + DEFAULT_PAID_AMOUNT, "paidAmount.lessThanOrEqual=" + SMALLER_PAID_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaidAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paidAmount is less than
        defaultPaymentFiltering("paidAmount.lessThan=" + UPDATED_PAID_AMOUNT, "paidAmount.lessThan=" + DEFAULT_PAID_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaidAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paidAmount is greater than
        defaultPaymentFiltering("paidAmount.greaterThan=" + SMALLER_PAID_AMOUNT, "paidAmount.greaterThan=" + DEFAULT_PAID_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where phoneNumber equals to
        defaultPaymentFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllPaymentsByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where phoneNumber in
        defaultPaymentFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where phoneNumber is not null
        defaultPaymentFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where phoneNumber contains
        defaultPaymentFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllPaymentsByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where phoneNumber does not contain
        defaultPaymentFiltering("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER, "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllPaymentsByPayDateIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where payDate equals to
        defaultPaymentFiltering("payDate.equals=" + DEFAULT_PAY_DATE, "payDate.equals=" + UPDATED_PAY_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentsByPayDateIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where payDate in
        defaultPaymentFiltering("payDate.in=" + DEFAULT_PAY_DATE + "," + UPDATED_PAY_DATE, "payDate.in=" + UPDATED_PAY_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentsByPayDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where payDate is not null
        defaultPaymentFiltering("payDate.specified=true", "payDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByPayDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where payDate is greater than or equal to
        defaultPaymentFiltering("payDate.greaterThanOrEqual=" + DEFAULT_PAY_DATE, "payDate.greaterThanOrEqual=" + UPDATED_PAY_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentsByPayDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where payDate is less than or equal to
        defaultPaymentFiltering("payDate.lessThanOrEqual=" + DEFAULT_PAY_DATE, "payDate.lessThanOrEqual=" + SMALLER_PAY_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentsByPayDateIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where payDate is less than
        defaultPaymentFiltering("payDate.lessThan=" + UPDATED_PAY_DATE, "payDate.lessThan=" + DEFAULT_PAY_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentsByPayDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where payDate is greater than
        defaultPaymentFiltering("payDate.greaterThan=" + SMALLER_PAY_DATE, "payDate.greaterThan=" + DEFAULT_PAY_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentsByClientRefIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where clientRef equals to
        defaultPaymentFiltering("clientRef.equals=" + DEFAULT_CLIENT_REF, "clientRef.equals=" + UPDATED_CLIENT_REF);
    }

    @Test
    @Transactional
    void getAllPaymentsByClientRefIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where clientRef in
        defaultPaymentFiltering("clientRef.in=" + DEFAULT_CLIENT_REF + "," + UPDATED_CLIENT_REF, "clientRef.in=" + UPDATED_CLIENT_REF);
    }

    @Test
    @Transactional
    void getAllPaymentsByClientRefIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where clientRef is not null
        defaultPaymentFiltering("clientRef.specified=true", "clientRef.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByClientRefContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where clientRef contains
        defaultPaymentFiltering("clientRef.contains=" + DEFAULT_CLIENT_REF, "clientRef.contains=" + UPDATED_CLIENT_REF);
    }

    @Test
    @Transactional
    void getAllPaymentsByClientRefNotContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where clientRef does not contain
        defaultPaymentFiltering("clientRef.doesNotContain=" + UPDATED_CLIENT_REF, "clientRef.doesNotContain=" + DEFAULT_CLIENT_REF);
    }

    @Test
    @Transactional
    void getAllPaymentsByWalletMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where walletMessage equals to
        defaultPaymentFiltering("walletMessage.equals=" + DEFAULT_WALLET_MESSAGE, "walletMessage.equals=" + UPDATED_WALLET_MESSAGE);
    }

    @Test
    @Transactional
    void getAllPaymentsByWalletMessageIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where walletMessage in
        defaultPaymentFiltering(
            "walletMessage.in=" + DEFAULT_WALLET_MESSAGE + "," + UPDATED_WALLET_MESSAGE,
            "walletMessage.in=" + UPDATED_WALLET_MESSAGE
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByWalletMessageIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where walletMessage is not null
        defaultPaymentFiltering("walletMessage.specified=true", "walletMessage.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByWalletMessageContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where walletMessage contains
        defaultPaymentFiltering("walletMessage.contains=" + DEFAULT_WALLET_MESSAGE, "walletMessage.contains=" + UPDATED_WALLET_MESSAGE);
    }

    @Test
    @Transactional
    void getAllPaymentsByWalletMessageNotContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where walletMessage does not contain
        defaultPaymentFiltering(
            "walletMessage.doesNotContain=" + UPDATED_WALLET_MESSAGE,
            "walletMessage.doesNotContain=" + DEFAULT_WALLET_MESSAGE
        );
    }

    @Test
    @Transactional
    void getAllPaymentsBySapMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where sapMessage equals to
        defaultPaymentFiltering("sapMessage.equals=" + DEFAULT_SAP_MESSAGE, "sapMessage.equals=" + UPDATED_SAP_MESSAGE);
    }

    @Test
    @Transactional
    void getAllPaymentsBySapMessageIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where sapMessage in
        defaultPaymentFiltering("sapMessage.in=" + DEFAULT_SAP_MESSAGE + "," + UPDATED_SAP_MESSAGE, "sapMessage.in=" + UPDATED_SAP_MESSAGE);
    }

    @Test
    @Transactional
    void getAllPaymentsBySapMessageIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where sapMessage is not null
        defaultPaymentFiltering("sapMessage.specified=true", "sapMessage.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsBySapMessageContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where sapMessage contains
        defaultPaymentFiltering("sapMessage.contains=" + DEFAULT_SAP_MESSAGE, "sapMessage.contains=" + UPDATED_SAP_MESSAGE);
    }

    @Test
    @Transactional
    void getAllPaymentsBySapMessageNotContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where sapMessage does not contain
        defaultPaymentFiltering("sapMessage.doesNotContain=" + UPDATED_SAP_MESSAGE, "sapMessage.doesNotContain=" + DEFAULT_SAP_MESSAGE);
    }

    @Test
    @Transactional
    void getAllPaymentsByPayWalletIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where payWallet equals to
        defaultPaymentFiltering("payWallet.equals=" + DEFAULT_PAY_WALLET, "payWallet.equals=" + UPDATED_PAY_WALLET);
    }

    @Test
    @Transactional
    void getAllPaymentsByPayWalletIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where payWallet in
        defaultPaymentFiltering("payWallet.in=" + DEFAULT_PAY_WALLET + "," + UPDATED_PAY_WALLET, "payWallet.in=" + UPDATED_PAY_WALLET);
    }

    @Test
    @Transactional
    void getAllPaymentsByPayWalletIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where payWallet is not null
        defaultPaymentFiltering("payWallet.specified=true", "payWallet.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByPayWalletContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where payWallet contains
        defaultPaymentFiltering("payWallet.contains=" + DEFAULT_PAY_WALLET, "payWallet.contains=" + UPDATED_PAY_WALLET);
    }

    @Test
    @Transactional
    void getAllPaymentsByPayWalletNotContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where payWallet does not contain
        defaultPaymentFiltering("payWallet.doesNotContain=" + UPDATED_PAY_WALLET, "payWallet.doesNotContain=" + DEFAULT_PAY_WALLET);
    }

    @Test
    @Transactional
    void getAllPaymentsByPayWalletStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where payWalletStatus equals to
        defaultPaymentFiltering(
            "payWalletStatus.equals=" + DEFAULT_PAY_WALLET_STATUS,
            "payWalletStatus.equals=" + UPDATED_PAY_WALLET_STATUS
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByPayWalletStatusIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where payWalletStatus in
        defaultPaymentFiltering(
            "payWalletStatus.in=" + DEFAULT_PAY_WALLET_STATUS + "," + UPDATED_PAY_WALLET_STATUS,
            "payWalletStatus.in=" + UPDATED_PAY_WALLET_STATUS
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByPayWalletStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where payWalletStatus is not null
        defaultPaymentFiltering("payWalletStatus.specified=true", "payWalletStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByPayWalletStatusContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where payWalletStatus contains
        defaultPaymentFiltering(
            "payWalletStatus.contains=" + DEFAULT_PAY_WALLET_STATUS,
            "payWalletStatus.contains=" + UPDATED_PAY_WALLET_STATUS
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByPayWalletStatusNotContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where payWalletStatus does not contain
        defaultPaymentFiltering(
            "payWalletStatus.doesNotContain=" + UPDATED_PAY_WALLET_STATUS,
            "payWalletStatus.doesNotContain=" + DEFAULT_PAY_WALLET_STATUS
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByPaySapStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paySapStatus equals to
        defaultPaymentFiltering("paySapStatus.equals=" + DEFAULT_PAY_SAP_STATUS, "paySapStatus.equals=" + UPDATED_PAY_SAP_STATUS);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaySapStatusIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paySapStatus in
        defaultPaymentFiltering(
            "paySapStatus.in=" + DEFAULT_PAY_SAP_STATUS + "," + UPDATED_PAY_SAP_STATUS,
            "paySapStatus.in=" + UPDATED_PAY_SAP_STATUS
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByPaySapStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paySapStatus is not null
        defaultPaymentFiltering("paySapStatus.specified=true", "paySapStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByPaySapStatusContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paySapStatus contains
        defaultPaymentFiltering("paySapStatus.contains=" + DEFAULT_PAY_SAP_STATUS, "paySapStatus.contains=" + UPDATED_PAY_SAP_STATUS);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaySapStatusNotContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paySapStatus does not contain
        defaultPaymentFiltering(
            "paySapStatus.doesNotContain=" + UPDATED_PAY_SAP_STATUS,
            "paySapStatus.doesNotContain=" + DEFAULT_PAY_SAP_STATUS
        );
    }

    private void defaultPaymentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPaymentShouldBeFound(shouldBeFound);
        defaultPaymentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaymentShouldBeFound(String filter) throws Exception {
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionId").value(hasItem(DEFAULT_TRANSACTION_ID)))
            .andExpect(jsonPath("$.[*].paidAmount").value(hasItem(DEFAULT_PAID_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].payDate").value(hasItem(DEFAULT_PAY_DATE.toString())))
            .andExpect(jsonPath("$.[*].clientRef").value(hasItem(DEFAULT_CLIENT_REF)))
            .andExpect(jsonPath("$.[*].walletMessage").value(hasItem(DEFAULT_WALLET_MESSAGE)))
            .andExpect(jsonPath("$.[*].sapMessage").value(hasItem(DEFAULT_SAP_MESSAGE)))
            .andExpect(jsonPath("$.[*].payWallet").value(hasItem(DEFAULT_PAY_WALLET)))
            .andExpect(jsonPath("$.[*].payWalletStatus").value(hasItem(DEFAULT_PAY_WALLET_STATUS)))
            .andExpect(jsonPath("$.[*].paySapStatus").value(hasItem(DEFAULT_PAY_SAP_STATUS)));

        // Check, that the count call also returns 1
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaymentShouldNotBeFound(String filter) throws Exception {
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPayment() throws Exception {
        // Get the payment
        restPaymentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the payment
        Payment updatedPayment = paymentRepository.findById(payment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPayment are not directly saved in db
        em.detach(updatedPayment);
        updatedPayment
            .transactionId(UPDATED_TRANSACTION_ID)
            .paidAmount(UPDATED_PAID_AMOUNT)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .payDate(UPDATED_PAY_DATE)
            .clientRef(UPDATED_CLIENT_REF)
            .walletMessage(UPDATED_WALLET_MESSAGE)
            .sapMessage(UPDATED_SAP_MESSAGE)
            .payWallet(UPDATED_PAY_WALLET)
            .payWalletStatus(UPDATED_PAY_WALLET_STATUS)
            .paySapStatus(UPDATED_PAY_SAP_STATUS);

        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPayment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPayment))
            )
            .andExpect(status().isOk());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPaymentToMatchAllProperties(updatedPayment);
    }

    @Test
    @Transactional
    void putNonExistingPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(put(ENTITY_API_URL_ID, payment.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(payment)))
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(payment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(payment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentWithPatch() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the payment using partial update
        Payment partialUpdatedPayment = new Payment();
        partialUpdatedPayment.setId(payment.getId());

        partialUpdatedPayment
            .paidAmount(UPDATED_PAID_AMOUNT)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .sapMessage(UPDATED_SAP_MESSAGE)
            .payWalletStatus(UPDATED_PAY_WALLET_STATUS)
            .paySapStatus(UPDATED_PAY_SAP_STATUS);

        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPayment))
            )
            .andExpect(status().isOk());

        // Validate the Payment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPayment, payment), getPersistedPayment(payment));
    }

    @Test
    @Transactional
    void fullUpdatePaymentWithPatch() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the payment using partial update
        Payment partialUpdatedPayment = new Payment();
        partialUpdatedPayment.setId(payment.getId());

        partialUpdatedPayment
            .transactionId(UPDATED_TRANSACTION_ID)
            .paidAmount(UPDATED_PAID_AMOUNT)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .payDate(UPDATED_PAY_DATE)
            .clientRef(UPDATED_CLIENT_REF)
            .walletMessage(UPDATED_WALLET_MESSAGE)
            .sapMessage(UPDATED_SAP_MESSAGE)
            .payWallet(UPDATED_PAY_WALLET)
            .payWalletStatus(UPDATED_PAY_WALLET_STATUS)
            .paySapStatus(UPDATED_PAY_SAP_STATUS);

        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPayment))
            )
            .andExpect(status().isOk());

        // Validate the Payment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentUpdatableFieldsEquals(partialUpdatedPayment, getPersistedPayment(partialUpdatedPayment));
    }

    @Test
    @Transactional
    void patchNonExistingPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, payment.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(payment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(payment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(payment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the payment
        restPaymentMockMvc
            .perform(delete(ENTITY_API_URL_ID, payment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return paymentRepository.count();
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

    protected Payment getPersistedPayment(Payment payment) {
        return paymentRepository.findById(payment.getId()).orElseThrow();
    }

    protected void assertPersistedPaymentToMatchAllProperties(Payment expectedPayment) {
        assertPaymentAllPropertiesEquals(expectedPayment, getPersistedPayment(expectedPayment));
    }

    protected void assertPersistedPaymentToMatchUpdatableProperties(Payment expectedPayment) {
        assertPaymentAllUpdatablePropertiesEquals(expectedPayment, getPersistedPayment(expectedPayment));
    }
}
