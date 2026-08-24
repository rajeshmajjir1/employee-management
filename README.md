# Employee Management Application

## 1. Project Overview

This project is a web-based Employee Management application developed using Java 17, Spring Boot, Spring Data JPA, H2 Database, Thymeleaf, JavaScript, and Bootstrap.

The application allows users to:

- Add employees
- View employee details
- Edit employee details
- List all employees
- Assign employees to departments
- Assign an optional manager to an employee
- Validate employee information
- Dynamically load departments from the backend
- Store employee and department information in an H2 database

---

## 2. Technologies Used

| Technology | Purpose |
|------------|---------|
| Java 17 | Programming language |
| Spring Boot | Application framework |
| Spring MVC | Web/controller layer |
| Spring Data JPA | Database persistence |
| Hibernate | ORM implementation |
| H2 Database | In-memory relational database |
| Thymeleaf | Server-side HTML rendering |
| JavaScript | Client-side validation and UI behavior |
| Bootstrap 5 | Responsive UI styling |
| Maven | Build and dependency management |

---

## 3. Features

### Employee Management

The application provides the following functionality:

- Add a new employee
- View employee details
- Edit an existing employee
- Display employees in a grid/table
- Navigate between employee listing and employee form

### Employee Fields

Each employee contains:

- First Name - Required
- Last Name - Required
- Date of Birth - Required
- Department - Required
- Salary - Required
- Manager - Optional

### Department Management

Departments are stored in a separate database table.

Departments are loaded dynamically from the backend instead of being hardcoded in the HTML.

Example departments:

- IT
- HR
- Finance
- Sales
- Marketing
- Operations

Additional departments can be added directly to the database and will automatically appear in the employee form.

### Manager Relationship

An employee can optionally have another employee as their manager.

The manager relationship is self-referencing:

Employee
   |
   └── manager_id
          |
          └── Employee.id
