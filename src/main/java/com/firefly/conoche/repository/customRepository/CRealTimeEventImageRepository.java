package com.firefly.conoche.repository.customRepository;

import com.firefly.conoche.domain.RealTimeEventImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by melvin on 3/11/2017.
 */
public interface CRealTimeEventImageRepository extends JpaRepository<RealTimeEventImage,Long> {
    Page<RealTimeEventImage> findByEventId(Long id, Pageable page);
}
