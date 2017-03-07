package com.firefly.conoche.service.mapper;

import com.firefly.conoche.domain.*;
import com.firefly.conoche.service.dto.MessageDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Message and its DTO MessageDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface MessageMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "event.id", target = "eventId")
    @Mapping(source = "event.name", target = "eventName")
    MessageDTO messageToMessageDTO(Message message);

    List<MessageDTO> messagesToMessageDTOs(List<Message> messages);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "eventId", target = "event")
    Message messageDTOToMessage(MessageDTO messageDTO);

    List<Message> messageDTOsToMessages(List<MessageDTO> messageDTOs);

    default Event eventFromId(Long id) {
        if (id == null) {
            return null;
        }
        Event event = new Event();
        event.setId(id);
        return event;
    }
}
