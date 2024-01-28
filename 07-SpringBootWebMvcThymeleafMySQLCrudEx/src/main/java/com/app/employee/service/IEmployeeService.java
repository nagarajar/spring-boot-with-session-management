package com.app.employee.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.app.employee.entity.Employee;
import com.app.employee.exception.EmployeeNotFoundException;


public interface IEmployeeService 
{
	Integer saveEmployee(Employee e);
	
	void updateEmployee(Employee e);
	
	//2nd change for Exception - declare throws
	void deleteEmployee(Integer id) throws EmployeeNotFoundException;
	
	Employee getOneEmployee(Integer id) throws EmployeeNotFoundException;
	
	List<Employee> getAllEmployee();
	
	Page<Employee> getAllEmployee(Pageable pageable);
}
