package com.firefly.conoche.service;

import com.firefly.conoche.domain.Event;
import com.firefly.conoche.domain.RealTimeEventImage;
import com.firefly.conoche.domain.User;
import com.firefly.conoche.repository.EventRepository;
import com.firefly.conoche.repository.PromotionRepository;
import com.firefly.conoche.repository.RealTimeEventImageRepository;
import com.firefly.conoche.repository.UserRepository;
import com.firefly.conoche.security.SecurityUtils;
import com.firefly.conoche.service.customService.CRealTimeEventImageService;
import com.firefly.conoche.service.dto.EventDTO;
import com.firefly.conoche.service.dto.UserDTO;
import com.firefly.conoche.service.mapper.EventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Event.
 */
@Service
@Transactional
public class EventService {

    private final Logger log = LoggerFactory.getLogger(EventService.class);
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final UserRepository userRepository;
    private final LocalService localService;
    private final PromotionRepository promotionRepository;
    private final CRealTimeEventImageService cRealTimeEventImageService;


    public EventService(EventRepository eventRepository,
                        EventMapper eventMapper,
                        LocalService localService,
                        UserRepository userRepository,
                        PromotionRepository promotionRepository,
                        CRealTimeEventImageService cRealTimeEventImageService) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.localService = localService;
        this.userRepository = userRepository;
        this.promotionRepository = promotionRepository;
        this.cRealTimeEventImageService = cRealTimeEventImageService;

    }

    /**
     * Save a event.
     *
     * @param eventDTO the entity to save
     * @return the persisted entity
     */
    public EventDTO save(EventDTO eventDTO) {
        log.debug("Request to save Event : {}", eventDTO);
        Event event = eventMapper.eventDTOToEvent(eventDTO);
        event = eventRepository.save(event);
        EventDTO result = eventMapper.eventToEventDTO(event);
        return result;
    }

    /**
     *  Get all the events.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<EventDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Events");
        Page<Event> result = eventRepository.findAll(pageable);
        return result.map(event -> eventMapper.eventToEventDTO(event));
    }

    /**
     *  Get one event by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public EventDTO findOne(Long id) {
        log.debug("Request to get Event : {}", id);
        Event event = eventRepository.findOneWithEagerRelationships(id);
        EventDTO eventDTO = eventMapper.eventToEventDTO(event);
        return eventDTO;
    }

    /**
     *  Delete the  event by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) throws IOException {
        log.debug("Request to delete Event : {}", id);
        Optional<Event> event = Optional.ofNullable(eventRepository.findOne(id));
        if (event.isPresent()) {
            for (RealTimeEventImage r : event.get().getRealTimeImages()) {
                cRealTimeEventImageService.delete(r.getId());
            }
        }
        event.map(Event::getPromotions)
            .ifPresent(p-> p.forEach(promotionRepository::delete));
        eventRepository.delete(id);
    }

    public void attendEvent(Long idEvent) {
         Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());

         user.ifPresent(u -> {
             Event event = eventRepository.findOneWithEagerRelationships(idEvent);
             event.addAttendingUsers(u);
             eventRepository.save(event);
             EventDTO eventDTO = eventMapper.eventToEventDTO(event);
         });

    }

    public void dismissEvent(Long idEvent) {
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());

        user.ifPresent(u -> {
            Event event = eventRepository.findOneWithEagerRelationships(idEvent);
            event.removeAttendingUsers(u);
            eventRepository.save(event);
            EventDTO eventDTO = eventMapper.eventToEventDTO(event);
        });

    }

}
