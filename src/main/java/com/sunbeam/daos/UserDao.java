package com.sunbeam.daos;

import java.util.List;
import java.util.Optional;

import com.sunbeam.pojos.User;

public interface UserDao extends AutoCloseable {
	
	public int save(User u) throws Exception;
	
	public List<User> findAll() throws Exception;
	
	public Optional<User> findByEmail(String email) throws Exception;
	
	public int updatePassword(String email, String newPassword) throws Exception;
	
	public int update(int id, String newFName, String newLName, String newEmail) throws Exception;
	
}
