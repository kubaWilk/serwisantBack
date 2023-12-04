package com.jakubwilk.serwisant.api.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

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

    @OneToMany(mappedBy = "repair",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    private List<Note> notes;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
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
