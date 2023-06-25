package com.bookly.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.bookly.dao.CustomerRepository;
import com.bookly.dao.RatingRepository;
import com.bookly.dto.CustomerDTO;
import com.bookly.dto.RatingDTO;
import com.bookly.entity.Customer;
import com.bookly.entity.Rating;

@Service
public class RatingService {
	
	@Autowired
	RatingRepository ratingRepository;
	@Autowired
	CustomerRepository customerRepository;
	public List<RatingDTO> getProductRating(Long productId) {	
		 return ratingRepository.findAllByProductId(productId).stream().map(o -> {
			 CustomerDTO dto = new CustomerDTO(o.getCustomer().getId(), o.getCustomer().getFirstName(), o.getCustomer().getLastName(), o.getCustomer().getEmail());
			 return new RatingDTO(o.getId(), o.getProductId(), dto, o.getRatingNumber(), o.getDescription());
		 }).toList();
	}
	public List<RatingDTO> addProductRating(@RequestBody RatingDTO dto) {
		List<Rating> list = ratingRepository.findAllByProductId(
				dto.getProductId()).stream().filter( o -> o.getCustomer().getEmail().equals(dto.getCustomer().getEmail())).toList(); 
		if (list.isEmpty()) {
			Rating obj = new Rating();
			Customer cust = customerRepository.findByEmail(dto.getCustomer().getEmail());			
			obj.setProductId(dto.getProductId());
			obj.setRatingNumber(dto.getRatingNumber());
			obj.setDescription(dto.getDescription());
			obj.setCustomer(cust);
			ratingRepository.save(obj);
		} else {
			Rating ratingObj = list.get(0);
			ratingObj.setRatingNumber(dto.getRatingNumber());
			ratingObj.setDescription(dto.getDescription());
			ratingRepository.save(ratingObj);
		}
		return getProductRating(dto.getProductId());
	}
}
