package com.example.medicalofficereservationsystem.service;

import com.example.medicalofficereservationsystem.api.dto.OfficeDtos.*;
import com.example.medicalofficereservationsystem.entities.Office;
import com.example.medicalofficereservationsystem.repository.OfficeRepository;
import com.example.medicalofficereservationsystem.service.Mapper.OfficeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfficeServiceImpl implements OfficeService{
    private final OfficeRepository officeRepository;
    private final OfficeMapper officeMapper;

    @Override
    @Transactional
    public OfficeResponse createOffice(OfficeCreateRequest req) {
        Office o =  officeMapper.toEntity(req);
        return officeMapper.toResponse(officeRepository.save(o));
    }

    @Override
    public List<OfficeResponse> getAllOffices() {
        return officeRepository.findAll().stream().map(officeMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public OfficeResponse updateOffice(Long id, OfficeUpdateRequest uptRequest) {
        Office o = officeRepository.findById(id).orElseThrow();
        officeMapper.updateFromRequest(o, uptRequest);
        return officeMapper.toResponse(officeRepository.save(o));

    }
}
