package com.firefly.conoche.service.customService;

import com.firefly.conoche.domain.enumeration.Provincia;
import com.firefly.conoche.repository.customRepository.CEventRepository;
import com.firefly.conoche.service.dto.EventDTO;
import com.firefly.conoche.service.mapper.EventMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
@Transactional
public class CEventService {

    private final CEventRepository ceventRepository;
    private final EventMapper eventMapper;
    public CEventService(CEventRepository ceventRepository, EventMapper eventMapper) {
        this.ceventRepository = ceventRepository;
        this.eventMapper = eventMapper;
    }

    @Transactional(readOnly = true)
    public Page<EventDTO> findByProvincia(Pageable page, Provincia provincia, String name, ZonedDateTime zonedDateTime) {
        return ceventRepository.findByProvincia(page, provincia, name, zonedDateTime)
            .map(eventMapper::eventToEventDTO);
    }

    @Transactional(readOnly = true)
    public Long countEventAfter(ZonedDateTime zonedDateTime) {
        return ceventRepository.countByFinalTimeGreaterThan(zonedDateTime);
    }


    /*

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


    */




}
