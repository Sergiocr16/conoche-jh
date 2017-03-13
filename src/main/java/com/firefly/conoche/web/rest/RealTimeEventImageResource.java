package com.firefly.conoche.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.firefly.conoche.service.RealTimeEventImageService;
import com.firefly.conoche.web.rest.util.HeaderUtil;
import com.firefly.conoche.web.rest.util.PaginationUtil;
import com.firefly.conoche.service.dto.RealTimeEventImageDTO;
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
 * REST controller for managing RealTimeEventImage.
 */
@RestController
@RequestMapping("/api")
public class RealTimeEventImageResource {

    private final Logger log = LoggerFactory.getLogger(RealTimeEventImageResource.class);

    private static final String ENTITY_NAME = "realTimeEventImage";

    private final RealTimeEventImageService realTimeEventImageService;

    public RealTimeEventImageResource(RealTimeEventImageService realTimeEventImageService) {
        this.realTimeEventImageService = realTimeEventImageService;
    }

    /**
     * POST  /real-time-event-images : Create a new realTimeEventImage.
     *
     * @param realTimeEventImageDTO the realTimeEventImageDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new realTimeEventImageDTO, or with status 400 (Bad Request) if the realTimeEventImage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/real-time-event-images")
    @Timed
    public ResponseEntity<RealTimeEventImageDTO> createRealTimeEventImage(@Valid @RequestBody RealTimeEventImageDTO realTimeEventImageDTO) throws URISyntaxException {
        log.debug("REST request to save RealTimeEventImage : {}", realTimeEventImageDTO);
        if (realTimeEventImageDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new realTimeEventImage cannot already have an ID")).body(null);
        }
        RealTimeEventImageDTO result = realTimeEventImageService.save(realTimeEventImageDTO);
        return ResponseEntity.created(new URI("/api/real-time-event-images/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /real-time-event-images : Updates an existing realTimeEventImage.
     *
     * @param realTimeEventImageDTO the realTimeEventImageDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated realTimeEventImageDTO,
     * or with status 400 (Bad Request) if the realTimeEventImageDTO is not valid,
     * or with status 500 (Internal Server Error) if the realTimeEventImageDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/real-time-event-images")
    @Timed
    public ResponseEntity<RealTimeEventImageDTO> updateRealTimeEventImage(@Valid @RequestBody RealTimeEventImageDTO realTimeEventImageDTO) throws URISyntaxException {
        log.debug("REST request to update RealTimeEventImage : {}", realTimeEventImageDTO);
        if (realTimeEventImageDTO.getId() == null) {
            return createRealTimeEventImage(realTimeEventImageDTO);
        }
        RealTimeEventImageDTO result = realTimeEventImageService.save(realTimeEventImageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, realTimeEventImageDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /real-time-event-images : get all the realTimeEventImages.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of realTimeEventImages in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/real-time-event-images")
    @Timed
    public ResponseEntity<List<RealTimeEventImageDTO>> getAllRealTimeEventImages(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of RealTimeEventImages");
        Page<RealTimeEventImageDTO> page = realTimeEventImageService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/real-time-event-images");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /real-time-event-images/:id : get the "id" realTimeEventImage.
     *
     * @param id the id of the realTimeEventImageDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the realTimeEventImageDTO, or with status 404 (Not Found)
     */
    @GetMapping("/real-time-event-images/{id}")
    @Timed
    public ResponseEntity<RealTimeEventImageDTO> getRealTimeEventImage(@PathVariable Long id) {
        log.debug("REST request to get RealTimeEventImage : {}", id);
        RealTimeEventImageDTO realTimeEventImageDTO = realTimeEventImageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(realTimeEventImageDTO));
    }

    /**
     * DELETE  /real-time-event-images/:id : delete the "id" realTimeEventImage.
     *
     * @param id the id of the realTimeEventImageDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/real-time-event-images/{id}")
    @Timed
    public ResponseEntity<Void> deleteRealTimeEventImage(@PathVariable Long id) {
        log.debug("REST request to delete RealTimeEventImage : {}", id);
        realTimeEventImageService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
