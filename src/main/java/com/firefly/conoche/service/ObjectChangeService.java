package com.firefly.conoche.service;

import com.firefly.conoche.domain.ObjectChange;
import com.firefly.conoche.repository.ObjectChangeRepository;
import com.firefly.conoche.service.dto.ObjectChangeDTO;
import com.firefly.conoche.service.mapper.ObjectChangeMapper;
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
 * Service Implementation for managing ObjectChange.
 */
@Service
@Transactional
public class ObjectChangeService {

    private final Logger log = LoggerFactory.getLogger(ObjectChangeService.class);
    
    private final ObjectChangeRepository objectChangeRepository;

    private final ObjectChangeMapper objectChangeMapper;

    public ObjectChangeService(ObjectChangeRepository objectChangeRepository, ObjectChangeMapper objectChangeMapper) {
        this.objectChangeRepository = objectChangeRepository;
        this.objectChangeMapper = objectChangeMapper;
    }

    /**
     * Save a objectChange.
     *
     * @param objectChangeDTO the entity to save
     * @return the persisted entity
     */
    public ObjectChangeDTO save(ObjectChangeDTO objectChangeDTO) {
        log.debug("Request to save ObjectChange : {}", objectChangeDTO);
        ObjectChange objectChange = objectChangeMapper.objectChangeDTOToObjectChange(objectChangeDTO);
        objectChange = objectChangeRepository.save(objectChange);
        ObjectChangeDTO result = objectChangeMapper.objectChangeToObjectChangeDTO(objectChange);
        return result;
    }

    /**
     *  Get all the objectChanges.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ObjectChangeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ObjectChanges");
        Page<ObjectChange> result = objectChangeRepository.findAll(pageable);
        return result.map(objectChange -> objectChangeMapper.objectChangeToObjectChangeDTO(objectChange));
    }

    /**
     *  Get one objectChange by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ObjectChangeDTO findOne(Long id) {
        log.debug("Request to get ObjectChange : {}", id);
        ObjectChange objectChange = objectChangeRepository.findOne(id);
        ObjectChangeDTO objectChangeDTO = objectChangeMapper.objectChangeToObjectChangeDTO(objectChange);
        return objectChangeDTO;
    }

    /**
     *  Delete the  objectChange by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ObjectChange : {}", id);
        objectChangeRepository.delete(id);
    }
}
