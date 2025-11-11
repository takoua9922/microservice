package tn.esprit.customer.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.customer.exception.CustomerNotFoundException;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

  private final CustomerRepository repository;
  private final CustomerMapper mapper;

  public String createCustomer(CustomerRequest request) {
    var customer = mapper.toCustomer(request);
    if (customer.getId() == null || customer.getId().isBlank()) {
      customer.setId(java.util.UUID.randomUUID().toString()); // Génère UUID si pas fourni
    }
    return repository.save(customer).getId();
  }

  public void updateCustomer(CustomerRequest request) {
    var customer = repository.findById(request.id())
            .orElseThrow(() -> new CustomerNotFoundException(
                    "Customer avec ID " + request.id() + " n'existe pas"));

    if (request.firstname() != null && !request.firstname().isBlank()) {
      customer.setFirstName(request.firstname());
    }
    if (request.lastname() != null && !request.lastname().isBlank()) {
      customer.setLastName(request.lastname());
    }
    if (request.email() != null && !request.email().isBlank()) {
      customer.setEmail(request.email());
    }
    if (request.address() != null) {
      customer.setAddress(request.address());
    }

    repository.save(customer);
  }

  public List<CustomerResponse> findAllCustomers() {
    return repository.findAll().stream()
            .map(mapper::fromCustomer)
            .collect(Collectors.toList());
  }

  public CustomerResponse findById(String id) {
    return repository.findById(id)
            .map(mapper::fromCustomer)
            .orElseThrow(() -> new CustomerNotFoundException("Customer avec ID " + id + " introuvable"));
  }

  public boolean existsById(String id) {
    return repository.existsById(id);
  }

  public void deleteCustomer(String id) {
    repository.deleteById(id);
  }
}