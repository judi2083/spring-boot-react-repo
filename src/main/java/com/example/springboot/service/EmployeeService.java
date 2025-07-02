package com.example.springboot.service;

import com.example.springboot.dto.EmployeeDTO;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {
    List<EmployeeDTO> getAllEmployees();
    Page<EmployeeDTO> getAllEmployees(Pageable pageable);
    EmployeeDTO getEmployeeById(Long id);
    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);
    EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO);
    void deleteEmployee(Long id);
}
