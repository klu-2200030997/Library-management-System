package com.librarymanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.librarymanagement.dao.AdminDao;
import com.librarymanagement.model.Admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

	@Autowired
	private AdminDao adminDao;

	@GetMapping("/")
	public String goToHomeDuringStart() {
		return "index";
	}

	@GetMapping("/home")
	public String goToHome() {
		return "index";
	}

	@GetMapping("/adminlogin")
	public String goToAdminLoginPage() {

		return "adminlogin";
	}

	@GetMapping("/adminregister")
	public String goToAdminRegisterPage() {

		return "adminregister";
	}

	@PostMapping("/adminregister")
	public ModelAndView registerAdmin(@ModelAttribute Admin admin) {
		ModelAndView mv = new ModelAndView();
		if (this.adminDao.save(admin) != null) {
			mv.addObject("status", "Admin Successfully Registered");
			mv.setViewName("index");
		}

		else {
			mv.addObject("status", "Admin Failed to Registered");
			mv.setViewName("index");

		}

		return mv;
	}

	@PostMapping("/adminlogin")
	public ModelAndView loginAdmin(HttpServletRequest request, @RequestParam("userId") String userId,
			@RequestParam("password") String password) {
		ModelAndView mv = new ModelAndView();

		Admin admin = adminDao.findByUserIdAndPassword(userId, password);

		if (admin != null) {
			HttpSession session = request.getSession();
			session.setAttribute("activeuser", admin);
			session.setAttribute("userlogin", "admin");
			mv.addObject("status", " Successfully Logged in as ADMIN!");
			mv.setViewName("index");
		}

		else {
			mv.addObject("status", "Failed to login as Admin!");
			mv.setViewName("index");
		}

		return mv;
	}

	@GetMapping("/logout")
	public ModelAndView loginAdmin(HttpSession session) {
		ModelAndView mv = new ModelAndView();

		session.removeAttribute("activeuser");
		session.removeAttribute("userlogin");

		mv.addObject("status", "Log out successful!!!");
		mv.setViewName("index");

		return mv;
	}

}
