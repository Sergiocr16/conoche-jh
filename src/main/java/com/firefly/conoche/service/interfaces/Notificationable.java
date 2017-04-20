package com.firefly.conoche.service.interfaces;

import java.util.Map;

/**
 * Created by melvin on 4/19/2017.
 */
public interface Notificationable {
   Long getId();
   Map<String, Object> getNotificationInfo();
}
