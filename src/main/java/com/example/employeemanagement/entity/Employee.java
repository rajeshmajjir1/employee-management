package com.example.employeemanagement.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.employeemanagement.validation.MinimumAge;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull(message = "Date of birth is required")
    @MinimumAge(value = 18, message = "Employee must be at least 18 years old")
    @Column(name = "dob", nullable = false)
    private LocalDate dob;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    @NotNull(message = "Department is required")
    private Department department;

    @NotNull(message = "Salary is required")
    @Positive(message = "Salary must be greater than zero")
    @Column(nullable = false)
    private BigDecimal salary;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;
    
    @Column(name = "passport_file_name")
    private String passportFileName;

    @Column(name = "passport_original_file_name")
    private String passportOriginalFileName;

    @Column(name = "passport_content_type")
    private String passportContentType;

    @Column(name = "passport_file_size")
    private Long passportFileSize;

    @Column(name = "passport_uploaded_at")
    private LocalDateTime passportUploadedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public BigDecimal getSalary() {
		return salary;
	}

	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}

	public Employee getManager() {
		return manager;
	}

	public void setManager(Employee manager) {
		this.manager = manager;
	}

	public String getPassportFileName() {
		return passportFileName;
	}

	public void setPassportFileName(String passportFileName) {
		this.passportFileName = passportFileName;
	}

	public String getPassportOriginalFileName() {
		return passportOriginalFileName;
	}

	public void setPassportOriginalFileName(String passportOriginalFileName) {
		this.passportOriginalFileName = passportOriginalFileName;
	}

	public String getPassportContentType() {
		return passportContentType;
	}

	public void setPassportContentType(String passportContentType) {
		this.passportContentType = passportContentType;
	}

	public Long getPassportFileSize() {
		return passportFileSize;
	}

	public void setPassportFileSize(Long passportFileSize) {
		this.passportFileSize = passportFileSize;
	}

	public LocalDateTime getPassportUploadedAt() {
		return passportUploadedAt;
	}

	public void setPassportUploadedAt(LocalDateTime passportUploadedAt) {
		this.passportUploadedAt = passportUploadedAt;
	}

    
}
