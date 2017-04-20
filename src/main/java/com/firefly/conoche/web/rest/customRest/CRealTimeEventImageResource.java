package com.firefly.conoche.web.rest.customRest;

import com.codahale.metrics.annotation.Timed;
import com.firefly.conoche.service.customService.CRealTimeEventImageService;
import com.firefly.conoche.service.dto.RealTimeEventImageDTO;
import com.firefly.conoche.web.rest.util.PaginationUtil;
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


@RestController
@RequestMapping("/api")
public class CRealTimeEventImageResource {

    private final Logger log = LoggerFactory.getLogger(com.firefly.conoche.web.rest.RealTimeEventImageResource.class);

    private static final String ENTITY_NAME = "realTimeEventImage";

    private final CRealTimeEventImageService realTimeEventImageService;

    public CRealTimeEventImageResource(CRealTimeEventImageService realTimeEventImageService) {
        this.realTimeEventImageService = realTimeEventImageService;
    }

    @GetMapping("/real-time-event-images/event/{idEvent}")
    @Timed
    public ResponseEntity<List<RealTimeEventImageDTO>> getImagesByEvent(@PathVariable Long idEvent,
                                                                        @PageableDefault(page= 0, value = Integer.MAX_VALUE)
                                                                        @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get mesages from Event : {}", idEvent);
        Page<RealTimeEventImageDTO> page = realTimeEventImageService.findEventRealTimeImages(idEvent, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/real-time-event-images/event/" + idEvent);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/real-time-event-images/event/{idEvent}/{hours}")
    @Timed
    public ResponseEntity<List<RealTimeEventImageDTO>> getLastNHourImages(@PathVariable Long idEvent,
                                                                          @PathVariable Long hours,
                                                                        @PageableDefault(page= 0, value = Integer.MAX_VALUE)
                                                                        @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get mesages from Event : {}", idEvent);
        Page<RealTimeEventImageDTO> page = realTimeEventImageService.findEventRealtimeImagesAftherDate(
                idEvent, ZonedDateTime.now().minusHours(hours), pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
            "/api/real-time-event-images/event/" + idEvent + "/" + hours);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
