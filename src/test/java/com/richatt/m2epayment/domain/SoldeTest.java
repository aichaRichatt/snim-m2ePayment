package com.richatt.m2epayment.domain;

import static com.richatt.m2epayment.domain.SoldeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.richatt.m2epayment.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SoldeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Solde.class);
        Solde solde1 = getSoldeSample1();
        Solde solde2 = new Solde();
        assertThat(solde1).isNotEqualTo(solde2);

        solde2.setId(solde1.getId());
        assertThat(solde1).isEqualTo(solde2);

        solde2 = getSoldeSample2();
        assertThat(solde1).isNotEqualTo(solde2);
    }
}
