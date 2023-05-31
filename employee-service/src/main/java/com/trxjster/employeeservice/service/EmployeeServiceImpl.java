package com.trxjster.employeeservice.service;

import com.trxjster.employeeservice.dto.EmployeeDto;
import com.trxjster.employeeservice.entity.Employee;
import com.trxjster.employeeservice.mapper.AutoEmployeeMapper;
import com.trxjster.employeeservice.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService{
    private final EmployeeRepository employeeRepository;


    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {
        Employee employee = AutoEmployeeMapper.MAPPER.mapToEmployee(employeeDto);
        Employee savedEmployee = employeeRepository.save(employee);
        employeeDto.setId(savedEmployee.getId());
        return employeeDto;
    }

    @Override
    public EmployeeDto getEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).get();
        EmployeeDto employeeDto = AutoEmployeeMapper.MAPPER.mapToEmployeeDto(employee);
        return employeeDto;
    }
}
