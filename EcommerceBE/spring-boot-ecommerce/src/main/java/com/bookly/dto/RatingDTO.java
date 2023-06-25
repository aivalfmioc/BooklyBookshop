package com.bookly.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RatingDTO {
	private Long id;
    private Long productId;
    private CustomerDTO customer; 
    private int ratingNumber;
    private String description;
}

