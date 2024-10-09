package com.librarymanagement.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.librarymanagement.model.BookCategory;

@Repository
public interface BookCategoryDao extends JpaRepository<BookCategory, Integer> {
	// Additional query methods (if needed) can be defined here
}
