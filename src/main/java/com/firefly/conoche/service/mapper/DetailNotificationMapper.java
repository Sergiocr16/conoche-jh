package com.firefly.conoche.service.mapper;

import com.firefly.conoche.domain.Notification;
import com.firefly.conoche.service.dto.DetailNotificationDTO;
import com.firefly.conoche.service.dto.NotificationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Created by melvin on 4/25/2017.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, ActionObjectMapper.class,})
public interface DetailNotificationMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    DetailNotificationDTO notificationToNotificationDTO(Notification notification);

    List<DetailNotificationDTO> notificationsToNotificationDTOs(List<Notification> notifications);

    @Mapping(source = "userId", target = "user")
    Notification notificationDTOToNotification(DetailNotificationDTO notificationDTO);

    List<Notification> notificationDTOsToNotifications(List<DetailNotificationDTO> notificationDTOs);
}
