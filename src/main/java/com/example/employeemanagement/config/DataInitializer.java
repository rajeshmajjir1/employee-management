package com.example.employeemanagement.config;

import com.example.employeemanagement.entity.Department;
import com.example.employeemanagement.repository.DepartmentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeDepartments(
            DepartmentRepository departmentRepository) {

        return args -> {

            if (departmentRepository.count() == 0) {

                departmentRepository.save(
                        new Department("IT")
                );

                departmentRepository.save(
                        new Department("HR")
                );

                departmentRepository.save(
                        new Department("Finance")
                );

                departmentRepository.save(
                        new Department("Sales")
                );

                departmentRepository.save(
                        new Department("Marketing")
                );
            }
        };
    }
}
