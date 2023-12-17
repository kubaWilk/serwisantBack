package com.jakubwilk.serwisant.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="device")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="manufacturer")
    private String manufacturer;

    @Column(name="model")
    private String model;

    @Column(name="serial_number")
    private String serialNumber;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(
            mappedBy = "device",
            fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.ALL
            })
    private List<Repair> repairs;

    public void removeRepair(Repair repair){
        if(repairs == null || repairs.isEmpty()) return;

        repairs.remove(repair);
    }
}
