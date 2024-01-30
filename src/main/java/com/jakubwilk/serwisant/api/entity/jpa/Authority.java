package com.jakubwilk.serwisant.api.entity.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jakubwilk.serwisant.api.entity.Role;
import jakarta.persistence.*;
import lombok.*;

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
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(name="username")
    private String username;

    @Column(name="authority")
    @Enumerated(EnumType.STRING)
    private Role authority;

    public Authority(User user, String username, Role authority) {
        this.user = user;
        this.username = username;
        this.authority = authority;
    }

    @Override
    public String toString() {
        return authority.toString();
    }
}
