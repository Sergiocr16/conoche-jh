package com.firefly.conoche.service.dto;


import com.firefly.conoche.domain.ActionObject;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Notification entity.
 */
public class NotificationDTO implements Serializable {

    private Long id;

    @NotNull
    private Boolean isRead;

    private Long userId;

    private String userLogin;


    private ActionObjectDTO actionObject;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public ActionObjectDTO getActionObject() {
        return actionObject;
    }

    public void setActionObject(ActionObjectDTO actionObject) {
        this.actionObject = actionObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NotificationDTO notificationDTO = (NotificationDTO) o;

        if ( ! Objects.equals(id, notificationDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "NotificationDTO{" +
            "id=" + id +
            ", isRead='" + isRead + "'" +
            '}';
    }
}
