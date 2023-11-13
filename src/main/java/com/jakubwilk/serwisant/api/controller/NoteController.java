package com.jakubwilk.serwisant.api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.jakubwilk.serwisant.api.entity.Note;
import com.jakubwilk.serwisant.api.service.NoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/note")
public class NoteController {
    private NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable("id") int id){
        Note found = noteService.findNoteById(id);

        return ResponseEntity.ok(found);
    }

    @GetMapping("/")
    public ResponseEntity<List<Note>> getAllNotes(){
        List<Note> result = noteService.findAllNotes();

        return ResponseEntity.ok(result);
    }

    @PostMapping("/")
    public ResponseEntity<Note> saveNote(@RequestBody JsonNode toSave){
        Note saved = noteService.saveNote(toSave);

        return new ResponseEntity<Note>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Note> updateNote(@RequestBody JsonNode toUpdate){
        Note updated = noteService.updateNote(toUpdate);
        return new ResponseEntity<Note>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Note> deleteNote(@PathVariable("id") int id){
        noteService.deleteNoteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
