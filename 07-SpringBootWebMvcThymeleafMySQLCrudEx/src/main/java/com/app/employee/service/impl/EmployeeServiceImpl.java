package com.app.employee.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.employee.entity.Employee;
import com.app.employee.exception.EmployeeNotFoundException;
import com.app.employee.repo.EmployeeRepository;
import com.app.employee.service.IEmployeeService;
import com.app.employee.util.EmployeeUtil;

@Service
public class EmployeeServiceImpl implements IEmployeeService 
{
	@Autowired
	private EmployeeRepository repo;
	
	//@Autowired
	//private EmployeeUtil util;
	
	public Integer saveEmployee(Employee e) {
		//util.commonCal(e);
		EmployeeUtil.commonCal(e);
		e = repo.save(e);
		return e.getEmpId();
	}

	public void updateEmployee(Employee e) {
		//util.commonCal(e);
		EmployeeUtil.commonCal(e);
		repo.save(e);
	}
	
	//3nd change for Exception - declare throws
	public void deleteEmployee(Integer id) throws EmployeeNotFoundException
	{
		//repo.deleteById(id);
		
		//Regular Approach
		/*Optional<Employee> opt = repo.findById(id);
		if(opt.isPresent()) {
			repo.delete(opt.get());
		}else{
			throw new EmployeeNotFoundException("Employee '"+id+"' Not Found");
		}*/
		
		//Standard Approach
		/*
		 * repo.delete( repo.findById(id) .orElseThrow(() -> new
		 * EmployeeNotFoundException("Employee '"+id+"' Not Found")) );
		 */
		
		//DRY - principle Don't Repeat Your self, Avoiding duplicate codes
		repo.delete(this.getOneEmployee(id));
	}

	public Employee getOneEmployee(Integer id) throws EmployeeNotFoundException
	{
		/*
		 * Optional<Employee> opt = repo.findById(id); return opt.get();
		 */
		return repo.findById(id)
		.orElseThrow(() -> new EmployeeNotFoundException("Employee '"+id+"' Not Found"));
	}

	@Override
	public List<Employee> getAllEmployee() {
		List<Employee> list = repo.findAll();
		return list;
	}
	
	@Override
	public Page<Employee> getAllEmployee(Pageable pageable) {
		Page<Employee> pages = repo.findAll(pageable);
		return pages;
	}
}
