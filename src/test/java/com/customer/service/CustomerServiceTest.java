package com.customer.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.customer.domain.Customer;
import com.mongodb.client.result.DeleteResult;

@RunWith(SpringRunner.class)
public class CustomerServiceTest {

	@Mock
	private MongoTemplate mongoTemplate;
	
	@Mock
	private DeleteResult deleteResult;

	@InjectMocks
	private CustomerService customerService;

	@Test
	public void testGetCustomer() {
		when(mongoTemplate.findById(Mockito.anyString(), Mockito.any())).thenReturn(new Customer());
		Customer customer = customerService.getCustomer("1234");
		assertThat(customer).isNotNull();
	}

	@Test
	public void testGetAllCustomers() {
		List<Customer> customerList = Lists.newArrayList();
		customerList.add(new Customer());
		when(mongoTemplate.findAll(Customer.class)).thenReturn(customerList);
		List<Customer> customerListResponse= customerService.getAllCustomers();
		assertNotNull(customerListResponse);
	}
	
	@Test
	public void testInsertCustomer() {
		when(mongoTemplate.insert(Mockito.any(Customer.class))).thenReturn(new Customer());
		Customer customer=customerService.insertCustomer(new Customer());
		assertNotNull(customer);
		
	}
	
	@Test
	public void testUpdateCustomer() {
		when(mongoTemplate.findById(Mockito.anyString(), Mockito.any())).thenReturn(new Customer());
		when(mongoTemplate.insert(Mockito.any(Customer.class))).thenReturn(new Customer());
		Customer customer=customerService.insertCustomer(new Customer());
		assertNotNull(customer);
	}
	
	@Test
	public void testDeleteCustomer() {
		when(mongoTemplate.findById(Mockito.anyString(), Mockito.any())).thenReturn(new Customer());
		when(mongoTemplate.remove(Mockito.any(Customer.class))).thenReturn(deleteResult);
		DeleteResult deleteResult=customerService.deleteCustomer("1234");
		assertNotNull(deleteResult);
	}
}
