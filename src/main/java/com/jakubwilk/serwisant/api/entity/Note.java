package com.jakubwilk.serwisant.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="note")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="note_id")
    private int id;

    @Enumerated(EnumType.ORDINAL)
    @Column(name="visibility")
    private Visibility visibility;

    @Column(name="message")
    private String message;

    @ManyToOne(cascade = {
            CascadeType.MERGE,
            CascadeType.DETACH,
            CascadeType.PERSIST,
            CascadeType.REFRESH})
    @JoinColumn(name="repair_id")
    private Repair repair;

    public enum Visibility{
        PUBLIC,
        PRIVATE
    }
}
