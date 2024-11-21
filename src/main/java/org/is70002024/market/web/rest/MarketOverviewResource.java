package org.is70002024.market.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.is70002024.market.domain.MarketOverview;
import org.is70002024.market.repository.MarketOverviewRepository;
import org.is70002024.market.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.is70002024.market.domain.MarketOverview}.
 */
@RestController
@RequestMapping("/api/market-overviews")
@Transactional
public class MarketOverviewResource {

    private static final Logger LOG = LoggerFactory.getLogger(MarketOverviewResource.class);

    private static final String ENTITY_NAME = "marketOverview";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MarketOverviewRepository marketOverviewRepository;

    public MarketOverviewResource(MarketOverviewRepository marketOverviewRepository) {
        this.marketOverviewRepository = marketOverviewRepository;
    }

    /**
     * {@code POST  /market-overviews} : Create a new marketOverview.
     *
     * @param marketOverview the marketOverview to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new marketOverview, or with status {@code 400 (Bad Request)} if the marketOverview has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MarketOverview> createMarketOverview(@Valid @RequestBody MarketOverview marketOverview)
        throws URISyntaxException {
        LOG.debug("REST request to save MarketOverview : {}", marketOverview);
        if (marketOverview.getId() != null) {
            throw new BadRequestAlertException("A new marketOverview cannot already have an ID", ENTITY_NAME, "idexists");
        }
        marketOverview = marketOverviewRepository.save(marketOverview);
        return ResponseEntity.created(new URI("/api/market-overviews/" + marketOverview.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, marketOverview.getId().toString()))
            .body(marketOverview);
    }

    /**
     * {@code PUT  /market-overviews/:id} : Updates an existing marketOverview.
     *
     * @param id the id of the marketOverview to save.
     * @param marketOverview the marketOverview to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated marketOverview,
     * or with status {@code 400 (Bad Request)} if the marketOverview is not valid,
     * or with status {@code 500 (Internal Server Error)} if the marketOverview couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MarketOverview> updateMarketOverview(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MarketOverview marketOverview
    ) throws URISyntaxException {
        LOG.debug("REST request to update MarketOverview : {}, {}", id, marketOverview);
        if (marketOverview.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, marketOverview.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!marketOverviewRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        marketOverview = marketOverviewRepository.save(marketOverview);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, marketOverview.getId().toString()))
            .body(marketOverview);
    }

    /**
     * {@code PATCH  /market-overviews/:id} : Partial updates given fields of an existing marketOverview, field will ignore if it is null
     *
     * @param id the id of the marketOverview to save.
     * @param marketOverview the marketOverview to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated marketOverview,
     * or with status {@code 400 (Bad Request)} if the marketOverview is not valid,
     * or with status {@code 404 (Not Found)} if the marketOverview is not found,
     * or with status {@code 500 (Internal Server Error)} if the marketOverview couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MarketOverview> partialUpdateMarketOverview(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MarketOverview marketOverview
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MarketOverview partially : {}, {}", id, marketOverview);
        if (marketOverview.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, marketOverview.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!marketOverviewRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MarketOverview> result = marketOverviewRepository
            .findById(marketOverview.getId())
            .map(existingMarketOverview -> {
                if (marketOverview.getName() != null) {
                    existingMarketOverview.setName(marketOverview.getName());
                }
                if (marketOverview.getPrice() != null) {
                    existingMarketOverview.setPrice(marketOverview.getPrice());
                }
                if (marketOverview.getChange() != null) {
                    existingMarketOverview.setChange(marketOverview.getChange());
                }
                if (marketOverview.getTicker() != null) {
                    existingMarketOverview.setTicker(marketOverview.getTicker());
                }
                if (marketOverview.getMarketdate() != null) {
                    existingMarketOverview.setMarketdate(marketOverview.getMarketdate());
                }

                return existingMarketOverview;
            })
            .map(marketOverviewRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, marketOverview.getId().toString())
        );
    }

    /**
     * {@code GET  /market-overviews} : get all the marketOverviews.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of marketOverviews in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MarketOverview>> getAllMarketOverviews(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of MarketOverviews");
        Page<MarketOverview> page = marketOverviewRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /market-overviews/:id} : get the "id" marketOverview.
     *
     * @param id the id of the marketOverview to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the marketOverview, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MarketOverview> getMarketOverview(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MarketOverview : {}", id);
        Optional<MarketOverview> marketOverview = marketOverviewRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(marketOverview);
    }

    /**
     * {@code DELETE  /market-overviews/:id} : delete the "id" marketOverview.
     *
     * @param id the id of the marketOverview to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMarketOverview(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MarketOverview : {}", id);
        marketOverviewRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
