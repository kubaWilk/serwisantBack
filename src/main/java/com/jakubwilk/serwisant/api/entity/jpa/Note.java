package com.jakubwilk.serwisant.api.entity.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @OneToOne
    @JoinColumn(name="author_id", unique = false)
    private User author;

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

    @CreatedDate
    @Column(name="created_at")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name="modified_at")
    private LocalDateTime lastModifiedDate;

    public enum Visibility{
        PUBLIC,
        PRIVATE
    }
}
