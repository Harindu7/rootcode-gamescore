package com.game.game.repositories;


import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.game.game.entity.Score;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

	// 2. A REST API to obtain the user's highest scores in each game.
	@Query("SELECT s FROM Score s WHERE s.user.id = :userId AND s.score = "
			+ "(SELECT MAX(sub.score) FROM Score sub WHERE sub.game.id = s.game.id AND sub.user.id = :userId)")
	List<Score> findHighestScoresByUser(@Param("userId") Long userId);

	// Bonus : Create a REST API to sort and retrieve a game's top ten highest scores.
	@Query("SELECT s FROM Score s WHERE s.game.id = :gameId ORDER BY s.score DESC")
	List<Score> findTop10ByGameId(@Param("gameId") Long gameId, Pageable pageable);

}
