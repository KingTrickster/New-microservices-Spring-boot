package com.trxjster.employeeservice.service;

import com.trxjster.employeeservice.dto.DepartmentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "http://localhost:8040", value = "DEPARTMENT-SERVICE")
public interface APIClient {

    @GetMapping("/api/departments/{departmentCode}")
    DepartmentDto getDepartment(@PathVariable String departmentCode);
}
