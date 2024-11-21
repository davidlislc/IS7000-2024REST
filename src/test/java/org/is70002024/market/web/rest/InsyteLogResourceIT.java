package org.is70002024.market.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.is70002024.market.domain.InsyteLogAsserts.*;
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
import org.is70002024.market.domain.InsyteLog;
import org.is70002024.market.repository.InsyteLogRepository;
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
 * Integration tests for the {@link InsyteLogResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InsyteLogResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ACTIVITY = "AAAAAAAAAA";
    private static final String UPDATED_ACTIVITY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_RUNDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RUNDATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/insyte-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InsyteLogRepository insyteLogRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private InsyteLogRepository insyteLogRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInsyteLogMockMvc;

    private InsyteLog insyteLog;

    private InsyteLog insertedInsyteLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsyteLog createEntity() {
        return new InsyteLog().name(DEFAULT_NAME).activity(DEFAULT_ACTIVITY).rundate(DEFAULT_RUNDATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsyteLog createUpdatedEntity() {
        return new InsyteLog().name(UPDATED_NAME).activity(UPDATED_ACTIVITY).rundate(UPDATED_RUNDATE);
    }

    @BeforeEach
    public void initTest() {
        insyteLog = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedInsyteLog != null) {
            insyteLogRepository.delete(insertedInsyteLog);
            insertedInsyteLog = null;
        }
    }

    @Test
    @Transactional
    void createInsyteLog() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InsyteLog
        var returnedInsyteLog = om.readValue(
            restInsyteLogMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insyteLog)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InsyteLog.class
        );

        // Validate the InsyteLog in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInsyteLogUpdatableFieldsEquals(returnedInsyteLog, getPersistedInsyteLog(returnedInsyteLog));

        insertedInsyteLog = returnedInsyteLog;
    }

    @Test
    @Transactional
    void createInsyteLogWithExistingId() throws Exception {
        // Create the InsyteLog with an existing ID
        insyteLog.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInsyteLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insyteLog)))
            .andExpect(status().isBadRequest());

        // Validate the InsyteLog in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        insyteLog.setName(null);

        // Create the InsyteLog, which fails.

        restInsyteLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insyteLog)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        insyteLog.setActivity(null);

        // Create the InsyteLog, which fails.

        restInsyteLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insyteLog)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRundateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        insyteLog.setRundate(null);

        // Create the InsyteLog, which fails.

        restInsyteLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insyteLog)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInsyteLogs() throws Exception {
        // Initialize the database
        insertedInsyteLog = insyteLogRepository.saveAndFlush(insyteLog);

        // Get all the insyteLogList
        restInsyteLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insyteLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].activity").value(hasItem(DEFAULT_ACTIVITY)))
            .andExpect(jsonPath("$.[*].rundate").value(hasItem(DEFAULT_RUNDATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInsyteLogsWithEagerRelationshipsIsEnabled() throws Exception {
        when(insyteLogRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInsyteLogMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(insyteLogRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInsyteLogsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(insyteLogRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInsyteLogMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(insyteLogRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getInsyteLog() throws Exception {
        // Initialize the database
        insertedInsyteLog = insyteLogRepository.saveAndFlush(insyteLog);

        // Get the insyteLog
        restInsyteLogMockMvc
            .perform(get(ENTITY_API_URL_ID, insyteLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(insyteLog.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.activity").value(DEFAULT_ACTIVITY))
            .andExpect(jsonPath("$.rundate").value(DEFAULT_RUNDATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingInsyteLog() throws Exception {
        // Get the insyteLog
        restInsyteLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInsyteLog() throws Exception {
        // Initialize the database
        insertedInsyteLog = insyteLogRepository.saveAndFlush(insyteLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insyteLog
        InsyteLog updatedInsyteLog = insyteLogRepository.findById(insyteLog.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInsyteLog are not directly saved in db
        em.detach(updatedInsyteLog);
        updatedInsyteLog.name(UPDATED_NAME).activity(UPDATED_ACTIVITY).rundate(UPDATED_RUNDATE);

        restInsyteLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInsyteLog.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInsyteLog))
            )
            .andExpect(status().isOk());

        // Validate the InsyteLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInsyteLogToMatchAllProperties(updatedInsyteLog);
    }

    @Test
    @Transactional
    void putNonExistingInsyteLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insyteLog.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsyteLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, insyteLog.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insyteLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsyteLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInsyteLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insyteLog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsyteLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insyteLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsyteLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInsyteLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insyteLog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsyteLogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insyteLog)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsyteLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInsyteLogWithPatch() throws Exception {
        // Initialize the database
        insertedInsyteLog = insyteLogRepository.saveAndFlush(insyteLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insyteLog using partial update
        InsyteLog partialUpdatedInsyteLog = new InsyteLog();
        partialUpdatedInsyteLog.setId(insyteLog.getId());

        partialUpdatedInsyteLog.name(UPDATED_NAME).activity(UPDATED_ACTIVITY).rundate(UPDATED_RUNDATE);

        restInsyteLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsyteLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInsyteLog))
            )
            .andExpect(status().isOk());

        // Validate the InsyteLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInsyteLogUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInsyteLog, insyteLog),
            getPersistedInsyteLog(insyteLog)
        );
    }

    @Test
    @Transactional
    void fullUpdateInsyteLogWithPatch() throws Exception {
        // Initialize the database
        insertedInsyteLog = insyteLogRepository.saveAndFlush(insyteLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insyteLog using partial update
        InsyteLog partialUpdatedInsyteLog = new InsyteLog();
        partialUpdatedInsyteLog.setId(insyteLog.getId());

        partialUpdatedInsyteLog.name(UPDATED_NAME).activity(UPDATED_ACTIVITY).rundate(UPDATED_RUNDATE);

        restInsyteLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsyteLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInsyteLog))
            )
            .andExpect(status().isOk());

        // Validate the InsyteLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInsyteLogUpdatableFieldsEquals(partialUpdatedInsyteLog, getPersistedInsyteLog(partialUpdatedInsyteLog));
    }

    @Test
    @Transactional
    void patchNonExistingInsyteLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insyteLog.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsyteLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, insyteLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insyteLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsyteLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInsyteLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insyteLog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsyteLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insyteLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsyteLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInsyteLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insyteLog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsyteLogMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(insyteLog)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsyteLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInsyteLog() throws Exception {
        // Initialize the database
        insertedInsyteLog = insyteLogRepository.saveAndFlush(insyteLog);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the insyteLog
        restInsyteLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, insyteLog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return insyteLogRepository.count();
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

    protected InsyteLog getPersistedInsyteLog(InsyteLog insyteLog) {
        return insyteLogRepository.findById(insyteLog.getId()).orElseThrow();
    }

    protected void assertPersistedInsyteLogToMatchAllProperties(InsyteLog expectedInsyteLog) {
        assertInsyteLogAllPropertiesEquals(expectedInsyteLog, getPersistedInsyteLog(expectedInsyteLog));
    }

    protected void assertPersistedInsyteLogToMatchUpdatableProperties(InsyteLog expectedInsyteLog) {
        assertInsyteLogAllUpdatablePropertiesEquals(expectedInsyteLog, getPersistedInsyteLog(expectedInsyteLog));
    }
}
