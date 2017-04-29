package com.firefly.conoche.service;

import com.firefly.conoche.domain.Promotion;
import com.firefly.conoche.repository.PromotionCodeRepository;
import com.firefly.conoche.repository.PromotionRepository;
import com.firefly.conoche.service.dto.PromotionDTO;
import com.firefly.conoche.service.mapper.PromotionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Promotion.
 */
@Service
@Transactional
public class PromotionService {

    private final Logger log = LoggerFactory.getLogger(PromotionService.class);

    private final PromotionRepository promotionRepository;

    private final PromotionCodeRepository promotionCodeRepository;

    private final PromotionMapper promotionMapper;

    public PromotionService(PromotionRepository promotionRepository, PromotionMapper promotionMapper,PromotionCodeRepository promotionCodeRepository) {
        this.promotionRepository = promotionRepository;
        this.promotionMapper = promotionMapper;
        this.promotionCodeRepository = promotionCodeRepository;
    }

    /**
     * Save a promotion.
     *
     * @param promotionDTO the entity to save
     * @return the persisted entity
     */
    public PromotionDTO save(PromotionDTO promotionDTO) {
        log.debug("Request to save Promotion : {}", promotionDTO);
        Promotion promotion = promotionMapper.promotionDTOToPromotion(promotionDTO);
        promotion.setCodeQuantity(promotionDTO.getCodeQuantity());
        promotion = promotionRepository.save(promotion);
        promotion.generatePromotionsCodes();
        promotion.getCodes().forEach(promotionCode -> {this.promotionCodeRepository.save(promotionCode);});
        PromotionDTO result = promotionMapper.promotionToPromotionDTO(promotion);
        return result;
    }

    /**
     *  Get all the promotions.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PromotionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Promotions");
        Page<Promotion> result = promotionRepository.findAll(pageable);
        return result.map(promotion -> promotionMapper.promotionToPromotionDTO(promotion));
    }

    /**
     *  Get one promotion by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public PromotionDTO findOne(Long id) {
        log.debug("Request to get Promotion : {}", id);
        Promotion promotion = promotionRepository.findOne(id);
        PromotionDTO promotionDTO = promotionMapper.promotionToPromotionDTO(promotion);
        return promotionDTO;
    }

    /**
     *  Delete the  promotion by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Promotion : {}", id);
        promotionRepository.delete(id);
    }
    /**
     *  AUTOR: DIEGO BARILLAS VALVERDE
     *  Servicio que se trae todos las promociones del id del evento que recibe.
     */
    @Transactional(readOnly = true)
    public Page<PromotionDTO> getByEvent(Pageable pageable,Long eventId) {
        log.debug("Request to get all Residents");
        List<Promotion> result = promotionRepository.findByEventId(eventId);
        return new PageImpl<>(result).map(promotion -> promotionMapper.promotionToPromotionDTO(promotion));

    }
}
