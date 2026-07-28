package com.example.ss13.employee;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

	@GetMapping
	public List<Employee> getEmployees() {
		return List.of(
				new Employee(1L, "Nguyen Van An", 1200.0),
				new Employee(2L, "Tran Thi Binh", 1500.0),
				new Employee(3L, "Le Minh Chau", 1800.0));
	}
}
