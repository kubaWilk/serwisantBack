package com.jakubwilk.serwisant.api.dao;

import com.jakubwilk.serwisant.api.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Integer> {
}
