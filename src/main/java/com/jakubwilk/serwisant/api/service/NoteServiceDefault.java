package com.jakubwilk.serwisant.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.jakubwilk.serwisant.api.entity.jpa.User;
import com.jakubwilk.serwisant.api.repository.NoteRepository;
import com.jakubwilk.serwisant.api.entity.jpa.Note;
import com.jakubwilk.serwisant.api.entity.jpa.Repair;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteServiceDefault implements NoteService {
    private final NoteRepository repository;
    private final RepairService repairService;
    private final UserService userService;

    public NoteServiceDefault(NoteRepository repository, RepairService repairService, UserService userService) {
        this.repository = repository;
        this.repairService = repairService;
        this.userService = userService;
    }

    @Override
    public Note findNoteById(int id){
        Optional<Note> result = repository.findById(id);

        if(result.isPresent()){
            return result.get();
        }else{
            throw new RuntimeException("Note with id: " + id + " not found");
        }
    }

    @Override
    public List<Note> findAllNotes() {
        return repository.findAll();
    }

    @Override
    public List<Note> findAllNotes(int repairId) {
        return repository.findAllNotesByRepairId(repairId);
    }

//

    @Override
    @Transactional
    public Note saveNote(JsonNode noteJsonNode) {
        checkNoteNode(noteJsonNode);

        int repairId = noteJsonNode.get("repairId").asInt();
        Repair theRepair = repairService.findById(repairId);

        int authorId = noteJsonNode.get("authorId").asInt();
        User author = userService.findById(authorId);

        Note newNote = Note.builder()
                .visibility(Note.Visibility.valueOf(noteJsonNode.get("visibility").asText().toUpperCase()))
                .message(noteJsonNode.get("message").asText())
                .repair(theRepair)
                .author(author)
                .build();

        return repository.save(newNote);
    }

    @Override
    public Note updateNote(JsonNode toUpdate) {
        checkNoteNode(toUpdate);
        if(!toUpdate.has("id"))
            throw new IllegalArgumentException("Note to update has to be provided with id!");

        int repairId = toUpdate.get("repair").asInt();
        Repair theRepair = repairService.findById(repairId);

        Note theNote = Note.builder()
                .id(toUpdate.get("id").asInt())
                .visibility(Note.Visibility.valueOf(toUpdate.get("visibility").asText()))
                .message(toUpdate.get("message").asText())
                .repair(theRepair)
                .build();

        return repository.save(theNote);
    }

    private void checkNoteNode(JsonNode node){
        if(node == null) throw new NullPointerException("Note can't be null!");
        if(!node.has("visibility"))
            throw new IllegalArgumentException("Note must contain visibility level!");
        if(!node.has("message"))
            throw new IllegalArgumentException("Note must contain message!");
        if(!node.has("repairId"))
            throw new IllegalArgumentException("Note must contain repair id!");
        if(!node.has("authorId"))
            throw new IllegalArgumentException("Note must have an author");
    }

    @Override
    public void deleteNoteById(int id) {
        repository.deleteById(id);
    }
}
