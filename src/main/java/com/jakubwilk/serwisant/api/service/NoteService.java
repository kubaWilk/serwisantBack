package com.jakubwilk.serwisant.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.jakubwilk.serwisant.api.entity.jpa.Note;

import java.util.List;

public interface NoteService {
    Note findNoteById(int id);

    List<Note> findAllNotes();

    List<Note> findAllNotes(int repairId);

    Note saveNote(JsonNode noteJsonNode);

    Note updateNote(JsonNode toUpdate);

    void deleteNoteById(int id);
}
