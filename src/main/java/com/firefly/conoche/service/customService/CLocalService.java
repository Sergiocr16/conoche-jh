package com.firefly.conoche.service.customService;

import com.firefly.conoche.domain.enumeration.Provincia;
import com.firefly.conoche.repository.customRepository.CLocalRepository;
import com.firefly.conoche.service.dto.EventDTO;
import com.firefly.conoche.service.dto.LocalDTO;

import com.firefly.conoche.service.mapper.LocalMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

/**
 * Created by Alberto on 3/31/2017.
 */
@Service
@Transactional
public class CLocalService {

    private final CLocalRepository clocalRepository;
    private final LocalMapper localmapper;
    public CLocalService(CLocalRepository clocalRepository, LocalMapper localmapper) {
        this.clocalRepository = clocalRepository;
        this.localmapper = localmapper;
    }

    public Page<LocalDTO> findByProvinciaAndName(Pageable page, Provincia provincia, String name, Long category) {
        return clocalRepository.findByProvinciaAndName(page, provincia, name, category)
            .map(localmapper::localToLocalDTO);
    }
   public Page<LocalDTO> findLocalsByOwner(Long id, Pageable page) {
       return clocalRepository.findByOwnerId(id, page)
            .map(localmapper::localToLocalDTO);
   }

   public Long count() {
        return clocalRepository.count();
   }
}
