package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Achivement;
import com.mycompany.myapp.repository.AchivementRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Achivement}.
 */
@RestController
@RequestMapping("/api/achivements")
@Transactional
public class AchivementResource {

    private final Logger log = LoggerFactory.getLogger(AchivementResource.class);

    private static final String ENTITY_NAME = "achivement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AchivementRepository achivementRepository;

    public AchivementResource(AchivementRepository achivementRepository) {
        this.achivementRepository = achivementRepository;
    }

    /**
     * {@code POST  /achivements} : Create a new achivement.
     *
     * @param achivement the achivement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new achivement, or with status {@code 400 (Bad Request)} if the achivement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Achivement> createAchivement(@RequestBody Achivement achivement) throws URISyntaxException {
        log.debug("REST request to save Achivement : {}", achivement);
        if (achivement.getId() != null) {
            throw new BadRequestAlertException("A new achivement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Achivement result = achivementRepository.save(achivement);
        return ResponseEntity
            .created(new URI("/api/achivements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /achivements/:id} : Updates an existing achivement.
     *
     * @param id the id of the achivement to save.
     * @param achivement the achivement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated achivement,
     * or with status {@code 400 (Bad Request)} if the achivement is not valid,
     * or with status {@code 500 (Internal Server Error)} if the achivement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Achivement> updateAchivement(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Achivement achivement
    ) throws URISyntaxException {
        log.debug("REST request to update Achivement : {}, {}", id, achivement);
        if (achivement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, achivement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!achivementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Achivement result = achivementRepository.save(achivement);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, achivement.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /achivements/:id} : Partial updates given fields of an existing achivement, field will ignore if it is null
     *
     * @param id the id of the achivement to save.
     * @param achivement the achivement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated achivement,
     * or with status {@code 400 (Bad Request)} if the achivement is not valid,
     * or with status {@code 404 (Not Found)} if the achivement is not found,
     * or with status {@code 500 (Internal Server Error)} if the achivement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Achivement> partialUpdateAchivement(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Achivement achivement
    ) throws URISyntaxException {
        log.debug("REST request to partial update Achivement partially : {}, {}", id, achivement);
        if (achivement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, achivement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!achivementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Achivement> result = achivementRepository
            .findById(achivement.getId())
            .map(existingAchivement -> {
                if (achivement.getName() != null) {
                    existingAchivement.setName(achivement.getName());
                }
                if (achivement.getPointValue() != null) {
                    existingAchivement.setPointValue(achivement.getPointValue());
                }

                return existingAchivement;
            })
            .map(achivementRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, achivement.getId().toString())
        );
    }

    /**
     * {@code GET  /achivements} : get all the achivements.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of achivements in body.
     */
    @GetMapping("")
    public List<Achivement> getAllAchivements() {
        log.debug("REST request to get all Achivements");
        return achivementRepository.findAll();
    }

    /**
     * {@code GET  /achivements/:id} : get the "id" achivement.
     *
     * @param id the id of the achivement to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the achivement, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Achivement> getAchivement(@PathVariable("id") Long id) {
        log.debug("REST request to get Achivement : {}", id);
        Optional<Achivement> achivement = achivementRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(achivement);
    }

    /**
     * {@code DELETE  /achivements/:id} : delete the "id" achivement.
     *
     * @param id the id of the achivement to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAchivement(@PathVariable("id") Long id) {
        log.debug("REST request to delete Achivement : {}", id);
        achivementRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
