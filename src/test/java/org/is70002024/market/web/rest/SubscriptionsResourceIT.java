package org.is70002024.market.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.is70002024.market.domain.SubscriptionsAsserts.*;
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
import org.is70002024.market.domain.Subscriptions;
import org.is70002024.market.repository.SubscriptionsRepository;
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
 * Integration tests for the {@link SubscriptionsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SubscriptionsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_SUBDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SUBDATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/subscriptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SubscriptionsRepository subscriptionsRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private SubscriptionsRepository subscriptionsRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubscriptionsMockMvc;

    private Subscriptions subscriptions;

    private Subscriptions insertedSubscriptions;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Subscriptions createEntity() {
        return new Subscriptions().name(DEFAULT_NAME).subdate(DEFAULT_SUBDATE).status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Subscriptions createUpdatedEntity() {
        return new Subscriptions().name(UPDATED_NAME).subdate(UPDATED_SUBDATE).status(UPDATED_STATUS);
    }

    @BeforeEach
    public void initTest() {
        subscriptions = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSubscriptions != null) {
            subscriptionsRepository.delete(insertedSubscriptions);
            insertedSubscriptions = null;
        }
    }

    @Test
    @Transactional
    void createSubscriptions() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Subscriptions
        var returnedSubscriptions = om.readValue(
            restSubscriptionsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscriptions)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Subscriptions.class
        );

        // Validate the Subscriptions in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSubscriptionsUpdatableFieldsEquals(returnedSubscriptions, getPersistedSubscriptions(returnedSubscriptions));

        insertedSubscriptions = returnedSubscriptions;
    }

    @Test
    @Transactional
    void createSubscriptionsWithExistingId() throws Exception {
        // Create the Subscriptions with an existing ID
        subscriptions.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubscriptionsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscriptions)))
            .andExpect(status().isBadRequest());

        // Validate the Subscriptions in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        subscriptions.setName(null);

        // Create the Subscriptions, which fails.

        restSubscriptionsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscriptions)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubdateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        subscriptions.setSubdate(null);

        // Create the Subscriptions, which fails.

        restSubscriptionsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscriptions)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        subscriptions.setStatus(null);

        // Create the Subscriptions, which fails.

        restSubscriptionsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscriptions)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSubscriptions() throws Exception {
        // Initialize the database
        insertedSubscriptions = subscriptionsRepository.saveAndFlush(subscriptions);

        // Get all the subscriptionsList
        restSubscriptionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subscriptions.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].subdate").value(hasItem(DEFAULT_SUBDATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSubscriptionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(subscriptionsRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSubscriptionsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(subscriptionsRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSubscriptionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(subscriptionsRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSubscriptionsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(subscriptionsRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSubscriptions() throws Exception {
        // Initialize the database
        insertedSubscriptions = subscriptionsRepository.saveAndFlush(subscriptions);

        // Get the subscriptions
        restSubscriptionsMockMvc
            .perform(get(ENTITY_API_URL_ID, subscriptions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subscriptions.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.subdate").value(DEFAULT_SUBDATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    void getNonExistingSubscriptions() throws Exception {
        // Get the subscriptions
        restSubscriptionsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSubscriptions() throws Exception {
        // Initialize the database
        insertedSubscriptions = subscriptionsRepository.saveAndFlush(subscriptions);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subscriptions
        Subscriptions updatedSubscriptions = subscriptionsRepository.findById(subscriptions.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSubscriptions are not directly saved in db
        em.detach(updatedSubscriptions);
        updatedSubscriptions.name(UPDATED_NAME).subdate(UPDATED_SUBDATE).status(UPDATED_STATUS);

        restSubscriptionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSubscriptions.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSubscriptions))
            )
            .andExpect(status().isOk());

        // Validate the Subscriptions in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSubscriptionsToMatchAllProperties(updatedSubscriptions);
    }

    @Test
    @Transactional
    void putNonExistingSubscriptions() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptions.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscriptionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subscriptions.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscriptions))
            )
            .andExpect(status().isBadRequest());

        // Validate the Subscriptions in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubscriptions() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptions.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subscriptions))
            )
            .andExpect(status().isBadRequest());

        // Validate the Subscriptions in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubscriptions() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptions.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subscriptions)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Subscriptions in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubscriptionsWithPatch() throws Exception {
        // Initialize the database
        insertedSubscriptions = subscriptionsRepository.saveAndFlush(subscriptions);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subscriptions using partial update
        Subscriptions partialUpdatedSubscriptions = new Subscriptions();
        partialUpdatedSubscriptions.setId(subscriptions.getId());

        partialUpdatedSubscriptions.name(UPDATED_NAME).status(UPDATED_STATUS);

        restSubscriptionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubscriptions.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubscriptions))
            )
            .andExpect(status().isOk());

        // Validate the Subscriptions in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubscriptionsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSubscriptions, subscriptions),
            getPersistedSubscriptions(subscriptions)
        );
    }

    @Test
    @Transactional
    void fullUpdateSubscriptionsWithPatch() throws Exception {
        // Initialize the database
        insertedSubscriptions = subscriptionsRepository.saveAndFlush(subscriptions);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subscriptions using partial update
        Subscriptions partialUpdatedSubscriptions = new Subscriptions();
        partialUpdatedSubscriptions.setId(subscriptions.getId());

        partialUpdatedSubscriptions.name(UPDATED_NAME).subdate(UPDATED_SUBDATE).status(UPDATED_STATUS);

        restSubscriptionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubscriptions.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubscriptions))
            )
            .andExpect(status().isOk());

        // Validate the Subscriptions in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubscriptionsUpdatableFieldsEquals(partialUpdatedSubscriptions, getPersistedSubscriptions(partialUpdatedSubscriptions));
    }

    @Test
    @Transactional
    void patchNonExistingSubscriptions() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptions.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscriptionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subscriptions.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(subscriptions))
            )
            .andExpect(status().isBadRequest());

        // Validate the Subscriptions in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubscriptions() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptions.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(subscriptions))
            )
            .andExpect(status().isBadRequest());

        // Validate the Subscriptions in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubscriptions() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subscriptions.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(subscriptions)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Subscriptions in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubscriptions() throws Exception {
        // Initialize the database
        insertedSubscriptions = subscriptionsRepository.saveAndFlush(subscriptions);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the subscriptions
        restSubscriptionsMockMvc
            .perform(delete(ENTITY_API_URL_ID, subscriptions.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return subscriptionsRepository.count();
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

    protected Subscriptions getPersistedSubscriptions(Subscriptions subscriptions) {
        return subscriptionsRepository.findById(subscriptions.getId()).orElseThrow();
    }

    protected void assertPersistedSubscriptionsToMatchAllProperties(Subscriptions expectedSubscriptions) {
        assertSubscriptionsAllPropertiesEquals(expectedSubscriptions, getPersistedSubscriptions(expectedSubscriptions));
    }

    protected void assertPersistedSubscriptionsToMatchUpdatableProperties(Subscriptions expectedSubscriptions) {
        assertSubscriptionsAllUpdatablePropertiesEquals(expectedSubscriptions, getPersistedSubscriptions(expectedSubscriptions));
    }
}
