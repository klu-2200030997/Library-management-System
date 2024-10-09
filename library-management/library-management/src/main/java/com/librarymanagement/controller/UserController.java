package com.librarymanagement.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.librarymanagement.dao.BookCategoryDao;
import com.librarymanagement.dao.BookDao;
import com.librarymanagement.dao.BookRequestDao;
import com.librarymanagement.dao.UserDao;
import com.librarymanagement.model.Book;
import com.librarymanagement.model.BookCategory;
import com.librarymanagement.model.BookRequest;
import com.librarymanagement.model.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

	@Autowired
	private UserDao userDao;

	@Autowired
	private BookDao bookDao;

	@Autowired
	private BookCategoryDao bookCategoryDao;

	@Autowired
	private BookRequestDao bookRequestDao;

	@GetMapping("/userlogin")
	public String goToLoginPage() {
		return "userlogin";
	}

	@GetMapping("/userregister")
	public String goToRegisterPage() {
		return "userregister";
	}

	@GetMapping("/viewstudents")
	public ModelAndView viewcustomers() {
		ModelAndView mv = new ModelAndView();

		List<User> users = this.userDao.findAll();

		mv.addObject("users", users);
		mv.setViewName("viewstudents");

		return mv;
	}

	@PostMapping("/userregister")
	public ModelAndView registerAdmin(@ModelAttribute User user) {
		ModelAndView mv = new ModelAndView();
		if (this.userDao.save(user) != null) {
			mv.addObject("status", user.getFirstname() + " Successfully Registered!");
			mv.setViewName("index");
		}

		else {
			mv.addObject("status", user.getFirstname() + " Failed to Registered User!");
			mv.setViewName("index");

		}

		return mv;
	}

	@PostMapping("/userlogin")
	public ModelAndView loginAdmin(HttpServletRequest request, @RequestParam("userId") String userId,
			@RequestParam("password") String password) {
		ModelAndView mv = new ModelAndView();

		User user = userDao.findByUserIdAndPassword(userId, password);

		if (user != null) {
			HttpSession session = request.getSession();
			session.setAttribute("activeuser", user);
			session.setAttribute("userlogin", "user");
			mv.addObject("status", user.getFirstname() + " Successfully Logged In!");
			mv.setViewName("index");
		}

		else {
			mv.addObject("status", "Failed to login!");
			mv.setViewName("index");
		}

		return mv;
	}

	@GetMapping("/viewStudentDetails")
	public ModelAndView viewStudentDetails(@RequestParam("id") int id) {
		ModelAndView mv = new ModelAndView();

		// Fetch the student details based on ID
		User student = userDao.findById(id).orElse(null);

		// Add the student details to the model
		mv.addObject("student", student);
		mv.setViewName("viewstudentdetails");

		return mv;
	}

	@PostMapping("/deleteStudent")
	public ModelAndView deleteStudent(@RequestParam("id") int id) {
		ModelAndView mv = new ModelAndView();
		userDao.deleteById(id);

		mv.addObject("status", "Student Deleted Successful!!!");
		mv.setViewName("index");

		return mv;
	}

	@GetMapping("/addcategory")
	public String addcategoryPage() {
		return "addcategory";
	}

	@PostMapping("/addcategory")
	public ModelAndView addCategory(@RequestParam("name") String name) {
		ModelAndView mv = new ModelAndView();

		// Create a new BookCategory object
		BookCategory category = new BookCategory();
		category.setName(name);

		// Save the category to the database
		bookCategoryDao.save(category);

		// Add success message to the model
		mv.addObject("status", "Category added successfully!");

		// Set the view name to 'index'
		mv.setViewName("index");

		return mv;
	}

	@GetMapping("/addbook")
	public ModelAndView addBookPage() {
		ModelAndView mv = new ModelAndView();

		List<BookCategory> categories = this.bookCategoryDao.findAll();

		// Add success message to the model
		mv.addObject("categories", categories);

		// Set the view name to 'index'
		mv.setViewName("addbook");

		return mv;
	}

	@PostMapping("/addbook")
	public ModelAndView addBook(@RequestParam("name") String name, @RequestParam("isbn") String isbn,
			@RequestParam("author") String author, @RequestParam("publisher") String publisher,
			@RequestParam("edition") String edition, @RequestParam("price") String price,
			@RequestParam("quantity") int quantity, @RequestParam("categoryId") int categoryId) {

		ModelAndView mv = new ModelAndView();

		// Create a new Book object
		Book book = new Book();
		book.setName(name);
		book.setIsbn(isbn);
		book.setAuthor(author);
		book.setPublisher(publisher);
		book.setEdition(edition);
		book.setPrice(Double.parseDouble(price));
		book.setQuantity(quantity);

		BookCategory category = bookCategoryDao.findById(categoryId).orElse(null);
		book.setCategory(category);

		bookDao.save(book);

		mv.addObject("status", "Book added successfully!");

		mv.setViewName("addbook");

		mv.addObject("status", "Status Added Successful!!!");
		mv.setViewName("index");
		return mv;
	}

	@GetMapping("/viewallbooks")
	public ModelAndView viewallbooksPage() {
		ModelAndView mv = new ModelAndView();

		List<Book> books = this.bookDao.findAll();

		mv.addObject("books", books);
		mv.setViewName("viewallbooks");
		return mv;
	}

	@GetMapping("/viewbookdetails")
	public ModelAndView viewBookDetails(@RequestParam("id") int id) {
		ModelAndView mv = new ModelAndView();

		// Fetch the book by id from the database
		Book book = bookDao.findById(id).orElse(null);

		// Add the book to the model
		mv.addObject("book", book);

		// Set the view name to 'bookdetails'
		mv.setViewName("bookdetails");

		return mv;
	}

	@GetMapping("/updatebook")
	public ModelAndView updateBookPage(@RequestParam("id") int id) {
		ModelAndView mv = new ModelAndView();

		// Fetch the book by id from the database
		Book book = bookDao.findById(id).orElse(null);
		List<BookCategory> categories = bookCategoryDao.findAll();

		// Add the book and categories to the model
		mv.addObject("book", book);
		mv.addObject("categories", categories);

		// Set the view name to 'updatebook'
		mv.setViewName("updatebook");

		return mv;
	}

	@PostMapping("/updatebook")
	public ModelAndView updateBook(@RequestParam("id") int id, @RequestParam("name") String name,
			@RequestParam("isbn") String isbn, @RequestParam("author") String author,
			@RequestParam("publisher") String publisher, @RequestParam("edition") String edition,
			@RequestParam("price") String price, @RequestParam("quantity") int quantity,
			@RequestParam("categoryId") int categoryId) {

		ModelAndView mv = new ModelAndView();

		// Fetch the existing book
		Book book = bookDao.findById(id).orElse(null);
		if (book != null) {
			book.setName(name);
			book.setIsbn(isbn);
			book.setAuthor(author);
			book.setPublisher(publisher);
			book.setEdition(edition);
			book.setPrice(Double.parseDouble(price));
			book.setQuantity(quantity);

			// Fetch the category and update the book
			BookCategory category = bookCategoryDao.findById(categoryId).orElse(null);
			book.setCategory(category);

			// Save the updated book
			bookDao.save(book);
			mv.addObject("status", "Book updated successfully!");
		} else {
			mv.addObject("status", "Book not found.");
		}

		// Redirect to the view all books page
		mv.setViewName("index");

		return mv;
	}

	@GetMapping("/deletebook")
	public ModelAndView deleteBook(@RequestParam("id") int id) {
		ModelAndView mv = new ModelAndView();

		bookDao.deleteById(id);

		mv.addObject("status", "Book deleted successfully!");

		mv.setViewName("index");

		return mv;
	}

	@GetMapping("/books")
	public ModelAndView booksPage() {
		ModelAndView mv = new ModelAndView();

		List<Book> books = this.bookDao.findAll();

		mv.addObject("books", books);
		mv.setViewName("books");
		return mv;
	}

	@GetMapping("/requestBook/{bookId}")
	public ModelAndView showRequestBookPage(@PathVariable("bookId") int bookId, HttpSession session) {
		ModelAndView mv = new ModelAndView();

		// Retrieve book details
		Book book = bookDao.findById(bookId).orElse(null);

		mv.addObject("book", book);
		mv.setViewName("requestbook");

		return mv;
	}

	@PostMapping("/submitBookRequest")
	public ModelAndView submitBookRequest(@RequestParam("bookId") int bookId,
			@RequestParam("returnDate") String returnDate, HttpSession session) {
		ModelAndView mv = new ModelAndView();

		// Retrieve book and user details
		Book book = bookDao.findById(bookId).orElse(null);
		User user = (User) session.getAttribute("activeuser");

		if (book != null && user != null) {
			// Create and save the book request
			BookRequest bookRequest = new BookRequest();
			bookRequest.setBook(book);
			bookRequest.setUser(user);
			bookRequest.setRequestDate(LocalDate.now().toString());
			bookRequest.setReturnDate(returnDate);
			bookRequest.setStatus("Pending");

			bookRequestDao.save(bookRequest);

			// Add success message to the model
			mv.addObject("status", "Book request submitted successfully!");
			mv.setViewName("index");
		} else {
			mv.addObject("status", "Failed to submit book request.");
			mv.setViewName("index");
		}

		return mv;
	}

	@GetMapping("/requestedbook")
	public ModelAndView submitBookRequest(HttpSession session) {
		ModelAndView mv = new ModelAndView();

		User user = (User) session.getAttribute("activeuser");

		List<BookRequest> requests = this.bookRequestDao.findByUser(user);
		mv.addObject("requests", requests);
		mv.setViewName("requestedbook");

		return mv;
	}

	@GetMapping("/allrequestedbook")
	public ModelAndView allrequestedbookPage() {
		ModelAndView mv = new ModelAndView();

		List<BookRequest> requests = this.bookRequestDao.findAll();
		mv.addObject("requests", requests);
		mv.setViewName("allrequestedbook");

		return mv;
	}

	@GetMapping("/approveRequest")
	public String approveRequest(@RequestParam("requestId") int id) {
		// Find the book request by ID
		BookRequest request = bookRequestDao.findById(id).orElse(null);

		if (request != null) {
			// Update the status to "Approved"
			request.setStatus("Approved");
			bookRequestDao.save(request);
		}

		// Redirect to the all requested books page
		return "redirect:/allrequestedbook";
	}

	@GetMapping("/rejectRequest")
	public String rejectRequest(@RequestParam("requestId") int id) {
		// Find the book request by ID
		BookRequest request = bookRequestDao.findById(id).orElse(null);

		if (request != null) {
			// Update the status to "Rejected"
			request.setStatus("Rejected");
			bookRequestDao.save(request);
		}

		// Redirect to the all requested books page
		return "redirect:/allrequestedbook";
	}

	@GetMapping("/returnBook")
	public ModelAndView returnBook(@RequestParam("requestId") int requestId) {
		ModelAndView mv = new ModelAndView();

		// Fetch the request by ID
		BookRequest request = bookRequestDao.findById(requestId).orElse(null);

		if (request != null) {
			request.setStatus("Returned"); // Set the status to "Returned"
			bookRequestDao.save(request);

			mv.addObject("status", "Book returned successfully!");
		} else {
			mv.addObject("status", "Request not found.");
		}

		// Redirect to the page showing the user's requested books
		mv.setViewName("index");

		return mv;
	}

	@GetMapping("/searchBooks")
	public ModelAndView searchBooks(@RequestParam("search") String searchTerm) {
		ModelAndView mv = new ModelAndView("availableBooks");

		List<Book> books;
		if (searchTerm != null && !searchTerm.isEmpty()) {
			books = bookDao.findByNameContainingIgnoreCase(searchTerm);
			mv.addObject("searchTerm", searchTerm);
		} else {
			books = bookDao.findAll();
		}

		mv.addObject("books", books);
		mv.setViewName("books");
		return mv;
	}

}
