package com.firefly.conoche.service.mapper;

import com.firefly.conoche.domain.*;
import com.firefly.conoche.service.dto.ServicioDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Servicio and its DTO ServicioDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ServicioMapper {

    ServicioDTO servicioToServicioDTO(Servicio servicio);

    List<ServicioDTO> serviciosToServicioDTOs(List<Servicio> servicios);

    Servicio servicioDTOToServicio(ServicioDTO servicioDTO);

    List<Servicio> servicioDTOsToServicios(List<ServicioDTO> servicioDTOs);
}
