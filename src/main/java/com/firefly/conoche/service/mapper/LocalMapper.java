package com.firefly.conoche.service.mapper;

import com.firefly.conoche.domain.*;
import com.firefly.conoche.service.dto.LocalDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Local and its DTO LocalDTO.
 */
@Mapper(componentModel = "spring", uses = {ServicioMapper.class, UserMapper.class, })
public interface LocalMapper {

    LocalDTO localToLocalDTO(Local local);

    List<LocalDTO> localsToLocalDTOs(List<Local> locals);

    @Mapping(target = "images", ignore = true)
    @Mapping(target = "events", ignore = true)
    @Mapping(target = "schedules", ignore = true)
    @Mapping(target = "ratings", ignore = true)
    Local localDTOToLocal(LocalDTO localDTO);

    List<Local> localDTOsToLocals(List<LocalDTO> localDTOs);

    default Servicio servicioFromId(Long id) {
        if (id == null) {
            return null;
        }
        Servicio servicio = new Servicio();
        servicio.setId(id);
        return servicio;
    }
}
