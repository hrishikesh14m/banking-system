package com.banking.service.serviceImpl;

import com.banking.dto.request.CustomerRequest;
import com.banking.dto.response.CustomerResponse;
import com.banking.entity.Customer;
import com.banking.enums.CustomerStatus;
import com.banking.exception.DuplicateResourceException;
import com.banking.exception.ResourceNotFoundException;
import com.banking.mapper.CustomerMapper;
import com.banking.repository.CustomerRepository;
import com.banking.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    @Transactional
    public CustomerResponse createCustomer(CustomerRequest customerRequest) {
        log.info("Creating customer with email={}", customerRequest.getEmail());

        if (customerRepository.existsByEmail(customerRequest.getEmail())) {
            throw new DuplicateResourceException(
                    "customer already exists with email " + customerRequest.getEmail()
            );
        }

        Customer customer = customerMapper.toEntity(customerRequest);

        Customer savedCustomer = customerRepository.save(customer);

        return customerMapper.toResponse(savedCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerByCustomerId(Long id) {

        log.info("fetching customer with customerId : {}", id);

        Customer customer = customerRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("customer not found with id : " + id)
        );

        return customerMapper.toResponse(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream().map(customerMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public CustomerResponse updateCustomer(Long customerId, CustomerRequest customerRequest) {

        Customer customer = customerRepository.findById(customerId).orElseThrow(
                () -> new ResourceNotFoundException("Customer not found with id: " + customerId)
        );

        customer.setName(customerRequest.getName());
        customer.setEmail(customerRequest.getEmail());
        customer.setPhone(customerRequest.getPhone());

        Customer savedCustomer = customerRepository.save(customer);

        return customerMapper.toResponse(savedCustomer);
    }

    @Override
    @Transactional
    public void deactivateCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(
                () -> new ResourceNotFoundException("Customer not found with id: " + customerId)
        );

        customer.setCustomerStatus(CustomerStatus.INACTIVE);

        customerRepository.save(customer);
    }
}
