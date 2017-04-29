package com.firefly.conoche.web.rest.customRest;

import com.codahale.metrics.annotation.Timed;
import com.firefly.conoche.service.customService.CAuthOwnerService;
import io.github.jhipster.web.util.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by melvin on 3/29/2017.
 * Resource para validar si el usuario actual es el dueño del un evento.
 */

@RestController
@RequestMapping("/api")
public class CAuthOwnerResource {

    private final CAuthOwnerService cAuthOwnerService;

    /**
     * Constructor.
     * @param cAuthOwnerService
     */
    public CAuthOwnerResource(CAuthOwnerService cAuthOwnerService) {
        this.cAuthOwnerService = cAuthOwnerService;
    }

    /**
     * Valida si el usuario actual es el dueño del un evento
     * @param idEvent id del evento.
     * @return true si es owner y false de lo contrario.
     */
    @GetMapping("/is-owner/{idEvent}")
    @Timed
    public ResponseEntity<Boolean> isOwner(@PathVariable Long idEvent) {
       return ResponseUtil.wrapOrNotFound(cAuthOwnerService.currentUserIsOwner(idEvent));
    }
}
