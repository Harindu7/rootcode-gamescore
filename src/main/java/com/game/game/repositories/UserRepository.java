package com.game.game.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.game.game.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
