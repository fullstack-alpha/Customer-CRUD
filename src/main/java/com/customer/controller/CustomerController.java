package com.customer.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.customer.domain.Customer;
import com.customer.service.CustomerService;

@RestController
@RequestMapping("/customer")
public class CustomerController {

	Logger logger = LoggerFactory.getLogger(CustomerController.class);

	private CustomerService customerService;

	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}

	@PutMapping("/")
	public void insertCustomerIntoStore(@RequestBody Customer customer) {
		try {
			logger.info("insert request received {}", customer);
			customerService.insertCustomer(customer);
			logger.info("successfully inserted {}", customer);
		} catch (Exception e) {
			logger.info("An error occured while inserting  request {} --- {}", customer, e.getMessage());
		}
	}

	@PostMapping("/{customerNumber}")
	public void updateCustomerInStore(@RequestBody Customer updateRequest, @PathVariable String customerNumber) {
		try {
			logger.info("update request received {} with customer number {}", updateRequest, customerNumber);
			customerService.updateCustomer(updateRequest, customerNumber);
			logger.info("successfully updated {} with customer number {}", updateRequest, customerNumber);
		} catch (Exception e) {
			logger.info("An error occured while updating  customerNumber {} --- {}", customerNumber, e.getMessage());
		}
	}

	@DeleteMapping("/{customerNumber}")
	public void deleteCustomerFromStore(@PathVariable String customerNumber) {
		try {
			logger.info("delete request received for customer number {}", customerNumber);
			customerService.deleteCustomer(customerNumber);
			logger.info("successfully deleted customer number {}", customerNumber);
		} catch (Exception e) {
			logger.info("An error occured while deleting  customerNumber {} --- {}", customerNumber, e.getMessage());
		}
	}

	@GetMapping("/{customerNumber}")
	public ResponseEntity<Object> getCustomerFromStore(@PathVariable String customerNumber) {
		logger.info("get request received for customer number {}", customerNumber);
		Customer customer = customerService.getCustomer(customerNumber);

		if (customer != null) {
			if ("false".equals(customer.getCustomerNumber())){
				logger.info("status found to be false for customer number {}", customerNumber);
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
			else {
				logger.info("returning customer {}  with customer number {}", customer, customerNumber);
				return ResponseEntity.ok().body(customer);
			}
		} else {
			logger.info("no customer found with customer number {}", customer, customerNumber);
			return ResponseEntity.badRequest().body("No Customer Found");
		}
	}

	@GetMapping("/")
	public List<Customer> getAllCustomersFromStore() {
		logger.info("get all request received for customer number");
		return customerService.getAllCustomers();
	}

}
