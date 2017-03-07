package com.firefly.conoche.service;

import com.firefly.conoche.domain.RatingLocal;
import com.firefly.conoche.repository.RatingLocalRepository;
import com.firefly.conoche.service.dto.RatingLocalDTO;
import com.firefly.conoche.service.mapper.RatingLocalMapper;
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
 * Service Implementation for managing RatingLocal.
 */
@Service
@Transactional
public class RatingLocalService {

    private final Logger log = LoggerFactory.getLogger(RatingLocalService.class);
    
    private final RatingLocalRepository ratingLocalRepository;

    private final RatingLocalMapper ratingLocalMapper;

    public RatingLocalService(RatingLocalRepository ratingLocalRepository, RatingLocalMapper ratingLocalMapper) {
        this.ratingLocalRepository = ratingLocalRepository;
        this.ratingLocalMapper = ratingLocalMapper;
    }

    /**
     * Save a ratingLocal.
     *
     * @param ratingLocalDTO the entity to save
     * @return the persisted entity
     */
    public RatingLocalDTO save(RatingLocalDTO ratingLocalDTO) {
        log.debug("Request to save RatingLocal : {}", ratingLocalDTO);
        RatingLocal ratingLocal = ratingLocalMapper.ratingLocalDTOToRatingLocal(ratingLocalDTO);
        ratingLocal = ratingLocalRepository.save(ratingLocal);
        RatingLocalDTO result = ratingLocalMapper.ratingLocalToRatingLocalDTO(ratingLocal);
        return result;
    }

    /**
     *  Get all the ratingLocals.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RatingLocalDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RatingLocals");
        Page<RatingLocal> result = ratingLocalRepository.findAll(pageable);
        return result.map(ratingLocal -> ratingLocalMapper.ratingLocalToRatingLocalDTO(ratingLocal));
    }

    /**
     *  Get one ratingLocal by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public RatingLocalDTO findOne(Long id) {
        log.debug("Request to get RatingLocal : {}", id);
        RatingLocal ratingLocal = ratingLocalRepository.findOne(id);
        RatingLocalDTO ratingLocalDTO = ratingLocalMapper.ratingLocalToRatingLocalDTO(ratingLocal);
        return ratingLocalDTO;
    }

    /**
     *  Delete the  ratingLocal by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete RatingLocal : {}", id);
        ratingLocalRepository.delete(id);
    }
}
