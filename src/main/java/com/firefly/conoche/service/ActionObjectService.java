package com.firefly.conoche.service;

import com.firefly.conoche.domain.ActionObject;
import com.firefly.conoche.repository.ActionObjectRepository;
import com.firefly.conoche.service.dto.ActionObjectDTO;
import com.firefly.conoche.service.mapper.ActionObjectMapper;
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
 * Service Implementation for managing ActionObject.
 */
@Service
@Transactional
public class ActionObjectService {

    private final Logger log = LoggerFactory.getLogger(ActionObjectService.class);
    
    private final ActionObjectRepository actionObjectRepository;

    private final ActionObjectMapper actionObjectMapper;

    public ActionObjectService(ActionObjectRepository actionObjectRepository, ActionObjectMapper actionObjectMapper) {
        this.actionObjectRepository = actionObjectRepository;
        this.actionObjectMapper = actionObjectMapper;
    }

    /**
     * Save a actionObject.
     *
     * @param actionObjectDTO the entity to save
     * @return the persisted entity
     */
    public ActionObjectDTO save(ActionObjectDTO actionObjectDTO) {
        log.debug("Request to save ActionObject : {}", actionObjectDTO);
        ActionObject actionObject = actionObjectMapper.actionObjectDTOToActionObject(actionObjectDTO);
        actionObject = actionObjectRepository.save(actionObject);
        ActionObjectDTO result = actionObjectMapper.actionObjectToActionObjectDTO(actionObject);
        return result;
    }

    /**
     *  Get all the actionObjects.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ActionObjectDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ActionObjects");
        Page<ActionObject> result = actionObjectRepository.findAll(pageable);
        return result.map(actionObject -> actionObjectMapper.actionObjectToActionObjectDTO(actionObject));
    }

    /**
     *  Get one actionObject by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ActionObjectDTO findOne(Long id) {
        log.debug("Request to get ActionObject : {}", id);
        ActionObject actionObject = actionObjectRepository.findOne(id);
        ActionObjectDTO actionObjectDTO = actionObjectMapper.actionObjectToActionObjectDTO(actionObject);
        return actionObjectDTO;
    }

    /**
     *  Delete the  actionObject by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ActionObject : {}", id);
        actionObjectRepository.delete(id);
    }
}
