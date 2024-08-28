package com.example.CrudWithJoins.controller;

import com.example.CrudWithJoins.entity.Address;
import com.example.CrudWithJoins.entity.Employee;
import com.example.CrudWithJoins.response.StatusResponse;
import com.example.CrudWithJoins.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    // Create a new address
    @PostMapping
    public ResponseEntity<StatusResponse<Address>> createAddress(@RequestBody Address address) {
        Address createdAddress = addressService.createAddress(address);
        StatusResponse<Address> response = new StatusResponse<>(HttpStatus.CREATED, "Address created successfully", createdAddress);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Get all addresses
    @GetMapping
    public ResponseEntity<StatusResponse<List<Address>>> getAllAddresses() {
        List<Address> addresses = addressService.getAllAddresses();
        StatusResponse<List<Address>> response = new StatusResponse<>(HttpStatus.OK, "Addresses retrieved successfully", addresses);
        return ResponseEntity.ok(response);
    }

    // Get an address by ID
    @GetMapping("/{id}")
    public ResponseEntity<StatusResponse<Address>> getAddressById(@PathVariable Long id) {
        Optional<Address> address = addressService.getAddressById(id);
        if (address.isPresent()) {
            StatusResponse<Address> response = new StatusResponse<>(HttpStatus.OK, "Address retrieved successfully", address.get());
            return ResponseEntity.ok(response);
        } else {
            StatusResponse<Address> response = new StatusResponse<>(HttpStatus.NOT_FOUND, "Address not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // Update an address
    @PutMapping("/{id}")
    public ResponseEntity<StatusResponse<Address>> updateAddress(@PathVariable Long id, @RequestBody Address address) {
        Optional<Address> updatedAddress = addressService.updateAddress(id, address);
        if (updatedAddress.isPresent()) {
            StatusResponse<Address> response = new StatusResponse<>(HttpStatus.OK, "Address updated successfully", updatedAddress.get());
            return ResponseEntity.ok(response);
        } else {
            StatusResponse<Address> response = new StatusResponse<>(HttpStatus.NOT_FOUND, "Address not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // Delete an address
    @DeleteMapping("/{id}")
    public ResponseEntity<StatusResponse<Void>> deleteAddress(@PathVariable Long id) {
        ResponseEntity<Void> deleteResponse = addressService.deleteAddress(id);
        if (deleteResponse.getStatusCode() == HttpStatus.NO_CONTENT) {
            StatusResponse<Void> response = new StatusResponse<>(HttpStatus.NO_CONTENT, "Address deleted successfully");
            return ResponseEntity.noContent().build();
        } else {
            StatusResponse<Void> response = new StatusResponse<>(HttpStatus.NOT_FOUND, "Address not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // Add an employee to an address
    @PostMapping("/{addressId}/employees")
    public ResponseEntity<StatusResponse<Address>> addEmployeeToAddress(@PathVariable Long addressId, @RequestBody Employee employee) {
        Optional<Address> updatedAddress = addressService.addEmployeeToAddress(addressId, employee);
        if (updatedAddress.isPresent()) {
            StatusResponse<Address> response = new StatusResponse<>(HttpStatus.OK, "Employee added to address successfully", updatedAddress.get());
            return ResponseEntity.ok(response);
        } else {
            StatusResponse<Address> response = new StatusResponse<>(HttpStatus.NOT_FOUND, "Address not found or Employee not valid");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    //Sorting
    @GetMapping("/sort/{field}")
    public List<Address> sort(@PathVariable String field) {
        return  addressService.sortAddress(field);
    }

    //Pagination
    @GetMapping("/pagination/{page}/{pageSize}")
    public Page<Address> paginationAddress(@PathVariable int page, @PathVariable int pageSize) {
        return addressService.getAddressWithPagination(page, pageSize);
    }

    //Searching
    @GetMapping("/search")
    public ResponseEntity<StatusResponse<Page<Address>>> searchAddresses(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String addressType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "addressId") String sortBy) {

        Page<Address> addresses = addressService.searchAddresses(city, addressType, page, pageSize, sortBy);
        StatusResponse<Page<Address>> response = new StatusResponse<>(HttpStatus.OK, "Addresses retrieved successfully", addresses);
        return ResponseEntity.ok(response);
    }

}
