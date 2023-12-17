package com.jakubwilk.serwisant.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    @Column(name="username")
    private String username;

    @Column(name="password")
    private String password;

    @Column(name="active")
    private boolean isActive;

    @Column(name="email")
    private String email;

    @OneToOne(fetch = FetchType.EAGER ,cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, name = "user_detail_id")
    private UserDetails userDetails;

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Authority> roles;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private PasswordResetToken resetToken;

    @OneToMany(mappedBy = "issuer",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    @JsonIgnore
    @ToString.Exclude
    private List<Repair> repairs;

    public void setRoles(Role role){
        if(this.roles == null){
            roles = new HashSet<>();
        }

        roles.add(new Authority(this, username, role));
    }

    public void removeRepair(Repair repair){
        if(repairs == null) return;
        this.repairs.remove(repair);
    }
}
