package net.openwebinars.springboot.errorhandling.service;

import lombok.RequiredArgsConstructor;
import net.openwebinars.springboot.errorhandling.exception.EmptyNoteListException;
import net.openwebinars.springboot.errorhandling.exception.NoteNotFoundException;
import net.openwebinars.springboot.errorhandling.model.Note;
import net.openwebinars.springboot.errorhandling.repo.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository repository;

    public List<Note> findAll() {
        List<Note> result = repository.findAll();

        if (result.isEmpty()) {
            throw new EmptyNoteListException();
        }

        return result;

    }

    public List<Note> getByAuthor(String author) {
        List<Note> result = repository.findByAuthor(author);

        if (result.isEmpty()) {
            throw new EmptyNoteListException();
        }

        return result;
    }

    public Note findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException(id));
    }

    public Note save(Note note) {
        // In this case, for now, we don't need any exceptions
        // beyond the validation mechanism itself
        return repository.save(note);
    }

    public Note edit(Long id, Note edited) {
        return repository.findById(id)
                .map(note -> {
                    note.setTitle(edited.getTitle());
                    note.setContent(edited.getContent());
                    note.setAuthor(edited.getAuthor());
                    note.setImportant(edited.isImportant());
                    return repository.save(note);
                })
                .orElseThrow(NoteNotFoundException::new);
    }

    public void delete(Long id) {
        // In this case, we do not want to use exceptions, but directly
        // prevent possible error
        if (repository.existsById(id))
            repository.deleteById(id);
    }

}
