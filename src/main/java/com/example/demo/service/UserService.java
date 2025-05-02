package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.ModerationStatus;
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
	}

	public List<User> getUsers() {
		return (List<User>) userRepository.findAll();
	}

	public User saveUser(User newUser) {
		return userRepository.save(newUser);
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

	public void saveReview(Review review) {
		reviewRepository.save(review);
	}

	/*public List<Review> getReviewsForUser(User user) {
		return reviewRepository.findByReviewedUser(user);
	}*/

	public List<Review> getReviewsForViewer(User reviewedUser, User requester) {
		boolean isAdmin = "ROLE_ADMIN".equals(requester.getRole());
		if (isAdmin) {
			return reviewRepository.findByReviewedUser(reviewedUser);
		} else {
			return reviewRepository.findByReviewedUserAndStatus(reviewedUser, ModerationStatus.APPROVED);
		}
	}

	public void deleteReview(Long reviewId) {
		reviewRepository.deleteById(reviewId);
	}

	public Optional<Review> findReviewById(Long reviewId) {
		return reviewRepository.findById(reviewId);
	}

	public List<Review> findAllReviews() {
		return reviewRepository.findByStatus(ModerationStatus.APPROVED);
	}
}
