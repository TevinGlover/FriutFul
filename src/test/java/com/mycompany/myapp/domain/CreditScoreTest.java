package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ChildTestSamples.*;
import static com.mycompany.myapp.domain.CreditScoreTestSamples.*;
import static com.mycompany.myapp.domain.ParentsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CreditScoreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CreditScore.class);
        CreditScore creditScore1 = getCreditScoreSample1();
        CreditScore creditScore2 = new CreditScore();
        assertThat(creditScore1).isNotEqualTo(creditScore2);

        creditScore2.setId(creditScore1.getId());
        assertThat(creditScore1).isEqualTo(creditScore2);

        creditScore2 = getCreditScoreSample2();
        assertThat(creditScore1).isNotEqualTo(creditScore2);
    }

    @Test
    void parentsTest() throws Exception {
        CreditScore creditScore = getCreditScoreRandomSampleGenerator();
        Parents parentsBack = getParentsRandomSampleGenerator();

        creditScore.setParents(parentsBack);
        assertThat(creditScore.getParents()).isEqualTo(parentsBack);
        assertThat(parentsBack.getCreditScore()).isEqualTo(creditScore);

        creditScore.parents(null);
        assertThat(creditScore.getParents()).isNull();
        assertThat(parentsBack.getCreditScore()).isNull();
    }

    @Test
    void childTest() throws Exception {
        CreditScore creditScore = getCreditScoreRandomSampleGenerator();
        Child childBack = getChildRandomSampleGenerator();

        creditScore.setChild(childBack);
        assertThat(creditScore.getChild()).isEqualTo(childBack);
        assertThat(childBack.getCreditScore()).isEqualTo(creditScore);

        creditScore.child(null);
        assertThat(creditScore.getChild()).isNull();
        assertThat(childBack.getCreditScore()).isNull();
    }
}
