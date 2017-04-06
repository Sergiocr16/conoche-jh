package com.firefly.conoche.service.customService;

import com.firefly.conoche.domain.enumeration.Provincia;
import com.firefly.conoche.repository.customRepository.CEventRepository;
import com.firefly.conoche.service.dto.EventDTO;
import com.firefly.conoche.service.mapper.EventMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CEventService {

    private final CEventRepository ceventRepository;
    private final EventMapper eventMapper;
    public CEventService(CEventRepository ceventRepository, EventMapper eventMapper) {
        this.ceventRepository = ceventRepository;
        this.eventMapper = eventMapper;
    }

    public Page<EventDTO> findByProvincia(Pageable page, Provincia provincia, String name) {
        return ceventRepository.findByLocalProvinciaAndNameContainingIgnoreCase(page, provincia, name)
            .map(eventMapper::eventToEventDTO);
    }
}
