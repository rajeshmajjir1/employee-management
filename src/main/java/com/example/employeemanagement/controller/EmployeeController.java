package com.example.employeemanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.service.DepartmentService;
import com.example.employeemanagement.service.EmployeeService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

	private final EmployeeService employeeService;
	private final DepartmentService departmentService;

	public EmployeeController(
	        EmployeeService employeeService,
	        DepartmentService departmentService) {

	    this.employeeService = employeeService;
	    this.departmentService = departmentService;
	}


    @GetMapping
    public String listEmployees(Model model) {
        model.addAttribute("employees",
                employeeService.getAllEmployees());

        return "employee-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {

        model.addAttribute(
                "employee",
                new Employee()
        );

        model.addAttribute(
                "employees",
                employeeService.getAllEmployees()
        );

        model.addAttribute(
                "departments",
                departmentService.getAllDepartments()
        );

        return "employee-form";
    }


    @PostMapping("/save")
    public String saveEmployee(
            @Valid @ModelAttribute("employee") Employee employee,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {

            model.addAttribute(
                    "employees",
                    employeeService.getAllEmployees()
            );

            return "employee-form";
        }

        employeeService.saveEmployee(employee);

        return "redirect:/employees";
    }

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


        model.addAttribute("employee", employee);

        return "employee-view";
    }

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

        model.addAttribute(
                "employees",
                employeeService.getAllEmployees()
        );

        model.addAttribute(
                "departments",
                departmentService.getAllDepartments()
        );

        return "employee-form";
    }

}
