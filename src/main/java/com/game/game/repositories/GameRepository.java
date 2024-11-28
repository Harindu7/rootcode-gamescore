package com.game.game.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.game.game.entity.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
	// Custom query method to find a game by name
	Optional<Game> findByName(String name);
}
