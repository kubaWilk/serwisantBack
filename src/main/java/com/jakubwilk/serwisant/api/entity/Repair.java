package com.jakubwilk.serwisant.api.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name="repair")
@Entity
public class Repair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = {
            CascadeType.MERGE,
            CascadeType.DETACH,
            CascadeType.PERSIST,
            CascadeType.REFRESH})
    private User issuer;

    @ManyToOne(cascade = {
            CascadeType.MERGE,
            CascadeType.DETACH,
            CascadeType.PERSIST,
            CascadeType.REFRESH})
    @JoinColumn(name="device_id", nullable = false)
    private Device device;

    @OneToMany(mappedBy = "repair", cascade = CascadeType.ALL)
    private List<Note> notes;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Cost> costs;

    @Enumerated(EnumType.STRING)
    private Status repairStatus;

    public enum Status{
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
}
