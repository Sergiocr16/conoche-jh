package com.firefly.conoche.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.firefly.conoche.domain.Promotion;
import com.firefly.conoche.service.PromotionCodeService;
import com.firefly.conoche.service.PromotionService;
import com.firefly.conoche.service.dto.PromotionDTO;
import com.firefly.conoche.web.rest.util.HeaderUtil;
import com.firefly.conoche.web.rest.util.PaginationUtil;
import com.firefly.conoche.service.dto.PromotionCodeDTO;
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
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing PromotionCode.
 */
@RestController
@RequestMapping("/api")
public class PromotionCodeResource {

    private final Logger log = LoggerFactory.getLogger(PromotionCodeResource.class);

    private static final String ENTITY_NAME = "promotionCode";

    private final PromotionCodeService promotionCodeService;

    private final PromotionService promotionService;

    public PromotionCodeResource(PromotionCodeService promotionCodeService,PromotionService promotionService) {
        this.promotionCodeService = promotionCodeService;
        this.promotionService = promotionService;
    }

    /**
     * POST  /promotion-codes : Create a new promotionCode.
     * author Sergio
     * @param promotionCodeDTO the promotionCodeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new promotionCodeDTO, or with status 400 (Bad Request) if the promotionCode has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/promotion-codes")
    @Timed
    public ResponseEntity<PromotionCodeDTO> createPromotionCode(@Valid @RequestBody PromotionCodeDTO promotionCodeDTO) throws URISyntaxException {
        log.debug("REST request to save PromotionCode : {}", promotionCodeDTO);
        if (promotionCodeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new promotionCode cannot already have an ID")).body(null);
        }
        PromotionCodeDTO result = promotionCodeService.save(promotionCodeDTO);
        return ResponseEntity.created(new URI("/api/promotion-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /promotion-codes : Updates an existing promotionCode.
     * author Sergio
     * @param promotionCodeDTO the promotionCodeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated promotionCodeDTO,
     * or with status 400 (Bad Request) if the promotionCodeDTO is not valid,
     * or with status 500 (Internal Server Error) if the promotionCodeDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/promotion-codes")
    @Timed
    public ResponseEntity<PromotionCodeDTO> updatePromotionCode(@Valid @RequestBody PromotionCodeDTO promotionCodeDTO) throws URISyntaxException {
        log.debug("REST request to update PromotionCode : {}", promotionCodeDTO);
        if (promotionCodeDTO.getId() == null) {
            return createPromotionCode(promotionCodeDTO);
        }
        PromotionCodeDTO result = promotionCodeService.save(promotionCodeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, promotionCodeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /promotion-codes : get all the promotionCodes.
     * author Sergio
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of promotionCodes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/promotion-codes")
    @Timed
    public ResponseEntity<List<PromotionCodeDTO>> getAllPromotionCodes(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of PromotionCodes");
        Page<PromotionCodeDTO> page = promotionCodeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/promotion-codes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    /**
     * GET  /promotion-codes : get all the promotionCodes by user and promotion.
     * author Sergio
     * @param userId the user id
     * @param promotionId the promotion id
     * @return the ResponseEntity with status 200 (OK) and the list of promotionCodes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/promotion-codes/byUserAndPromotion/{userId}/{promotionId}")
    @Timed
    public ResponseEntity<List<PromotionCodeDTO>> getByUserIdAndPromotionId( @PathVariable (value = "userId")  Long userId, @PathVariable(value = "promotionId")  Long promotionId)
        throws URISyntaxException {
        log.debug("REST request to get a page of PromotionCodes");
        Page<PromotionCodeDTO> page = promotionCodeService.findByUserAndPromotion(userId,promotionId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/promotion-codes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    /**
     * GET  /promotion-codes : get all the promotionCodes by user
     * author Sergio
     * @param userId the user id
     * @return the ResponseEntity with status 200 (OK) and the list of promotionCodes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/promotion-codes/byUser/{userId}")
    @Timed
    public ResponseEntity<List<PromotionCodeDTO>> getByUser( @PathVariable (value = "userId")  Long userId)
        throws URISyntaxException {
        log.debug("REST request to get a page of PromotionCodes");
        Page<PromotionCodeDTO> page = promotionCodeService.findByUserId(userId);
        page.forEach(promotionCodeDTO -> {
            PromotionDTO promo = this.promotionService.findOne(promotionCodeDTO.getPromotionId());
            if(promo.getFinalTime().isBefore(ZonedDateTime.now())){
                this.promotionCodeService.delete(promotionCodeDTO.getId());
            }
        });
        Page<PromotionCodeDTO> result = promotionCodeService.findByUserId(userId);
        result.forEach(promotionCodeDTO -> {
            promotionCodeDTO.setPromotion(this.promotionService.findOne(promotionCodeDTO.getPromotionId()));
        });
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/promotion-codes");
        return new ResponseEntity<>(result.getContent(), headers, HttpStatus.OK);
    }
    /**
     * GET  /promotion-codes : get all the promotionCodes by promotion.
     * author Sergio
     * @param promotionId the promotion id
     * @return the ResponseEntity with status 200 (OK) and the list of promotionCodes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/promotion-codes/availableByPromotion/{promotionId}")
    @Timed
    public ResponseEntity<List<PromotionCodeDTO>> getAvailableByPromotionId(@PathVariable(value = "promotionId")  Long promotionId)
        throws URISyntaxException {
        log.debug("REST request to get a page of PromotionCodes");
        Page<PromotionCodeDTO> page = promotionCodeService.findAvailableCodesByPromotionId(promotionId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/promotion-codes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    /**
     * Redeem a code cased on the user id and the promotion id
     * author Sergio
     * @param userId the user id
     * @param promotionId the promotion id
     * @return the ResponseEntity with status 200 (OK)
     */
    @GetMapping("/promotion-codes/redeem/{promotionId}/{userId}")
    @Timed
    public ResponseEntity<List<PromotionCodeDTO>> redemCode(@PathVariable(value = "promotionId")  Long promotionId,@PathVariable(value = "userId")  Long userId)
        throws URISyntaxException {
        log.debug("REST request to get a page of PromotionCodes");
        PromotionCodeDTO page = promotionCodeService.redeemCodeInPromotion(promotionId,userId);
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders("/api/promotion-codes");
        return new ResponseEntity<>(HttpStatus.OK);
    }
    /**
     * GET  /promotion-codes : get all the promotionCodes by code.
     * author Sergio
     * @param code the code
     * @return the ResponseEntity with status 200 (OK) and the list of promotionCodes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/promotion-codes/findCode/{code}")
    @Timed
    public ResponseEntity<PromotionCodeDTO> findByCode(@PathVariable(value = "code")  String code)
        throws URISyntaxException {
        log.debug("REST request to get a page of PromotionCodes");
        PromotionCodeDTO result = promotionCodeService.findByCode(code);
        if(result!=null) {
            result.setPromotion(this.promotionService.findOne(result.getPromotionId()));
            return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * GET  /promotion-codes/:id : get the "id" promotionCode.
     *
     * @param id the id of the promotionCodeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the promotionCodeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/promotion-codes/{id}")
    @Timed
    public ResponseEntity<PromotionCodeDTO> getPromotionCode(@PathVariable Long id) {
        log.debug("REST request to get PromotionCode : {}", id);
        PromotionCodeDTO promotionCodeDTO = promotionCodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(promotionCodeDTO));
    }

    /**
     * DELETE  /promotion-codes/:id : delete the "id" promotionCode.
     *
     * @param id the id of the promotionCodeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/promotion-codes/{id}")
    @Timed
    public ResponseEntity<Void> deletePromotionCode(@PathVariable Long id) {
        log.debug("REST request to delete PromotionCode : {}", id);
        promotionCodeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
