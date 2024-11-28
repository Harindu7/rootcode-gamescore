package com.game.game.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.game.game.entity.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
}
