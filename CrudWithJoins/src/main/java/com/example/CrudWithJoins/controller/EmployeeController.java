package com.example.CrudWithJoins.controller;

import com.example.CrudWithJoins.entity.Employee;
import com.example.CrudWithJoins.response.StatusResponse;
import com.example.CrudWithJoins.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // Create a new employee
    @PostMapping
    public ResponseEntity<StatusResponse<Employee>> createEmployee(@RequestBody Employee employee) {
        if (employee == null) {
            StatusResponse<Employee> response = new StatusResponse<>(HttpStatus.BAD_REQUEST, "Invalid employee data");
            return ResponseEntity.badRequest().body(response);
        }
        Employee createdEmployee = employeeService.createEmployee(employee);
        StatusResponse<Employee> response = new StatusResponse<>(HttpStatus.CREATED, "Employee created successfully", createdEmployee);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Get all employees
    @GetMapping
    public ResponseEntity<StatusResponse<List<Employee>>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        StatusResponse<List<Employee>> response = new StatusResponse<>(HttpStatus.OK, "Employees retrieved successfully", employees);
        return ResponseEntity.ok(response);
    }

    // Get an employee by ID
    @GetMapping("/{id}")
    public ResponseEntity<StatusResponse<Employee>> getEmployeeById(@PathVariable Long id) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        if (employee.isPresent()) {
            StatusResponse<Employee> response = new StatusResponse<>(HttpStatus.OK, "Employee retrieved successfully", employee.get());
            return ResponseEntity.ok(response);
        } else {
            StatusResponse<Employee> response = new StatusResponse<>(HttpStatus.NOT_FOUND, "Employee not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // Update an employee
    @PutMapping("/{id}")
    public ResponseEntity<StatusResponse<Employee>> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        if (employee == null) {
            StatusResponse<Employee> response = new StatusResponse<>(HttpStatus.BAD_REQUEST, "Invalid employee data");
            return ResponseEntity.badRequest().body(response);
        }
        Optional<Employee> updatedEmployee = employeeService.updateEmployee(id, employee);
        if (updatedEmployee.isPresent()) {
            StatusResponse<Employee> response = new StatusResponse<>(HttpStatus.OK, "Employee updated successfully", updatedEmployee.get());
            return ResponseEntity.ok(response);
        } else {
            StatusResponse<Employee> response = new StatusResponse<>(HttpStatus.NOT_FOUND, "Employee not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // Delete an employee
    @DeleteMapping("/{id}")
    public ResponseEntity<StatusResponse<Void>> deleteEmployee(@PathVariable Long id) {
        boolean deleted = employeeService.deleteEmployee(id);
        if (deleted) {
            StatusResponse<Void> response = new StatusResponse<>(HttpStatus.NO_CONTENT, "Employee deleted successfully");
            return ResponseEntity.noContent().build();
        } else {
            StatusResponse<Void> response = new StatusResponse<>(HttpStatus.NOT_FOUND, "Employee not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // Assign an employee to an address
    @PutMapping("/{employeeId}/address/{addressId}")
    public ResponseEntity<StatusResponse<Employee>> assignEmployeeToAddress(@PathVariable Long employeeId, @PathVariable Long addressId) {
        Optional<Employee> updatedEmployee = employeeService.assignEmployeeToAddress(employeeId, addressId);
        if (updatedEmployee.isPresent()) {
            StatusResponse<Employee> response = new StatusResponse<>(HttpStatus.OK, "Employee assigned to address successfully", updatedEmployee.get());
            return ResponseEntity.ok(response);
        } else {
            StatusResponse<Employee> response = new StatusResponse<>(HttpStatus.NOT_FOUND, "Employee or address not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    //Sorting
    @GetMapping("/sort/{field}")
    public List<Employee> sort(@PathVariable String field) {
        return employeeService.sortEmployee(field);
    }

    //Pagination
    @GetMapping("/pagination/{page}/{pageSize}")
    public Page<Employee> paginationEmployee(@PathVariable int page, @PathVariable int pageSize) {
        return employeeService.getEmployeeWithPagination(page, pageSize);
    }

    //Searching
    @GetMapping("/search")
    public ResponseEntity<StatusResponse<Page<Employee>>> searchEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String position,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "employeeId") String sortBy) {

        Page<Employee> employees = employeeService.searchEmployees(name, position, page, pageSize, sortBy);
        StatusResponse<Page<Employee>> response = new StatusResponse<>(HttpStatus.OK, "Employees retrieved successfully", employees);
        return ResponseEntity.ok(response);
    }
}
