package com.jakubwilk.serwisant.api.entity.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Table(name="repair")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @JoinColumn(name="issuer_user_id")
//    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private User issuer;

    @ManyToOne(
            cascade = {
            CascadeType.MERGE,
            CascadeType.DETACH,
            CascadeType.PERSIST,
            CascadeType.REFRESH},
            fetch = FetchType.EAGER)
    @JoinColumn(name="device_id")
//    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private Device device;

    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "repair",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<Note> notes;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    private List<Cost> costs;

    @Enumerated(EnumType.STRING)
    private RepairStatus repairStatus;

    @Column(name="description")
    private String description;

    @Column(name="estimated_cost")
    private double estimatedCost;

    public enum RepairStatus {
        OPEN,
        WAITING_FOR_CUSTOMER,
        WAITING_FOR_SUPLIER,
        CANCELED,
        CLOSED
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

    public void deleteCost(Cost theCost){
        if(this.costs == null) {
            throw new RuntimeException("Repair already has no costs!");
        }else{
            this.costs.remove(theCost);
        }
    }
}
