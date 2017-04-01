package com.firefly.conoche.web.rest.customRest;

import com.codahale.metrics.annotation.Timed;
import com.firefly.conoche.service.customService.CLocalService;
import com.firefly.conoche.service.dto.LocalDTO;
import com.firefly.conoche.service.dto.RealTimeEventImageDTO;
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
}
