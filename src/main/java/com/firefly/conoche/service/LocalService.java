package com.firefly.conoche.service;

import com.firefly.conoche.domain.Event;
import com.firefly.conoche.domain.Local;
import com.firefly.conoche.domain.RatingLocal;
import com.firefly.conoche.domain.User;
import com.firefly.conoche.repository.EventRepository;
import com.firefly.conoche.repository.LocalRepository;
import com.firefly.conoche.repository.RatingLocalRepository;
import com.firefly.conoche.repository.UserRepository;
import com.firefly.conoche.security.SecurityUtils;
import com.firefly.conoche.service.dto.EventDTO;
import com.firefly.conoche.service.dto.LocalDTO;
import com.firefly.conoche.service.mapper.EventMapper;
import com.firefly.conoche.service.mapper.LocalMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Local.
 * Mover saveWithCurrentUser y findByCategoryId a otro servicio para evitar perderlos
 */
@Service
@Transactional
public class LocalService {

    private final Logger log = LoggerFactory.getLogger(LocalService.class);
    private final LocalRepository localRepository;
    private final LocalMapper localMapper;
    private final UserService userService;
    private final RatingLocalRepository ratingLocalRepository;
    private final UserRepository userRepository;
    private final EventService eventService;
    private final EventMapper eventMapper;


    public LocalService(LocalRepository localRepository,
                        LocalMapper localMapper,
                        UserService userService,
                        RatingLocalRepository ratingLocalRepository,
                        UserRepository userRepository,
                        EventService eventService,
                        EventMapper eventMapper) {
        this.localRepository = localRepository;
        this.localMapper = localMapper;
        this.userService = userService;
        this.userRepository = userRepository;
        this.ratingLocalRepository = ratingLocalRepository;
        this.eventService = eventService;
        this.eventMapper = eventMapper;
    }

    public LocalDTO saveWithCurrentUser(LocalDTO localDTO) {
        localDTO.setOwnerId(userService.getUserWithAuthorities().getId());
        return save(localDTO);
    }

    /**
     * Save a local.
     *
     * @param localDTO the entity to save
     * @return the persisted entity
     */
    public LocalDTO save(LocalDTO localDTO) {
        log.debug("Request to save Local : {}", localDTO);
        Local local = localMapper.localDTOToLocal(localDTO);
        local = localRepository.save(local);
        LocalDTO result = localMapper.localToLocalDTO(local);
        return result;
    }

    /**
     *  Get all the locals.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LocalDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Locals");
        Page<Local> result = localRepository.findAll(pageable);
        return result.map(local -> localMapper.localToLocalDTO(local));
    }

    @Transactional(readOnly = true)
    public Page<LocalDTO> findByCategoryId(Long categoryId,Pageable pageable) {
        log.debug("Request to get all Locals");
        Page<Local> result = localRepository.findBylocalCategoryId(categoryId,pageable);
        return result.map(local -> localMapper.localToLocalDTO(local));
    }

    /**
     *  Get one local by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public LocalDTO findOne(Long id) {
        log.debug("Request to get Local : {}", id);
        Local local = localRepository.findOneWithEagerRelationships(id);
        LocalDTO localDTO = localMapper.localToLocalDTO(local);
        return localDTO;
    }

    /**
     *  Delete the  local by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) throws IOException{
        log.debug("Request to delete Local : {}", id);
        Local local = localRepository.findOne(id);

        if(local != null) {
            for (Event e : local.getEvents()) {
                eventService.delete(e.getId());
            }
        }
        localRepository.delete(id);
    }
    /**
     *  AUTOR: DIEGO BARILLAS VALVERDE
     *  Servicio que se trae todos los locales por medio del id del user que recibe
     */
    @Transactional(readOnly = true)
    public Page<LocalDTO> getByOwner(Pageable pageable,Long OwnerId) {
        log.debug("Request to get all Residents");
        List<Local> result = localRepository.findByOwnerId(OwnerId);
        return new PageImpl<>(result).map(local-> localMapper.localToLocalDTO(local));

    }

    public void subscribeLocal(Long idLocal) {
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        user.ifPresent(u -> {
            Local local = localRepository.findOneWithEagerRelationships(idLocal);
            local.addSubcribers(u);
            localRepository.save(local);
            LocalDTO localDTO = localMapper.localToLocalDTO(local);
        });

    }

    public void unsubscribeLocal(Long idLocal) {
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        user.ifPresent(u -> {
            Local local = localRepository.findOneWithEagerRelationships(idLocal);
            local.removeSubcribers(u);
            localRepository.save(local);
            LocalDTO localDTO = localMapper.localToLocalDTO(local);
        });

    }


}
