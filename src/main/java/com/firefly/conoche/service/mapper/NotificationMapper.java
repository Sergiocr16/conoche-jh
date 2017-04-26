package com.firefly.conoche.service.mapper;

import com.firefly.conoche.domain.*;
import com.firefly.conoche.service.dto.NotificationDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Notification and its DTO NotificationDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface NotificationMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "actionObject.id", target = "actionObjectId")
    NotificationDTO notificationToNotificationDTO(Notification notification);

    List<NotificationDTO> notificationsToNotificationDTOs(List<Notification> notifications);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "actionObjectId", target = "actionObject")
    Notification notificationDTOToNotification(NotificationDTO notificationDTO);

    List<Notification> notificationDTOsToNotifications(List<NotificationDTO> notificationDTOs);

    default ActionObject actionObjectFromId(Long id) {
        if (id == null) {
            return null;
        }
        ActionObject actionObject = new ActionObject();
        actionObject.setId(id);
        return actionObject;
    }
}
