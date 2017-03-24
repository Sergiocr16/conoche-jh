
package com.firefly.conoche.service.customService;

import com.firefly.conoche.domain.RealTimeEventImage;
import com.firefly.conoche.repository.RealTimeEventImageRepository;
import com.firefly.conoche.repository.customRepository.CRealTimeEventImageRepository;
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

/**
 * Service Implementation for managing RealTimeEventImage.
 */
@Service
@Transactional(rollbackFor = IOException.class)
public class CRealTimeEventImageService {

    private final Logger log = LoggerFactory.getLogger(com.firefly.conoche.service.RealTimeEventImageService.class);

    private final CRealTimeEventImageRepository realTimeEventImageRepository;
    private final RealTimeEventImageMapper realTimeEventImageMapper;
    private final CloudinaryService cloudinaryService;

    public CRealTimeEventImageService(CRealTimeEventImageRepository realTimeEventImageRepository,
                                      RealTimeEventImageMapper realTimeEventImageMapper,
                                      CloudinaryService cloudinaryService) {
        this.realTimeEventImageRepository = realTimeEventImageRepository;
        this.realTimeEventImageMapper = realTimeEventImageMapper;
        this.cloudinaryService = cloudinaryService;
    }

    @Transactional(readOnly = true)
    public Page<RealTimeEventImageDTO> findEventRealTimeImages(Long id, Pageable page) {
        return realTimeEventImageRepository.findByEventId(id, page)
            .map(realTimeEventImageMapper::realTimeEventImageToRealTimeEventImageDTO);
    }

    public void delete(RealTimeEventImageDTO realTimeEventImageDTO) throws IOException{
        realTimeEventImageRepository.delete(realTimeEventImageDTO.getId());
        cloudinaryService.delete(realTimeEventImageDTO.getImageUrl());
    }

}
