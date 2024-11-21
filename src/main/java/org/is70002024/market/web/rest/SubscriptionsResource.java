package org.is70002024.market.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.is70002024.market.domain.Subscriptions;
import org.is70002024.market.repository.SubscriptionsRepository;
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
 * REST controller for managing {@link org.is70002024.market.domain.Subscriptions}.
 */
@RestController
@RequestMapping("/api/subscriptions")
@Transactional
public class SubscriptionsResource {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionsResource.class);

    private static final String ENTITY_NAME = "subscriptions";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubscriptionsRepository subscriptionsRepository;

    public SubscriptionsResource(SubscriptionsRepository subscriptionsRepository) {
        this.subscriptionsRepository = subscriptionsRepository;
    }

    /**
     * {@code POST  /subscriptions} : Create a new subscriptions.
     *
     * @param subscriptions the subscriptions to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subscriptions, or with status {@code 400 (Bad Request)} if the subscriptions has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Subscriptions> createSubscriptions(@Valid @RequestBody Subscriptions subscriptions) throws URISyntaxException {
        LOG.debug("REST request to save Subscriptions : {}", subscriptions);
        if (subscriptions.getId() != null) {
            throw new BadRequestAlertException("A new subscriptions cannot already have an ID", ENTITY_NAME, "idexists");
        }
        subscriptions = subscriptionsRepository.save(subscriptions);
        return ResponseEntity.created(new URI("/api/subscriptions/" + subscriptions.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, subscriptions.getId().toString()))
            .body(subscriptions);
    }

    /**
     * {@code PUT  /subscriptions/:id} : Updates an existing subscriptions.
     *
     * @param id the id of the subscriptions to save.
     * @param subscriptions the subscriptions to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptions,
     * or with status {@code 400 (Bad Request)} if the subscriptions is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subscriptions couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Subscriptions> updateSubscriptions(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Subscriptions subscriptions
    ) throws URISyntaxException {
        LOG.debug("REST request to update Subscriptions : {}, {}", id, subscriptions);
        if (subscriptions.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscriptions.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subscriptionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        subscriptions = subscriptionsRepository.save(subscriptions);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, subscriptions.getId().toString()))
            .body(subscriptions);
    }

    /**
     * {@code PATCH  /subscriptions/:id} : Partial updates given fields of an existing subscriptions, field will ignore if it is null
     *
     * @param id the id of the subscriptions to save.
     * @param subscriptions the subscriptions to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptions,
     * or with status {@code 400 (Bad Request)} if the subscriptions is not valid,
     * or with status {@code 404 (Not Found)} if the subscriptions is not found,
     * or with status {@code 500 (Internal Server Error)} if the subscriptions couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Subscriptions> partialUpdateSubscriptions(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Subscriptions subscriptions
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Subscriptions partially : {}, {}", id, subscriptions);
        if (subscriptions.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscriptions.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subscriptionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Subscriptions> result = subscriptionsRepository
            .findById(subscriptions.getId())
            .map(existingSubscriptions -> {
                if (subscriptions.getName() != null) {
                    existingSubscriptions.setName(subscriptions.getName());
                }
                if (subscriptions.getSubdate() != null) {
                    existingSubscriptions.setSubdate(subscriptions.getSubdate());
                }
                if (subscriptions.getStatus() != null) {
                    existingSubscriptions.setStatus(subscriptions.getStatus());
                }

                return existingSubscriptions;
            })
            .map(subscriptionsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, subscriptions.getId().toString())
        );
    }

    /**
     * {@code GET  /subscriptions} : get all the subscriptions.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subscriptions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Subscriptions>> getAllSubscriptions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Subscriptions");
        Page<Subscriptions> page;
        if (eagerload) {
            page = subscriptionsRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = subscriptionsRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /subscriptions/:id} : get the "id" subscriptions.
     *
     * @param id the id of the subscriptions to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subscriptions, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Subscriptions> getSubscriptions(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Subscriptions : {}", id);
        Optional<Subscriptions> subscriptions = subscriptionsRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(subscriptions);
    }

    /**
     * {@code DELETE  /subscriptions/:id} : delete the "id" subscriptions.
     *
     * @param id the id of the subscriptions to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscriptions(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Subscriptions : {}", id);
        subscriptionsRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
