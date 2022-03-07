package pr1Java.services.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pr1Java.model.Game;
import pr1Java.model.exceptions.DuplicateException;
import pr1Java.model.exceptions.NotFoundException;
import pr1Java.persistence.GameRepository;

@RestController
@RequestMapping("/basketball-games/games")
@CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}, allowedHeaders = "*", allowCredentials = "true")
public class GameController {
    @Autowired
    private GameRepository gameRepository;

    @GetMapping
    public Iterable<Game> getAll() {
        return gameRepository.getAll();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getOne(@PathVariable int id) {
        try {
            return new ResponseEntity<>(gameRepository.getOne(id), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Game game) {
        try {
            return new ResponseEntity<>(gameRepository.add(game), HttpStatus.OK);
        } catch (DuplicateException e) {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> remove(@PathVariable int id) {
        try {
            return new ResponseEntity<>(gameRepository.remove(id), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> modify(@PathVariable int id, @RequestBody Game game) {
        try {
            return new ResponseEntity<>(gameRepository.modify(id, game), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }
}
