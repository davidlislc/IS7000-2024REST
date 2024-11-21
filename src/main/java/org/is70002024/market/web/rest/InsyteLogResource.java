package org.is70002024.market.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.is70002024.market.domain.InsyteLog;
import org.is70002024.market.repository.InsyteLogRepository;
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
 * REST controller for managing {@link org.is70002024.market.domain.InsyteLog}.
 */
@RestController
@RequestMapping("/api/insyte-logs")
@Transactional
public class InsyteLogResource {

    private static final Logger LOG = LoggerFactory.getLogger(InsyteLogResource.class);

    private static final String ENTITY_NAME = "insyteLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InsyteLogRepository insyteLogRepository;

    public InsyteLogResource(InsyteLogRepository insyteLogRepository) {
        this.insyteLogRepository = insyteLogRepository;
    }

    /**
     * {@code POST  /insyte-logs} : Create a new insyteLog.
     *
     * @param insyteLog the insyteLog to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new insyteLog, or with status {@code 400 (Bad Request)} if the insyteLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InsyteLog> createInsyteLog(@Valid @RequestBody InsyteLog insyteLog) throws URISyntaxException {
        LOG.debug("REST request to save InsyteLog : {}", insyteLog);
        if (insyteLog.getId() != null) {
            throw new BadRequestAlertException("A new insyteLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        insyteLog = insyteLogRepository.save(insyteLog);
        return ResponseEntity.created(new URI("/api/insyte-logs/" + insyteLog.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, insyteLog.getId().toString()))
            .body(insyteLog);
    }

    /**
     * {@code PUT  /insyte-logs/:id} : Updates an existing insyteLog.
     *
     * @param id the id of the insyteLog to save.
     * @param insyteLog the insyteLog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insyteLog,
     * or with status {@code 400 (Bad Request)} if the insyteLog is not valid,
     * or with status {@code 500 (Internal Server Error)} if the insyteLog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InsyteLog> updateInsyteLog(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InsyteLog insyteLog
    ) throws URISyntaxException {
        LOG.debug("REST request to update InsyteLog : {}, {}", id, insyteLog);
        if (insyteLog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, insyteLog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!insyteLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        insyteLog = insyteLogRepository.save(insyteLog);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, insyteLog.getId().toString()))
            .body(insyteLog);
    }

    /**
     * {@code PATCH  /insyte-logs/:id} : Partial updates given fields of an existing insyteLog, field will ignore if it is null
     *
     * @param id the id of the insyteLog to save.
     * @param insyteLog the insyteLog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insyteLog,
     * or with status {@code 400 (Bad Request)} if the insyteLog is not valid,
     * or with status {@code 404 (Not Found)} if the insyteLog is not found,
     * or with status {@code 500 (Internal Server Error)} if the insyteLog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InsyteLog> partialUpdateInsyteLog(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InsyteLog insyteLog
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update InsyteLog partially : {}, {}", id, insyteLog);
        if (insyteLog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, insyteLog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!insyteLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InsyteLog> result = insyteLogRepository
            .findById(insyteLog.getId())
            .map(existingInsyteLog -> {
                if (insyteLog.getName() != null) {
                    existingInsyteLog.setName(insyteLog.getName());
                }
                if (insyteLog.getActivity() != null) {
                    existingInsyteLog.setActivity(insyteLog.getActivity());
                }
                if (insyteLog.getRundate() != null) {
                    existingInsyteLog.setRundate(insyteLog.getRundate());
                }

                return existingInsyteLog;
            })
            .map(insyteLogRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, insyteLog.getId().toString())
        );
    }

    /**
     * {@code GET  /insyte-logs} : get all the insyteLogs.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of insyteLogs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InsyteLog>> getAllInsyteLogs(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of InsyteLogs");
        Page<InsyteLog> page;
        if (eagerload) {
            page = insyteLogRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = insyteLogRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /insyte-logs/:id} : get the "id" insyteLog.
     *
     * @param id the id of the insyteLog to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the insyteLog, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InsyteLog> getInsyteLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InsyteLog : {}", id);
        Optional<InsyteLog> insyteLog = insyteLogRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(insyteLog);
    }

    /**
     * {@code DELETE  /insyte-logs/:id} : delete the "id" insyteLog.
     *
     * @param id the id of the insyteLog to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInsyteLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InsyteLog : {}", id);
        insyteLogRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
