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

import com.game.game.entity.User;
import com.game.game.services.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;

	// Create a new user
	@PostMapping("/reg")
	public ResponseEntity<?> createUser(@RequestBody User user) {
		try {
			User createdUser = userService.createUser(user);
			return ResponseEntity.ok(createdUser);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while creating the user: " + e.getMessage());
		}
	}

	// Get all users
	@GetMapping
	public ResponseEntity<?> getAllUsers() {
		try {
			List<User> users = userService.getAllUsers();
			return ResponseEntity.ok(users);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while fetching users: " + e.getMessage());
		}
	}

	// Get a user by ID
	@GetMapping("/{id}")
	public ResponseEntity<?> getUserById(@PathVariable Long id) {
		try {
			Optional<User> userOptional = userService.getUserById(id);
			if (userOptional.isPresent()) {
				return ResponseEntity.ok(userOptional.get());
			} else {
				return ResponseEntity.ok("User with ID " + id + " not found");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while fetching the user: " + e.getMessage());
		}
	}

	// Update a user
	@PutMapping("/{id}")
	public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
		try {
			User updatedUser = userService.updateUser(id, user);
			return ResponseEntity.ok(updatedUser);
		} catch (RuntimeException e) {
			return ResponseEntity.ok("User with ID " + id + " not found or could not be updated: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while updating the user: " + e.getMessage());
		}
	}

	// Delete a user
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id) {
		try {
			userService.deleteUser(id);
			return ResponseEntity.ok("User with ID " + id + " was successfully deleted");
		} catch (RuntimeException e) {
			return ResponseEntity.ok("User with ID " + id + " not found: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while deleting the user: " + e.getMessage());
		}
	}
}
