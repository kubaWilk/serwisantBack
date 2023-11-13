package com.jakubwilk.serwisant.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.jakubwilk.serwisant.api.dao.NoteRepository;
import com.jakubwilk.serwisant.api.entity.Note;
import com.jakubwilk.serwisant.api.entity.Repair;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteServiceDefault implements NoteService {
    private NoteRepository repository;
    private RepairService repairService;

    public NoteServiceDefault(NoteRepository repository, RepairService repairService) {
        this.repository = repository;
        this.repairService = repairService;
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
        List<Note> notes = repository.findAll();
        return notes;
    }

    @Override
    @Transactional
    public Note saveNote(JsonNode noteJsonNode) {
        checkNoteNode(noteJsonNode);

        int repairId = noteJsonNode.get("repair").asInt();
        Repair theRepair = repairService.findById(repairId);

        Note newNote = Note.builder()
                .visibility(Note.Visibility.valueOf(noteJsonNode.get("visibility").asText()))
                .message(noteJsonNode.get("message").asText())
                .repair(theRepair)
                .build();

        return repository.save(newNote);
    }

    @Override
    public Note updateNote(JsonNode toUpdate) {
        checkNoteNode(toUpdate);
        if(!toUpdate.has("id")) throw new IllegalArgumentException("Note to update has to be provided with id!");

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
        if(!node.has("repair"))
            throw new IllegalArgumentException("Note must contain repair id!");
    }

    @Override
    public void deleteNoteById(int id) {
        repository.deleteById(id);
    }
}
