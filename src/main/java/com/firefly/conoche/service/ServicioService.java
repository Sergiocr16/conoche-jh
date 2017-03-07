package com.firefly.conoche.service;

import com.firefly.conoche.domain.Servicio;
import com.firefly.conoche.repository.ServicioRepository;
import com.firefly.conoche.service.dto.ServicioDTO;
import com.firefly.conoche.service.mapper.ServicioMapper;
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
 * Service Implementation for managing Servicio.
 */
@Service
@Transactional
public class ServicioService {

    private final Logger log = LoggerFactory.getLogger(ServicioService.class);
    
    private final ServicioRepository servicioRepository;

    private final ServicioMapper servicioMapper;

    public ServicioService(ServicioRepository servicioRepository, ServicioMapper servicioMapper) {
        this.servicioRepository = servicioRepository;
        this.servicioMapper = servicioMapper;
    }

    /**
     * Save a servicio.
     *
     * @param servicioDTO the entity to save
     * @return the persisted entity
     */
    public ServicioDTO save(ServicioDTO servicioDTO) {
        log.debug("Request to save Servicio : {}", servicioDTO);
        Servicio servicio = servicioMapper.servicioDTOToServicio(servicioDTO);
        servicio = servicioRepository.save(servicio);
        ServicioDTO result = servicioMapper.servicioToServicioDTO(servicio);
        return result;
    }

    /**
     *  Get all the servicios.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ServicioDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Servicios");
        Page<Servicio> result = servicioRepository.findAll(pageable);
        return result.map(servicio -> servicioMapper.servicioToServicioDTO(servicio));
    }

    /**
     *  Get one servicio by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ServicioDTO findOne(Long id) {
        log.debug("Request to get Servicio : {}", id);
        Servicio servicio = servicioRepository.findOne(id);
        ServicioDTO servicioDTO = servicioMapper.servicioToServicioDTO(servicio);
        return servicioDTO;
    }

    /**
     *  Delete the  servicio by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Servicio : {}", id);
        servicioRepository.delete(id);
    }
}
