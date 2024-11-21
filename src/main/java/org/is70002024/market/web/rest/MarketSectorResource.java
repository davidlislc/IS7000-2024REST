package org.is70002024.market.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.is70002024.market.domain.MarketSector;
import org.is70002024.market.repository.MarketSectorRepository;
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
 * REST controller for managing {@link org.is70002024.market.domain.MarketSector}.
 */
@RestController
@RequestMapping("/api/market-sectors")
@Transactional
public class MarketSectorResource {

    private static final Logger LOG = LoggerFactory.getLogger(MarketSectorResource.class);

    private static final String ENTITY_NAME = "marketSector";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MarketSectorRepository marketSectorRepository;

    public MarketSectorResource(MarketSectorRepository marketSectorRepository) {
        this.marketSectorRepository = marketSectorRepository;
    }

    /**
     * {@code POST  /market-sectors} : Create a new marketSector.
     *
     * @param marketSector the marketSector to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new marketSector, or with status {@code 400 (Bad Request)} if the marketSector has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MarketSector> createMarketSector(@Valid @RequestBody MarketSector marketSector) throws URISyntaxException {
        LOG.debug("REST request to save MarketSector : {}", marketSector);
        if (marketSector.getId() != null) {
            throw new BadRequestAlertException("A new marketSector cannot already have an ID", ENTITY_NAME, "idexists");
        }
        marketSector = marketSectorRepository.save(marketSector);
        return ResponseEntity.created(new URI("/api/market-sectors/" + marketSector.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, marketSector.getId().toString()))
            .body(marketSector);
    }

    /**
     * {@code PUT  /market-sectors/:id} : Updates an existing marketSector.
     *
     * @param id the id of the marketSector to save.
     * @param marketSector the marketSector to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated marketSector,
     * or with status {@code 400 (Bad Request)} if the marketSector is not valid,
     * or with status {@code 500 (Internal Server Error)} if the marketSector couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MarketSector> updateMarketSector(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MarketSector marketSector
    ) throws URISyntaxException {
        LOG.debug("REST request to update MarketSector : {}, {}", id, marketSector);
        if (marketSector.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, marketSector.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!marketSectorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        marketSector = marketSectorRepository.save(marketSector);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, marketSector.getId().toString()))
            .body(marketSector);
    }

    /**
     * {@code PATCH  /market-sectors/:id} : Partial updates given fields of an existing marketSector, field will ignore if it is null
     *
     * @param id the id of the marketSector to save.
     * @param marketSector the marketSector to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated marketSector,
     * or with status {@code 400 (Bad Request)} if the marketSector is not valid,
     * or with status {@code 404 (Not Found)} if the marketSector is not found,
     * or with status {@code 500 (Internal Server Error)} if the marketSector couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MarketSector> partialUpdateMarketSector(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MarketSector marketSector
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MarketSector partially : {}, {}", id, marketSector);
        if (marketSector.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, marketSector.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!marketSectorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MarketSector> result = marketSectorRepository
            .findById(marketSector.getId())
            .map(existingMarketSector -> {
                if (marketSector.getName() != null) {
                    existingMarketSector.setName(marketSector.getName());
                }
                if (marketSector.getPrice() != null) {
                    existingMarketSector.setPrice(marketSector.getPrice());
                }
                if (marketSector.getChange() != null) {
                    existingMarketSector.setChange(marketSector.getChange());
                }
                if (marketSector.getMarketdate() != null) {
                    existingMarketSector.setMarketdate(marketSector.getMarketdate());
                }

                return existingMarketSector;
            })
            .map(marketSectorRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, marketSector.getId().toString())
        );
    }

    /**
     * {@code GET  /market-sectors} : get all the marketSectors.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of marketSectors in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MarketSector>> getAllMarketSectors(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of MarketSectors");
        Page<MarketSector> page = marketSectorRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /market-sectors/:id} : get the "id" marketSector.
     *
     * @param id the id of the marketSector to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the marketSector, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MarketSector> getMarketSector(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MarketSector : {}", id);
        Optional<MarketSector> marketSector = marketSectorRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(marketSector);
    }

    /**
     * {@code DELETE  /market-sectors/:id} : delete the "id" marketSector.
     *
     * @param id the id of the marketSector to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMarketSector(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MarketSector : {}", id);
        marketSectorRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
