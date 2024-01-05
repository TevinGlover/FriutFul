package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.BalanceTestSamples.*;
import static com.mycompany.myapp.domain.TransactionsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransactionsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Transactions.class);
        Transactions transactions1 = getTransactionsSample1();
        Transactions transactions2 = new Transactions();
        assertThat(transactions1).isNotEqualTo(transactions2);

        transactions2.setId(transactions1.getId());
        assertThat(transactions1).isEqualTo(transactions2);

        transactions2 = getTransactionsSample2();
        assertThat(transactions1).isNotEqualTo(transactions2);
    }

    @Test
    void accountTest() throws Exception {
        Transactions transactions = getTransactionsRandomSampleGenerator();
        Balance balanceBack = getBalanceRandomSampleGenerator();

        transactions.setAccount(balanceBack);
        assertThat(transactions.getAccount()).isEqualTo(balanceBack);

        transactions.account(null);
        assertThat(transactions.getAccount()).isNull();
    }
}
