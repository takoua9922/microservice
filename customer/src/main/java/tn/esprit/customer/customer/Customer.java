package tn.esprit.customer.customer;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customers") // optionnel mais propre
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  private String email;
  private String phone;

  @Embedded
  private Address address;
}