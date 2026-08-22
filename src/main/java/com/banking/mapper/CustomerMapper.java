package com.banking.mapper;

import com.banking.dto.request.CustomerRequest;
import com.banking.dto.response.CustomerResponse;
import com.banking.entity.Customer;
import com.banking.enums.CustomerStatus;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toEntity(CustomerRequest request){
        return Customer.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getEmail())
                .customerStatus(CustomerStatus.ACTIVE)
                .build();
    }

    public CustomerResponse toResponse(Customer customer){

        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .customerStatus(customer.getCustomerStatus())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }
}
