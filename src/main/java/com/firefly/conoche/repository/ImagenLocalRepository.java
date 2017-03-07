package com.firefly.conoche.repository;

import com.firefly.conoche.domain.ImagenLocal;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ImagenLocal entity.
 */
@SuppressWarnings("unused")
public interface ImagenLocalRepository extends JpaRepository<ImagenLocal,Long> {

}
