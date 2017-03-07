package com.firefly.conoche.repository;

import com.firefly.conoche.domain.Servicio;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Servicio entity.
 */
@SuppressWarnings("unused")
public interface ServicioRepository extends JpaRepository<Servicio,Long> {

}
