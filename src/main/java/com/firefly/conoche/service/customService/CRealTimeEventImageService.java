
package com.firefly.conoche.service.customService;

import com.firefly.conoche.domain.RealTimeEventImage;
import com.firefly.conoche.repository.RealTimeEventImageRepository;
import com.firefly.conoche.repository.customRepository.CRealTimeEventImageRepository;
import com.firefly.conoche.security.SecurityUtils;
import com.firefly.conoche.service.dto.RealTimeEventImageDTO;
import com.firefly.conoche.service.mapper.RealTimeEventImageMapper;
import com.firefly.conoche.service.util.CloudinaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

/**
 * Service Implementation for managing RealTimeEventImage.
 */
@Service
@Transactional(rollbackFor = IOException.class)
public class CRealTimeEventImageService {

    private final CRealTimeEventImageRepository realTimeEventImageRepository;
    private final RealTimeEventImageMapper realTimeEventImageMapper;
    private final CloudinaryService cloudinaryService;
    private final CAuthOwnerService cAuthOwnerService;

    public CRealTimeEventImageService(CRealTimeEventImageRepository realTimeEventImageRepository,
                                      RealTimeEventImageMapper realTimeEventImageMapper,
                                      CloudinaryService cloudinaryService,
                                      CAuthOwnerService cAuthOwnerService) {
        this.realTimeEventImageRepository = realTimeEventImageRepository;
        this.realTimeEventImageMapper     = realTimeEventImageMapper;
        this.cloudinaryService            = cloudinaryService;
        this.cAuthOwnerService            = cAuthOwnerService;
    }

    @Transactional(readOnly = true)
    public Page<RealTimeEventImageDTO> findEventRealTimeImages(Long id, Pageable page) {
        return realTimeEventImageRepository.findByEventId(id, page)
            .map(realTimeEventImageMapper::realTimeEventImageToRealTimeEventImageDTO);
    }

    //cambiar excepciones por mas especificas.
    public RealTimeEventImageDTO delete(long idRealTimeImage) throws IOException{
        RealTimeEventImageDTO imageDTO = Optional
            .ofNullable(realTimeEventImageRepository.findOne(idRealTimeImage))
            .map(realTimeEventImageMapper::realTimeEventImageToRealTimeEventImageDTO)
            .orElseThrow(() -> new RuntimeException("There is no image with id: " + idRealTimeImage));


        Long idEvent = imageDTO.getEventId();
        boolean isOwner = cAuthOwnerService.currentUserIsOwner(idEvent)
            .orElse(false);
        if(!isOwner) {
            throw new RuntimeException(String.format(
                "User %s is not the owner of event %d",
                SecurityUtils.getCurrentUserLogin(), idEvent));
        }

        realTimeEventImageRepository.delete(imageDTO.getId());
        cloudinaryService.delete(imageDTO.getImageUrl());
        return imageDTO;
    }
}
