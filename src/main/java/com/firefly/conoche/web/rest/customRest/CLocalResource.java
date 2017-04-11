package com.firefly.conoche.web.rest.customRest;

import com.codahale.metrics.annotation.Timed;
import com.firefly.conoche.domain.enumeration.Provincia;
import com.firefly.conoche.service.customService.CLocalService;
import com.firefly.conoche.service.dto.EventDTO;
import com.firefly.conoche.service.dto.LocalDTO;
import com.firefly.conoche.service.dto.RealTimeEventImageDTO;
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
import java.util.Optional;

/**
 * Created by Alberto on 3/31/2017.
 */


@RestController
@RequestMapping("/api")


public class CLocalResource {

    private final CLocalService cLocalService;
    public CLocalResource(CLocalService cLocalService) {
        this.cLocalService = cLocalService;
    }

    @GetMapping("/local/owner/{ownerId}")
    @Timed
    public ResponseEntity<List<LocalDTO>> getMesageByEvent(@PathVariable Long ownerId,
                                                                        @PageableDefault(page= 0, value = Integer.MAX_VALUE)
                                                                        @ApiParam Pageable pageable) throws URISyntaxException {

        Page<LocalDTO> page = cLocalService.findLocalsByOwner(ownerId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "api/local/owner/" +ownerId);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/local/count")
    @Timed
    public ResponseEntity<WrapperDTO<Long>> getMesageByEvent() {
        Long count = cLocalService.count();
        return ResponseEntity.ok().body(new WrapperDTO<>(count));
    }

    @GetMapping("/local/search")
    @Timed
    public ResponseEntity<List<LocalDTO>> getMesageByEvent(@RequestParam(value="provincia",required = false) Provincia provincia,
                                                           @RequestParam(value="search",required = false) Optional<String> name,
                                                           @RequestParam(value="idCategory", required = false) Long category,
                                                           @PageableDefault(page= 0, value = Integer.MAX_VALUE)
                                                           @ApiParam Pageable pageable) throws URISyntaxException {
        Page<LocalDTO> page = cLocalService.findByProvinciaAndName(pageable, provincia,
            name.orElse(""),category);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "api/local/search");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
