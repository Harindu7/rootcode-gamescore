package com.game.game.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.game.game.entity.Game;
import com.game.game.services.GameService;

import java.util.Optional;

@RestController
@RequestMapping("/api/games")
public class GameController {

	@Autowired
	private GameService gameService;

	// Create a new game
	@PostMapping
	public ResponseEntity<?> createGame(@RequestBody Game game) {
		try {
			Game createdGame = gameService.createGame(game);
			return ResponseEntity.ok(createdGame);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while creating the game: " + e.getMessage());
		}
	}

	// Get all games
	@GetMapping
	public ResponseEntity<?> getAllGames() {
		try {
			List<Game> games = gameService.getAllGames();
			return ResponseEntity.ok(games);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while fetching games: " + e.getMessage());
		}
	}

	// Get a game by ID
	@GetMapping("/{id}")
	public ResponseEntity<?> getGameById(@PathVariable Long id) {
		try {
			Optional<Game> gameOptional = gameService.getGameById(id);
			if (gameOptional.isPresent()) {
				return ResponseEntity.ok(gameOptional.get());
			} else {
				return ResponseEntity.ok("Game with ID " + id + " not found");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while fetching the game: " + e.getMessage());
		}
	}

	// Update a game
	@PutMapping("/{id}")
	public ResponseEntity<?> updateGame(@PathVariable Long id, @RequestBody Game game) {
		try {
			Game updatedGame = gameService.updateGame(id, game);
			return ResponseEntity.ok(updatedGame);
		} catch (RuntimeException e) {
			return ResponseEntity.ok("Game with ID " + id + " not found or could not be updated: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while updating the game: " + e.getMessage());
		}
	}

	// Delete a game
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteGame(@PathVariable Long id) {
		try {
			gameService.deleteGame(id);
			return ResponseEntity.ok("Game with ID " + id + " was successfully deleted");
		} catch (RuntimeException e) {
			return ResponseEntity.ok("Game with ID " + id + " not found: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while deleting the game: " + e.getMessage());
		}
	}
}
