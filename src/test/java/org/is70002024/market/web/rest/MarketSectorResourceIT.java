package org.is70002024.market.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.is70002024.market.domain.MarketSectorAsserts.*;
import static org.is70002024.market.web.rest.TestUtil.createUpdateProxyForBean;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.is70002024.market.IntegrationTest;
import org.is70002024.market.domain.MarketSector;
import org.is70002024.market.domain.enumeration.SECTOR;
import org.is70002024.market.repository.MarketSectorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MarketSectorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MarketSectorResourceIT {

    private static final SECTOR DEFAULT_NAME = SECTOR.XLC;
    private static final SECTOR UPDATED_NAME = SECTOR.XLY;

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final Double DEFAULT_CHANGE = 1D;
    private static final Double UPDATED_CHANGE = 2D;

    private static final LocalDate DEFAULT_MARKETDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MARKETDATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/market-sectors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MarketSectorRepository marketSectorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMarketSectorMockMvc;

    private MarketSector marketSector;

    private MarketSector insertedMarketSector;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MarketSector createEntity() {
        return new MarketSector().name(DEFAULT_NAME).price(DEFAULT_PRICE).change(DEFAULT_CHANGE).marketdate(DEFAULT_MARKETDATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MarketSector createUpdatedEntity() {
        return new MarketSector().name(UPDATED_NAME).price(UPDATED_PRICE).change(UPDATED_CHANGE).marketdate(UPDATED_MARKETDATE);
    }

    @BeforeEach
    public void initTest() {
        marketSector = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMarketSector != null) {
            marketSectorRepository.delete(insertedMarketSector);
            insertedMarketSector = null;
        }
    }

    @Test
    @Transactional
    void createMarketSector() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MarketSector
        var returnedMarketSector = om.readValue(
            restMarketSectorMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(marketSector)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MarketSector.class
        );

        // Validate the MarketSector in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMarketSectorUpdatableFieldsEquals(returnedMarketSector, getPersistedMarketSector(returnedMarketSector));

        insertedMarketSector = returnedMarketSector;
    }

    @Test
    @Transactional
    void createMarketSectorWithExistingId() throws Exception {
        // Create the MarketSector with an existing ID
        marketSector.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMarketSectorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(marketSector)))
            .andExpect(status().isBadRequest());

        // Validate the MarketSector in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        marketSector.setName(null);

        // Create the MarketSector, which fails.

        restMarketSectorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(marketSector)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        marketSector.setPrice(null);

        // Create the MarketSector, which fails.

        restMarketSectorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(marketSector)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkChangeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        marketSector.setChange(null);

        // Create the MarketSector, which fails.

        restMarketSectorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(marketSector)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMarketdateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        marketSector.setMarketdate(null);

        // Create the MarketSector, which fails.

        restMarketSectorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(marketSector)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMarketSectors() throws Exception {
        // Initialize the database
        insertedMarketSector = marketSectorRepository.saveAndFlush(marketSector);

        // Get all the marketSectorList
        restMarketSectorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(marketSector.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].change").value(hasItem(DEFAULT_CHANGE.doubleValue())))
            .andExpect(jsonPath("$.[*].marketdate").value(hasItem(DEFAULT_MARKETDATE.toString())));
    }

    @Test
    @Transactional
    void getMarketSector() throws Exception {
        // Initialize the database
        insertedMarketSector = marketSectorRepository.saveAndFlush(marketSector);

        // Get the marketSector
        restMarketSectorMockMvc
            .perform(get(ENTITY_API_URL_ID, marketSector.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(marketSector.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.change").value(DEFAULT_CHANGE.doubleValue()))
            .andExpect(jsonPath("$.marketdate").value(DEFAULT_MARKETDATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMarketSector() throws Exception {
        // Get the marketSector
        restMarketSectorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMarketSector() throws Exception {
        // Initialize the database
        insertedMarketSector = marketSectorRepository.saveAndFlush(marketSector);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the marketSector
        MarketSector updatedMarketSector = marketSectorRepository.findById(marketSector.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMarketSector are not directly saved in db
        em.detach(updatedMarketSector);
        updatedMarketSector.name(UPDATED_NAME).price(UPDATED_PRICE).change(UPDATED_CHANGE).marketdate(UPDATED_MARKETDATE);

        restMarketSectorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMarketSector.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedMarketSector))
            )
            .andExpect(status().isOk());

        // Validate the MarketSector in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMarketSectorToMatchAllProperties(updatedMarketSector);
    }

    @Test
    @Transactional
    void putNonExistingMarketSector() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        marketSector.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMarketSectorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, marketSector.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(marketSector))
            )
            .andExpect(status().isBadRequest());

        // Validate the MarketSector in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMarketSector() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        marketSector.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarketSectorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(marketSector))
            )
            .andExpect(status().isBadRequest());

        // Validate the MarketSector in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMarketSector() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        marketSector.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarketSectorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(marketSector)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MarketSector in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMarketSectorWithPatch() throws Exception {
        // Initialize the database
        insertedMarketSector = marketSectorRepository.saveAndFlush(marketSector);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the marketSector using partial update
        MarketSector partialUpdatedMarketSector = new MarketSector();
        partialUpdatedMarketSector.setId(marketSector.getId());

        partialUpdatedMarketSector.name(UPDATED_NAME).price(UPDATED_PRICE);

        restMarketSectorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMarketSector.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMarketSector))
            )
            .andExpect(status().isOk());

        // Validate the MarketSector in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMarketSectorUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMarketSector, marketSector),
            getPersistedMarketSector(marketSector)
        );
    }

    @Test
    @Transactional
    void fullUpdateMarketSectorWithPatch() throws Exception {
        // Initialize the database
        insertedMarketSector = marketSectorRepository.saveAndFlush(marketSector);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the marketSector using partial update
        MarketSector partialUpdatedMarketSector = new MarketSector();
        partialUpdatedMarketSector.setId(marketSector.getId());

        partialUpdatedMarketSector.name(UPDATED_NAME).price(UPDATED_PRICE).change(UPDATED_CHANGE).marketdate(UPDATED_MARKETDATE);

        restMarketSectorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMarketSector.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMarketSector))
            )
            .andExpect(status().isOk());

        // Validate the MarketSector in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMarketSectorUpdatableFieldsEquals(partialUpdatedMarketSector, getPersistedMarketSector(partialUpdatedMarketSector));
    }

    @Test
    @Transactional
    void patchNonExistingMarketSector() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        marketSector.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMarketSectorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, marketSector.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(marketSector))
            )
            .andExpect(status().isBadRequest());

        // Validate the MarketSector in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMarketSector() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        marketSector.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarketSectorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(marketSector))
            )
            .andExpect(status().isBadRequest());

        // Validate the MarketSector in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMarketSector() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        marketSector.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarketSectorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(marketSector)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MarketSector in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMarketSector() throws Exception {
        // Initialize the database
        insertedMarketSector = marketSectorRepository.saveAndFlush(marketSector);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the marketSector
        restMarketSectorMockMvc
            .perform(delete(ENTITY_API_URL_ID, marketSector.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return marketSectorRepository.count();
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

    protected MarketSector getPersistedMarketSector(MarketSector marketSector) {
        return marketSectorRepository.findById(marketSector.getId()).orElseThrow();
    }

    protected void assertPersistedMarketSectorToMatchAllProperties(MarketSector expectedMarketSector) {
        assertMarketSectorAllPropertiesEquals(expectedMarketSector, getPersistedMarketSector(expectedMarketSector));
    }

    protected void assertPersistedMarketSectorToMatchUpdatableProperties(MarketSector expectedMarketSector) {
        assertMarketSectorAllUpdatablePropertiesEquals(expectedMarketSector, getPersistedMarketSector(expectedMarketSector));
    }
}
