package com.app.employee.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.employee.entity.Employee;
import com.app.employee.exception.EmployeeNotFoundException;
import com.app.employee.service.IEmployeeService;
import com.app.employee.util.EmployeeUtil;

@Controller
@RequestMapping("/employee")
public class EmployeeController 
{
	@Autowired
	private IEmployeeService service;
	
	/**
	 * 1. SHOW THE REGISTER PAGE
	 * This method is used to display Register Page
	 * when end-user enters /register with GET Type
	 */
	@GetMapping("/register")
	public String showRegisterPage(Model model)
	{	
		EmployeeUtil.createDeptList(model);
		return "EmployeeRegister";
	}
	
	
	/**
	 * 2. ON CLICK FORM SUBMIT, READ DATA (@MODELATTRIBUTE)
	 * This method is used to read Form data as Model Attribute
	 * It will make call to service method by passing same form object
	 * Service method returns PK(ID). 
	 * Controller returns String message back to UI using Model
	 * @param employee
	 * @param model
	 * @return
	 */
	@PostMapping("/save")
	public String showRegisterPage(
			//Form to Controller
			@ModelAttribute Employee employee,
			//Controller to Form
			Model model)
	{
		Integer id = service.saveEmployee(employee);
		String message = new StringBuffer().append("EMPLOYEE ID = '")
				.append(id).append("' CREATED ").toString();
		model.addAttribute("message", message);
		//for dynamic dept list drop down
		EmployeeUtil.createDeptList(model);
		return "EmployeeRegister";
	}
	
	
	/**
	 * 3. Display all rows as a table
	 * This method is executed for request URL /all + GET.
	 * It will fetch data from Service as List<T>
	 * Send this data to UI(View) using Model(I)
	 * In UI use th:each="tempVariable:${collectionName}" to read data 
	 * and print as HTML Table.
	 * @param model
	 * @return
	 */
	/*@GetMapping("/all")
	public String showData(
			Model model,
			@RequestParam(value = "message",required = false) String message) 
	{
		List<Employee> list = service.getAllEmployee();
		model.addAttribute("list", list);
		model.addAttribute("message", message);
		return "EmployeeData";
	}*/
	//Using Pagination concept
	@GetMapping("/all")
	public String showData(
			Model model,
			@PageableDefault(page = 0,size = 3) Pageable pageable,
			@RequestParam(value = "message",required = false) String message, HttpSession session) 
	{
		//List<Employee> list = service.getAllEmployee();
		Page<Employee> page = service.getAllEmployee(pageable);
		model.addAttribute("list", page.getContent());
		model.addAttribute("page", page);
		model.addAttribute("message", message);
		session.setAttribute("Hello", "Hello");
		session.setAttribute("Employees", page);
		return "EmployeeData";
	}
	
	/**
	 * 4. Delete based on id
	 * On Click Delete HyperLink, a Request is made by browser looks like 
	 * /employee/delete?id=someVal.
	 * Read data using Annotation @RequestParam and call service to delete from db.
	 * 
	 * Just redirect to /all with one message (RedirectAttribute)
	 * that will display all rows with message. 
	 */
	@GetMapping("/delete")
	public String deleteData(
			@RequestParam("id") Integer empId,
			RedirectAttributes attributes)
	{
		String msg = null;
		//4th change for Exception should add try catch block
		try {
			service.deleteEmployee(empId);
			msg = "Employee '"+empId+"' Deleted";
			
		} catch (EmployeeNotFoundException e) {
			e.printStackTrace();
			msg = e.getMessage();
		}
		attributes.addAttribute("message", msg);
		return "redirect:all";
	}
	
	
	/**
	 * 5. On Click Edit Link(HyperLink) Show data in Edit Form.
	 * When end user clicks on EDIT Link, internal request looks like /edit?empId=10
	 * Read DB Row using service call, that may return employee object else throw exception
	 * (if not found).
	 * If object is present use Model to send that object to Form(UI).
	 * Else redirect to /all with ErrorMessage(Redirect Attributes).
	 * @param empId
	 * @param model
	 * @param attributes
	 * @return
	 */
	@GetMapping("/edit")
	public String showEdit(
			@RequestParam("id") Integer empId,
			Model model,
			RedirectAttributes attributes
			)
	{
		String page = null;
		try {
			Employee employee = service.getOneEmployee(empId);
			model.addAttribute("employee", employee);
			//for dynamic dept list drop down
			EmployeeUtil.createDeptList(model);
			page = "EmployeeEdit";
			
		} catch (EmployeeNotFoundException e) {
					e.printStackTrace();
					attributes.addAttribute("message", e.getMessage());
					page = "redirect:all";
		}
		return page;
	}
	
	//6. UPDATE FORM DATA AND SUBMIT
	@PostMapping("/update")
	public String updateData(
			@ModelAttribute Employee employee,
			RedirectAttributes attributes
			) 
	{
		service.updateEmployee(employee);
		attributes.addAttribute("message", "Employee '"+employee.getEmpId()+"' Updated");
		return "redirect:all";
	}
	
	@GetMapping("/getSessionAttribute")
	@ResponseBody
	public Page<Employee> getSessionAttribute(HttpSession session) {
	    //String helloAttribute = (String) session.getAttribute("Hello");
		Page<Employee> employees = (Page<Employee>) session.getAttribute("Employees");
		//return "Session Attribute 'Hello': " + helloAttribute;
	    return employees;
	}

}

