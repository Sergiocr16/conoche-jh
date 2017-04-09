package com.firefly.conoche.repository.customRepository;

import com.firefly.conoche.domain.Event;
import com.firefly.conoche.domain.Local;
import com.firefly.conoche.domain.enumeration.Provincia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;


/**
 * Created by melvin on 3/11/2017.
 */
public interface CLocalRepository extends JpaRepository<Local,Long> {
    Page<Local> findByOwnerId(Long ownerId, Pageable pageable);


    @Query("select local from Local local where " +
        "(:prov is null or local.provincia = :prov) " +
        "and (:idCategory is null or local.localCategory.id = :idCategory) " +
        "and lower(local.name) like lower(concat('%', :name,'%')) ")
    Page<Local> findByProvinciaAndName(Pageable page,
                                @Param("prov") Provincia provincia,
                                @Param("name") String name,
                                @Param("idCategory") Long idCategory);
}
