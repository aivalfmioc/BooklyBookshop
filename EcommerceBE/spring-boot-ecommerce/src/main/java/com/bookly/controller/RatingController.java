package com.bookly.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookly.dto.RatingDTO;
import com.bookly.service.RatingService;

@CrossOrigin("https://localhost:4200")
@RestController
@RequestMapping("/api/rating")
public class RatingController {
	
	@Autowired
	RatingService ratingService;

	@GetMapping("/{productId}")
	List<RatingDTO> getProductRating(@PathVariable Long productId) {
		return ratingService.getProductRating(productId);
	}
	
	@PostMapping("/")
	List<RatingDTO> addProductRating(@RequestBody RatingDTO dto) {
		return ratingService.addProductRating(dto);
	}
	
}
