package com.game.game.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Caching;

import com.game.game.entity.Game;
import com.game.game.repositories.GameRepository;

@Service
public class GameService {

	private final GameRepository gameRepository;

	@Autowired
	public GameService(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}

	// Create a new game
	@CachePut(value = "games", key = "#game.id")
	@CacheEvict(value = "gamesAll", allEntries = true) 
	public Game createGame(Game game) {
		return gameRepository.save(game);
	}

	// Get all games
	@Cacheable(value = "gamesAll")
	public List<Game> getAllGames() {
		return gameRepository.findAll();
	}

	// Get a game by ID
	@Cacheable(value = "games", key = "#id")
	public Optional<Game> getGameById(Long id) {
		return gameRepository.findById(id);
	}

	// Update an existing game
	@CachePut(value = "games", key = "#id")
	@CacheEvict(value = "gamesAll", allEntries = true) 
	public Game updateGame(Long id, Game updatedGame) {
		return gameRepository.findById(id).map(game -> {
			game.setName(updatedGame.getName());
			game.setDescription(updatedGame.getDescription());
			return gameRepository.save(game);
		}).orElseThrow(() -> new RuntimeException("Game not found with id: " + id));
	}

	// Delete a game
	@Caching(evict = { @CacheEvict(value = "games", key = "#id"), 
			@CacheEvict(value = "gamesAll", allEntries = true) 
	})
	public void deleteGame(Long id) {
		if (gameRepository.existsById(id)) {
			gameRepository.deleteById(id);
		} else {
			throw new RuntimeException("Game not found with id: " + id);
		}
	}
}
