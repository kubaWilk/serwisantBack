package com.jakubwilk.serwisant.api.entity.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jakubwilk.serwisant.api.entity.Role;
import com.jakubwilk.serwisant.api.utils.AuthoritySerializer;
import com.jakubwilk.serwisant.api.utils.AuthoritySetDeserializer;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name="users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    @Column(name="username", unique = true)
    private String username;

    @Column(name="password")
    @ToString.Exclude
    @JsonIgnore
    private String password;

    @Column(name="active")
    private boolean isActive;

    @Column(name="email", unique = true)
    private String email;

    @OneToOne(fetch = FetchType.EAGER ,cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, name = "user_detail_id")
    private UserInfo userInfo;

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    private Set<Authority> roles;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private PasswordResetToken resetToken;

    @OneToMany(mappedBy = "issuer",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<Repair> repairs;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Note> notes;

    @CreatedDate
    @Column(name="created_at")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name="modified_at")
    private LocalDateTime lastModifiedDate;

    public void addRole(Role role){
        if(this.roles == null){
            roles = new HashSet<>();
        }

        boolean hasRole = roles.stream().anyMatch(authority ->
                authority.getAuthority() == role);
        if(!hasRole) roles.add(new Authority(this, username, role));
    }

    public void removeRole(Role theRole){
        if(this.roles == null) return;

        this.roles.removeIf(role -> role.getAuthority() == theRole);
    }

    public void removeRepair(Repair repair){
        if(repairs == null) return;
        this.repairs.remove(repair);
    }

    public void removeNote(Note theNote){
        if(notes == null) return;
        this.notes.remove(theNote);
    }

    public List<String> getRoles() {
        List<String> authorities = new ArrayList<>();
        for(Authority role : roles){
            authorities.add(role.getAuthority().toString());
        }
        return authorities;
    }

    @JsonIgnore
    public Set<Authority> getAuthoritySet(){
        return roles;
    }

    public void setAuthoritySet(Set<Authority> authorities){
        this.roles = authorities;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        for(Authority role: roles){
            authorities.add(new SimpleGrantedAuthority(role.getAuthority().toString()));
        }

        return authorities;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return isActive;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return isActive;
    }

    public static boolean isOnlyCustomer(JwtAuthenticationToken authentication){
        Collection<? extends GrantedAuthority> roles = authentication.getAuthorities();

        return roles.size() == 1 &&
                roles.stream().anyMatch(role -> "SCOPE_ROLE_CUSTOMER".equals(role.getAuthority()));
    }
}