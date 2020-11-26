package com.customer.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.customer.domain.Customer;
import com.mongodb.client.result.DeleteResult;

@Service
public class CustomerService {

	Logger logger = LoggerFactory.getLogger(CustomerService.class);

	private final MongoTemplate mongoTemplate;

	public CustomerService(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Cacheable(value = "customers", key = "#customerNumber")
	public Customer getCustomer(@PathVariable String customerNumber) {
		try {
			logger.info("start searching in for customer with customerNumber {}", customerNumber);
			Customer customer = mongoTemplate.findById(customerNumber, Customer.class);
			logger.info("found customer {} with customerNumber {}", customer, customerNumber);
			return customer;
		} catch (Exception e) {
			logger.info("An error occured while searching in for customer with customerNumber {} ----", customerNumber,
					e.getMessage());
			throw e;
		}

	}

	public List<Customer> getAllCustomers() {
		try {
			logger.info("start searching  for  all customer in db");
			List<Customer> customerList = mongoTemplate.findAll(Customer.class);
			if (!customerList.isEmpty()) {
				logger.info("found customers in db {} ", customerList);
				return customerList;
			} else {
				logger.info("no customer found in db");
				return null;
			}
		} catch (Exception e) {
			logger.info("An error occured while searching in for customers --- {}", e.getMessage());
			throw e;
		}
	}

	@CachePut(value = "customers", key = "#customer.customerNumber")
	public Customer insertCustomer(Customer customer) {
		try {
			if (customer.getStatus() == null) {
				customer.setStatus("true");
			}
			logger.info("start inserting customer {} in db", customer);
			return mongoTemplate.insert(customer);
		} catch (Exception e) {
			logger.info("An error occured while inserting customer {}  --- {}", customer, e.getMessage());
			throw e;
		}
	}

	@CachePut(value = "customers", key = "#customerNumber")
	public Customer updateCustomer(Customer updateRequest, String customerNumber) {
		try {
			Customer customerFetched = getCustomer(customerNumber);
			if (customerFetched != null) {
				Optional.ofNullable(updateRequest.getFirstName()).ifPresent(customerFetched::setFirstName);
				Optional.ofNullable(updateRequest.getLastName()).ifPresent(customerFetched::setLastName);
				logger.info("start updating customer {} in db for req {} ", customerNumber, updateRequest);
				return mongoTemplate.save(customerFetched);
			}
			logger.info("no customer found in db with customer Number {} ", customerNumber);
			return null;
		} catch (Exception e) {
			logger.info("An error occured while updating customer {}  --- {}", customerNumber, e.getMessage());
			throw e;
		}
	}

	@CacheEvict(value = "customers", allEntries = true)
	public DeleteResult deleteCustomer(@PathVariable String customerNumber) {
		try {
			Customer customer = getCustomer(customerNumber);
			if (customer != null) {
				logger.info("start deleting customer {} ", customerNumber);
				return mongoTemplate.remove(customer);
			}
			logger.info("no customer found in db with customer Number {} ", customerNumber);
			return null;
		} catch (Exception e) {
			logger.info("An error occured while deleting customer {}  --- {}", customerNumber, e.getMessage());
			throw e;
		}
	}

}
