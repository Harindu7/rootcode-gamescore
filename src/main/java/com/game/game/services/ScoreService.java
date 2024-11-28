package com.game.game.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.game.game.entity.Score;
import com.game.game.repositories.ScoreRepository;

@Service
public class ScoreService {

	private final ScoreRepository scoreRepository;

	@Autowired
	public ScoreService(ScoreRepository scoreRepository) {
		this.scoreRepository = scoreRepository;
	}

	// 1. A REST API that allows saving a user's game scores.
	@CachePut(value = "scores", key = "#score.id")
	@CacheEvict(value = "scoresAll", allEntries = true) // Clear cached score list
	public Score createScore(Score score) {
		return scoreRepository.save(score);
	}

	// 2. A REST API to obtain the user's highest scores in each game.
	@Cacheable(value = "userHighestScores", key = "#userId")
	public List<Score> getUserHighestScores(Long userId) {
		return scoreRepository.findHighestScoresByUser(userId);
	}
	
	// Bonus : Create a REST API to sort and retrieve a game's top ten highest scores.
	@Cacheable(value = "gameTopScores", key = "#gameId")
	public List<Score> getTopScoresByGame(Long gameId) {
	    Pageable pageable = PageRequest.of(0, 10); // Top 10
	    return scoreRepository.findTop10ByGameId(gameId, pageable);
	}



	// Get all scores
	@Cacheable(value = "scoresAll")
	public List<Score> getAllScores() {
		return scoreRepository.findAll();
	}

	// Get a score by ID
	@Cacheable(value = "scores", key = "#id")
	public Optional<Score> getScoreById(Long id) {
		return scoreRepository.findById(id);
	}

	// Delete a score
	@Caching(evict = { @CacheEvict(value = "scores", key = "#id"), // Evict individual score cache
			@CacheEvict(value = "scoresAll", allEntries = true) // Clear cached score list
	})
	public void deleteScore(Long id) {
		if (scoreRepository.existsById(id)) {
			scoreRepository.deleteById(id);
		} else {
			throw new RuntimeException("Score not found with id: " + id);
		}
	}

}
