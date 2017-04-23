package com.firefly.conoche.web.rest.customRest;

import com.codahale.metrics.annotation.Timed;
import com.firefly.conoche.domain.enumeration.Provincia;
import com.firefly.conoche.service.customService.CNotificationService;
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
 */
@RestController
@RequestMapping("/api")
public class CNotificationResource {

    private final Logger log = LoggerFactory.getLogger(com.firefly.conoche.web.rest.NotificationResource.class);

    private static final String ENTITY_NAME = "notification";

    private final CNotificationService cNotificationService;

    public CNotificationResource(CNotificationService cNotificationService) {
        this.cNotificationService = cNotificationService;
    }


    @GetMapping("/notification/user")
    @Timed
    public ResponseEntity<List<NotificationDTO>> getMesageByEvent(@RequestParam(value="read",required = false) Boolean read,
                                                           @PageableDefault(page= 0, value = Integer.MAX_VALUE)
                                                           @ApiParam Pageable pageable) throws URISyntaxException {

        Page<NotificationDTO> page = cNotificationService.getAllNotificationsFromCurrent(pageable, read);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "api/notification/user");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
