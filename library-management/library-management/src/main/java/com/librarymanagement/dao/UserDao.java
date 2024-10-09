package com.librarymanagement.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.librarymanagement.model.User;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {
	
	User findByUserIdAndPassword(String userId, String password);
	User findByEmailid(String emailId);
	User findByEmailidAndMobileno(String emailId, String mobileNo);

}
