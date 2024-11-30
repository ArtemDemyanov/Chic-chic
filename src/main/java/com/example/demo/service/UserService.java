package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Review;
import com.example.demo.model.User;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
	@Autowired
    UserRepository userRepository;
	@Autowired
	private ReviewRepository reviewRepository;
	
	public UserService() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public List<User> getUsers() {
		return (List<User>) userRepository.findAll();
	}

	public void saveUser(User newUser) {
		userRepository.save(newUser);
	}
	
	public Optional<User> findByID(Long id) {
		 return userRepository.findById(id);
	}
	
	public void deleteUser(Long id) {
		User user = userRepository.findById(id)
				  .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
		userRepository.delete(user);
	}
	
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	public Review saveReview(Review review) {
		return reviewRepository.save(review);
	}

	public List<Review> getReviewsForUser(User user) {
		return reviewRepository.findByReviewedUser(user);
	}
	public void deleteReview(Long reviewId) {
		reviewRepository.deleteById(reviewId);
	}
	public Optional<Review> findReviewById(Long reviewId) {
		return reviewRepository.findById(reviewId);
	}
}
