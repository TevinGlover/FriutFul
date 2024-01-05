package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Balance;
import com.mycompany.myapp.repository.BalanceRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Balance}.
 */
@RestController
@RequestMapping("/api/balances")
@Transactional
public class BalanceResource {

    private final Logger log = LoggerFactory.getLogger(BalanceResource.class);

    private static final String ENTITY_NAME = "balance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BalanceRepository balanceRepository;

    public BalanceResource(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    /**
     * {@code POST  /balances} : Create a new balance.
     *
     * @param balance the balance to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new balance, or with status {@code 400 (Bad Request)} if the balance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Balance> createBalance(@RequestBody Balance balance) throws URISyntaxException {
        log.debug("REST request to save Balance : {}", balance);
        if (balance.getId() != null) {
            throw new BadRequestAlertException("A new balance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Balance result = balanceRepository.save(balance);
        return ResponseEntity
            .created(new URI("/api/balances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /balances/:id} : Updates an existing balance.
     *
     * @param id the id of the balance to save.
     * @param balance the balance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated balance,
     * or with status {@code 400 (Bad Request)} if the balance is not valid,
     * or with status {@code 500 (Internal Server Error)} if the balance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Balance> updateBalance(@PathVariable(value = "id", required = false) final Long id, @RequestBody Balance balance)
        throws URISyntaxException {
        log.debug("REST request to update Balance : {}, {}", id, balance);
        if (balance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, balance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!balanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Balance result = balanceRepository.save(balance);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, balance.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /balances/:id} : Partial updates given fields of an existing balance, field will ignore if it is null
     *
     * @param id the id of the balance to save.
     * @param balance the balance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated balance,
     * or with status {@code 400 (Bad Request)} if the balance is not valid,
     * or with status {@code 404 (Not Found)} if the balance is not found,
     * or with status {@code 500 (Internal Server Error)} if the balance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Balance> partialUpdateBalance(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Balance balance
    ) throws URISyntaxException {
        log.debug("REST request to partial update Balance partially : {}, {}", id, balance);
        if (balance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, balance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!balanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Balance> result = balanceRepository
            .findById(balance.getId())
            .map(existingBalance -> {
                if (balance.getCreditCardType() != null) {
                    existingBalance.setCreditCardType(balance.getCreditCardType());
                }
                if (balance.getCreditcardNum() != null) {
                    existingBalance.setCreditcardNum(balance.getCreditcardNum());
                }
                if (balance.getVaildThru() != null) {
                    existingBalance.setVaildThru(balance.getVaildThru());
                }
                if (balance.getCvs() != null) {
                    existingBalance.setCvs(balance.getCvs());
                }
                if (balance.getMaxLimit() != null) {
                    existingBalance.setMaxLimit(balance.getMaxLimit());
                }
                if (balance.getThrityPrecentLimit() != null) {
                    existingBalance.setThrityPrecentLimit(balance.getThrityPrecentLimit());
                }

                return existingBalance;
            })
            .map(balanceRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, balance.getId().toString())
        );
    }

    /**
     * {@code GET  /balances} : get all the balances.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of balances in body.
     */
    @GetMapping("")
    public List<Balance> getAllBalances(@RequestParam(name = "filter", required = false) String filter) {
        if ("child-is-null".equals(filter)) {
            log.debug("REST request to get all Balances where child is null");
            return StreamSupport
                .stream(balanceRepository.findAll().spliterator(), false)
                .filter(balance -> balance.getChild() == null)
                .toList();
        }
        log.debug("REST request to get all Balances");
        return balanceRepository.findAll();
    }

    /**
     * {@code GET  /balances/:id} : get the "id" balance.
     *
     * @param id the id of the balance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the balance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Balance> getBalance(@PathVariable("id") Long id) {
        log.debug("REST request to get Balance : {}", id);
        Optional<Balance> balance = balanceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(balance);
    }

    /**
     * {@code DELETE  /balances/:id} : delete the "id" balance.
     *
     * @param id the id of the balance to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBalance(@PathVariable("id") Long id) {
        log.debug("REST request to delete Balance : {}", id);
        balanceRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
