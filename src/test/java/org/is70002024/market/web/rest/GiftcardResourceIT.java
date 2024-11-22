package org.is70002024.market.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.is70002024.market.domain.GiftcardAsserts.*;
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
import org.is70002024.market.domain.Giftcard;
import org.is70002024.market.repository.GiftcardRepository;
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
 * Integration tests for the {@link GiftcardResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class GiftcardResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_GIFTCARDAMOUNT = 1D;
    private static final Double UPDATED_GIFTCARDAMOUNT = 2D;

    private static final LocalDate DEFAULT_ADD_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ADD_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/giftcards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private GiftcardRepository giftcardRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private GiftcardRepository giftcardRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGiftcardMockMvc;

    private Giftcard giftcard;

    private Giftcard insertedGiftcard;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Giftcard createEntity() {
        return new Giftcard().name(DEFAULT_NAME).giftcardamount(DEFAULT_GIFTCARDAMOUNT).addDate(DEFAULT_ADD_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Giftcard createUpdatedEntity() {
        return new Giftcard().name(UPDATED_NAME).giftcardamount(UPDATED_GIFTCARDAMOUNT).addDate(UPDATED_ADD_DATE);
    }

    @BeforeEach
    public void initTest() {
        giftcard = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedGiftcard != null) {
            giftcardRepository.delete(insertedGiftcard);
            insertedGiftcard = null;
        }
    }

    @Test
    @Transactional
    void createGiftcard() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Giftcard
        var returnedGiftcard = om.readValue(
            restGiftcardMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(giftcard)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Giftcard.class
        );

        // Validate the Giftcard in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertGiftcardUpdatableFieldsEquals(returnedGiftcard, getPersistedGiftcard(returnedGiftcard));

        insertedGiftcard = returnedGiftcard;
    }

    @Test
    @Transactional
    void createGiftcardWithExistingId() throws Exception {
        // Create the Giftcard with an existing ID
        giftcard.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGiftcardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(giftcard)))
            .andExpect(status().isBadRequest());

        // Validate the Giftcard in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        giftcard.setName(null);

        // Create the Giftcard, which fails.

        restGiftcardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(giftcard)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGiftcardamountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        giftcard.setGiftcardamount(null);

        // Create the Giftcard, which fails.

        restGiftcardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(giftcard)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        giftcard.setAddDate(null);

        // Create the Giftcard, which fails.

        restGiftcardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(giftcard)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGiftcards() throws Exception {
        // Initialize the database
        insertedGiftcard = giftcardRepository.saveAndFlush(giftcard);

        // Get all the giftcardList
        restGiftcardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(giftcard.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].giftcardamount").value(hasItem(DEFAULT_GIFTCARDAMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].addDate").value(hasItem(DEFAULT_ADD_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllGiftcardsWithEagerRelationshipsIsEnabled() throws Exception {
        when(giftcardRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGiftcardMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(giftcardRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllGiftcardsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(giftcardRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGiftcardMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(giftcardRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getGiftcard() throws Exception {
        // Initialize the database
        insertedGiftcard = giftcardRepository.saveAndFlush(giftcard);

        // Get the giftcard
        restGiftcardMockMvc
            .perform(get(ENTITY_API_URL_ID, giftcard.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(giftcard.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.giftcardamount").value(DEFAULT_GIFTCARDAMOUNT.doubleValue()))
            .andExpect(jsonPath("$.addDate").value(DEFAULT_ADD_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingGiftcard() throws Exception {
        // Get the giftcard
        restGiftcardMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGiftcard() throws Exception {
        // Initialize the database
        insertedGiftcard = giftcardRepository.saveAndFlush(giftcard);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the giftcard
        Giftcard updatedGiftcard = giftcardRepository.findById(giftcard.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedGiftcard are not directly saved in db
        em.detach(updatedGiftcard);
        updatedGiftcard.name(UPDATED_NAME).giftcardamount(UPDATED_GIFTCARDAMOUNT).addDate(UPDATED_ADD_DATE);

        restGiftcardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGiftcard.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedGiftcard))
            )
            .andExpect(status().isOk());

        // Validate the Giftcard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedGiftcardToMatchAllProperties(updatedGiftcard);
    }

    @Test
    @Transactional
    void putNonExistingGiftcard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        giftcard.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGiftcardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, giftcard.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(giftcard))
            )
            .andExpect(status().isBadRequest());

        // Validate the Giftcard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGiftcard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        giftcard.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGiftcardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(giftcard))
            )
            .andExpect(status().isBadRequest());

        // Validate the Giftcard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGiftcard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        giftcard.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGiftcardMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(giftcard)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Giftcard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGiftcardWithPatch() throws Exception {
        // Initialize the database
        insertedGiftcard = giftcardRepository.saveAndFlush(giftcard);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the giftcard using partial update
        Giftcard partialUpdatedGiftcard = new Giftcard();
        partialUpdatedGiftcard.setId(giftcard.getId());

        restGiftcardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGiftcard.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGiftcard))
            )
            .andExpect(status().isOk());

        // Validate the Giftcard in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGiftcardUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedGiftcard, giftcard), getPersistedGiftcard(giftcard));
    }

    @Test
    @Transactional
    void fullUpdateGiftcardWithPatch() throws Exception {
        // Initialize the database
        insertedGiftcard = giftcardRepository.saveAndFlush(giftcard);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the giftcard using partial update
        Giftcard partialUpdatedGiftcard = new Giftcard();
        partialUpdatedGiftcard.setId(giftcard.getId());

        partialUpdatedGiftcard.name(UPDATED_NAME).giftcardamount(UPDATED_GIFTCARDAMOUNT).addDate(UPDATED_ADD_DATE);

        restGiftcardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGiftcard.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGiftcard))
            )
            .andExpect(status().isOk());

        // Validate the Giftcard in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGiftcardUpdatableFieldsEquals(partialUpdatedGiftcard, getPersistedGiftcard(partialUpdatedGiftcard));
    }

    @Test
    @Transactional
    void patchNonExistingGiftcard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        giftcard.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGiftcardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, giftcard.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(giftcard))
            )
            .andExpect(status().isBadRequest());

        // Validate the Giftcard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGiftcard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        giftcard.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGiftcardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(giftcard))
            )
            .andExpect(status().isBadRequest());

        // Validate the Giftcard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGiftcard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        giftcard.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGiftcardMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(giftcard)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Giftcard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGiftcard() throws Exception {
        // Initialize the database
        insertedGiftcard = giftcardRepository.saveAndFlush(giftcard);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the giftcard
        restGiftcardMockMvc
            .perform(delete(ENTITY_API_URL_ID, giftcard.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return giftcardRepository.count();
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

    protected Giftcard getPersistedGiftcard(Giftcard giftcard) {
        return giftcardRepository.findById(giftcard.getId()).orElseThrow();
    }

    protected void assertPersistedGiftcardToMatchAllProperties(Giftcard expectedGiftcard) {
        assertGiftcardAllPropertiesEquals(expectedGiftcard, getPersistedGiftcard(expectedGiftcard));
    }

    protected void assertPersistedGiftcardToMatchUpdatableProperties(Giftcard expectedGiftcard) {
        assertGiftcardAllUpdatablePropertiesEquals(expectedGiftcard, getPersistedGiftcard(expectedGiftcard));
    }
}
