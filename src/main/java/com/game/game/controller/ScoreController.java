package com.game.game.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.game.game.entity.Score;
import com.game.game.services.ScoreService;

import java.util.Optional;

@RestController
@RequestMapping("/api/scores")
public class ScoreController {

	@Autowired
	private ScoreService scoreService;

	// 1. A REST API that allows saving a user's game scores.
	@PostMapping
	public ResponseEntity<?> createScore(@RequestBody Score score) {
		try {
			Score createdScore = scoreService.createScore(score);
			return ResponseEntity.ok(createdScore);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while creating the score: " + e.getMessage());
		}
	}

	// 2. A REST API to obtain the user's highest scores in each game.
	@GetMapping("/user/{userId}/highest-scores")
	public ResponseEntity<?> getUserHighestScores(@PathVariable Long userId) {
		try {
			List<Score> highestScores = scoreService.getUserHighestScores(userId);
			if (highestScores.isEmpty()) {
				return ResponseEntity.ok("No scores found for user with ID " + userId);
			}
			return ResponseEntity.ok(highestScores);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while fetching the highest scores: " + e.getMessage());
		}
	}

	// Bonus : Create a REST API to sort and retrieve a game's top ten highest scores.
	@GetMapping("/game/{gameId}/top-scores")
	public ResponseEntity<?> getTopScoresByGame(@PathVariable Long gameId) {
		try {
			List<Score> topScores = scoreService.getTopScoresByGame(gameId);
			if (topScores.isEmpty()) {
				return ResponseEntity.ok("No scores found for game with ID " + gameId);
			}
			return ResponseEntity.ok(topScores);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while fetching the top scores: " + e.getMessage());
		}
	}

	// Get all scores
	@GetMapping
	public ResponseEntity<?> getAllScores() {
		try {
			List<Score> scores = scoreService.getAllScores();
			return ResponseEntity.ok(scores);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while fetching scores: " + e.getMessage());
		}
	}

	// Get a score by ID
	@GetMapping("/{id}")
	public ResponseEntity<?> getScoreById(@PathVariable Long id) {
		try {
			Optional<Score> scoreOptional = scoreService.getScoreById(id);
			if (scoreOptional.isPresent()) {
				return ResponseEntity.ok(scoreOptional.get());
			} else {
				return ResponseEntity.ok("Score with ID " + id + " not found");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while fetching the score: " + e.getMessage());
		}
	}

	// Delete a score
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteScore(@PathVariable Long id) {
		try {
			scoreService.deleteScore(id);
			return ResponseEntity.ok("Score with ID " + id + " was successfully deleted");
		} catch (RuntimeException e) {
			return ResponseEntity.ok("Score with ID " + id + " not found: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while deleting the score: " + e.getMessage());
		}
	}
}
