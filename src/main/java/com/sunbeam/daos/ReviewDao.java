package com.sunbeam.daos;

import java.util.List;
import java.util.Optional;

import com.sunbeam.pojos.Review;

public interface ReviewDao extends AutoCloseable{
	public List<Review> findAll() throws Exception;
	public List<Review> findSharedReviews(int uid) throws Exception;
	public int save(Review q) throws Exception;
	public int update(Review r) throws Exception;
	public Optional<Review> findReviewById(int uid) throws Exception;
	public List<Review> findReviewsById(int uid) throws Exception;
	public int delete(int id) throws Exception;
	
}
