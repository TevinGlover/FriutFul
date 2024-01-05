package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Parents;
import com.mycompany.myapp.repository.ParentsRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Parents}.
 */
@RestController
@RequestMapping("/api/parents")
@Transactional
public class ParentsResource {

    private final Logger log = LoggerFactory.getLogger(ParentsResource.class);

    private static final String ENTITY_NAME = "parents";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParentsRepository parentsRepository;

    public ParentsResource(ParentsRepository parentsRepository) {
        this.parentsRepository = parentsRepository;
    }

    /**
     * {@code POST  /parents} : Create a new parents.
     *
     * @param parents the parents to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new parents, or with status {@code 400 (Bad Request)} if the parents has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Parents> createParents(@RequestBody Parents parents) throws URISyntaxException {
        log.debug("REST request to save Parents : {}", parents);
        if (parents.getId() != null) {
            throw new BadRequestAlertException("A new parents cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Parents result = parentsRepository.save(parents);
        return ResponseEntity
            .created(new URI("/api/parents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /parents/:id} : Updates an existing parents.
     *
     * @param id the id of the parents to save.
     * @param parents the parents to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parents,
     * or with status {@code 400 (Bad Request)} if the parents is not valid,
     * or with status {@code 500 (Internal Server Error)} if the parents couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Parents> updateParents(@PathVariable(value = "id", required = false) final Long id, @RequestBody Parents parents)
        throws URISyntaxException {
        log.debug("REST request to update Parents : {}, {}", id, parents);
        if (parents.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parents.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parentsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Parents result = parentsRepository.save(parents);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, parents.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /parents/:id} : Partial updates given fields of an existing parents, field will ignore if it is null
     *
     * @param id the id of the parents to save.
     * @param parents the parents to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parents,
     * or with status {@code 400 (Bad Request)} if the parents is not valid,
     * or with status {@code 404 (Not Found)} if the parents is not found,
     * or with status {@code 500 (Internal Server Error)} if the parents couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Parents> partialUpdateParents(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Parents parents
    ) throws URISyntaxException {
        log.debug("REST request to partial update Parents partially : {}, {}", id, parents);
        if (parents.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parents.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parentsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Parents> result = parentsRepository
            .findById(parents.getId())
            .map(existingParents -> {
                if (parents.getParentsFristName() != null) {
                    existingParents.setParentsFristName(parents.getParentsFristName());
                }
                if (parents.getParentsLastName() != null) {
                    existingParents.setParentsLastName(parents.getParentsLastName());
                }

                return existingParents;
            })
            .map(parentsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, parents.getId().toString())
        );
    }

    /**
     * {@code GET  /parents} : get all the parents.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of parents in body.
     */
    @GetMapping("")
    public List<Parents> getAllParents(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        log.debug("REST request to get all Parents");
        if (eagerload) {
            return parentsRepository.findAllWithEagerRelationships();
        } else {
            return parentsRepository.findAll();
        }
    }

    /**
     * {@code GET  /parents/:id} : get the "id" parents.
     *
     * @param id the id of the parents to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the parents, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Parents> getParents(@PathVariable("id") Long id) {
        log.debug("REST request to get Parents : {}", id);
        Optional<Parents> parents = parentsRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(parents);
    }

    /**
     * {@code DELETE  /parents/:id} : delete the "id" parents.
     *
     * @param id the id of the parents to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParents(@PathVariable("id") Long id) {
        log.debug("REST request to delete Parents : {}", id);
        parentsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
