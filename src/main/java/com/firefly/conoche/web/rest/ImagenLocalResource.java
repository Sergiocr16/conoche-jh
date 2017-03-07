package com.firefly.conoche.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.firefly.conoche.service.ImagenLocalService;
import com.firefly.conoche.web.rest.util.HeaderUtil;
import com.firefly.conoche.web.rest.util.PaginationUtil;
import com.firefly.conoche.service.dto.ImagenLocalDTO;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing ImagenLocal.
 */
@RestController
@RequestMapping("/api")
public class ImagenLocalResource {

    private final Logger log = LoggerFactory.getLogger(ImagenLocalResource.class);

    private static final String ENTITY_NAME = "imagenLocal";
        
    private final ImagenLocalService imagenLocalService;

    public ImagenLocalResource(ImagenLocalService imagenLocalService) {
        this.imagenLocalService = imagenLocalService;
    }

    /**
     * POST  /imagen-locals : Create a new imagenLocal.
     *
     * @param imagenLocalDTO the imagenLocalDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new imagenLocalDTO, or with status 400 (Bad Request) if the imagenLocal has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/imagen-locals")
    @Timed
    public ResponseEntity<ImagenLocalDTO> createImagenLocal(@RequestBody ImagenLocalDTO imagenLocalDTO) throws URISyntaxException {
        log.debug("REST request to save ImagenLocal : {}", imagenLocalDTO);
        if (imagenLocalDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new imagenLocal cannot already have an ID")).body(null);
        }
        ImagenLocalDTO result = imagenLocalService.save(imagenLocalDTO);
        return ResponseEntity.created(new URI("/api/imagen-locals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /imagen-locals : Updates an existing imagenLocal.
     *
     * @param imagenLocalDTO the imagenLocalDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated imagenLocalDTO,
     * or with status 400 (Bad Request) if the imagenLocalDTO is not valid,
     * or with status 500 (Internal Server Error) if the imagenLocalDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/imagen-locals")
    @Timed
    public ResponseEntity<ImagenLocalDTO> updateImagenLocal(@RequestBody ImagenLocalDTO imagenLocalDTO) throws URISyntaxException {
        log.debug("REST request to update ImagenLocal : {}", imagenLocalDTO);
        if (imagenLocalDTO.getId() == null) {
            return createImagenLocal(imagenLocalDTO);
        }
        ImagenLocalDTO result = imagenLocalService.save(imagenLocalDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, imagenLocalDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /imagen-locals : get all the imagenLocals.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of imagenLocals in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/imagen-locals")
    @Timed
    public ResponseEntity<List<ImagenLocalDTO>> getAllImagenLocals(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ImagenLocals");
        Page<ImagenLocalDTO> page = imagenLocalService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/imagen-locals");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /imagen-locals/:id : get the "id" imagenLocal.
     *
     * @param id the id of the imagenLocalDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the imagenLocalDTO, or with status 404 (Not Found)
     */
    @GetMapping("/imagen-locals/{id}")
    @Timed
    public ResponseEntity<ImagenLocalDTO> getImagenLocal(@PathVariable Long id) {
        log.debug("REST request to get ImagenLocal : {}", id);
        ImagenLocalDTO imagenLocalDTO = imagenLocalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(imagenLocalDTO));
    }

    /**
     * DELETE  /imagen-locals/:id : delete the "id" imagenLocal.
     *
     * @param id the id of the imagenLocalDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/imagen-locals/{id}")
    @Timed
    public ResponseEntity<Void> deleteImagenLocal(@PathVariable Long id) {
        log.debug("REST request to delete ImagenLocal : {}", id);
        imagenLocalService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
