package com.example.employeemanagement.service;

import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.validation.PassportFileValidator;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PassportStorageService passportStorageService;

    public EmployeeService(
            EmployeeRepository employeeRepository,
            PassportStorageService passportStorageService) {

        this.employeeRepository = employeeRepository;
        this.passportStorageService = passportStorageService;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    /*
     * Existing save method.
     * This can still be used when no passport upload is involved.
     */
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    /*
     * Save employee with passport upload.
     */
    public Employee saveEmployee(
            Employee employee,
            MultipartFile passport) {

        // Validate passport
        String validationError =
                PassportFileValidator.validate(passport);

        if (validationError != null) {
            throw new IllegalArgumentException(validationError);
        }

        // Store passport file
        String storedFileName =
                passportStorageService.store(passport);

        // Store passport metadata
        employee.setPassportFileName(storedFileName);

        employee.setPassportOriginalFileName(
                passport.getOriginalFilename()
        );

        employee.setPassportContentType(
                passport.getContentType()
        );

        employee.setPassportFileSize(
                passport.getSize()
        );

        employee.setPassportUploadedAt(
                LocalDateTime.now()
        );

        // Save employee
        return employeeRepository.save(employee);
    }
}