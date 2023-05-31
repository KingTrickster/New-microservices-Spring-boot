package com.trxjster.employeeservice.service;

import com.trxjster.common.exception.EmailAlreadyExistsException;
import com.trxjster.common.exception.ResourceNotFoundException;
import com.trxjster.employeeservice.dto.EmployeeDto;
import com.trxjster.employeeservice.entity.Employee;
import com.trxjster.employeeservice.mapper.AutoEmployeeMapper;
import com.trxjster.employeeservice.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService{
    private final EmployeeRepository employeeRepository;


    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {
        Optional<Employee> optionalEmployee = employeeRepository.findByEmail(employeeDto.getEmail());
        if(optionalEmployee.isPresent())
            throw new EmailAlreadyExistsException("Email has already been registered.");
        Employee employee = AutoEmployeeMapper.MAPPER.mapToEmployee(employeeDto);
        Employee savedEmployee = employeeRepository.save(employee);
        employeeDto.setId(savedEmployee.getId());
        return employeeDto;
    }

    @Override
    public EmployeeDto getEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new ResourceNotFoundException("Employee", "id", employeeId));
        return AutoEmployeeMapper.MAPPER.mapToEmployeeDto(employee);
    }
}
