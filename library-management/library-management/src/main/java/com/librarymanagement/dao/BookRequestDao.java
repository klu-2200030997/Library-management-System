package com.librarymanagement.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.librarymanagement.model.BookRequest;
import com.librarymanagement.model.User;

@Repository
public interface BookRequestDao extends JpaRepository<BookRequest, Integer> {
	// Additional query methods can be defined here
	
	List<BookRequest> findByUser(User user);
	
}