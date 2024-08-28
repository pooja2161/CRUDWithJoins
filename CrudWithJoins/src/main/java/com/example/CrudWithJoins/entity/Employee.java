package com.example.CrudWithJoins.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "employee_details")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "emp_id")
    private Long employeeId;

    private String name;
    private String position;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;
}
