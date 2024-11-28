package com.game.game.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Caching;

import com.game.game.entity.User;
import com.game.game.repositories.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	// Create a new user
	@CachePut(value = "users", key = "#user.id")
	@CacheEvict(value = "usersAll", allEntries = true) // Clear cached user list
	public User createUser(User user) {
		return userRepository.save(user);
	}

	// Get all users
	@Cacheable(value = "usersAll")
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	// Get a user by ID
	@Cacheable(value = "users", key = "#id")
	public Optional<User> getUserById(Long id) {
		return userRepository.findById(id);
	}

	// Update an existing user
	@CachePut(value = "users", key = "#id")
	@CacheEvict(value = "usersAll", allEntries = true) // Clear cached user list
	public User updateUser(Long id, User updatedUser) {
		return userRepository.findById(id).map(user -> {
			user.setUsername(updatedUser.getUsername());
			user.setEmail(updatedUser.getEmail());
			return userRepository.save(user);
		}).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
	}

	// Delete a user
	@Caching(evict = { @CacheEvict(value = "users", key = "#id"), // Evict individual user cache
			@CacheEvict(value = "usersAll", allEntries = true) // Clear cached user list
	})
	public void deleteUser(Long id) {
		if (userRepository.existsById(id)) {
			userRepository.deleteById(id);
		} else {
			throw new RuntimeException("User not found with id: " + id);
		}
	}
}
