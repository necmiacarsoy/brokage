package com.inghubs.brokage.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.inghubs.brokage.repository.CustomerRepository;

@Service
public class BrokageUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    public BrokageUserDetailsService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return customerRepository.findByUsername(username) 
            .orElseThrow(() -> new UsernameNotFoundException("Customer not found"));
    }

}
