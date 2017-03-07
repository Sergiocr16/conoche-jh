package com.firefly.conoche.service;

import com.firefly.conoche.domain.EventImage;
import com.firefly.conoche.repository.EventImageRepository;
import com.firefly.conoche.service.dto.EventImageDTO;
import com.firefly.conoche.service.mapper.EventImageMapper;
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
 * Service Implementation for managing EventImage.
 */
@Service
@Transactional
public class EventImageService {

    private final Logger log = LoggerFactory.getLogger(EventImageService.class);
    
    private final EventImageRepository eventImageRepository;

    private final EventImageMapper eventImageMapper;

    public EventImageService(EventImageRepository eventImageRepository, EventImageMapper eventImageMapper) {
        this.eventImageRepository = eventImageRepository;
        this.eventImageMapper = eventImageMapper;
    }

    /**
     * Save a eventImage.
     *
     * @param eventImageDTO the entity to save
     * @return the persisted entity
     */
    public EventImageDTO save(EventImageDTO eventImageDTO) {
        log.debug("Request to save EventImage : {}", eventImageDTO);
        EventImage eventImage = eventImageMapper.eventImageDTOToEventImage(eventImageDTO);
        eventImage = eventImageRepository.save(eventImage);
        EventImageDTO result = eventImageMapper.eventImageToEventImageDTO(eventImage);
        return result;
    }

    /**
     *  Get all the eventImages.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<EventImageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EventImages");
        Page<EventImage> result = eventImageRepository.findAll(pageable);
        return result.map(eventImage -> eventImageMapper.eventImageToEventImageDTO(eventImage));
    }

    /**
     *  Get one eventImage by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public EventImageDTO findOne(Long id) {
        log.debug("Request to get EventImage : {}", id);
        EventImage eventImage = eventImageRepository.findOne(id);
        EventImageDTO eventImageDTO = eventImageMapper.eventImageToEventImageDTO(eventImage);
        return eventImageDTO;
    }

    /**
     *  Delete the  eventImage by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete EventImage : {}", id);
        eventImageRepository.delete(id);
    }
}
