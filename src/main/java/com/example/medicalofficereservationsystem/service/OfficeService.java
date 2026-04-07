package com.example.medicalofficereservationsystem.service;

import com.example.medicalofficereservationsystem.api.dto.OfficeDtos.*;

import java.util.List;

public interface OfficeService {
    OfficeResponse createOffice(OfficeCreateRequest req);
    List<OfficeResponse> getAllOffices();
    OfficeResponse updateOffice(Long id, OfficeUpdateRequest uptRequest);

}
