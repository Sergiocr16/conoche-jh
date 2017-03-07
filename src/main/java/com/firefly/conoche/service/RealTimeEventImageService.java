package com.firefly.conoche.service;

import com.firefly.conoche.domain.RealTimeEventImage;
import com.firefly.conoche.repository.RealTimeEventImageRepository;
import com.firefly.conoche.service.dto.RealTimeEventImageDTO;
import com.firefly.conoche.service.mapper.RealTimeEventImageMapper;
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
 * Service Implementation for managing RealTimeEventImage.
 */
@Service
@Transactional
public class RealTimeEventImageService {

    private final Logger log = LoggerFactory.getLogger(RealTimeEventImageService.class);
    
    private final RealTimeEventImageRepository realTimeEventImageRepository;

    private final RealTimeEventImageMapper realTimeEventImageMapper;

    public RealTimeEventImageService(RealTimeEventImageRepository realTimeEventImageRepository, RealTimeEventImageMapper realTimeEventImageMapper) {
        this.realTimeEventImageRepository = realTimeEventImageRepository;
        this.realTimeEventImageMapper = realTimeEventImageMapper;
    }

    /**
     * Save a realTimeEventImage.
     *
     * @param realTimeEventImageDTO the entity to save
     * @return the persisted entity
     */
    public RealTimeEventImageDTO save(RealTimeEventImageDTO realTimeEventImageDTO) {
        log.debug("Request to save RealTimeEventImage : {}", realTimeEventImageDTO);
        RealTimeEventImage realTimeEventImage = realTimeEventImageMapper.realTimeEventImageDTOToRealTimeEventImage(realTimeEventImageDTO);
        realTimeEventImage = realTimeEventImageRepository.save(realTimeEventImage);
        RealTimeEventImageDTO result = realTimeEventImageMapper.realTimeEventImageToRealTimeEventImageDTO(realTimeEventImage);
        return result;
    }

    /**
     *  Get all the realTimeEventImages.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RealTimeEventImageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RealTimeEventImages");
        Page<RealTimeEventImage> result = realTimeEventImageRepository.findAll(pageable);
        return result.map(realTimeEventImage -> realTimeEventImageMapper.realTimeEventImageToRealTimeEventImageDTO(realTimeEventImage));
    }

    /**
     *  Get one realTimeEventImage by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public RealTimeEventImageDTO findOne(Long id) {
        log.debug("Request to get RealTimeEventImage : {}", id);
        RealTimeEventImage realTimeEventImage = realTimeEventImageRepository.findOne(id);
        RealTimeEventImageDTO realTimeEventImageDTO = realTimeEventImageMapper.realTimeEventImageToRealTimeEventImageDTO(realTimeEventImage);
        return realTimeEventImageDTO;
    }

    /**
     *  Delete the  realTimeEventImage by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete RealTimeEventImage : {}", id);
        realTimeEventImageRepository.delete(id);
    }
}
