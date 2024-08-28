package com.example.CrudWithJoins.service;

import com.example.CrudWithJoins.entity.Employee;
import com.example.CrudWithJoins.entity.Address;
import com.example.CrudWithJoins.repository.EmployeeRepository;
import com.example.CrudWithJoins.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AddressRepository addressRepository;

    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public Optional<Employee> updateEmployee(Long id, Employee employee) {
        if (employeeRepository.existsById(id)) {
            employee.setEmployeeId(id);
            return Optional.of(employeeRepository.save(employee));
        }
        return Optional.empty();
    }

    public boolean deleteEmployee(Long id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> assignEmployeeToAddress(Long employeeId, Long addressId) {
        Optional<Employee> employeeOpt = employeeRepository.findById(employeeId);
        Optional<Address> addressOpt = addressRepository.findById(addressId);

        if (employeeOpt.isPresent() && addressOpt.isPresent()) {
            Employee employee = employeeOpt.get();
            Address address = addressOpt.get();
            employee.setAddress(address);
            employeeRepository.save(employee);
            return Optional.of(employee);
        }
        return Optional.empty();
    }

    //sorting
    public List<Employee> sortEmployee(String field) {
        return employeeRepository.findAll(Sort.by(Sort.Direction.ASC, field));
    }

    //pagination
    public Page<Employee> getEmployeeWithPagination(int page, int pageSize) {
        return employeeRepository.findAll(PageRequest.of(page, pageSize));
    }

    //searching
    public Page<Employee> searchEmployees(String name, String position, int page, int pageSize, String sortBy) {
        Pageable pageRequest = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, sortBy));
        if (name != null && position != null) {
            return employeeRepository.findByNameAndPosition(name, position, pageRequest);
        } else if (name != null) {
            return employeeRepository.findByName(name, pageRequest);
        } else if (position != null) {
            return employeeRepository.findByPosition(position, pageRequest);
        } else {
            return employeeRepository.findAll(pageRequest);
        }
    }
}
