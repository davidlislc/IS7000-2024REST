package org.is70002024.market.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.is70002024.market.domain.BatchAsserts.*;
import static org.is70002024.market.web.rest.TestUtil.createUpdateProxyForBean;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.is70002024.market.IntegrationTest;
import org.is70002024.market.domain.Batch;
import org.is70002024.market.domain.enumeration.Status;
import org.is70002024.market.repository.BatchRepository;
import org.is70002024.market.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
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
 * Integration tests for the {@link BatchResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BatchResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_JOB = "AAAAAAAAAA";
    private static final String UPDATED_JOB = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_RUNDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RUNDATE = LocalDate.now(ZoneId.systemDefault());

    private static final Status DEFAULT_BATCHSTATUS = Status.PENDING;
    private static final Status UPDATED_BATCHSTATUS = Status.RUNNING;

    private static final String ENTITY_API_URL = "/api/batches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private BatchRepository batchRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBatchMockMvc;

    private Batch batch;

    private Batch insertedBatch;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Batch createEntity() {
        return new Batch().name(DEFAULT_NAME).job(DEFAULT_JOB).rundate(DEFAULT_RUNDATE).batchstatus(DEFAULT_BATCHSTATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Batch createUpdatedEntity() {
        return new Batch().name(UPDATED_NAME).job(UPDATED_JOB).rundate(UPDATED_RUNDATE).batchstatus(UPDATED_BATCHSTATUS);
    }

    @BeforeEach
    public void initTest() {
        batch = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedBatch != null) {
            batchRepository.delete(insertedBatch);
            insertedBatch = null;
        }
    }

    @Test
    @Transactional
    void createBatch() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Batch
        var returnedBatch = om.readValue(
            restBatchMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(batch)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Batch.class
        );

        // Validate the Batch in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertBatchUpdatableFieldsEquals(returnedBatch, getPersistedBatch(returnedBatch));

        insertedBatch = returnedBatch;
    }

    @Test
    @Transactional
    void createBatchWithExistingId() throws Exception {
        // Create the Batch with an existing ID
        batch.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(batch)))
            .andExpect(status().isBadRequest());

        // Validate the Batch in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        batch.setName(null);

        // Create the Batch, which fails.

        restBatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(batch)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkJobIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        batch.setJob(null);

        // Create the Batch, which fails.

        restBatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(batch)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRundateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        batch.setRundate(null);

        // Create the Batch, which fails.

        restBatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(batch)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBatchstatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        batch.setBatchstatus(null);

        // Create the Batch, which fails.

        restBatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(batch)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBatches() throws Exception {
        // Initialize the database
        insertedBatch = batchRepository.saveAndFlush(batch);

        // Get all the batchList
        restBatchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(batch.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].job").value(hasItem(DEFAULT_JOB)))
            .andExpect(jsonPath("$.[*].rundate").value(hasItem(DEFAULT_RUNDATE.toString())))
            .andExpect(jsonPath("$.[*].batchstatus").value(hasItem(DEFAULT_BATCHSTATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBatchesWithEagerRelationshipsIsEnabled() throws Exception {
        when(batchRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBatchMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(batchRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBatchesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(batchRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBatchMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(batchRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getBatch() throws Exception {
        // Initialize the database
        insertedBatch = batchRepository.saveAndFlush(batch);

        // Get the batch
        restBatchMockMvc
            .perform(get(ENTITY_API_URL_ID, batch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(batch.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.job").value(DEFAULT_JOB))
            .andExpect(jsonPath("$.rundate").value(DEFAULT_RUNDATE.toString()))
            .andExpect(jsonPath("$.batchstatus").value(DEFAULT_BATCHSTATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingBatch() throws Exception {
        // Get the batch
        restBatchMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBatch() throws Exception {
        // Initialize the database
        insertedBatch = batchRepository.saveAndFlush(batch);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the batch
        Batch updatedBatch = batchRepository.findById(batch.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBatch are not directly saved in db
        em.detach(updatedBatch);
        updatedBatch.name(UPDATED_NAME).job(UPDATED_JOB).rundate(UPDATED_RUNDATE).batchstatus(UPDATED_BATCHSTATUS);

        restBatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBatch.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedBatch))
            )
            .andExpect(status().isOk());

        // Validate the Batch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBatchToMatchAllProperties(updatedBatch);
    }

    @Test
    @Transactional
    void putNonExistingBatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        batch.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBatchMockMvc
            .perform(put(ENTITY_API_URL_ID, batch.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(batch)))
            .andExpect(status().isBadRequest());

        // Validate the Batch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        batch.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(batch))
            )
            .andExpect(status().isBadRequest());

        // Validate the Batch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        batch.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBatchMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(batch)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Batch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBatchWithPatch() throws Exception {
        // Initialize the database
        insertedBatch = batchRepository.saveAndFlush(batch);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the batch using partial update
        Batch partialUpdatedBatch = new Batch();
        partialUpdatedBatch.setId(batch.getId());

        partialUpdatedBatch.job(UPDATED_JOB).rundate(UPDATED_RUNDATE).batchstatus(UPDATED_BATCHSTATUS);

        restBatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBatch.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBatch))
            )
            .andExpect(status().isOk());

        // Validate the Batch in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBatchUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedBatch, batch), getPersistedBatch(batch));
    }

    @Test
    @Transactional
    void fullUpdateBatchWithPatch() throws Exception {
        // Initialize the database
        insertedBatch = batchRepository.saveAndFlush(batch);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the batch using partial update
        Batch partialUpdatedBatch = new Batch();
        partialUpdatedBatch.setId(batch.getId());

        partialUpdatedBatch.name(UPDATED_NAME).job(UPDATED_JOB).rundate(UPDATED_RUNDATE).batchstatus(UPDATED_BATCHSTATUS);

        restBatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBatch.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBatch))
            )
            .andExpect(status().isOk());

        // Validate the Batch in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBatchUpdatableFieldsEquals(partialUpdatedBatch, getPersistedBatch(partialUpdatedBatch));
    }

    @Test
    @Transactional
    void patchNonExistingBatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        batch.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, batch.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(batch))
            )
            .andExpect(status().isBadRequest());

        // Validate the Batch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        batch.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(batch))
            )
            .andExpect(status().isBadRequest());

        // Validate the Batch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        batch.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBatchMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(batch)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Batch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBatch() throws Exception {
        // Initialize the database
        insertedBatch = batchRepository.saveAndFlush(batch);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the batch
        restBatchMockMvc
            .perform(delete(ENTITY_API_URL_ID, batch.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return batchRepository.count();
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

    protected Batch getPersistedBatch(Batch batch) {
        return batchRepository.findById(batch.getId()).orElseThrow();
    }

    protected void assertPersistedBatchToMatchAllProperties(Batch expectedBatch) {
        assertBatchAllPropertiesEquals(expectedBatch, getPersistedBatch(expectedBatch));
    }

    protected void assertPersistedBatchToMatchUpdatableProperties(Batch expectedBatch) {
        assertBatchAllUpdatablePropertiesEquals(expectedBatch, getPersistedBatch(expectedBatch));
    }
}
