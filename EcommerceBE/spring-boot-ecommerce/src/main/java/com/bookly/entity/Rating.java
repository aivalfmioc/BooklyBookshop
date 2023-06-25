package com.bookly.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="rating")
@Data
public class Rating {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "rating_number")
    private int ratingNumber;

    @Column(name = "description")
    private String description;
    
    @ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="cust_id")
    private Customer customer;

}
