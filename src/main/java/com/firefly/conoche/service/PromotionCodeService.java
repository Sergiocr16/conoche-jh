package com.firefly.conoche.service;

import com.firefly.conoche.domain.PromotionCode;
import com.firefly.conoche.repository.PromotionCodeRepository;
import com.firefly.conoche.service.dto.PromotionCodeDTO;
import com.firefly.conoche.service.mapper.PromotionCodeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing PromotionCode.
 */
@Service
@Transactional
public class PromotionCodeService {

    private final Logger log = LoggerFactory.getLogger(PromotionCodeService.class);
    
    private final PromotionCodeRepository promotionCodeRepository;

    private final PromotionCodeMapper promotionCodeMapper;

    public PromotionCodeService(PromotionCodeRepository promotionCodeRepository, PromotionCodeMapper promotionCodeMapper) {
        this.promotionCodeRepository = promotionCodeRepository;
        this.promotionCodeMapper = promotionCodeMapper;
    }

    /**
     * Save a promotionCode.
     *
     * @param promotionCodeDTO the entity to save
     * @return the persisted entity
     */
    public PromotionCodeDTO save(PromotionCodeDTO promotionCodeDTO) {
        log.debug("Request to save PromotionCode : {}", promotionCodeDTO);
        PromotionCode promotionCode = promotionCodeMapper.promotionCodeDTOToPromotionCode(promotionCodeDTO);
        promotionCode = promotionCodeRepository.save(promotionCode);
        PromotionCodeDTO result = promotionCodeMapper.promotionCodeToPromotionCodeDTO(promotionCode);
        return result;
    }

    /**
     *  Get all the promotionCodes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PromotionCodeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PromotionCodes");
        Page<PromotionCode> result = promotionCodeRepository.findAll(pageable);
        return result.map(promotionCode -> promotionCodeMapper.promotionCodeToPromotionCodeDTO(promotionCode));
    }

    /**
     *  Get one promotionCode by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public PromotionCodeDTO findOne(Long id) {
        log.debug("Request to get PromotionCode : {}", id);
        PromotionCode promotionCode = promotionCodeRepository.findOne(id);
        PromotionCodeDTO promotionCodeDTO = promotionCodeMapper.promotionCodeToPromotionCodeDTO(promotionCode);
        return promotionCodeDTO;
    }

    /**
     *  Delete the  promotionCode by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PromotionCode : {}", id);
        promotionCodeRepository.delete(id);
    }
}
