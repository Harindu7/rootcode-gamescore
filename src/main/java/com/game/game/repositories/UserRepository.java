package com.game.game.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.game.game.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	// Custom query method to find a user by username
	Optional<User> findByUsername(String username);

	// Custom query method to find a user by email
	Optional<User> findByEmail(String email);
}
