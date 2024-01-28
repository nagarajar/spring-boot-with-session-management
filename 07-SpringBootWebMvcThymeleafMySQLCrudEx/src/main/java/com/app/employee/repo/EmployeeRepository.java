package com.app.employee.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.employee.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

}
