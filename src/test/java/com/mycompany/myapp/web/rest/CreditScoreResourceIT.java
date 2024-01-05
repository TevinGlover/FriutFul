package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.CreditScore;
import com.mycompany.myapp.repository.CreditScoreRepository;
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
 * Integration tests for the {@link CreditScoreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CreditScoreResourceIT {

    private static final Instant DEFAULT_MONTH = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MONTH = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_SCORE_NUMBER = 1;
    private static final Integer UPDATED_SCORE_NUMBER = 2;

    private static final String ENTITY_API_URL = "/api/credit-scores";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CreditScoreRepository creditScoreRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCreditScoreMockMvc;

    private CreditScore creditScore;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CreditScore createEntity(EntityManager em) {
        CreditScore creditScore = new CreditScore().month(DEFAULT_MONTH).scoreNumber(DEFAULT_SCORE_NUMBER);
        return creditScore;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CreditScore createUpdatedEntity(EntityManager em) {
        CreditScore creditScore = new CreditScore().month(UPDATED_MONTH).scoreNumber(UPDATED_SCORE_NUMBER);
        return creditScore;
    }

    @BeforeEach
    public void initTest() {
        creditScore = createEntity(em);
    }

    @Test
    @Transactional
    void createCreditScore() throws Exception {
        int databaseSizeBeforeCreate = creditScoreRepository.findAll().size();
        // Create the CreditScore
        restCreditScoreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(creditScore)))
            .andExpect(status().isCreated());

        // Validate the CreditScore in the database
        List<CreditScore> creditScoreList = creditScoreRepository.findAll();
        assertThat(creditScoreList).hasSize(databaseSizeBeforeCreate + 1);
        CreditScore testCreditScore = creditScoreList.get(creditScoreList.size() - 1);
        assertThat(testCreditScore.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testCreditScore.getScoreNumber()).isEqualTo(DEFAULT_SCORE_NUMBER);
    }

    @Test
    @Transactional
    void createCreditScoreWithExistingId() throws Exception {
        // Create the CreditScore with an existing ID
        creditScore.setId(1L);

        int databaseSizeBeforeCreate = creditScoreRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCreditScoreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(creditScore)))
            .andExpect(status().isBadRequest());

        // Validate the CreditScore in the database
        List<CreditScore> creditScoreList = creditScoreRepository.findAll();
        assertThat(creditScoreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCreditScores() throws Exception {
        // Initialize the database
        creditScoreRepository.saveAndFlush(creditScore);

        // Get all the creditScoreList
        restCreditScoreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(creditScore.getId().intValue())))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH.toString())))
            .andExpect(jsonPath("$.[*].scoreNumber").value(hasItem(DEFAULT_SCORE_NUMBER)));
    }

    @Test
    @Transactional
    void getCreditScore() throws Exception {
        // Initialize the database
        creditScoreRepository.saveAndFlush(creditScore);

        // Get the creditScore
        restCreditScoreMockMvc
            .perform(get(ENTITY_API_URL_ID, creditScore.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(creditScore.getId().intValue()))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH.toString()))
            .andExpect(jsonPath("$.scoreNumber").value(DEFAULT_SCORE_NUMBER));
    }

    @Test
    @Transactional
    void getNonExistingCreditScore() throws Exception {
        // Get the creditScore
        restCreditScoreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCreditScore() throws Exception {
        // Initialize the database
        creditScoreRepository.saveAndFlush(creditScore);

        int databaseSizeBeforeUpdate = creditScoreRepository.findAll().size();

        // Update the creditScore
        CreditScore updatedCreditScore = creditScoreRepository.findById(creditScore.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCreditScore are not directly saved in db
        em.detach(updatedCreditScore);
        updatedCreditScore.month(UPDATED_MONTH).scoreNumber(UPDATED_SCORE_NUMBER);

        restCreditScoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCreditScore.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCreditScore))
            )
            .andExpect(status().isOk());

        // Validate the CreditScore in the database
        List<CreditScore> creditScoreList = creditScoreRepository.findAll();
        assertThat(creditScoreList).hasSize(databaseSizeBeforeUpdate);
        CreditScore testCreditScore = creditScoreList.get(creditScoreList.size() - 1);
        assertThat(testCreditScore.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testCreditScore.getScoreNumber()).isEqualTo(UPDATED_SCORE_NUMBER);
    }

    @Test
    @Transactional
    void putNonExistingCreditScore() throws Exception {
        int databaseSizeBeforeUpdate = creditScoreRepository.findAll().size();
        creditScore.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCreditScoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, creditScore.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(creditScore))
            )
            .andExpect(status().isBadRequest());

        // Validate the CreditScore in the database
        List<CreditScore> creditScoreList = creditScoreRepository.findAll();
        assertThat(creditScoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCreditScore() throws Exception {
        int databaseSizeBeforeUpdate = creditScoreRepository.findAll().size();
        creditScore.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCreditScoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(creditScore))
            )
            .andExpect(status().isBadRequest());

        // Validate the CreditScore in the database
        List<CreditScore> creditScoreList = creditScoreRepository.findAll();
        assertThat(creditScoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCreditScore() throws Exception {
        int databaseSizeBeforeUpdate = creditScoreRepository.findAll().size();
        creditScore.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCreditScoreMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(creditScore)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CreditScore in the database
        List<CreditScore> creditScoreList = creditScoreRepository.findAll();
        assertThat(creditScoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCreditScoreWithPatch() throws Exception {
        // Initialize the database
        creditScoreRepository.saveAndFlush(creditScore);

        int databaseSizeBeforeUpdate = creditScoreRepository.findAll().size();

        // Update the creditScore using partial update
        CreditScore partialUpdatedCreditScore = new CreditScore();
        partialUpdatedCreditScore.setId(creditScore.getId());

        restCreditScoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCreditScore.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCreditScore))
            )
            .andExpect(status().isOk());

        // Validate the CreditScore in the database
        List<CreditScore> creditScoreList = creditScoreRepository.findAll();
        assertThat(creditScoreList).hasSize(databaseSizeBeforeUpdate);
        CreditScore testCreditScore = creditScoreList.get(creditScoreList.size() - 1);
        assertThat(testCreditScore.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testCreditScore.getScoreNumber()).isEqualTo(DEFAULT_SCORE_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdateCreditScoreWithPatch() throws Exception {
        // Initialize the database
        creditScoreRepository.saveAndFlush(creditScore);

        int databaseSizeBeforeUpdate = creditScoreRepository.findAll().size();

        // Update the creditScore using partial update
        CreditScore partialUpdatedCreditScore = new CreditScore();
        partialUpdatedCreditScore.setId(creditScore.getId());

        partialUpdatedCreditScore.month(UPDATED_MONTH).scoreNumber(UPDATED_SCORE_NUMBER);

        restCreditScoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCreditScore.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCreditScore))
            )
            .andExpect(status().isOk());

        // Validate the CreditScore in the database
        List<CreditScore> creditScoreList = creditScoreRepository.findAll();
        assertThat(creditScoreList).hasSize(databaseSizeBeforeUpdate);
        CreditScore testCreditScore = creditScoreList.get(creditScoreList.size() - 1);
        assertThat(testCreditScore.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testCreditScore.getScoreNumber()).isEqualTo(UPDATED_SCORE_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingCreditScore() throws Exception {
        int databaseSizeBeforeUpdate = creditScoreRepository.findAll().size();
        creditScore.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCreditScoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, creditScore.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(creditScore))
            )
            .andExpect(status().isBadRequest());

        // Validate the CreditScore in the database
        List<CreditScore> creditScoreList = creditScoreRepository.findAll();
        assertThat(creditScoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCreditScore() throws Exception {
        int databaseSizeBeforeUpdate = creditScoreRepository.findAll().size();
        creditScore.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCreditScoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(creditScore))
            )
            .andExpect(status().isBadRequest());

        // Validate the CreditScore in the database
        List<CreditScore> creditScoreList = creditScoreRepository.findAll();
        assertThat(creditScoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCreditScore() throws Exception {
        int databaseSizeBeforeUpdate = creditScoreRepository.findAll().size();
        creditScore.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCreditScoreMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(creditScore))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CreditScore in the database
        List<CreditScore> creditScoreList = creditScoreRepository.findAll();
        assertThat(creditScoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCreditScore() throws Exception {
        // Initialize the database
        creditScoreRepository.saveAndFlush(creditScore);

        int databaseSizeBeforeDelete = creditScoreRepository.findAll().size();

        // Delete the creditScore
        restCreditScoreMockMvc
            .perform(delete(ENTITY_API_URL_ID, creditScore.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CreditScore> creditScoreList = creditScoreRepository.findAll();
        assertThat(creditScoreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
