package net.openwebinars.springboot.errorhandling.controller;

import lombok.RequiredArgsConstructor;
import net.openwebinars.springboot.errorhandling.model.Note;
import net.openwebinars.springboot.errorhandling.service.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/note")
@RequiredArgsConstructor
public class NoteController {

    //private final NoteRepository repository;
    private final NoteService noteService;

    @GetMapping("/")
    //public ResponseEntity<List<Note>> getAll() {
    public List<Note> getAll() {
        // We use a common method to return the response of all List<Note>
        //return buildResponseOfAList(repository.findAll());

        return noteService.findAll();

    }

    @GetMapping("/{id}")
    //public ResponseEntity<Note> getById(@PathVariable Long id) {
    public Note getById(@PathVariable Long id) {
        /*
         The ResponseEntity.of method takes an Optional<?> as an argument and returns
         - 200 Ok if Optional.isPresent() == true
         - 404 Not Found if Optional.isEmpty() == true
         */
        //return ResponseEntity.of(repository.findById(id));

        return noteService.findById(id);

    }

    @GetMapping("/author/{author}")
    //public ResponseEntity<List<Note>> getByAuthor(@PathVariable String author) {
    public List<Note> getByAuthor(@PathVariable String author) {
        // We use a common method to return the response of all List<Note>
        //return buildResponseOfAList(repository.findByAuthor(author));

        return noteService.getByAuthor(author);
    }

    /*private ResponseEntity<List<Note>> buildResponseOfAList(List<Note> list) {
        if (list.isEmpty())
            //return ResponseEntity.notFound().build();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No notes found");
        else
            return ResponseEntity.ok(list);
    }*/

    @PostMapping("/")
    public ResponseEntity<Note> createNewNote(@Valid @RequestBody Note note) {

        // In this method, we do want to manage the response, to return 201
        Note created = noteService.save(note);

        URI createdURI = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId()).toUri();

        /*
         Typically, the correct response to a POST request is 201 Created.
         Additionally, a Location header can be returned with the URI that
         allows us to make the GET request to the newly created resource.
         */
        return ResponseEntity
                .created(createdURI)
                .body(created);

    }

    @PutMapping("/{id}")
    //public ResponseEntity<Note> edit(@PathVariable Long id, @RequestBody Note edited) {
    public Note edit(@PathVariable Long id, @RequestBody Note edited) {
        return noteService.edit(id, edited);

        /*
        return ResponseEntity.of(
                repository.findById(id)
                    .map(note -> {
                        note.setTitle(edited.getTitle());
                        note.setContent(edited.getContent());
                        note.setAuthor(edited.getAuthor());
                        note.setImportant(edited.isImportant());
                        return repository.save(note);
                    }));
           */

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        // We leave this line commented to cause a 500 error if we delete the same resource twice
        //if (repository.existsById(id))
        //   repository.deleteById(id);
        noteService.delete(id);

        return ResponseEntity.noContent().build();

    }

}
