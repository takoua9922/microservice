package tn.esprit.customer.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CustomerRequest(
        String id,
        @NotBlank(message = "Le prénom est obligatoire") String firstname,
        @NotBlank(message = "Le nom est obligatoire") String lastname,
        @NotBlank(message = "L'email est obligatoire") @Email(message = "Email invalide") String email,
        String phone,
        Address address
) {}