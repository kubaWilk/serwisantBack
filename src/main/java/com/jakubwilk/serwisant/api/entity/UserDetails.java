package com.jakubwilk.serwisant.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="user_details")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_details_id")
    private int id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="street")
    private String street;

    @Column(name="post_code")
    private String postCode;

    @Column(name="city")
    private String city;

    @ToString.Exclude
    @OneToOne(mappedBy = "userDetails",
                fetch = FetchType.LAZY,
                cascade = CascadeType.ALL)
    private User user;
}
