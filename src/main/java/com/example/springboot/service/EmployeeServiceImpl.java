package com.example.springboot.service;

import com.example.springboot.dto.EmployeeDTO;
import com.example.springboot.entity.Employee;
import com.example.springboot.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        return repository.findAll()
                .stream()
                .map(emp -> modelMapper.map(emp, EmployeeDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<EmployeeDTO> getAllEmployees(Pageable pageable) {
        return repository.findAll(pageable)
                .map(emp -> modelMapper.map(emp, EmployeeDTO.class));
    }

    @Override
    public EmployeeDTO getEmployeeById(Long id) {
        log.debug("Looking up employee with ID: {}", id);
        Employee emp = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id " + id));
        return modelMapper.map(emp, EmployeeDTO.class);
    }

    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        Employee emp = modelMapper.map(employeeDTO, Employee.class);
        Employee saved = repository.save(emp);
        return modelMapper.map(saved, EmployeeDTO.class);
    }

    @Override
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Employee existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id " + id));
        
        modelMapper.map(employeeDTO, existing);
        Employee updated = repository.save(existing);
        return modelMapper.map(updated, EmployeeDTO.class);
    }

    @Override
    public void deleteEmployee(Long id) {
        repository.deleteById(id);
    }
}
