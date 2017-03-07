package com.firefly.conoche.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.firefly.conoche.service.ActionObjectService;
import com.firefly.conoche.web.rest.util.HeaderUtil;
import com.firefly.conoche.web.rest.util.PaginationUtil;
import com.firefly.conoche.service.dto.ActionObjectDTO;
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
 * REST controller for managing ActionObject.
 */
@RestController
@RequestMapping("/api")
public class ActionObjectResource {

    private final Logger log = LoggerFactory.getLogger(ActionObjectResource.class);

    private static final String ENTITY_NAME = "actionObject";
        
    private final ActionObjectService actionObjectService;

    public ActionObjectResource(ActionObjectService actionObjectService) {
        this.actionObjectService = actionObjectService;
    }

    /**
     * POST  /action-objects : Create a new actionObject.
     *
     * @param actionObjectDTO the actionObjectDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new actionObjectDTO, or with status 400 (Bad Request) if the actionObject has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/action-objects")
    @Timed
    public ResponseEntity<ActionObjectDTO> createActionObject(@Valid @RequestBody ActionObjectDTO actionObjectDTO) throws URISyntaxException {
        log.debug("REST request to save ActionObject : {}", actionObjectDTO);
        if (actionObjectDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new actionObject cannot already have an ID")).body(null);
        }
        ActionObjectDTO result = actionObjectService.save(actionObjectDTO);
        return ResponseEntity.created(new URI("/api/action-objects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /action-objects : Updates an existing actionObject.
     *
     * @param actionObjectDTO the actionObjectDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated actionObjectDTO,
     * or with status 400 (Bad Request) if the actionObjectDTO is not valid,
     * or with status 500 (Internal Server Error) if the actionObjectDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/action-objects")
    @Timed
    public ResponseEntity<ActionObjectDTO> updateActionObject(@Valid @RequestBody ActionObjectDTO actionObjectDTO) throws URISyntaxException {
        log.debug("REST request to update ActionObject : {}", actionObjectDTO);
        if (actionObjectDTO.getId() == null) {
            return createActionObject(actionObjectDTO);
        }
        ActionObjectDTO result = actionObjectService.save(actionObjectDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, actionObjectDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /action-objects : get all the actionObjects.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of actionObjects in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/action-objects")
    @Timed
    public ResponseEntity<List<ActionObjectDTO>> getAllActionObjects(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ActionObjects");
        Page<ActionObjectDTO> page = actionObjectService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/action-objects");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /action-objects/:id : get the "id" actionObject.
     *
     * @param id the id of the actionObjectDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the actionObjectDTO, or with status 404 (Not Found)
     */
    @GetMapping("/action-objects/{id}")
    @Timed
    public ResponseEntity<ActionObjectDTO> getActionObject(@PathVariable Long id) {
        log.debug("REST request to get ActionObject : {}", id);
        ActionObjectDTO actionObjectDTO = actionObjectService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(actionObjectDTO));
    }

    /**
     * DELETE  /action-objects/:id : delete the "id" actionObject.
     *
     * @param id the id of the actionObjectDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/action-objects/{id}")
    @Timed
    public ResponseEntity<Void> deleteActionObject(@PathVariable Long id) {
        log.debug("REST request to delete ActionObject : {}", id);
        actionObjectService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
