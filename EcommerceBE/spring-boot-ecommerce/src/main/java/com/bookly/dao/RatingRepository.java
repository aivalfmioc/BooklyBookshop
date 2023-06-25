package com.bookly.dao;

import java.util.List;

import com.bookly.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long>  {

	List<Rating> findAllByProductId(Long productId);
//	List<Rating> findAllByProductIdAndEmail(Long productId, String email);
}
