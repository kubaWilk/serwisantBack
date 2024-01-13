package com.jakubwilk.serwisant.api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.jakubwilk.serwisant.api.entity.jpa.Note;
import com.jakubwilk.serwisant.api.service.NoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/note")
@ControllerAdvice
public class NoteController {
    private NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/{id}")
    @Secured("ROLE_CUSTOMER")
    public ResponseEntity<Note> getNoteById(@PathVariable("id") int id){
        Note found = noteService.findNoteById(id);

        return ResponseEntity.ok(found);
    }

    @GetMapping("/all")
    @Secured("ROLE_CUSTOMER")
    public ResponseEntity<List<Note>> getAllNotes(@RequestParam(name = "repairid") int repairId){
        List<Note> result = noteService.findAllNotes(repairId);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/")
    @Secured("ROLE_CUSTOMER")
    public ResponseEntity<List<Note>> getAllNotes(){
        List<Note> result = noteService.findAllNotes();

        return ResponseEntity.ok(result);
    }

    //    It consumes JSON in such format:
    //    {
    //        "visibility": PUBLIC or PRIVTE,
    //        "message": string,
    //        "repairId": int,
    //        "authorId": int
    //    }

    @PostMapping("/")
    @Secured("ROLE_CUSTOMER")
    public ResponseEntity<Note> saveNote(@RequestBody JsonNode toSave){
        Note saved = noteService.saveNote(toSave);

        return new ResponseEntity<Note>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/")
    @Secured("ROLE_CUSTOMER")
    public ResponseEntity<Note> updateNote(@RequestBody JsonNode toUpdate){
        Note updated = noteService.updateNote(toUpdate);
        return new ResponseEntity<Note>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_CUSTOMER")
    public ResponseEntity<Note> deleteNote(@PathVariable("id") int id){
        noteService.deleteNoteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
