package com.firefly.conoche.service;

import com.firefly.conoche.domain.ImagenLocal;
import com.firefly.conoche.repository.ImagenLocalRepository;
import com.firefly.conoche.service.dto.ImagenLocalDTO;
import com.firefly.conoche.service.mapper.ImagenLocalMapper;
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
 * Service Implementation for managing ImagenLocal.
 */
@Service
@Transactional
public class ImagenLocalService {

    private final Logger log = LoggerFactory.getLogger(ImagenLocalService.class);
    
    private final ImagenLocalRepository imagenLocalRepository;

    private final ImagenLocalMapper imagenLocalMapper;

    public ImagenLocalService(ImagenLocalRepository imagenLocalRepository, ImagenLocalMapper imagenLocalMapper) {
        this.imagenLocalRepository = imagenLocalRepository;
        this.imagenLocalMapper = imagenLocalMapper;
    }

    /**
     * Save a imagenLocal.
     *
     * @param imagenLocalDTO the entity to save
     * @return the persisted entity
     */
    public ImagenLocalDTO save(ImagenLocalDTO imagenLocalDTO) {
        log.debug("Request to save ImagenLocal : {}", imagenLocalDTO);
        ImagenLocal imagenLocal = imagenLocalMapper.imagenLocalDTOToImagenLocal(imagenLocalDTO);
        imagenLocal = imagenLocalRepository.save(imagenLocal);
        ImagenLocalDTO result = imagenLocalMapper.imagenLocalToImagenLocalDTO(imagenLocal);
        return result;
    }

    /**
     *  Get all the imagenLocals.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ImagenLocalDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ImagenLocals");
        Page<ImagenLocal> result = imagenLocalRepository.findAll(pageable);
        return result.map(imagenLocal -> imagenLocalMapper.imagenLocalToImagenLocalDTO(imagenLocal));
    }

    /**
     *  Get one imagenLocal by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ImagenLocalDTO findOne(Long id) {
        log.debug("Request to get ImagenLocal : {}", id);
        ImagenLocal imagenLocal = imagenLocalRepository.findOne(id);
        ImagenLocalDTO imagenLocalDTO = imagenLocalMapper.imagenLocalToImagenLocalDTO(imagenLocal);
        return imagenLocalDTO;
    }

    /**
     *  Delete the  imagenLocal by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ImagenLocal : {}", id);
        imagenLocalRepository.delete(id);
    }
}
