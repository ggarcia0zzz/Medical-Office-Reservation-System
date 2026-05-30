package com.example.medicalofficereservationsystem.api.dto.controller;

import com.example.medicalofficereservationsystem.api.dto.OfficeDtos.*;
import com.example.medicalofficereservationsystem.service.OfficeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
public class OfficeController {

    private final OfficeService service;

    @PostMapping("/offices")
    public ResponseEntity<OfficeResponse> create(@Validated @RequestBody OfficeCreateRequest req,
                                                 UriComponentsBuilder uriBuilder) {
        var body = service.createOffice(req);
        var location = uriBuilder.path("/api/offices/{officeId}").buildAndExpand(body.id()).toUri();

        return ResponseEntity.created(location).body(body);
    }

    @GetMapping("/office")
    public ResponseEntity<List<OfficeResponse>> getOffice() {
        return ResponseEntity.ok(service.getAllOffices());
    }

    @PatchMapping("/office/{officeId}")
    public ResponseEntity<OfficeResponse> update(@PathVariable Long officeId,
                                                 @Valid @RequestBody OfficeUpdateRequest req){
        return ResponseEntity.ok(service.updateOffice(officeId, req));
    }
}
