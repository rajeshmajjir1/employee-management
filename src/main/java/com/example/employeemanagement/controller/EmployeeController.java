package com.example.employeemanagement.controller;

import java.time.LocalDate;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.service.DepartmentService;
import com.example.employeemanagement.service.EmployeeService;
import com.example.employeemanagement.service.PassportStorageService;
import com.example.employeemanagement.validation.PassportFileValidator;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final PassportStorageService passportStorageService;

    public EmployeeController(
            EmployeeService employeeService,
            DepartmentService departmentService,
            PassportStorageService passportStorageService) {

        this.employeeService = employeeService;
        this.departmentService = departmentService;
        this.passportStorageService = passportStorageService;
    }

    /**
     * Employee listing.
     */
    @GetMapping
    public String listEmployees(Model model) {

        model.addAttribute(
                "employees",
                employeeService.getAllEmployees()
        );

        return "employee-list";
    }

    /**
     * Add employee form.
     */
    @GetMapping("/add")
    public String showAddForm(Model model) {

        Employee employee = new Employee();

        model.addAttribute(
                "employee",
                employee
        );

        addFormData(model, null);

        return "employee-form";
    }

    /**
     * Save / Update employee.
     */
    @PostMapping("/save")
    public String saveEmployee(
            @Valid @ModelAttribute("employee") Employee employee,
            BindingResult result,
            @RequestParam(
                    value = "passport",
                    required = false
            ) MultipartFile passport,
            Model model) {

        /*
         * Bean validation errors.
         */
        if (result.hasErrors()) {

            addFormData(
                    model,
                    employee.getId()
            );

            return "employee-form";
        }

        /*
         * Passport is mandatory only
         * when creating a new employee.
         */
        if (employee.getId() == null &&
                (passport == null || passport.isEmpty())) {

            model.addAttribute(
                    "passportError",
                    "Passport file is required."
            );

            addFormData(model, null);

            return "employee-form";
        }

        /*
         * Validate passport when supplied.
         */
        if (passport != null &&
                !passport.isEmpty()) {

            String passportError =
                    PassportFileValidator.validate(passport);

            if (passportError != null) {

                model.addAttribute(
                        "passportError",
                        passportError
                );

                addFormData(
                        model,
                        employee.getId()
                );

                return "employee-form";
            }
        }

        /*
         * Save employee and passport.
         */
        employeeService.saveEmployee(
                employee,
                passport
        );

        return "redirect:/employees";
    }

    /**
     * View employee details.
     */
    @GetMapping("/view/{id}")
    public String viewEmployee(
            @PathVariable Long id,
            Model model) {

        Employee employee = employeeService
                .getEmployeeById(id)
                .orElse(null);

        if (employee == null) {
            return "redirect:/employees";
        }

        model.addAttribute(
                "employee",
                employee
        );

        return "employee-view";
    }

    /**
     * Edit employee.
     */
    @GetMapping("/edit/{id}")
    public String editEmployee(
            @PathVariable Long id,
            Model model) {

        Employee employee = employeeService
                .getEmployeeById(id)
                .orElse(null);

        if (employee == null) {
            return "redirect:/employees";
        }

        model.addAttribute(
                "employee",
                employee
        );

        addFormData(
                model,
                id
        );

        return "employee-form";
    }

    /**
     * View passport.
     *
     * URL:
     * /employees/passport/{employeeId}
     */
    @GetMapping("/passport/{id}")
    public ResponseEntity<Resource> viewPassport(
            @PathVariable Long id) {

        Employee employee = employeeService
                .getEmployeeById(id)
                .orElse(null);

        /*
         * Employee doesn't exist.
         */
        if (employee == null) {
            return ResponseEntity.notFound().build();
        }

        /*
         * Employee doesn't have a passport.
         */
        if (employee.getPassportFileName() == null ||
                employee.getPassportFileName().isBlank()) {

            return ResponseEntity.notFound().build();
        }

        try {

            Resource resource =
                    passportStorageService.load(
                            employee.getPassportFileName()
                    );

            String contentType =
                    employee.getPassportContentType();

            /*
             * Fallback content type.
             */
            if (contentType == null ||
                    contentType.isBlank()) {

                contentType =
                        MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            return ResponseEntity.ok()
                    .contentType(
                            MediaType.parseMediaType(
                                    contentType
                            )
                    )
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" +
                            employee.getPassportOriginalFileName() +
                            "\""
                    )
                    .body(resource);

        } catch (RuntimeException ex) {

            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Common form data.
     */
    private void addFormData(
            Model model,
            Long employeeId) {

        var employees =
                employeeService.getAllEmployees();

        /*
         * Don't allow an employee
         * to select themselves as manager.
         */
        if (employeeId != null) {

            employees = employees.stream()
                    .filter(employee ->
                            !employee.getId()
                                    .equals(employeeId))
                    .toList();
        }

        model.addAttribute(
                "employees",
                employees
        );

        model.addAttribute(
                "departments",
                departmentService.getAllDepartments()
        );

        /*
         * Employee must be at least 18.
         */
        model.addAttribute(
                "maxDob",
                LocalDate.now().minusYears(18)
        );
    }
}