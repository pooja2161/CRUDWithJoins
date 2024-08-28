package com.example.CrudWithJoins.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.CrudWithJoins.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Page<Employee> findByName(String name, Pageable pageable);
    Page<Employee> findByPosition(String position, Pageable pageable);
    Page<Employee> findByNameAndPosition(String name, String position, Pageable pageable);
}
