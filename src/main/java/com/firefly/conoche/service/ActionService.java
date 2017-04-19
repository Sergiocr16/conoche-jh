package com.firefly.conoche.service;

import com.firefly.conoche.domain.Action;
import com.firefly.conoche.repository.ActionRepository;
import com.firefly.conoche.service.dto.ActionDTO;
import com.firefly.conoche.service.mapper.ActionMapper;
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
 * Service Implementation for managing Action.
 */
@Service
@Transactional
public class ActionService {

    private final Logger log = LoggerFactory.getLogger(ActionService.class);
    
    private final ActionRepository actionRepository;

    private final ActionMapper actionMapper;

    public ActionService(ActionRepository actionRepository, ActionMapper actionMapper) {
        this.actionRepository = actionRepository;
        this.actionMapper = actionMapper;
    }

    /**
     * Save a action.
     *
     * @param actionDTO the entity to save
     * @return the persisted entity
     */
    public ActionDTO save(ActionDTO actionDTO) {
        log.debug("Request to save Action : {}", actionDTO);
        Action action = actionMapper.actionDTOToAction(actionDTO);
        action = actionRepository.save(action);
        ActionDTO result = actionMapper.actionToActionDTO(action);
        return result;
    }

    /**
     *  Get all the actions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ActionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Actions");
        Page<Action> result = actionRepository.findAll(pageable);
        return result.map(action -> actionMapper.actionToActionDTO(action));
    }

    /**
     *  Get one action by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ActionDTO findOne(Long id) {
        log.debug("Request to get Action : {}", id);
        Action action = actionRepository.findOne(id);
        ActionDTO actionDTO = actionMapper.actionToActionDTO(action);
        return actionDTO;
    }

    /**
     *  Delete the  action by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Action : {}", id);
        actionRepository.delete(id);
    }
}
