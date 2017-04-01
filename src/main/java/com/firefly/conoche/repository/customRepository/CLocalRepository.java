package com.firefly.conoche.repository.customRepository;

import com.firefly.conoche.domain.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;



/**
 * Created by melvin on 3/11/2017.
 */
public interface CLocalRepository extends JpaRepository<Local,Long> {
    Page<Local> findByOwnerId(Long ownerId, Pageable pageable);
}
