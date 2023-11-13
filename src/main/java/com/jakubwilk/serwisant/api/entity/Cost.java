package com.jakubwilk.serwisant.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="cost")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
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
    private Repair repair;

    public enum CostType{
        PART,
        SERVICE
    }
}
