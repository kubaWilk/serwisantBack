package com.jakubwilk.serwisant.api.entity.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name="repair")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Repair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(
            cascade = {
            CascadeType.MERGE,
            CascadeType.DETACH,
            CascadeType.PERSIST,
            CascadeType.REFRESH},
            fetch = FetchType.EAGER
    )
    @JoinColumn(name="issuer_user_id", nullable = false)
    private User issuer;

    @ManyToOne(
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.DETACH,
                    CascadeType.PERSIST,
                    CascadeType.REFRESH},
            fetch = FetchType.EAGER)
    @JoinColumn(name="device_id", nullable = false)
    private Device device;

    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "repair",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<Note> notes;

    @OneToMany(mappedBy = "repair",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    @ToString.Exclude
    @JsonIgnore
    private List<Cost> costs;

    @Enumerated(EnumType.STRING)
    private RepairStatus repairStatus;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    @Column(name="description")
    private String description;

    @Column(name="estimated_cost")
    private double estimatedCost;

    @CreatedDate
    @Column(name="created_at")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name="modified_at")
    private LocalDateTime lastModifiedDate;

    @Column(name="cost_accepted")
    private boolean isCostAccepted;

    @PreRemove
    private void preRemove(){
        if(issuer != null) {
            issuer.removeRepair(this);
        }
    }

    public void addNote(Note theNote){
        if(this.notes == null){
            this.notes = new ArrayList<>();
        }

        this.notes.add(theNote);
    }

    public void addCost(Cost theCost){
        if(this.costs == null) {
            this.costs = new ArrayList<>();
        }

        this.costs.add(theCost);
    }

    public void removeCost(Cost theCost){
        if(this.costs == null) {
            throw new RuntimeException("Repair already has no costs!");
        }else{
            this.costs.remove(theCost);
        }
    }
}
