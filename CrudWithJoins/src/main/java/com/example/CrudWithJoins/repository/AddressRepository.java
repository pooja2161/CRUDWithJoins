package com.example.CrudWithJoins.repository;

import com.example.CrudWithJoins.entity.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Page<Address> findByCity(String city, Pageable pageable);
    Page<Address> findByAddressType(String addressType, Pageable pageable);
    Page<Address> findByCityAndAddressType(String city, String addressType, Pageable pageable);
}