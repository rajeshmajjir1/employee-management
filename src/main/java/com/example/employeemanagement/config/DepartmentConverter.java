package com.example.employeemanagement.config;

import com.example.employeemanagement.entity.Department;
import com.example.employeemanagement.repository.DepartmentRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DepartmentConverter
        implements Converter<String, Department> {

    private final DepartmentRepository departmentRepository;

    public DepartmentConverter(
            DepartmentRepository departmentRepository) {

        this.departmentRepository = departmentRepository;
    }

    @Override
    public Department convert(String source) {

        if (source == null || source.isBlank()) {
            return null;
        }

        Long departmentId = Long.valueOf(source);

        return departmentRepository
                .findById(departmentId)
                .orElse(null);
    }
}
