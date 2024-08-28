package com.example.CrudWithJoins.service;

import com.example.CrudWithJoins.entity.Address;
import com.example.CrudWithJoins.entity.Employee;
import com.example.CrudWithJoins.repository.AddressRepository;
import com.example.CrudWithJoins.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    public Address createAddress(Address address) {
        return addressRepository.save(address);
    }

    public Optional<Address> getAddressById(Long id) {
        return addressRepository.findById(id);
    }

    public Optional<Address> updateAddress(Long id, Address address) {
        if (addressRepository.existsById(id)) {
            address.setAddressId(id);
            Address updatedAddress = addressRepository.save(address);
            return Optional.of(updatedAddress);
        } else {
            return Optional.empty();
        }
    }

    public ResponseEntity<Void> deleteAddress(Long id) {
        if (addressRepository.existsById(id)) {
            addressRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public Optional<Address> addEmployeeToAddress(Long addressId, Employee employee) {
        Optional<Address> addressOpt = addressRepository.findById(addressId);
        if (addressOpt.isPresent()) {
            Address address = addressOpt.get();
            employee.setAddress(address);
            employeeRepository.save(employee);
            address.getEmployees().add(employee);
            addressRepository.save(address);
            return Optional.of(address);
        }
        return Optional.empty();
    }

    //sorting
    public List<Address> sortAddress(String field) {
        return addressRepository.findAll(Sort.by(Sort.Direction.ASC, field));
    }

    //pagination
    public  Page<Address> getAddressWithPagination(int page, int pageSize) {
        return addressRepository.findAll(PageRequest.of(page, pageSize));
    }

    //searching
    public Page<Address> searchAddresses(String city, String addressType, int page, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, sortBy));
        if (city != null && addressType != null) {
            return addressRepository.findByCityAndAddressType(city, addressType, pageable);
        } else if (city != null) {
            return addressRepository.findByCity(city, pageable);
        } else if (addressType != null) {
            return addressRepository.findByAddressType(addressType, pageable);
        } else {
            return addressRepository.findAll(pageable);
        }
    }
}


