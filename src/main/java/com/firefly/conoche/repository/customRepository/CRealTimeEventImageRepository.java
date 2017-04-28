package com.firefly.conoche.repository.customRepository;

import com.firefly.conoche.domain.Event;
import com.firefly.conoche.domain.RealTimeEventImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Created by melvin on 3/11/2017.
 */
public interface CRealTimeEventImageRepository extends JpaRepository<RealTimeEventImage,Long> {
    Page<RealTimeEventImage> findByEventId(Long id, Pageable page);
    List<RealTimeEventImage> findByEventIn(Collection<Event> events);
    Page<RealTimeEventImage> findByEventIdAndCreationTimeGreaterThan(Long id, ZonedDateTime date, Pageable page);
}
