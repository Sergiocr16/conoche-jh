package com.firefly.conoche.repository;

import com.firefly.conoche.domain.Event;
import com.firefly.conoche.domain.Local;
import com.firefly.conoche.domain.Promotion;

import com.firefly.conoche.repository.notifications.NotifyRepository;
import com.firefly.conoche.repository.notifications.especificImpl.LocalRepositoryCustom;
import com.firefly.conoche.repository.notifications.especificImpl.PromotionRepositoryCustom;
import org.springframework.data.jpa.repository.*;

import java.util.Collection;
import java.util.List;

/**
 * Spring Data JPA repository for the Promotion entity.
 * AUTOR: DIEGO BARILLAS VALVERDE
 */
@SuppressWarnings("unused")
public interface PromotionRepository extends NotifyRepository<Promotion>, PromotionRepositoryCustom {
    /**
     *Descripcion: se trae todos las promociones por el id del evento
     */
    List<Promotion> findByEventId(Long eventId);
    List<Promotion> findByEventIn(Collection<Event> events);
}
