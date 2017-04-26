package com.firefly.conoche.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.firefly.conoche.service.ObjectChangeService;
import com.firefly.conoche.web.rest.util.HeaderUtil;
import com.firefly.conoche.web.rest.util.PaginationUtil;
import com.firefly.conoche.service.dto.ObjectChangeDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing ObjectChange.
 */
@RestController
@RequestMapping("/api")
public class ObjectChangeResource {

    private final Logger log = LoggerFactory.getLogger(ObjectChangeResource.class);

    private static final String ENTITY_NAME = "objectChange";
        
    private final ObjectChangeService objectChangeService;

    public ObjectChangeResource(ObjectChangeService objectChangeService) {
        this.objectChangeService = objectChangeService;
    }

    /**
     * POST  /object-changes : Create a new objectChange.
     *
     * @param objectChangeDTO the objectChangeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new objectChangeDTO, or with status 400 (Bad Request) if the objectChange has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/object-changes")
    @Timed
    public ResponseEntity<ObjectChangeDTO> createObjectChange(@Valid @RequestBody ObjectChangeDTO objectChangeDTO) throws URISyntaxException {
        log.debug("REST request to save ObjectChange : {}", objectChangeDTO);
        if (objectChangeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new objectChange cannot already have an ID")).body(null);
        }
        ObjectChangeDTO result = objectChangeService.save(objectChangeDTO);
        return ResponseEntity.created(new URI("/api/object-changes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /object-changes : Updates an existing objectChange.
     *
     * @param objectChangeDTO the objectChangeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated objectChangeDTO,
     * or with status 400 (Bad Request) if the objectChangeDTO is not valid,
     * or with status 500 (Internal Server Error) if the objectChangeDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/object-changes")
    @Timed
    public ResponseEntity<ObjectChangeDTO> updateObjectChange(@Valid @RequestBody ObjectChangeDTO objectChangeDTO) throws URISyntaxException {
        log.debug("REST request to update ObjectChange : {}", objectChangeDTO);
        if (objectChangeDTO.getId() == null) {
            return createObjectChange(objectChangeDTO);
        }
        ObjectChangeDTO result = objectChangeService.save(objectChangeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, objectChangeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /object-changes : get all the objectChanges.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of objectChanges in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/object-changes")
    @Timed
    public ResponseEntity<List<ObjectChangeDTO>> getAllObjectChanges(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ObjectChanges");
        Page<ObjectChangeDTO> page = objectChangeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/object-changes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /object-changes/:id : get the "id" objectChange.
     *
     * @param id the id of the objectChangeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the objectChangeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/object-changes/{id}")
    @Timed
    public ResponseEntity<ObjectChangeDTO> getObjectChange(@PathVariable Long id) {
        log.debug("REST request to get ObjectChange : {}", id);
        ObjectChangeDTO objectChangeDTO = objectChangeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(objectChangeDTO));
    }

    /**
     * DELETE  /object-changes/:id : delete the "id" objectChange.
     *
     * @param id the id of the objectChangeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/object-changes/{id}")
    @Timed
    public ResponseEntity<Void> deleteObjectChange(@PathVariable Long id) {
        log.debug("REST request to delete ObjectChange : {}", id);
        objectChangeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
