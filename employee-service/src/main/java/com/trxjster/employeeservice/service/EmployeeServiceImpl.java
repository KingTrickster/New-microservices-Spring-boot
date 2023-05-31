package com.trxjster.employeeservice.service;

import com.trxjster.common.exception.EmailAlreadyExistsException;
import com.trxjster.common.exception.ResourceNotFoundException;
import com.trxjster.employeeservice.dto.APIResponseDto;
import com.trxjster.employeeservice.dto.DepartmentDto;
import com.trxjster.employeeservice.dto.EmployeeDto;
import com.trxjster.employeeservice.entity.Employee;
import com.trxjster.employeeservice.mapper.AutoEmployeeMapper;
import com.trxjster.employeeservice.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService{
    private final EmployeeRepository employeeRepository;
    private final APIClient apiClient;
//    private final RestTemplate restTemplate;


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
    public APIResponseDto getEmployeeById(Long employeeId) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new ResourceNotFoundException("Employee", "id", employeeId));

//        ResponseEntity<DepartmentDto> responseEntity = restTemplate.getForEntity("http://localhost:8040/api/departments/" + employee.getDepartmentCode(),
//                DepartmentDto.class);

//        DepartmentDto departmentDto = responseEntity.getBody();

        DepartmentDto departmentDto = apiClient.getDepartment(employee.getDepartmentCode());
        EmployeeDto employeeDto = new EmployeeDto(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getDepartmentCode()
        );

        APIResponseDto apiResponseDto = new APIResponseDto();
        apiResponseDto.setEmployeeDto(employeeDto);
        apiResponseDto.setDepartmentDto(departmentDto);

        return apiResponseDto;
    }
}
