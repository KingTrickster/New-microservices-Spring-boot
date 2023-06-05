package com.trxjster.employeeservice.service;

import com.trxjster.common.exception.EmailAlreadyExistsException;
import com.trxjster.common.exception.ResourceNotFoundException;
import com.trxjster.employeeservice.dto.APIResponseDto;
import com.trxjster.employeeservice.dto.DepartmentDto;
import com.trxjster.employeeservice.dto.EmployeeDto;
import com.trxjster.employeeservice.dto.OrganizationDto;
import com.trxjster.employeeservice.entity.Employee;
import com.trxjster.employeeservice.mapper.AutoEmployeeMapper;
import com.trxjster.employeeservice.repository.EmployeeRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService{
    private static final Logger logger = Logger.getLogger(String.valueOf(EmployeeServiceImpl.class));
    private final EmployeeRepository employeeRepository;
    private final APIClient apiClient;
    private final WebClient webClient;
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

//    @CircuitBreaker(name = "${spring.application.name}", fallbackMethod = "getDefaultDepartment") // Circuit breaker
    @Retry(name = "${spring.application.name}", fallbackMethod = "getDefaultDepartment") // Retry Pattern
    @Override
    public APIResponseDto getEmployeeById(Long employeeId) {

        logger.info("inside getEmployeeByID method");
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new ResourceNotFoundException("Employee", "id", employeeId));

//        ResponseEntity<DepartmentDto> responseEntity = restTemplate.getForEntity("http://localhost:8040/api/departments/" + employee.getDepartmentCode(),
//                DepartmentDto.class);

//        DepartmentDto departmentDto = responseEntity.getBody();

//        DepartmentDto departmentDto = apiClient.getDepartment(employee.getDepartmentCode());
        DepartmentDto departmentDto = webClient.get()
                .uri("http://localhost:8040/api/departments/" + employee.getDepartmentCode())
                .retrieve()
                .bodyToMono(DepartmentDto.class)
                .block();

        OrganizationDto organizationDto = webClient.get()
                .uri("http://localhost:8043/api/organizations/" + employee.getOrganizationCode())
                .retrieve()
                .bodyToMono(OrganizationDto.class)
                .block();

        EmployeeDto employeeDto = AutoEmployeeMapper.MAPPER.mapToEmployeeDto(employee);

        APIResponseDto apiResponseDto = new APIResponseDto();
        apiResponseDto.setEmployeeDto(employeeDto);
        apiResponseDto.setDepartmentDto(departmentDto);
        apiResponseDto.setOrganizationDto(organizationDto);

        return apiResponseDto;
    }

    public APIResponseDto getDefaultDepartment(Long employeeId, Exception exception){
        logger.info("inside fallback method");
        Employee employee = employeeRepository.findById(employeeId).get();

        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setDepartmentName("R&D Department");
        departmentDto.setDepartmentCode("RD001");
        departmentDto.setDepartmentDescription("Research and Development dpt");

        EmployeeDto employeeDto = AutoEmployeeMapper.MAPPER.mapToEmployeeDto(employee);
        APIResponseDto apiResponseDto = new APIResponseDto();
        apiResponseDto.setEmployeeDto(employeeDto);
        apiResponseDto.setDepartmentDto(departmentDto);

        return apiResponseDto;
    }
}
