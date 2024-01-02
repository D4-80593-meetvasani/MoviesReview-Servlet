package com.sunbeam.daos;

import java.util.List;

import com.sunbeam.pojos.Movies;

public interface MovieDao extends AutoCloseable{
	public List<Movies> findAll() throws Exception;

}
