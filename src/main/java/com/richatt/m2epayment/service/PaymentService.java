package com.richatt.m2epayment.service;

import com.richatt.m2epayment.domain.Payment;
import com.richatt.m2epayment.repository.PaymentRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.richatt.m2epayment.domain.Payment}.
 */
@Service
@Transactional
public class PaymentService {

    private final Logger log = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    /**
     * Save a payment.
     *
     * @param payment the entity to save.
     * @return the persisted entity.
     */
    public Payment save(Payment payment) {
        log.debug("Request to save Payment : {}", payment);
        return paymentRepository.save(payment);
    }

    /**
     * Update a payment.
     *
     * @param payment the entity to save.
     * @return the persisted entity.
     */
    public Payment update(Payment payment) {
        log.debug("Request to update Payment : {}", payment);
        return paymentRepository.save(payment);
    }

    /**
     * Partially update a payment.
     *
     * @param payment the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Payment> partialUpdate(Payment payment) {
        log.debug("Request to partially update Payment : {}", payment);

        return paymentRepository
            .findById(payment.getId())
            .map(existingPayment -> {
                if (payment.getTransactionId() != null) {
                    existingPayment.setTransactionId(payment.getTransactionId());
                }
                if (payment.getPaidAmount() != null) {
                    existingPayment.setPaidAmount(payment.getPaidAmount());
                }
                if (payment.getPhoneNumber() != null) {
                    existingPayment.setPhoneNumber(payment.getPhoneNumber());
                }
                if (payment.getPayDate() != null) {
                    existingPayment.setPayDate(payment.getPayDate());
                }
                if (payment.getClientRef() != null) {
                    existingPayment.setClientRef(payment.getClientRef());
                }
                if (payment.getWalletMessage() != null) {
                    existingPayment.setWalletMessage(payment.getWalletMessage());
                }
                if (payment.getSapMessage() != null) {
                    existingPayment.setSapMessage(payment.getSapMessage());
                }
                if (payment.getPayWallet() != null) {
                    existingPayment.setPayWallet(payment.getPayWallet());
                }
                if (payment.getPayWalletStatus() != null) {
                    existingPayment.setPayWalletStatus(payment.getPayWalletStatus());
                }
                if (payment.getPaySapStatus() != null) {
                    existingPayment.setPaySapStatus(payment.getPaySapStatus());
                }

                return existingPayment;
            })
            .map(paymentRepository::save);
    }

    /**
     * Get one payment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Payment> findOne(Long id) {
        log.debug("Request to get Payment : {}", id);
        return paymentRepository.findById(id);
    }

    /**
     * Delete the payment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Payment : {}", id);
        paymentRepository.deleteById(id);
    }
}
