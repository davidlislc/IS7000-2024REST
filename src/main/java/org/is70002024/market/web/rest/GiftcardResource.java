package org.is70002024.market.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.is70002024.market.domain.Giftcard;
import org.is70002024.market.repository.GiftcardRepository;
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
 * REST controller for managing {@link org.is70002024.market.domain.Giftcard}.
 */
@RestController
@RequestMapping("/api/giftcards")
@Transactional
public class GiftcardResource {

    private static final Logger LOG = LoggerFactory.getLogger(GiftcardResource.class);

    private static final String ENTITY_NAME = "giftcard";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GiftcardRepository giftcardRepository;

    public GiftcardResource(GiftcardRepository giftcardRepository) {
        this.giftcardRepository = giftcardRepository;
    }

    /**
     * {@code POST  /giftcards} : Create a new giftcard.
     *
     * @param giftcard the giftcard to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new giftcard, or with status {@code 400 (Bad Request)} if the giftcard has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Giftcard> createGiftcard(@Valid @RequestBody Giftcard giftcard) throws URISyntaxException {
        LOG.debug("REST request to save Giftcard : {}", giftcard);
        if (giftcard.getId() != null) {
            throw new BadRequestAlertException("A new giftcard cannot already have an ID", ENTITY_NAME, "idexists");
        }
        giftcard = giftcardRepository.save(giftcard);
        return ResponseEntity.created(new URI("/api/giftcards/" + giftcard.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, giftcard.getId().toString()))
            .body(giftcard);
    }

    /**
     * {@code PUT  /giftcards/:id} : Updates an existing giftcard.
     *
     * @param id the id of the giftcard to save.
     * @param giftcard the giftcard to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated giftcard,
     * or with status {@code 400 (Bad Request)} if the giftcard is not valid,
     * or with status {@code 500 (Internal Server Error)} if the giftcard couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Giftcard> updateGiftcard(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Giftcard giftcard
    ) throws URISyntaxException {
        LOG.debug("REST request to update Giftcard : {}, {}", id, giftcard);
        if (giftcard.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, giftcard.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!giftcardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        giftcard = giftcardRepository.save(giftcard);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, giftcard.getId().toString()))
            .body(giftcard);
    }

    /**
     * {@code PATCH  /giftcards/:id} : Partial updates given fields of an existing giftcard, field will ignore if it is null
     *
     * @param id the id of the giftcard to save.
     * @param giftcard the giftcard to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated giftcard,
     * or with status {@code 400 (Bad Request)} if the giftcard is not valid,
     * or with status {@code 404 (Not Found)} if the giftcard is not found,
     * or with status {@code 500 (Internal Server Error)} if the giftcard couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Giftcard> partialUpdateGiftcard(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Giftcard giftcard
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Giftcard partially : {}, {}", id, giftcard);
        if (giftcard.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, giftcard.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!giftcardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Giftcard> result = giftcardRepository
            .findById(giftcard.getId())
            .map(existingGiftcard -> {
                if (giftcard.getName() != null) {
                    existingGiftcard.setName(giftcard.getName());
                }
                if (giftcard.getGiftcardamount() != null) {
                    existingGiftcard.setGiftcardamount(giftcard.getGiftcardamount());
                }
                if (giftcard.getAddDate() != null) {
                    existingGiftcard.setAddDate(giftcard.getAddDate());
                }

                return existingGiftcard;
            })
            .map(giftcardRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, giftcard.getId().toString())
        );
    }

    /**
     * {@code GET  /giftcards} : get all the giftcards.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of giftcards in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Giftcard>> getAllGiftcards(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Giftcards");
        Page<Giftcard> page;
        if (eagerload) {
            page = giftcardRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = giftcardRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /giftcards/:id} : get the "id" giftcard.
     *
     * @param id the id of the giftcard to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the giftcard, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Giftcard> getGiftcard(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Giftcard : {}", id);
        Optional<Giftcard> giftcard = giftcardRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(giftcard);
    }

    /**
     * {@code DELETE  /giftcards/:id} : delete the "id" giftcard.
     *
     * @param id the id of the giftcard to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGiftcard(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Giftcard : {}", id);
        giftcardRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
