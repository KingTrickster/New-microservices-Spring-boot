package com.trxjster.employeeservice.service;

import com.trxjster.employeeservice.dto.APIResponseDto;
import com.trxjster.employeeservice.dto.EmployeeDto;

public interface EmployeeService {
    EmployeeDto saveEmployee(EmployeeDto employeeDto);
    APIResponseDto getEmployeeById(Long employeeId);
}
