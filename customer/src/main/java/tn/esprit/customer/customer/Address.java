package tn.esprit.customer.customer;


import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Address {

    private String street;
    private String houseNumber;
    private String zipCode;
    private String city;
    private String country;
}
