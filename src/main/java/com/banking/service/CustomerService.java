package com.banking.service;

import com.banking.dto.request.CustomerRequest;
import com.banking.dto.response.CustomerResponse;

import java.util.List;

public interface CustomerService {

    CustomerResponse createCustomer(CustomerRequest customerRequest);

    CustomerResponse getCustomerByCustomerId(Long id);

    List<CustomerResponse> getAllCustomers();

    CustomerResponse updateCustomer(Long customerId,CustomerRequest customerRequest);

    void deactivateCustomer(Long customerId);


}


