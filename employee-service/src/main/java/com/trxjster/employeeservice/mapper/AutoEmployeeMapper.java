package com.trxjster.employeeservice.mapper;

import com.trxjster.employeeservice.dto.EmployeeDto;
import com.trxjster.employeeservice.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AutoEmployeeMapper {
    AutoEmployeeMapper MAPPER = Mappers.getMapper(AutoEmployeeMapper.class);

    EmployeeDto mapToEmployeeDto(Employee employee);
    Employee mapToEmployee(EmployeeDto employeeDto);
}
