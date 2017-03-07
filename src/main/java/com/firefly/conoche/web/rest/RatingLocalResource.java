package com.firefly.conoche.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.firefly.conoche.service.RatingLocalService;
import com.firefly.conoche.web.rest.util.HeaderUtil;
import com.firefly.conoche.web.rest.util.PaginationUtil;
import com.firefly.conoche.service.dto.RatingLocalDTO;
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
 * REST controller for managing RatingLocal.
 */
@RestController
@RequestMapping("/api")
public class RatingLocalResource {

    private final Logger log = LoggerFactory.getLogger(RatingLocalResource.class);

    private static final String ENTITY_NAME = "ratingLocal";
        
    private final RatingLocalService ratingLocalService;

    public RatingLocalResource(RatingLocalService ratingLocalService) {
        this.ratingLocalService = ratingLocalService;
    }

    /**
     * POST  /rating-locals : Create a new ratingLocal.
     *
     * @param ratingLocalDTO the ratingLocalDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ratingLocalDTO, or with status 400 (Bad Request) if the ratingLocal has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rating-locals")
    @Timed
    public ResponseEntity<RatingLocalDTO> createRatingLocal(@Valid @RequestBody RatingLocalDTO ratingLocalDTO) throws URISyntaxException {
        log.debug("REST request to save RatingLocal : {}", ratingLocalDTO);
        if (ratingLocalDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new ratingLocal cannot already have an ID")).body(null);
        }
        RatingLocalDTO result = ratingLocalService.save(ratingLocalDTO);
        return ResponseEntity.created(new URI("/api/rating-locals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rating-locals : Updates an existing ratingLocal.
     *
     * @param ratingLocalDTO the ratingLocalDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ratingLocalDTO,
     * or with status 400 (Bad Request) if the ratingLocalDTO is not valid,
     * or with status 500 (Internal Server Error) if the ratingLocalDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rating-locals")
    @Timed
    public ResponseEntity<RatingLocalDTO> updateRatingLocal(@Valid @RequestBody RatingLocalDTO ratingLocalDTO) throws URISyntaxException {
        log.debug("REST request to update RatingLocal : {}", ratingLocalDTO);
        if (ratingLocalDTO.getId() == null) {
            return createRatingLocal(ratingLocalDTO);
        }
        RatingLocalDTO result = ratingLocalService.save(ratingLocalDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ratingLocalDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rating-locals : get all the ratingLocals.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of ratingLocals in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/rating-locals")
    @Timed
    public ResponseEntity<List<RatingLocalDTO>> getAllRatingLocals(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of RatingLocals");
        Page<RatingLocalDTO> page = ratingLocalService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/rating-locals");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /rating-locals/:id : get the "id" ratingLocal.
     *
     * @param id the id of the ratingLocalDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ratingLocalDTO, or with status 404 (Not Found)
     */
    @GetMapping("/rating-locals/{id}")
    @Timed
    public ResponseEntity<RatingLocalDTO> getRatingLocal(@PathVariable Long id) {
        log.debug("REST request to get RatingLocal : {}", id);
        RatingLocalDTO ratingLocalDTO = ratingLocalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(ratingLocalDTO));
    }

    /**
     * DELETE  /rating-locals/:id : delete the "id" ratingLocal.
     *
     * @param id the id of the ratingLocalDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rating-locals/{id}")
    @Timed
    public ResponseEntity<Void> deleteRatingLocal(@PathVariable Long id) {
        log.debug("REST request to delete RatingLocal : {}", id);
        ratingLocalService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
