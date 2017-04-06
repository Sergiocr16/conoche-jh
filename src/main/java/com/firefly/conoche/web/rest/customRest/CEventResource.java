package com.firefly.conoche.web.rest.customRest;

import com.codahale.metrics.annotation.Timed;
import com.firefly.conoche.domain.enumeration.Provincia;
import com.firefly.conoche.service.customService.CEventService;
import com.firefly.conoche.service.dto.EventDTO;
import com.firefly.conoche.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by ileanaguntanis on 5/4/17.
 */
@RestController
@RequestMapping("/api")
public class CEventResource {
    private final CEventService cEventService;
    public CEventResource(CEventService cEventService) {
        this.cEventService = cEventService;
    }

    @GetMapping("/event/location/{provincia}/{name}")
    @Timed
    public ResponseEntity<List<EventDTO>> getMesageByEvent(@PathVariable Provincia provincia,
                                                           @PathVariable String name,
                                                           @PageableDefault(page= 0, value = Integer.MAX_VALUE)
                                                           @ApiParam Pageable pageable) throws URISyntaxException {

        Page<EventDTO> page = cEventService.findByProvincia(pageable, provincia, name);
        String url = String.format("/event/location/%s/%s", provincia.name(), name);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, url);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
