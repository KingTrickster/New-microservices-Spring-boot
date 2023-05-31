package com.trxjster.departmentservice.service;

import com.trxjster.common.exception.ResourceNotFoundException;
import com.trxjster.departmentservice.dto.DepartmentDto;
import com.trxjster.departmentservice.entity.Department;
import com.trxjster.departmentservice.mapper.AutoDepartmentMapper;
import com.trxjster.departmentservice.repository.DepartmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService{
    private final DepartmentRepository departmentRepository;


    @Override
    public DepartmentDto saveDepartment(DepartmentDto departmentDto) {
        Department department = AutoDepartmentMapper.MAPPER.mapToDepartment(departmentDto);
        Department savedDepartment = departmentRepository.save(department);
        departmentDto.setId(savedDepartment.getId());
        return departmentDto;
    }

    @Override
    public DepartmentDto getDepartmentByCode(String departmentCode) {
        Department department = departmentRepository.findByDepartmentCode(departmentCode).orElseThrow(
                () -> new ResourceNotFoundException("Department", "code", Long.parseLong(departmentCode))
        );
        DepartmentDto departmentDto = AutoDepartmentMapper.MAPPER.mapToDepartmentDto(department);
        return departmentDto;
    }
}
