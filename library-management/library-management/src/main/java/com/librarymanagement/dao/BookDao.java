package com.librarymanagement.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.librarymanagement.model.Book;

@Repository
public interface BookDao extends JpaRepository<Book, Integer> {

	// Find books by their category
	List<Book> findByCategoryId(int categoryId);
	
	List<Book> findByNameContainingIgnoreCase(String name);

}
