package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.BalanceTestSamples.*;
import static com.mycompany.myapp.domain.ChildTestSamples.*;
import static com.mycompany.myapp.domain.ParentsTestSamples.*;
import static com.mycompany.myapp.domain.TransactionsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class BalanceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Balance.class);
        Balance balance1 = getBalanceSample1();
        Balance balance2 = new Balance();
        assertThat(balance1).isNotEqualTo(balance2);

        balance2.setId(balance1.getId());
        assertThat(balance1).isEqualTo(balance2);

        balance2 = getBalanceSample2();
        assertThat(balance1).isNotEqualTo(balance2);
    }

    @Test
    void transactionsTest() throws Exception {
        Balance balance = getBalanceRandomSampleGenerator();
        Transactions transactionsBack = getTransactionsRandomSampleGenerator();

        balance.addTransactions(transactionsBack);
        assertThat(balance.getTransactions()).containsOnly(transactionsBack);
        assertThat(transactionsBack.getAccount()).isEqualTo(balance);

        balance.removeTransactions(transactionsBack);
        assertThat(balance.getTransactions()).doesNotContain(transactionsBack);
        assertThat(transactionsBack.getAccount()).isNull();

        balance.transactions(new HashSet<>(Set.of(transactionsBack)));
        assertThat(balance.getTransactions()).containsOnly(transactionsBack);
        assertThat(transactionsBack.getAccount()).isEqualTo(balance);

        balance.setTransactions(new HashSet<>());
        assertThat(balance.getTransactions()).doesNotContain(transactionsBack);
        assertThat(transactionsBack.getAccount()).isNull();
    }

    @Test
    void parentsTest() throws Exception {
        Balance balance = getBalanceRandomSampleGenerator();
        Parents parentsBack = getParentsRandomSampleGenerator();

        balance.setParents(parentsBack);
        assertThat(balance.getParents()).isEqualTo(parentsBack);

        balance.parents(null);
        assertThat(balance.getParents()).isNull();
    }

    @Test
    void childTest() throws Exception {
        Balance balance = getBalanceRandomSampleGenerator();
        Child childBack = getChildRandomSampleGenerator();

        balance.setChild(childBack);
        assertThat(balance.getChild()).isEqualTo(childBack);
        assertThat(childBack.getAccount()).isEqualTo(balance);

        balance.child(null);
        assertThat(balance.getChild()).isNull();
        assertThat(childBack.getAccount()).isNull();
    }
}
