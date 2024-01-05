package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Balance;
import com.mycompany.myapp.repository.BalanceRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
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
 * Integration tests for the {@link BalanceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BalanceResourceIT {

    private static final String DEFAULT_CREDIT_CARD_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CREDIT_CARD_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_CREDITCARD_NUM = 1;
    private static final Integer UPDATED_CREDITCARD_NUM = 2;

    private static final Instant DEFAULT_VAILD_THRU = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VAILD_THRU = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_CVS = 1;
    private static final Integer UPDATED_CVS = 2;

    private static final Double DEFAULT_MAX_LIMIT = 1D;
    private static final Double UPDATED_MAX_LIMIT = 2D;

    private static final Double DEFAULT_THRITY_PRECENT_LIMIT = 1D;
    private static final Double UPDATED_THRITY_PRECENT_LIMIT = 2D;

    private static final String ENTITY_API_URL = "/api/balances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBalanceMockMvc;

    private Balance balance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Balance createEntity(EntityManager em) {
        Balance balance = new Balance()
            .creditCardType(DEFAULT_CREDIT_CARD_TYPE)
            .creditcardNum(DEFAULT_CREDITCARD_NUM)
            .vaildThru(DEFAULT_VAILD_THRU)
            .cvs(DEFAULT_CVS)
            .maxLimit(DEFAULT_MAX_LIMIT)
            .thrityPrecentLimit(DEFAULT_THRITY_PRECENT_LIMIT);
        return balance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Balance createUpdatedEntity(EntityManager em) {
        Balance balance = new Balance()
            .creditCardType(UPDATED_CREDIT_CARD_TYPE)
            .creditcardNum(UPDATED_CREDITCARD_NUM)
            .vaildThru(UPDATED_VAILD_THRU)
            .cvs(UPDATED_CVS)
            .maxLimit(UPDATED_MAX_LIMIT)
            .thrityPrecentLimit(UPDATED_THRITY_PRECENT_LIMIT);
        return balance;
    }

    @BeforeEach
    public void initTest() {
        balance = createEntity(em);
    }

    @Test
    @Transactional
    void createBalance() throws Exception {
        int databaseSizeBeforeCreate = balanceRepository.findAll().size();
        // Create the Balance
        restBalanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(balance)))
            .andExpect(status().isCreated());

        // Validate the Balance in the database
        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeCreate + 1);
        Balance testBalance = balanceList.get(balanceList.size() - 1);
        assertThat(testBalance.getCreditCardType()).isEqualTo(DEFAULT_CREDIT_CARD_TYPE);
        assertThat(testBalance.getCreditcardNum()).isEqualTo(DEFAULT_CREDITCARD_NUM);
        assertThat(testBalance.getVaildThru()).isEqualTo(DEFAULT_VAILD_THRU);
        assertThat(testBalance.getCvs()).isEqualTo(DEFAULT_CVS);
        assertThat(testBalance.getMaxLimit()).isEqualTo(DEFAULT_MAX_LIMIT);
        assertThat(testBalance.getThrityPrecentLimit()).isEqualTo(DEFAULT_THRITY_PRECENT_LIMIT);
    }

    @Test
    @Transactional
    void createBalanceWithExistingId() throws Exception {
        // Create the Balance with an existing ID
        balance.setId(1L);

        int databaseSizeBeforeCreate = balanceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBalanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(balance)))
            .andExpect(status().isBadRequest());

        // Validate the Balance in the database
        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBalances() throws Exception {
        // Initialize the database
        balanceRepository.saveAndFlush(balance);

        // Get all the balanceList
        restBalanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(balance.getId().intValue())))
            .andExpect(jsonPath("$.[*].creditCardType").value(hasItem(DEFAULT_CREDIT_CARD_TYPE)))
            .andExpect(jsonPath("$.[*].creditcardNum").value(hasItem(DEFAULT_CREDITCARD_NUM)))
            .andExpect(jsonPath("$.[*].vaildThru").value(hasItem(DEFAULT_VAILD_THRU.toString())))
            .andExpect(jsonPath("$.[*].cvs").value(hasItem(DEFAULT_CVS)))
            .andExpect(jsonPath("$.[*].maxLimit").value(hasItem(DEFAULT_MAX_LIMIT.doubleValue())))
            .andExpect(jsonPath("$.[*].thrityPrecentLimit").value(hasItem(DEFAULT_THRITY_PRECENT_LIMIT.doubleValue())));
    }

    @Test
    @Transactional
    void getBalance() throws Exception {
        // Initialize the database
        balanceRepository.saveAndFlush(balance);

        // Get the balance
        restBalanceMockMvc
            .perform(get(ENTITY_API_URL_ID, balance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(balance.getId().intValue()))
            .andExpect(jsonPath("$.creditCardType").value(DEFAULT_CREDIT_CARD_TYPE))
            .andExpect(jsonPath("$.creditcardNum").value(DEFAULT_CREDITCARD_NUM))
            .andExpect(jsonPath("$.vaildThru").value(DEFAULT_VAILD_THRU.toString()))
            .andExpect(jsonPath("$.cvs").value(DEFAULT_CVS))
            .andExpect(jsonPath("$.maxLimit").value(DEFAULT_MAX_LIMIT.doubleValue()))
            .andExpect(jsonPath("$.thrityPrecentLimit").value(DEFAULT_THRITY_PRECENT_LIMIT.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingBalance() throws Exception {
        // Get the balance
        restBalanceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBalance() throws Exception {
        // Initialize the database
        balanceRepository.saveAndFlush(balance);

        int databaseSizeBeforeUpdate = balanceRepository.findAll().size();

        // Update the balance
        Balance updatedBalance = balanceRepository.findById(balance.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBalance are not directly saved in db
        em.detach(updatedBalance);
        updatedBalance
            .creditCardType(UPDATED_CREDIT_CARD_TYPE)
            .creditcardNum(UPDATED_CREDITCARD_NUM)
            .vaildThru(UPDATED_VAILD_THRU)
            .cvs(UPDATED_CVS)
            .maxLimit(UPDATED_MAX_LIMIT)
            .thrityPrecentLimit(UPDATED_THRITY_PRECENT_LIMIT);

        restBalanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBalance.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBalance))
            )
            .andExpect(status().isOk());

        // Validate the Balance in the database
        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeUpdate);
        Balance testBalance = balanceList.get(balanceList.size() - 1);
        assertThat(testBalance.getCreditCardType()).isEqualTo(UPDATED_CREDIT_CARD_TYPE);
        assertThat(testBalance.getCreditcardNum()).isEqualTo(UPDATED_CREDITCARD_NUM);
        assertThat(testBalance.getVaildThru()).isEqualTo(UPDATED_VAILD_THRU);
        assertThat(testBalance.getCvs()).isEqualTo(UPDATED_CVS);
        assertThat(testBalance.getMaxLimit()).isEqualTo(UPDATED_MAX_LIMIT);
        assertThat(testBalance.getThrityPrecentLimit()).isEqualTo(UPDATED_THRITY_PRECENT_LIMIT);
    }

    @Test
    @Transactional
    void putNonExistingBalance() throws Exception {
        int databaseSizeBeforeUpdate = balanceRepository.findAll().size();
        balance.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBalanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, balance.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(balance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Balance in the database
        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBalance() throws Exception {
        int databaseSizeBeforeUpdate = balanceRepository.findAll().size();
        balance.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBalanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(balance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Balance in the database
        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBalance() throws Exception {
        int databaseSizeBeforeUpdate = balanceRepository.findAll().size();
        balance.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBalanceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(balance)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Balance in the database
        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBalanceWithPatch() throws Exception {
        // Initialize the database
        balanceRepository.saveAndFlush(balance);

        int databaseSizeBeforeUpdate = balanceRepository.findAll().size();

        // Update the balance using partial update
        Balance partialUpdatedBalance = new Balance();
        partialUpdatedBalance.setId(balance.getId());

        partialUpdatedBalance.creditcardNum(UPDATED_CREDITCARD_NUM).cvs(UPDATED_CVS).thrityPrecentLimit(UPDATED_THRITY_PRECENT_LIMIT);

        restBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBalance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBalance))
            )
            .andExpect(status().isOk());

        // Validate the Balance in the database
        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeUpdate);
        Balance testBalance = balanceList.get(balanceList.size() - 1);
        assertThat(testBalance.getCreditCardType()).isEqualTo(DEFAULT_CREDIT_CARD_TYPE);
        assertThat(testBalance.getCreditcardNum()).isEqualTo(UPDATED_CREDITCARD_NUM);
        assertThat(testBalance.getVaildThru()).isEqualTo(DEFAULT_VAILD_THRU);
        assertThat(testBalance.getCvs()).isEqualTo(UPDATED_CVS);
        assertThat(testBalance.getMaxLimit()).isEqualTo(DEFAULT_MAX_LIMIT);
        assertThat(testBalance.getThrityPrecentLimit()).isEqualTo(UPDATED_THRITY_PRECENT_LIMIT);
    }

    @Test
    @Transactional
    void fullUpdateBalanceWithPatch() throws Exception {
        // Initialize the database
        balanceRepository.saveAndFlush(balance);

        int databaseSizeBeforeUpdate = balanceRepository.findAll().size();

        // Update the balance using partial update
        Balance partialUpdatedBalance = new Balance();
        partialUpdatedBalance.setId(balance.getId());

        partialUpdatedBalance
            .creditCardType(UPDATED_CREDIT_CARD_TYPE)
            .creditcardNum(UPDATED_CREDITCARD_NUM)
            .vaildThru(UPDATED_VAILD_THRU)
            .cvs(UPDATED_CVS)
            .maxLimit(UPDATED_MAX_LIMIT)
            .thrityPrecentLimit(UPDATED_THRITY_PRECENT_LIMIT);

        restBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBalance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBalance))
            )
            .andExpect(status().isOk());

        // Validate the Balance in the database
        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeUpdate);
        Balance testBalance = balanceList.get(balanceList.size() - 1);
        assertThat(testBalance.getCreditCardType()).isEqualTo(UPDATED_CREDIT_CARD_TYPE);
        assertThat(testBalance.getCreditcardNum()).isEqualTo(UPDATED_CREDITCARD_NUM);
        assertThat(testBalance.getVaildThru()).isEqualTo(UPDATED_VAILD_THRU);
        assertThat(testBalance.getCvs()).isEqualTo(UPDATED_CVS);
        assertThat(testBalance.getMaxLimit()).isEqualTo(UPDATED_MAX_LIMIT);
        assertThat(testBalance.getThrityPrecentLimit()).isEqualTo(UPDATED_THRITY_PRECENT_LIMIT);
    }

    @Test
    @Transactional
    void patchNonExistingBalance() throws Exception {
        int databaseSizeBeforeUpdate = balanceRepository.findAll().size();
        balance.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, balance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(balance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Balance in the database
        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBalance() throws Exception {
        int databaseSizeBeforeUpdate = balanceRepository.findAll().size();
        balance.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(balance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Balance in the database
        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBalance() throws Exception {
        int databaseSizeBeforeUpdate = balanceRepository.findAll().size();
        balance.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBalanceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(balance)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Balance in the database
        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBalance() throws Exception {
        // Initialize the database
        balanceRepository.saveAndFlush(balance);

        int databaseSizeBeforeDelete = balanceRepository.findAll().size();

        // Delete the balance
        restBalanceMockMvc
            .perform(delete(ENTITY_API_URL_ID, balance.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
