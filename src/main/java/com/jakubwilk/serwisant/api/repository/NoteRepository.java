package com.jakubwilk.serwisant.api.repository;

import com.jakubwilk.serwisant.api.entity.jpa.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Integer> {
    @Query("SELECT n FROM Note n where n.repair.id = :id")
    List<Note> findAllNotesByRepairId(int id);
}
