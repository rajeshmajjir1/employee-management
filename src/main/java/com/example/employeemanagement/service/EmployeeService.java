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

    /**
     * Get all employees.
     */
    public List<Employee> getAllEmployees() {

        return employeeRepository.findAll();
    }

    /**
     * Get employee by ID.
     */
    public Optional<Employee> getEmployeeById(Long id) {

        return employeeRepository.findById(id);
    }

    /**
     * Save employee without passport.
     */
    public Employee saveEmployee(Employee employee) {

        return employeeRepository.save(employee);
    }

    /**
     * Save employee with optional passport.
     *
     * Create:
     * - Passport is mandatory.
     *
     * Update:
     * - Passport is optional.
     * - Existing passport is retained if no new file is supplied.
     * - Existing passport metadata is preserved.
     * - New passport replaces the old passport metadata.
     */
    public Employee saveEmployee(
            Employee employee,
            MultipartFile passport) {

        /*
         * CREATE
         */
        if (employee.getId() == null) {

            /*
             * Passport is mandatory when
             * creating a new employee.
             */
            if (passport == null || passport.isEmpty()) {

                throw new IllegalArgumentException(
                        "Passport file is required."
                );
            }

            /*
             * Validate passport.
             */
            String validationError =
                    PassportFileValidator.validate(passport);

            if (validationError != null) {

                throw new IllegalArgumentException(
                        validationError
                );
            }

            /*
             * Store passport file.
             */
            storePassport(employee, passport);

            return employeeRepository.save(employee);
        }

        /*
         * UPDATE
         */

        /*
         * Load the existing employee from database.
         */
        Employee existingEmployee =
                employeeRepository.findById(employee.getId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Employee not found: "
                                                + employee.getId()
                                )
                        );

        /*
         * Preserve existing passport information.
         *
         * This is important because the edit form
         * does not submit the existing physical file.
         */
        employee.setPassportFileName(
                existingEmployee.getPassportFileName()
        );

        employee.setPassportOriginalFileName(
                existingEmployee.getPassportOriginalFileName()
        );

        employee.setPassportContentType(
                existingEmployee.getPassportContentType()
        );

        employee.setPassportFileSize(
                existingEmployee.getPassportFileSize()
        );

        employee.setPassportUploadedAt(
                existingEmployee.getPassportUploadedAt()
        );

        /*
         * If user selected a new passport,
         * validate and replace the passport metadata.
         */
        if (passport != null && !passport.isEmpty()) {

            String validationError =
                    PassportFileValidator.validate(passport);

            if (validationError != null) {

                throw new IllegalArgumentException(
                        validationError
                );
            }

            /*
             * Store the new passport.
             */
            storePassport(employee, passport);
        }

        /*
         * Save updated employee.
         */
        return employeeRepository.save(employee);
    }

    /**
     * Store passport and update employee metadata.
     */
    private void storePassport(
            Employee employee,
            MultipartFile passport) {

        String storedFileName =
                passportStorageService.store(passport);

        employee.setPassportFileName(
                storedFileName
        );

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
    }
}
