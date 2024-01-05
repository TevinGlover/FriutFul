package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Achivement;
import com.mycompany.myapp.repository.AchivementRepository;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link AchivementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AchivementResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_POINT_VALUE = 1;
    private static final Integer UPDATED_POINT_VALUE = 2;

    private static final String ENTITY_API_URL = "/api/achivements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AchivementRepository achivementRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAchivementMockMvc;

    private Achivement achivement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Achivement createEntity(EntityManager em) {
        Achivement achivement = new Achivement().name(DEFAULT_NAME).pointValue(DEFAULT_POINT_VALUE);
        return achivement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Achivement createUpdatedEntity(EntityManager em) {
        Achivement achivement = new Achivement().name(UPDATED_NAME).pointValue(UPDATED_POINT_VALUE);
        return achivement;
    }

    @BeforeEach
    public void initTest() {
        achivement = createEntity(em);
    }

    @Test
    @Transactional
    void createAchivement() throws Exception {
        int databaseSizeBeforeCreate = achivementRepository.findAll().size();
        // Create the Achivement
        restAchivementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(achivement)))
            .andExpect(status().isCreated());

        // Validate the Achivement in the database
        List<Achivement> achivementList = achivementRepository.findAll();
        assertThat(achivementList).hasSize(databaseSizeBeforeCreate + 1);
        Achivement testAchivement = achivementList.get(achivementList.size() - 1);
        assertThat(testAchivement.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAchivement.getPointValue()).isEqualTo(DEFAULT_POINT_VALUE);
    }

    @Test
    @Transactional
    void createAchivementWithExistingId() throws Exception {
        // Create the Achivement with an existing ID
        achivement.setId(1L);

        int databaseSizeBeforeCreate = achivementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAchivementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(achivement)))
            .andExpect(status().isBadRequest());

        // Validate the Achivement in the database
        List<Achivement> achivementList = achivementRepository.findAll();
        assertThat(achivementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAchivements() throws Exception {
        // Initialize the database
        achivementRepository.saveAndFlush(achivement);

        // Get all the achivementList
        restAchivementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(achivement.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].pointValue").value(hasItem(DEFAULT_POINT_VALUE)));
    }

    @Test
    @Transactional
    void getAchivement() throws Exception {
        // Initialize the database
        achivementRepository.saveAndFlush(achivement);

        // Get the achivement
        restAchivementMockMvc
            .perform(get(ENTITY_API_URL_ID, achivement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(achivement.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.pointValue").value(DEFAULT_POINT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingAchivement() throws Exception {
        // Get the achivement
        restAchivementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAchivement() throws Exception {
        // Initialize the database
        achivementRepository.saveAndFlush(achivement);

        int databaseSizeBeforeUpdate = achivementRepository.findAll().size();

        // Update the achivement
        Achivement updatedAchivement = achivementRepository.findById(achivement.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAchivement are not directly saved in db
        em.detach(updatedAchivement);
        updatedAchivement.name(UPDATED_NAME).pointValue(UPDATED_POINT_VALUE);

        restAchivementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAchivement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAchivement))
            )
            .andExpect(status().isOk());

        // Validate the Achivement in the database
        List<Achivement> achivementList = achivementRepository.findAll();
        assertThat(achivementList).hasSize(databaseSizeBeforeUpdate);
        Achivement testAchivement = achivementList.get(achivementList.size() - 1);
        assertThat(testAchivement.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAchivement.getPointValue()).isEqualTo(UPDATED_POINT_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingAchivement() throws Exception {
        int databaseSizeBeforeUpdate = achivementRepository.findAll().size();
        achivement.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAchivementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, achivement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(achivement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Achivement in the database
        List<Achivement> achivementList = achivementRepository.findAll();
        assertThat(achivementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAchivement() throws Exception {
        int databaseSizeBeforeUpdate = achivementRepository.findAll().size();
        achivement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAchivementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(achivement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Achivement in the database
        List<Achivement> achivementList = achivementRepository.findAll();
        assertThat(achivementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAchivement() throws Exception {
        int databaseSizeBeforeUpdate = achivementRepository.findAll().size();
        achivement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAchivementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(achivement)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Achivement in the database
        List<Achivement> achivementList = achivementRepository.findAll();
        assertThat(achivementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAchivementWithPatch() throws Exception {
        // Initialize the database
        achivementRepository.saveAndFlush(achivement);

        int databaseSizeBeforeUpdate = achivementRepository.findAll().size();

        // Update the achivement using partial update
        Achivement partialUpdatedAchivement = new Achivement();
        partialUpdatedAchivement.setId(achivement.getId());

        partialUpdatedAchivement.name(UPDATED_NAME).pointValue(UPDATED_POINT_VALUE);

        restAchivementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAchivement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAchivement))
            )
            .andExpect(status().isOk());

        // Validate the Achivement in the database
        List<Achivement> achivementList = achivementRepository.findAll();
        assertThat(achivementList).hasSize(databaseSizeBeforeUpdate);
        Achivement testAchivement = achivementList.get(achivementList.size() - 1);
        assertThat(testAchivement.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAchivement.getPointValue()).isEqualTo(UPDATED_POINT_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateAchivementWithPatch() throws Exception {
        // Initialize the database
        achivementRepository.saveAndFlush(achivement);

        int databaseSizeBeforeUpdate = achivementRepository.findAll().size();

        // Update the achivement using partial update
        Achivement partialUpdatedAchivement = new Achivement();
        partialUpdatedAchivement.setId(achivement.getId());

        partialUpdatedAchivement.name(UPDATED_NAME).pointValue(UPDATED_POINT_VALUE);

        restAchivementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAchivement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAchivement))
            )
            .andExpect(status().isOk());

        // Validate the Achivement in the database
        List<Achivement> achivementList = achivementRepository.findAll();
        assertThat(achivementList).hasSize(databaseSizeBeforeUpdate);
        Achivement testAchivement = achivementList.get(achivementList.size() - 1);
        assertThat(testAchivement.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAchivement.getPointValue()).isEqualTo(UPDATED_POINT_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingAchivement() throws Exception {
        int databaseSizeBeforeUpdate = achivementRepository.findAll().size();
        achivement.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAchivementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, achivement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(achivement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Achivement in the database
        List<Achivement> achivementList = achivementRepository.findAll();
        assertThat(achivementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAchivement() throws Exception {
        int databaseSizeBeforeUpdate = achivementRepository.findAll().size();
        achivement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAchivementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(achivement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Achivement in the database
        List<Achivement> achivementList = achivementRepository.findAll();
        assertThat(achivementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAchivement() throws Exception {
        int databaseSizeBeforeUpdate = achivementRepository.findAll().size();
        achivement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAchivementMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(achivement))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Achivement in the database
        List<Achivement> achivementList = achivementRepository.findAll();
        assertThat(achivementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAchivement() throws Exception {
        // Initialize the database
        achivementRepository.saveAndFlush(achivement);

        int databaseSizeBeforeDelete = achivementRepository.findAll().size();

        // Delete the achivement
        restAchivementMockMvc
            .perform(delete(ENTITY_API_URL_ID, achivement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Achivement> achivementList = achivementRepository.findAll();
        assertThat(achivementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
