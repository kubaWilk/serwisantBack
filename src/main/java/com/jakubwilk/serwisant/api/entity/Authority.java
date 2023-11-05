package com.jakubwilk.serwisant.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashMap;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="authorities")
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="authority_id")
    private int id;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="user_id")
    private User user;

    @Column(name="username")
    private String username;

    @Column(name="authority")
    private String authority;

    public static String CUSTOMER = "ROLE_CUSTOMER";
    public static String EMPLOYEE = "ROLE_EMPLOYEE";
    public static String ADMIN = "ROLE_ADMIN";

    public Authority(User user, String username, String authority) {
        this.user = user;
        this.username = username;
        this.authority = authority;
    }
}
