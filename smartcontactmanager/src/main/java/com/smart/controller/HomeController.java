package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@Autowired
	private UserRepository userRepository; 
	
	
	@RequestMapping("/")
	public String home(Model model)
	{
		model.addAttribute("title","Home-Smart Contact Manager");
		return "home";
	}
	
	
	@RequestMapping("/about")
	public String about(Model model)
	{
		model.addAttribute("title", "About-Smart Contact Manager");
		return "about";
	}
	
	@RequestMapping("/signup")
	public String signup(Model model,HttpSession session)
	{
		model.addAttribute("title","Register-Smart Contact Manager");
		model.addAttribute("user",new User());
		session.setAttribute("message", new Message(null, null));
		return "signup";
	}
	
	@PostMapping("do_register")
	public String registerUser(@ModelAttribute("user") User user,
							   @RequestParam(value="agreement",defaultValue="false") boolean agreement,
							   Model model,
							   HttpSession session)
	{
		try {
			
			if (!agreement)
			{
//				System.out.println("You have not agreed to the Terms and Conditions");
				throw new Exception("You have not agreed to the Terms and Conditions");
			}
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			
			System.out.println("Agreement"+agreement);
			System.out.println("User"+user);
			
			User result=userRepository.save(user);
			model.addAttribute("user", new User());
			session.setAttribute("message", new Message("Successfully Registered!!","alert-success"));
			
			System.out.println("USER"+user);
			return "signup";
			
		} catch (Exception e) {
			// TODO: handle exception
			user.setPassword(null);
			model.addAttribute("user",user);
			session.setAttribute("message",new Message("Something went wrong!!" + e.getMessage(),"alert-danger"));
			e.printStackTrace();
			return "signup";
		}

		
	}
}
