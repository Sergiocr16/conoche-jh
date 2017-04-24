package com.firefly.conoche.service.customService;

import com.firefly.conoche.domain.Local;
import com.firefly.conoche.domain.enumeration.Provincia;
import com.firefly.conoche.repository.customRepository.CLocalRepository;
import com.firefly.conoche.service.UserService;
import com.firefly.conoche.service.dto.EventDTO;
import com.firefly.conoche.service.dto.LocalDTO;

import com.firefly.conoche.service.mapper.LocalMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * Created by Alberto on 3/31/2017.
 */
@Service
@Transactional
public class CLocalService {

    private final CLocalRepository clocalRepository;
    private final LocalMapper localmapper;
    private final UserService userService;

    public CLocalService(CLocalRepository clocalRepository,
                         LocalMapper localmapper,
                         UserService userService) {
        this.clocalRepository = clocalRepository;
        this.localmapper = localmapper;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public Page<LocalDTO> findByProvinciaAndName(Pageable page, Provincia provincia, String name, Long category) {
        return clocalRepository.findByProvinciaAndName(page, provincia, name, category)
            .map(localmapper::localToLocalDTO);
    }

    @Transactional(readOnly = true)
   public Page<LocalDTO> findLocalsByOwner(Long id, Pageable page) {
       return clocalRepository.findByOwnerId(id, page)
            .map(localmapper::localToLocalDTO);
   }

   public Optional<LocalDTO> suscribeCurrentToLocal(Long idLocal) {
        Optional<Local> local = Optional.ofNullable(clocalRepository.findOne(idLocal));
        return Optional.ofNullable(userService.getUserWithAuthorities())
            .flatMap(u -> local.map( l -> {
                l.addSubcribers(u);
                return clocalRepository.save(l);
            }).map(localmapper::localToLocalDTO));
   }
    @Transactional(readOnly = true)
   public Long count() {
        return clocalRepository.count();
   }
}
