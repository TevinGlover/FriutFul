package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Parents;
import com.mycompany.myapp.repository.ParentsRepository;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ParentsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ParentsResourceIT {

    private static final String DEFAULT_PARENTS_FRIST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PARENTS_FRIST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PARENTS_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PARENTS_LAST_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/parents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ParentsRepository parentsRepository;

    @Mock
    private ParentsRepository parentsRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParentsMockMvc;

    private Parents parents;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parents createEntity(EntityManager em) {
        Parents parents = new Parents().parentsFristName(DEFAULT_PARENTS_FRIST_NAME).parentsLastName(DEFAULT_PARENTS_LAST_NAME);
        return parents;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parents createUpdatedEntity(EntityManager em) {
        Parents parents = new Parents().parentsFristName(UPDATED_PARENTS_FRIST_NAME).parentsLastName(UPDATED_PARENTS_LAST_NAME);
        return parents;
    }

    @BeforeEach
    public void initTest() {
        parents = createEntity(em);
    }

    @Test
    @Transactional
    void createParents() throws Exception {
        int databaseSizeBeforeCreate = parentsRepository.findAll().size();
        // Create the Parents
        restParentsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parents)))
            .andExpect(status().isCreated());

        // Validate the Parents in the database
        List<Parents> parentsList = parentsRepository.findAll();
        assertThat(parentsList).hasSize(databaseSizeBeforeCreate + 1);
        Parents testParents = parentsList.get(parentsList.size() - 1);
        assertThat(testParents.getParentsFristName()).isEqualTo(DEFAULT_PARENTS_FRIST_NAME);
        assertThat(testParents.getParentsLastName()).isEqualTo(DEFAULT_PARENTS_LAST_NAME);
    }

    @Test
    @Transactional
    void createParentsWithExistingId() throws Exception {
        // Create the Parents with an existing ID
        parents.setId(1L);

        int databaseSizeBeforeCreate = parentsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restParentsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parents)))
            .andExpect(status().isBadRequest());

        // Validate the Parents in the database
        List<Parents> parentsList = parentsRepository.findAll();
        assertThat(parentsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllParents() throws Exception {
        // Initialize the database
        parentsRepository.saveAndFlush(parents);

        // Get all the parentsList
        restParentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parents.getId().intValue())))
            .andExpect(jsonPath("$.[*].parentsFristName").value(hasItem(DEFAULT_PARENTS_FRIST_NAME)))
            .andExpect(jsonPath("$.[*].parentsLastName").value(hasItem(DEFAULT_PARENTS_LAST_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllParentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(parentsRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restParentsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(parentsRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllParentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(parentsRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restParentsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(parentsRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getParents() throws Exception {
        // Initialize the database
        parentsRepository.saveAndFlush(parents);

        // Get the parents
        restParentsMockMvc
            .perform(get(ENTITY_API_URL_ID, parents.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(parents.getId().intValue()))
            .andExpect(jsonPath("$.parentsFristName").value(DEFAULT_PARENTS_FRIST_NAME))
            .andExpect(jsonPath("$.parentsLastName").value(DEFAULT_PARENTS_LAST_NAME));
    }

    @Test
    @Transactional
    void getNonExistingParents() throws Exception {
        // Get the parents
        restParentsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingParents() throws Exception {
        // Initialize the database
        parentsRepository.saveAndFlush(parents);

        int databaseSizeBeforeUpdate = parentsRepository.findAll().size();

        // Update the parents
        Parents updatedParents = parentsRepository.findById(parents.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedParents are not directly saved in db
        em.detach(updatedParents);
        updatedParents.parentsFristName(UPDATED_PARENTS_FRIST_NAME).parentsLastName(UPDATED_PARENTS_LAST_NAME);

        restParentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedParents.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedParents))
            )
            .andExpect(status().isOk());

        // Validate the Parents in the database
        List<Parents> parentsList = parentsRepository.findAll();
        assertThat(parentsList).hasSize(databaseSizeBeforeUpdate);
        Parents testParents = parentsList.get(parentsList.size() - 1);
        assertThat(testParents.getParentsFristName()).isEqualTo(UPDATED_PARENTS_FRIST_NAME);
        assertThat(testParents.getParentsLastName()).isEqualTo(UPDATED_PARENTS_LAST_NAME);
    }

    @Test
    @Transactional
    void putNonExistingParents() throws Exception {
        int databaseSizeBeforeUpdate = parentsRepository.findAll().size();
        parents.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, parents.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parents))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parents in the database
        List<Parents> parentsList = parentsRepository.findAll();
        assertThat(parentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchParents() throws Exception {
        int databaseSizeBeforeUpdate = parentsRepository.findAll().size();
        parents.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parents))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parents in the database
        List<Parents> parentsList = parentsRepository.findAll();
        assertThat(parentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamParents() throws Exception {
        int databaseSizeBeforeUpdate = parentsRepository.findAll().size();
        parents.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParentsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parents)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Parents in the database
        List<Parents> parentsList = parentsRepository.findAll();
        assertThat(parentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateParentsWithPatch() throws Exception {
        // Initialize the database
        parentsRepository.saveAndFlush(parents);

        int databaseSizeBeforeUpdate = parentsRepository.findAll().size();

        // Update the parents using partial update
        Parents partialUpdatedParents = new Parents();
        partialUpdatedParents.setId(parents.getId());

        partialUpdatedParents.parentsLastName(UPDATED_PARENTS_LAST_NAME);

        restParentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParents.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParents))
            )
            .andExpect(status().isOk());

        // Validate the Parents in the database
        List<Parents> parentsList = parentsRepository.findAll();
        assertThat(parentsList).hasSize(databaseSizeBeforeUpdate);
        Parents testParents = parentsList.get(parentsList.size() - 1);
        assertThat(testParents.getParentsFristName()).isEqualTo(DEFAULT_PARENTS_FRIST_NAME);
        assertThat(testParents.getParentsLastName()).isEqualTo(UPDATED_PARENTS_LAST_NAME);
    }

    @Test
    @Transactional
    void fullUpdateParentsWithPatch() throws Exception {
        // Initialize the database
        parentsRepository.saveAndFlush(parents);

        int databaseSizeBeforeUpdate = parentsRepository.findAll().size();

        // Update the parents using partial update
        Parents partialUpdatedParents = new Parents();
        partialUpdatedParents.setId(parents.getId());

        partialUpdatedParents.parentsFristName(UPDATED_PARENTS_FRIST_NAME).parentsLastName(UPDATED_PARENTS_LAST_NAME);

        restParentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParents.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParents))
            )
            .andExpect(status().isOk());

        // Validate the Parents in the database
        List<Parents> parentsList = parentsRepository.findAll();
        assertThat(parentsList).hasSize(databaseSizeBeforeUpdate);
        Parents testParents = parentsList.get(parentsList.size() - 1);
        assertThat(testParents.getParentsFristName()).isEqualTo(UPDATED_PARENTS_FRIST_NAME);
        assertThat(testParents.getParentsLastName()).isEqualTo(UPDATED_PARENTS_LAST_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingParents() throws Exception {
        int databaseSizeBeforeUpdate = parentsRepository.findAll().size();
        parents.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, parents.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parents))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parents in the database
        List<Parents> parentsList = parentsRepository.findAll();
        assertThat(parentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchParents() throws Exception {
        int databaseSizeBeforeUpdate = parentsRepository.findAll().size();
        parents.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parents))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parents in the database
        List<Parents> parentsList = parentsRepository.findAll();
        assertThat(parentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamParents() throws Exception {
        int databaseSizeBeforeUpdate = parentsRepository.findAll().size();
        parents.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParentsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(parents)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Parents in the database
        List<Parents> parentsList = parentsRepository.findAll();
        assertThat(parentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteParents() throws Exception {
        // Initialize the database
        parentsRepository.saveAndFlush(parents);

        int databaseSizeBeforeDelete = parentsRepository.findAll().size();

        // Delete the parents
        restParentsMockMvc
            .perform(delete(ENTITY_API_URL_ID, parents.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Parents> parentsList = parentsRepository.findAll();
        assertThat(parentsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
