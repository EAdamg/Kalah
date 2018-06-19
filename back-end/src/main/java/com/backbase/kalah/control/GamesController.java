package com.backbase.kalah.control;

import com.backbase.kalah.game.Game;
import com.backbase.kalah.game.GameStatus;
import com.backbase.kalah.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RestController
@EnableAutoConfiguration
@RequestMapping("/games")
public class GamesController {

    private HttpServletRequest request;

    private final Map<UUID, Game> gamesMap = new HashMap<>();

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createGame(@RequestBody GameCreationRequest gameCreationRequest) {
        UUID uuid = UUID.randomUUID();
        StringBuffer requestURL = request.getRequestURL();
        requestURL.append("/").append(uuid.toString());
        this.gamesMap.put(uuid, new Game(gameCreationRequest.getNumStonesPerPit(), gameCreationRequest.getDifficulty()));
        return new ResponseEntity<>(new GameCreationResponse(uuid.toString(), requestURL.toString()), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}/pits/{pitId}", method = RequestMethod.PUT)
    public ResponseEntity<?> makeMove(@PathVariable String id, @PathVariable int pitId) throws NonExistentGameException, EmptyPitException, InvalidPitForPlayerException, InvalidPitIdException, FinishedGameException {
        UUID gameId = UUID.fromString(id);
        Game game = this.gamesMap.get(gameId);
        if (game == null) {
            throw new NonExistentGameException(id);
        }
        if (game.isFinished()) {
            throw new FinishedGameException(id);
        }
        GameStatus gameStatus = game.makeMove(pitId);
        boolean win = gameStatus.isWinner();
        String message = win ? String.format("%s has won!!", gameStatus.getPlayer().getName()) : String.format("It is now %s's turn", gameStatus.getPlayer().getName());
        return new ResponseEntity<>(new GameMoveResponse(id, request.getRequestURL().toString(), gameStatus.getBoardStatus(), message, !win ? gameStatus.getPlayer().getName() : null), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}/ai", method = RequestMethod.PUT)
    public ResponseEntity<?> makeAiMove(@PathVariable String id) throws NonExistentGameException, EmptyPitException, InvalidPitForPlayerException, InvalidPitIdException, FinishedGameException {
        UUID gameId = UUID.fromString(id);
        Game game = this.gamesMap.get(gameId);
        if (game == null) {
            throw new NonExistentGameException(id);
        }
        if (game.isFinished()) {
            throw new FinishedGameException(id);
        }
        GameStatus gameStatus = game.makeAiMove();
        boolean win = gameStatus.isWinner();
        String message = win ? String.format("%s has won!!", gameStatus.getPlayer().getName()) : String.format("It is now %s's turn", gameStatus.getPlayer().getName());
        return new ResponseEntity<>(new GameMoveResponse(id, request.getRequestURL().toString(), gameStatus.getBoardStatus(), message, !win ? gameStatus.getPlayer().getName() : null), HttpStatus.OK);
    }

    public static void main(String[] args) {
        SpringApplication.run(GamesController.class, args);
    }

    @ControllerAdvice
    class GameExceptionHandler extends ResponseEntityExceptionHandler {

        @Override
        protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
            ErrorHandler errorHandler = new ErrorHandler(HttpStatus.BAD_REQUEST);
            errorHandler.setMessage("Malformed JSON request");
            return buildResponseEntity(errorHandler);
        }

        @ExceptionHandler({EmptyPitException.class, InvalidPitIdException.class, InvalidPitForPlayerException.class, FinishedGameException.class})
        protected ResponseEntity<Object> handleHttpMessageInvalid(Exception ex) {
            ErrorHandler errorHandler = new ErrorHandler(BAD_REQUEST);
            errorHandler.setMessage(ex.getMessage());
            return buildResponseEntity(errorHandler);
        }

        @ExceptionHandler(NonExistentGameException.class)
        protected ResponseEntity<Object> handleEntityNotFound(
                NonExistentGameException ex) {
            ErrorHandler errorHandler = new ErrorHandler(NOT_FOUND);
            errorHandler.setMessage(ex.getMessage());
            return buildResponseEntity(errorHandler);
        }

        @ExceptionHandler(Exception.class)
        protected ResponseEntity<Object> handleException(
                Exception ex) {
            ErrorHandler errorHandler = new ErrorHandler(INTERNAL_SERVER_ERROR);
            errorHandler.setMessage(ex.getMessage());
            return buildResponseEntity(errorHandler);
        }

        private ResponseEntity<Object> buildResponseEntity(ErrorHandler errorHandler) {
            return new ResponseEntity<>(errorHandler, errorHandler.getStatus());
        }

    }

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }
}
