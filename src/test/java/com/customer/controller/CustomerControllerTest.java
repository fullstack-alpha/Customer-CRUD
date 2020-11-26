package com.customer.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.customer.controller.CustomerController;
import com.customer.domain.Customer;
import com.customer.service.CustomerService;
import com.mongodb.client.result.DeleteResult;

@RunWith(SpringRunner.class)
public class CustomerControllerTest {

	private MockMvc mockMvc;

	@Mock
	private CustomerService customerService;
	
	@Mock
	private DeleteResult deleteResult;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(new CustomerController(customerService)).build();
	}

	private final String insertCustomerBody = "{\r\n\t\"customerNumber\":\"123\",\r\n\t\"firstName\":\"Ajith\",\r\n\t\"lastName\":\"Alias\"\r\n}";
	private final String updateCustomerBody = "{\r\n\t\"firstName\":\"Ajith\",\r\n\t\"lastName\":\"T Alias\"\r\n}";

	@Test
	public void testInsertCustomerIntoStore() throws Exception {
		when(customerService.insertCustomer(Mockito.any())).thenReturn(new Customer());
		this.mockMvc.perform(put("/customer/").content(insertCustomerBody).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk());

	}

	@Test
	public void testUpdateCustomerInStore() throws Exception {
		when(customerService.updateCustomer(Mockito.any(), Mockito.anyString())).thenReturn(new Customer());
		this.mockMvc.perform(post("/customer/1234").content(updateCustomerBody).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk());
	}

	@Test
	public void deleteCustomerFromStore() throws Exception {
		when(customerService.deleteCustomer(Mockito.anyString())).thenReturn(deleteResult);
		this.mockMvc.perform(delete("/customer/1234")).andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	public void testGetCustomerFromStore() throws Exception {		
		when(customerService.getCustomer(Mockito.any())).thenReturn(new Customer());
		this.mockMvc.perform(get("/customer/1234")).andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	public void testGetAllCustomersFromStore() throws Exception {
		List<Customer> customerList = Lists.newArrayList();
		customerList.add(new Customer());
		when(customerService.getAllCustomers()).thenReturn(customerList);
		this.mockMvc.perform(get("/customer/")).andDo(print()).andExpect(status().isOk());
	}

}
