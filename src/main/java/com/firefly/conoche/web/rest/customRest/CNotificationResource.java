package com.firefly.conoche.web.rest.customRest;

import com.codahale.metrics.annotation.Timed;
import com.firefly.conoche.domain.Notification;
import com.firefly.conoche.domain.enumeration.Provincia;
import com.firefly.conoche.security.SecurityUtils;
import com.firefly.conoche.service.NotificationService;
import com.firefly.conoche.service.customService.CNotificationService;
import com.firefly.conoche.service.dto.DetailNotificationDTO;
import com.firefly.conoche.service.dto.EventDTO;
import com.firefly.conoche.service.dto.NotificationDTO;
import com.firefly.conoche.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;


/**
 * REST controller for managing Notification.
 * Controlador rest para las notificaciones.
 */
@RestController
@RequestMapping("/api")
public class CNotificationResource {

    private final Logger log = LoggerFactory.getLogger(com.firefly.conoche.web.rest.NotificationResource.class);

    private final CNotificationService cNotificationService;
    private final NotificationService notificationService;

    /**
     * Constuctor.
     * @param cNotificationService
     * @param notificationService
     */
    public CNotificationResource(CNotificationService cNotificationService, NotificationService notificationService) {
        this.cNotificationService = cNotificationService;
        this.notificationService = notificationService;
    }


    /**
     *
     * @param read en caso de ser verdadero se trae las
     *             notificaciones leidas. Ademas en caso de ser
     *             null se retornan todas las notificaciones
     * @param pageable objecto para la paginacion
     * @return notificaciones
     * @throws URISyntaxException
     */
    @GetMapping("/notification/user")
    @Timed
    public ResponseEntity<List<DetailNotificationDTO>> getMesageByEvent(@RequestParam(value="read",required = false) Boolean read,
                                                           @PageableDefault(page= 0, value = Integer.MAX_VALUE)
                                                           @ApiParam Pageable pageable) throws URISyntaxException {

        Page<DetailNotificationDTO> page = cNotificationService.getAllNotificationsFromCurrent(pageable, read);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "api/notification/user");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    /**
     *
     * @param id
     * @return Status code unauthorized en caso de no ser el dueño de la aplicacion el que haga la consulta.
     * @throws URISyntaxException
     */
    @PostMapping("/notification/{id}/read")
    @Timed
    public ResponseEntity getMesageByEvent(@PathVariable Long id) throws URISyntaxException {

        Optional<NotificationDTO> notificationDTO = Optional
            .ofNullable(notificationService.findOne(id));

        if(!notificationDTO.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return notificationDTO.filter( n -> n.getUserLogin()
            .equals(SecurityUtils.getCurrentUserLogin()))
            .map(n -> {
                n.setIsRead(true);
                notificationService.save(n);
                return ResponseEntity.ok().build();
            })
            .orElse(new ResponseEntity(HttpStatus.UNAUTHORIZED));
    }


}
