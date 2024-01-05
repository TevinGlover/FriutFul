package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Child;
import com.mycompany.myapp.repository.ChildRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Child}.
 */
@RestController
@RequestMapping("/api/children")
@Transactional
public class ChildResource {

    private final Logger log = LoggerFactory.getLogger(ChildResource.class);

    private static final String ENTITY_NAME = "child";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChildRepository childRepository;

    public ChildResource(ChildRepository childRepository) {
        this.childRepository = childRepository;
    }

    /**
     * {@code POST  /children} : Create a new child.
     *
     * @param child the child to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new child, or with status {@code 400 (Bad Request)} if the child has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Child> createChild(@RequestBody Child child) throws URISyntaxException {
        log.debug("REST request to save Child : {}", child);
        if (child.getId() != null) {
            throw new BadRequestAlertException("A new child cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Child result = childRepository.save(child);
        return ResponseEntity
            .created(new URI("/api/children/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /children/:id} : Updates an existing child.
     *
     * @param id the id of the child to save.
     * @param child the child to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated child,
     * or with status {@code 400 (Bad Request)} if the child is not valid,
     * or with status {@code 500 (Internal Server Error)} if the child couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Child> updateChild(@PathVariable(value = "id", required = false) final Long id, @RequestBody Child child)
        throws URISyntaxException {
        log.debug("REST request to update Child : {}, {}", id, child);
        if (child.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, child.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!childRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Child result = childRepository.save(child);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, child.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /children/:id} : Partial updates given fields of an existing child, field will ignore if it is null
     *
     * @param id the id of the child to save.
     * @param child the child to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated child,
     * or with status {@code 400 (Bad Request)} if the child is not valid,
     * or with status {@code 404 (Not Found)} if the child is not found,
     * or with status {@code 500 (Internal Server Error)} if the child couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Child> partialUpdateChild(@PathVariable(value = "id", required = false) final Long id, @RequestBody Child child)
        throws URISyntaxException {
        log.debug("REST request to partial update Child partially : {}, {}", id, child);
        if (child.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, child.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!childRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Child> result = childRepository
            .findById(child.getId())
            .map(existingChild -> {
                if (child.getUserFristName() != null) {
                    existingChild.setUserFristName(child.getUserFristName());
                }
                if (child.getUserLastName() != null) {
                    existingChild.setUserLastName(child.getUserLastName());
                }

                return existingChild;
            })
            .map(childRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, child.getId().toString())
        );
    }

    /**
     * {@code GET  /children} : get all the children.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of children in body.
     */
    @GetMapping("")
    public List<Child> getAllChildren() {
        log.debug("REST request to get all Children");
        return childRepository.findAll();
    }

    /**
     * {@code GET  /children/:id} : get the "id" child.
     *
     * @param id the id of the child to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the child, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Child> getChild(@PathVariable("id") Long id) {
        log.debug("REST request to get Child : {}", id);
        Optional<Child> child = childRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(child);
    }

    /**
     * {@code DELETE  /children/:id} : delete the "id" child.
     *
     * @param id the id of the child to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChild(@PathVariable("id") Long id) {
        log.debug("REST request to delete Child : {}", id);
        childRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
