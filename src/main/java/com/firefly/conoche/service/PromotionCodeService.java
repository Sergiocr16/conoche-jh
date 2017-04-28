package com.firefly.conoche.service;

import com.firefly.conoche.domain.Promotion;
import com.firefly.conoche.domain.PromotionCode;
import com.firefly.conoche.repository.PromotionCodeRepository;
import com.firefly.conoche.repository.UserRepository;
import com.firefly.conoche.service.dto.PromotionCodeDTO;
import com.firefly.conoche.service.mapper.PromotionCodeMapper;
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
 * Service Implementation for managing PromotionCode.
 */
@Service
@Transactional
public class PromotionCodeService {

    private final Logger log = LoggerFactory.getLogger(PromotionCodeService.class);

    private final PromotionCodeRepository promotionCodeRepository;

    private final UserRepository userRepository;

    private final PromotionCodeMapper promotionCodeMapper;

    public PromotionCodeService(PromotionCodeRepository promotionCodeRepository, PromotionCodeMapper promotionCodeMapper, UserRepository userRepository) {
        this.promotionCodeRepository = promotionCodeRepository;
        this.promotionCodeMapper = promotionCodeMapper;
        this.userRepository = userRepository;
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
     *  Get all the promotionCodes by user and promotiondId.
     *
     *  @param userId the user id
     *  @param promotionId the promotion id
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PromotionCodeDTO> findByUserAndPromotion(Long userId,Long promotionId) {
        log.debug("Request to get all PromotionCodes");
        List<PromotionCode> result = promotionCodeRepository.findByPromotionIdAndUserId(promotionId,userId);
        return new PageImpl<PromotionCode>(result).map(promotionCode -> promotionCodeMapper.promotionCodeToPromotionCodeDTO(promotionCode));
    }
    /**
     *  Get all the promotionCodes by user.
     *
     *  @param userId the user id
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PromotionCodeDTO> findByUserId(Long userId) {
        log.debug("Request to get all PromotionCodes");
        List<PromotionCode> result = promotionCodeRepository.findByUserIdAndActive(userId,true);
        return new PageImpl<PromotionCode>(result).map(promotionCode -> promotionCodeMapper.promotionCodeToPromotionCodeDTO(promotionCode));
    }
    /**
     *  Redeem the last available code of a promotion for an user
     *
     *  @param userId the user id
     *  @param promotionId the user id
     *  @return the promotionCode
     */

    public PromotionCodeDTO redeemCodeInPromotion(Long promotionId,Long userId) {
        log.debug("Request to get all PromotionCodes");
        PromotionCode result = promotionCodeRepository.findTop1ByPromotionIdAndUserIdIsNull(promotionId);
        PromotionCodeDTO promotionCodeDTO = promotionCodeMapper.promotionCodeToPromotionCodeDTO(result);
        promotionCodeDTO.setUserId(userId);
        PromotionCode promotionCode = promotionCodeMapper.promotionCodeDTOToPromotionCode(promotionCodeDTO);
        promotionCodeRepository.save(promotionCode);
        promotionCodeDTO.setUserId(userId);
        PromotionCode p = this.promotionCodeRepository.save(promotionCode);
        String a = "";
        return promotionCodeMapper.promotionCodeToPromotionCodeDTO(result);
    }

    /**
     *  Get the last available code of a promotion for an user
     *
     *  @param promotionId the user id
     *  @return the promotionCode
     */
    @Transactional(readOnly = true)
    public Page<PromotionCodeDTO> findAvailableCodesByPromotionId(Long promotionId) {
        log.debug("Request to get all Promotions available");
        List<PromotionCode> result = promotionCodeRepository.findByPromotionIdAndUserIdIsNullAndActive(promotionId,true);
        return new PageImpl<PromotionCode>(result).map(promotionCode -> promotionCodeMapper.promotionCodeToPromotionCodeDTO(promotionCode));
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

    @Transactional(readOnly = true)
    public PromotionCodeDTO findByCode(String code) {
        log.debug("Request to get PromotionCode : {}", code);
        PromotionCode promotionCode = promotionCodeRepository.findByCodeAndActive(code,true);
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
