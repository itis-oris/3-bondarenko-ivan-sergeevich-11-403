package com.itis.cryptotracker.controller.rest;

import com.itis.cryptotracker.mapper.AlertMapper;
import com.itis.cryptotracker.model.enums.AlertDirection;
import com.itis.cryptotracker.model.enums.AlertStatus;
import com.itis.cryptotracker.dto.form.AlertForm;
import com.itis.cryptotracker.dto.request.AlertRequest;
import com.itis.cryptotracker.dto.response.AlertResponse;
import com.itis.cryptotracker.security.UserPrincipal;
import com.itis.cryptotracker.service.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Alerts", description = "User price alerts")
@RestController
@RequestMapping("/api/v1/alerts")
@RequiredArgsConstructor
public class AlertRestController {

    private final AlertService alertService;
    private final AlertMapper alertMapper;

    @Operation(summary = "List my alerts (with optional filters)")
    @GetMapping
    public List<AlertResponse> list(@AuthenticationPrincipal UserPrincipal principal,
                                    @Parameter(description = "Filter by status") @RequestParam(required = false) AlertStatus status,
                                    @Parameter(description = "Filter by direction") @RequestParam(required = false) AlertDirection direction,
                                    @Parameter(description = "Coin name/symbol substring") @RequestParam(required = false) String q) {
        return alertService.searchUserAlerts(principal.getId(), status, direction, q).stream()
                .map(alertMapper::toResponse)
                .toList();
    }

    @Operation(summary = "Get one of my alerts by id")
    @GetMapping("/{id}")
    public AlertResponse get(@AuthenticationPrincipal UserPrincipal principal,
                             @PathVariable Long id) {
        return alertMapper.toResponse(alertService.findOwned(id, principal.getId()));
    }

    @Operation(summary = "Create a new alert")
    @PostMapping
    public ResponseEntity<AlertResponse> create(@AuthenticationPrincipal UserPrincipal principal,
                                                @Valid @RequestBody AlertRequest request) {
        var saved = alertService.createFromRequest(principal.getId(), request);
        AlertResponse body = alertMapper.toResponse(saved);
        return ResponseEntity.created(URI.create("/api/v1/alerts/" + body.getId())).body(body);
    }

    @Operation(summary = "Update an existing alert")
    @PutMapping("/{id}")
    public AlertResponse update(@AuthenticationPrincipal UserPrincipal principal,
                                @PathVariable Long id,
                                @Valid @RequestBody AlertRequest request) {
        AlertForm form = new AlertForm();
        form.setCoinId(request.getCoinId());
        form.setDirection(request.getDirection());
        form.setTargetPrice(request.getTargetPrice());
        form.setChannel(request.getChannel());
        form.setComment(request.getComment());
        return alertMapper.toResponse(alertService.update(id, principal.getId(), form));
    }

    @Operation(summary = "Reactivate a triggered or disabled alert")
    @PostMapping("/{id}/reactivate")
    public AlertResponse reactivate(@AuthenticationPrincipal UserPrincipal principal,
                                    @PathVariable Long id) {
        return alertMapper.toResponse(alertService.reactivate(id, principal.getId()));
    }

    @Operation(summary = "Delete an alert")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal UserPrincipal principal,
                       @PathVariable Long id) {
        alertService.delete(id, principal.getId());
    }
}
