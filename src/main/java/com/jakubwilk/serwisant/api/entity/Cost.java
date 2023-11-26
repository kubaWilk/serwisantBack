package com.jakubwilk.serwisant.api.entity;

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
    private float price;

    @Enumerated(EnumType.ORDINAL)
    private CostType costType;

    @ManyToOne(cascade = {
            CascadeType.MERGE,
            CascadeType.DETACH,
            CascadeType.PERSIST,
            CascadeType.REFRESH})
    @JoinColumn(name="repair_id", nullable = false)
    @ToString.Exclude
    @JsonIgnore
    private Repair repair;

    public enum CostType{
        PART,
        SERVICE
    }
}
