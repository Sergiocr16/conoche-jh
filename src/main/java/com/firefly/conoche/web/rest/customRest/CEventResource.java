package com.firefly.conoche.web.rest.customRest;

import com.codahale.metrics.annotation.Timed;
import com.firefly.conoche.domain.enumeration.Provincia;
import com.firefly.conoche.service.customService.CEventService;
import com.firefly.conoche.service.dto.EventDTO;
import com.firefly.conoche.service.dto.WrapperDTO;
import com.firefly.conoche.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
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
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

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

    @GetMapping("/event/search")
    @Timed
    public ResponseEntity<List<EventDTO>> getMesageByEvent(@RequestParam(value="provincia",required = false) Provincia provincia,
                                                           @RequestParam(value="search",required = false) Optional<String> name,
                                                           @RequestParam(value="history") Boolean history,
                                                           @PageableDefault(page= 0, value = Integer.MAX_VALUE)
                                                           @ApiParam Pageable pageable) throws URISyntaxException {



        ZonedDateTime zonedDateTime = history ? null : ZonedDateTime.now();
        Page<EventDTO> page = cEventService.findByProvincia(pageable, provincia,
            name.orElse(""), zonedDateTime);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "api/event/location");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/event/count")
    @Timed
    public ResponseEntity<WrapperDTO<Long>> getMesageByEvent() {
        Long count = cEventService.countEventAfter(ZonedDateTime.now());
        return ResponseEntity.ok().body(new WrapperDTO<>(count));
    }

}
