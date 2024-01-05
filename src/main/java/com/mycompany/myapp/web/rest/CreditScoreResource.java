package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.CreditScore;
import com.mycompany.myapp.repository.CreditScoreRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.CreditScore}.
 */
@RestController
@RequestMapping("/api/credit-scores")
@Transactional
public class CreditScoreResource {

    private final Logger log = LoggerFactory.getLogger(CreditScoreResource.class);

    private static final String ENTITY_NAME = "creditScore";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CreditScoreRepository creditScoreRepository;

    public CreditScoreResource(CreditScoreRepository creditScoreRepository) {
        this.creditScoreRepository = creditScoreRepository;
    }

    /**
     * {@code POST  /credit-scores} : Create a new creditScore.
     *
     * @param creditScore the creditScore to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new creditScore, or with status {@code 400 (Bad Request)} if the creditScore has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CreditScore> createCreditScore(@RequestBody CreditScore creditScore) throws URISyntaxException {
        log.debug("REST request to save CreditScore : {}", creditScore);
        if (creditScore.getId() != null) {
            throw new BadRequestAlertException("A new creditScore cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CreditScore result = creditScoreRepository.save(creditScore);
        return ResponseEntity
            .created(new URI("/api/credit-scores/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /credit-scores/:id} : Updates an existing creditScore.
     *
     * @param id the id of the creditScore to save.
     * @param creditScore the creditScore to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated creditScore,
     * or with status {@code 400 (Bad Request)} if the creditScore is not valid,
     * or with status {@code 500 (Internal Server Error)} if the creditScore couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CreditScore> updateCreditScore(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CreditScore creditScore
    ) throws URISyntaxException {
        log.debug("REST request to update CreditScore : {}, {}", id, creditScore);
        if (creditScore.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, creditScore.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!creditScoreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CreditScore result = creditScoreRepository.save(creditScore);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, creditScore.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /credit-scores/:id} : Partial updates given fields of an existing creditScore, field will ignore if it is null
     *
     * @param id the id of the creditScore to save.
     * @param creditScore the creditScore to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated creditScore,
     * or with status {@code 400 (Bad Request)} if the creditScore is not valid,
     * or with status {@code 404 (Not Found)} if the creditScore is not found,
     * or with status {@code 500 (Internal Server Error)} if the creditScore couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CreditScore> partialUpdateCreditScore(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CreditScore creditScore
    ) throws URISyntaxException {
        log.debug("REST request to partial update CreditScore partially : {}, {}", id, creditScore);
        if (creditScore.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, creditScore.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!creditScoreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CreditScore> result = creditScoreRepository
            .findById(creditScore.getId())
            .map(existingCreditScore -> {
                if (creditScore.getMonth() != null) {
                    existingCreditScore.setMonth(creditScore.getMonth());
                }
                if (creditScore.getScoreNumber() != null) {
                    existingCreditScore.setScoreNumber(creditScore.getScoreNumber());
                }

                return existingCreditScore;
            })
            .map(creditScoreRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, creditScore.getId().toString())
        );
    }

    /**
     * {@code GET  /credit-scores} : get all the creditScores.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of creditScores in body.
     */
    @GetMapping("")
    public List<CreditScore> getAllCreditScores(@RequestParam(name = "filter", required = false) String filter) {
        if ("parents-is-null".equals(filter)) {
            log.debug("REST request to get all CreditScores where parents is null");
            return StreamSupport
                .stream(creditScoreRepository.findAll().spliterator(), false)
                .filter(creditScore -> creditScore.getParents() == null)
                .toList();
        }

        if ("child-is-null".equals(filter)) {
            log.debug("REST request to get all CreditScores where child is null");
            return StreamSupport
                .stream(creditScoreRepository.findAll().spliterator(), false)
                .filter(creditScore -> creditScore.getChild() == null)
                .toList();
        }
        log.debug("REST request to get all CreditScores");
        return creditScoreRepository.findAll();
    }

    /**
     * {@code GET  /credit-scores/:id} : get the "id" creditScore.
     *
     * @param id the id of the creditScore to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the creditScore, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CreditScore> getCreditScore(@PathVariable("id") Long id) {
        log.debug("REST request to get CreditScore : {}", id);
        Optional<CreditScore> creditScore = creditScoreRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(creditScore);
    }

    /**
     * {@code DELETE  /credit-scores/:id} : delete the "id" creditScore.
     *
     * @param id the id of the creditScore to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCreditScore(@PathVariable("id") Long id) {
        log.debug("REST request to delete CreditScore : {}", id);
        creditScoreRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
