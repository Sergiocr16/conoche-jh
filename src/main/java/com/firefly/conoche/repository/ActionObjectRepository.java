package com.firefly.conoche.repository;

import com.firefly.conoche.domain.ActionObject;

import com.firefly.conoche.domain.enumeration.ActionObjectType;
import org.springframework.data.jpa.repository.*;

import javax.swing.text.StyledEditorKit;
import java.util.List;

/**
 * Spring Data JPA repository for the ActionObject entity.
 */
@SuppressWarnings("unused")
public interface ActionObjectRepository extends JpaRepository<ActionObject,Long> {
    List<ActionObject> findByObjectTypeAndObjectIdAndActiveTrue(ActionObjectType objectType, Long objectId);
}
