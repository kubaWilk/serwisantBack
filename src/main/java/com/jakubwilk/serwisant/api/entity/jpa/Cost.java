package com.jakubwilk.serwisant.api.entity.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="cost")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Cost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="price")
    private double price;

    @Column(name="name")
    private String name;

    @Enumerated(EnumType.ORDINAL)
    private CostType costType;

    @ManyToOne(cascade = {
            CascadeType.MERGE,
            CascadeType.DETACH,
            CascadeType.PERSIST,
            CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    @JoinColumn(name="repair_id")
    @ToString.Exclude
    @JsonIgnore
    private Repair repair;

    public enum CostType{
        PART,
        SERVICE
    }
}
