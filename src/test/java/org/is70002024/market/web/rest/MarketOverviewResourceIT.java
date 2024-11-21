package org.is70002024.market.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.is70002024.market.domain.MarketOverviewAsserts.*;
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
import org.is70002024.market.domain.MarketOverview;
import org.is70002024.market.domain.enumeration.INDEX;
import org.is70002024.market.repository.MarketOverviewRepository;
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
 * Integration tests for the {@link MarketOverviewResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MarketOverviewResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final Double DEFAULT_CHANGE = 1D;
    private static final Double UPDATED_CHANGE = 2D;

    private static final INDEX DEFAULT_TICKER = INDEX.SPY;
    private static final INDEX UPDATED_TICKER = INDEX.DOW;

    private static final LocalDate DEFAULT_MARKETDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MARKETDATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/market-overviews";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MarketOverviewRepository marketOverviewRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMarketOverviewMockMvc;

    private MarketOverview marketOverview;

    private MarketOverview insertedMarketOverview;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MarketOverview createEntity() {
        return new MarketOverview()
            .name(DEFAULT_NAME)
            .price(DEFAULT_PRICE)
            .change(DEFAULT_CHANGE)
            .ticker(DEFAULT_TICKER)
            .marketdate(DEFAULT_MARKETDATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MarketOverview createUpdatedEntity() {
        return new MarketOverview()
            .name(UPDATED_NAME)
            .price(UPDATED_PRICE)
            .change(UPDATED_CHANGE)
            .ticker(UPDATED_TICKER)
            .marketdate(UPDATED_MARKETDATE);
    }

    @BeforeEach
    public void initTest() {
        marketOverview = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMarketOverview != null) {
            marketOverviewRepository.delete(insertedMarketOverview);
            insertedMarketOverview = null;
        }
    }

    @Test
    @Transactional
    void createMarketOverview() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MarketOverview
        var returnedMarketOverview = om.readValue(
            restMarketOverviewMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(marketOverview)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MarketOverview.class
        );

        // Validate the MarketOverview in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMarketOverviewUpdatableFieldsEquals(returnedMarketOverview, getPersistedMarketOverview(returnedMarketOverview));

        insertedMarketOverview = returnedMarketOverview;
    }

    @Test
    @Transactional
    void createMarketOverviewWithExistingId() throws Exception {
        // Create the MarketOverview with an existing ID
        marketOverview.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMarketOverviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(marketOverview)))
            .andExpect(status().isBadRequest());

        // Validate the MarketOverview in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        marketOverview.setName(null);

        // Create the MarketOverview, which fails.

        restMarketOverviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(marketOverview)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        marketOverview.setPrice(null);

        // Create the MarketOverview, which fails.

        restMarketOverviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(marketOverview)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkChangeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        marketOverview.setChange(null);

        // Create the MarketOverview, which fails.

        restMarketOverviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(marketOverview)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTickerIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        marketOverview.setTicker(null);

        // Create the MarketOverview, which fails.

        restMarketOverviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(marketOverview)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMarketdateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        marketOverview.setMarketdate(null);

        // Create the MarketOverview, which fails.

        restMarketOverviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(marketOverview)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMarketOverviews() throws Exception {
        // Initialize the database
        insertedMarketOverview = marketOverviewRepository.saveAndFlush(marketOverview);

        // Get all the marketOverviewList
        restMarketOverviewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(marketOverview.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].change").value(hasItem(DEFAULT_CHANGE.doubleValue())))
            .andExpect(jsonPath("$.[*].ticker").value(hasItem(DEFAULT_TICKER.toString())))
            .andExpect(jsonPath("$.[*].marketdate").value(hasItem(DEFAULT_MARKETDATE.toString())));
    }

    @Test
    @Transactional
    void getMarketOverview() throws Exception {
        // Initialize the database
        insertedMarketOverview = marketOverviewRepository.saveAndFlush(marketOverview);

        // Get the marketOverview
        restMarketOverviewMockMvc
            .perform(get(ENTITY_API_URL_ID, marketOverview.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(marketOverview.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.change").value(DEFAULT_CHANGE.doubleValue()))
            .andExpect(jsonPath("$.ticker").value(DEFAULT_TICKER.toString()))
            .andExpect(jsonPath("$.marketdate").value(DEFAULT_MARKETDATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMarketOverview() throws Exception {
        // Get the marketOverview
        restMarketOverviewMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMarketOverview() throws Exception {
        // Initialize the database
        insertedMarketOverview = marketOverviewRepository.saveAndFlush(marketOverview);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the marketOverview
        MarketOverview updatedMarketOverview = marketOverviewRepository.findById(marketOverview.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMarketOverview are not directly saved in db
        em.detach(updatedMarketOverview);
        updatedMarketOverview
            .name(UPDATED_NAME)
            .price(UPDATED_PRICE)
            .change(UPDATED_CHANGE)
            .ticker(UPDATED_TICKER)
            .marketdate(UPDATED_MARKETDATE);

        restMarketOverviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMarketOverview.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedMarketOverview))
            )
            .andExpect(status().isOk());

        // Validate the MarketOverview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMarketOverviewToMatchAllProperties(updatedMarketOverview);
    }

    @Test
    @Transactional
    void putNonExistingMarketOverview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        marketOverview.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMarketOverviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, marketOverview.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(marketOverview))
            )
            .andExpect(status().isBadRequest());

        // Validate the MarketOverview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMarketOverview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        marketOverview.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarketOverviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(marketOverview))
            )
            .andExpect(status().isBadRequest());

        // Validate the MarketOverview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMarketOverview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        marketOverview.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarketOverviewMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(marketOverview)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MarketOverview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMarketOverviewWithPatch() throws Exception {
        // Initialize the database
        insertedMarketOverview = marketOverviewRepository.saveAndFlush(marketOverview);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the marketOverview using partial update
        MarketOverview partialUpdatedMarketOverview = new MarketOverview();
        partialUpdatedMarketOverview.setId(marketOverview.getId());

        partialUpdatedMarketOverview.name(UPDATED_NAME).price(UPDATED_PRICE).ticker(UPDATED_TICKER).marketdate(UPDATED_MARKETDATE);

        restMarketOverviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMarketOverview.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMarketOverview))
            )
            .andExpect(status().isOk());

        // Validate the MarketOverview in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMarketOverviewUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMarketOverview, marketOverview),
            getPersistedMarketOverview(marketOverview)
        );
    }

    @Test
    @Transactional
    void fullUpdateMarketOverviewWithPatch() throws Exception {
        // Initialize the database
        insertedMarketOverview = marketOverviewRepository.saveAndFlush(marketOverview);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the marketOverview using partial update
        MarketOverview partialUpdatedMarketOverview = new MarketOverview();
        partialUpdatedMarketOverview.setId(marketOverview.getId());

        partialUpdatedMarketOverview
            .name(UPDATED_NAME)
            .price(UPDATED_PRICE)
            .change(UPDATED_CHANGE)
            .ticker(UPDATED_TICKER)
            .marketdate(UPDATED_MARKETDATE);

        restMarketOverviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMarketOverview.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMarketOverview))
            )
            .andExpect(status().isOk());

        // Validate the MarketOverview in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMarketOverviewUpdatableFieldsEquals(partialUpdatedMarketOverview, getPersistedMarketOverview(partialUpdatedMarketOverview));
    }

    @Test
    @Transactional
    void patchNonExistingMarketOverview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        marketOverview.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMarketOverviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, marketOverview.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(marketOverview))
            )
            .andExpect(status().isBadRequest());

        // Validate the MarketOverview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMarketOverview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        marketOverview.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarketOverviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(marketOverview))
            )
            .andExpect(status().isBadRequest());

        // Validate the MarketOverview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMarketOverview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        marketOverview.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarketOverviewMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(marketOverview)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MarketOverview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMarketOverview() throws Exception {
        // Initialize the database
        insertedMarketOverview = marketOverviewRepository.saveAndFlush(marketOverview);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the marketOverview
        restMarketOverviewMockMvc
            .perform(delete(ENTITY_API_URL_ID, marketOverview.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return marketOverviewRepository.count();
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

    protected MarketOverview getPersistedMarketOverview(MarketOverview marketOverview) {
        return marketOverviewRepository.findById(marketOverview.getId()).orElseThrow();
    }

    protected void assertPersistedMarketOverviewToMatchAllProperties(MarketOverview expectedMarketOverview) {
        assertMarketOverviewAllPropertiesEquals(expectedMarketOverview, getPersistedMarketOverview(expectedMarketOverview));
    }

    protected void assertPersistedMarketOverviewToMatchUpdatableProperties(MarketOverview expectedMarketOverview) {
        assertMarketOverviewAllUpdatablePropertiesEquals(expectedMarketOverview, getPersistedMarketOverview(expectedMarketOverview));
    }
}
