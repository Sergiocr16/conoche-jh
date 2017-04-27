package com.firefly.conoche.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.firefly.conoche.service.LocalService;
import com.firefly.conoche.web.rest.util.HeaderUtil;
import com.firefly.conoche.web.rest.util.PaginationUtil;
import com.firefly.conoche.service.dto.LocalDTO;
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
import java.util.List;
import java.util.Optional;

/**
 * Mover los metodos createLocal y getAllByCategory a otro archivo para evitar perder datos al regenerar la entidad
 */
@RestController
@RequestMapping("/api")
public class LocalResource {

    private final Logger log = LoggerFactory.getLogger(LocalResource.class);

    private static final String ENTITY_NAME = "local";

    private final LocalService localService;


    public LocalResource(LocalService localService) {
        this.localService = localService;
    }

    /**
     * POST  /locals : Create a new local.
     *
     * @param localDTO the localDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new localDTO, or with status 400 (Bad Request) if the local has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/locals")
    @Timed
    public ResponseEntity<LocalDTO> createLocal(@Valid @RequestBody LocalDTO localDTO) throws URISyntaxException {
        log.debug("REST request to save Local : {}", localDTO);
        if (localDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new local cannot already have an ID")).body(null);
        }
        LocalDTO result = localService.saveWithCurrentUser(localDTO);
        return ResponseEntity.created(new URI("/api/locals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /locals : Updates an existing local.
     *
     * @param localDTO the localDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated localDTO,
     * or with status 400 (Bad Request) if the localDTO is not valid,
     * or with status 500 (Internal Server Error) if the localDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/locals")
    @Timed
    public ResponseEntity<LocalDTO> updateLocal(@Valid @RequestBody LocalDTO localDTO) throws URISyntaxException {
        log.debug("REST request to update Local : {}", localDTO);
        if (localDTO.getId() == null) {
            return createLocal(localDTO);
        }
        LocalDTO result = localService.save(localDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, localDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /locals : get all the locals.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of locals in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/locals")
    @Timed
    public ResponseEntity<List<LocalDTO>> getAllLocals(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Locals");
        Page<LocalDTO> page = localService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/locals");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/locals/byCategory/{categoryId}")
    @Timed
    public ResponseEntity<List<LocalDTO>> getAllByCategory(@PathVariable Long categoryId,@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Locals");
        Page<LocalDTO> page = localService.findByCategoryId(categoryId,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/locals");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    /**
     * GET  /locals/:id : get the "id" local.
     *
     * @param id the id of the localDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the localDTO, or with status 404 (Not Found)
     */
    @GetMapping("/locals/{id}")
    @Timed
    public ResponseEntity<LocalDTO> getLocal(@PathVariable Long id) {
        log.debug("REST request to get Local : {}", id);
        LocalDTO localDTO = localService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(localDTO));
    }

    /**
     * DELETE  /locals/:id : delete the "id" local.
     *
     * @param id the id of the localDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/locals/{id}")
    @Timed
    public ResponseEntity<Void> deleteLocal(@PathVariable Long id) {
        log.debug("REST request to delete Local : {}", id);
        localService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/getByOwner")
    @Timed
    public ResponseEntity<List<LocalDTO>> getByOwner(@ApiParam Pageable pageable, Long ownerId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Residents");
        Page<LocalDTO> page = localService.getByOwner(pageable,ownerId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "api/getByOwner");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PostMapping("/locals/subscribeLocal")
    @Timed
    public void subscribeToLocal(@RequestBody Long idLocal) throws URISyntaxException {
        localService.subscribeLocal(idLocal);
    }

    @PostMapping("/locals/unsubscribeLocal")
    @Timed
    public void unsubscribeToLocal(@RequestBody Long idLocal) throws URISyntaxException {
        localService.unsubscribeLocal(idLocal);
    }

}
