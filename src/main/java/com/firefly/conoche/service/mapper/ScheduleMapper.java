package com.firefly.conoche.service.mapper;

import com.firefly.conoche.domain.*;
import com.firefly.conoche.service.dto.ScheduleDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Schedule and its DTO ScheduleDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ScheduleMapper {

    @Mapping(source = "local.id", target = "localId")
    @Mapping(source = "local.name", target = "localName")
    ScheduleDTO scheduleToScheduleDTO(Schedule schedule);

    List<ScheduleDTO> schedulesToScheduleDTOs(List<Schedule> schedules);

    @Mapping(source = "localId", target = "local")
    Schedule scheduleDTOToSchedule(ScheduleDTO scheduleDTO);

    List<Schedule> scheduleDTOsToSchedules(List<ScheduleDTO> scheduleDTOs);

    default Local localFromId(Long id) {
        if (id == null) {
            return null;
        }
        Local local = new Local();
        local.setId(id);
        return local;
    }
}
