package com.app.employee.exception;

//public class EmployeeNotFoundException extends RuntimeException
//Recommended approach, 1st Change
public class EmployeeNotFoundException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EmployeeNotFoundException() {
		super();
	}
	public EmployeeNotFoundException(String message) {
		super(message);
	}
	

	
			
}	

