package com.example.medicalofficereservationsystem.service;

import com.example.medicalofficereservationsystem.api.dto.OfficeDtos.*;
import com.example.medicalofficereservationsystem.api.dto.PatientDtos.*;
import com.example.medicalofficereservationsystem.api.dto.ReportDtos.*;
import com.example.medicalofficereservationsystem.entities.Office;
import com.example.medicalofficereservationsystem.entities.Patient;
import com.example.medicalofficereservationsystem.enums.AppointmentStatus;
import com.example.medicalofficereservationsystem.repository.AppointmentRepository;
import com.example.medicalofficereservationsystem.repository.DoctorRepository;
import com.example.medicalofficereservationsystem.repository.OfficeRepository;
import com.example.medicalofficereservationsystem.service.Mapper.DoctorMapper;
import com.example.medicalofficereservationsystem.service.Mapper.OfficeMapper;
import com.example.medicalofficereservationsystem.service.Mapper.PatientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final AppointmentRepository appointmentRepository;
    private final PatientMapper patientMapper;
    private final OfficeRepository officeRepository;
    private final OfficeMapper officeMapper;

    @Override
    public DoctorProductivityReport getDoctorProductivityReport(Long doctorId) {

        //Getting the doctor and prepping it for display at the report
        var doctor = doctorMapper.toProductivityResponse(doctorRepository.findById(doctorId).orElseThrow());

        //Getting the other fields of the report
        Long completedAppointments = appointmentRepository.countAppointmentByStatusIsAndDoctor_IdIs(AppointmentStatus.COMPLETED, doctorId);
        Long totalAppointments = appointmentRepository.countAppointmentByDoctor_IdIs(doctorId);

        if(totalAppointments > 0){
            float productivityPercent = (float) completedAppointments/totalAppointments *100;
            return doctorMapper.toProductivityReport(doctor, completedAppointments, totalAppointments, productivityPercent);
        }
        return doctorMapper.toProductivityReport(doctor, 0L, 0L, 0);
    }

    @Override
    public List<PatientNoShowResponse> getPatientNoShowReport() {
        var page = PageRequest.of(0, 10);

        List<Object[]> patientInfo = appointmentRepository.findPatientsByNoShows(page);

        List<PatientNoShowResponse> patientRanking = new ArrayList<>();

        for(Object[] p: patientInfo){
            var patient = patientMapper.toNoShowResponse((Patient) p[0], (Long) p[1]);
            patientRanking.add(patient);
        }

        return patientRanking;
    }

    @Override
    public List<OfficeOccupancyResponse> getOfficeOccupancyReport(LocalDateTime startDate, LocalDateTime endDate, int pageNumber, int pageSize) {
        var page = PageRequest.of(pageNumber, pageSize);

        Page<Object[]> officeInfo = officeRepository.findOfficeOccupancyData(startDate, endDate, page);

        Long days = ChronoUnit.DAYS.between(startDate, endDate);

        List<OfficeOccupancyResponse> officeReport = new ArrayList<>();

        for (Object[] o: officeInfo){

            Office office = (Office) o[0];
            Duration totalAvailable = Duration.between(office.getOpeningHour(), office.getClosingHour());
            Long availableSeconds = totalAvailable.getSeconds()*days;

            Double busySeconds = (Double) o[1];

            Long occupancyPercent = (long) (busySeconds/availableSeconds*100);

            var officeResponse = officeMapper.toOccupancyResponse( office, busySeconds/3600, availableSeconds/3600, occupancyPercent);

            officeReport.add(officeResponse);

        }
        return officeReport;
    }

}
