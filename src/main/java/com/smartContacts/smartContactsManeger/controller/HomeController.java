package com.smartContacts.smartContactsManeger.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.smartContacts.smartContactsManeger.dao.UserRepository;
import com.smartContacts.smartContactsManeger.entites.User;
import com.smartContacts.smartContactsManeger.helper.Message;
import com.smartContacts.smartContactsManeger.services.sendResetEmailService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;



@Controller
public class HomeController {
	
	@Autowired
	UserRepository userRepository ;
	@Autowired
	sendResetEmailService sendResetEmailService;
	

	@RequestMapping("/")
//	@ResponseBody
	public String home(Model model){
		System.out.println("/addUser is working...");
		
		
//		 	User user = new User();
//	        user.setName("Sayan Sarkar");
//	        user.setPassword("Bony2001");
//	        user.setEmail("sayan@gmail.com");
//	        user.setAbout("Test user from controller");
//	        user.setRole("USER");
//	        user.setEnabled(true);
//	        
//	        contactsRepository.save(user);
		
		model.addAttribute("title","Home Page");
		 model.addAttribute("activePage", "Home");
		
		return "home";
	}
	
	@RequestMapping("/about")
	public String about(Model model){
		System.out.println("/addUser is working...");
		

		
		model.addAttribute("title","About Page");
		 model.addAttribute("activePage", "About");
		
		return "about";
	}
	
	@RequestMapping("/register")
	public String singUp(Model model,HttpSession session){
//		System.out.println("/addUser is working...");
		

		
		model.addAttribute("title","Register");
		model.addAttribute("user",new User());

		session.removeAttribute("message");
		 model.addAttribute("activePage", "SignUp");
		return "singUp";
	}
	
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
//	do_register
	@PostMapping("/do_register" )
	public String registerHeandeler(@ModelAttribute User user, @RequestParam(defaultValue = "false") Boolean agreement, Model model,HttpSession session){
		
		
		
		System.out.println(agreement);
		System.out.println(user);
		
		try {
		if(!agreement) {
			System.out.println("You have not agreed the T&C.");
			model.addAttribute("user",user);
			model.addAttribute("TandC","*You have not agreed the T&C.");
			throw new Exception("You have not agreed the T&C");
			
//			return "singUp";
		}
		user.setRole("USER");
		user.setEnabled(true);
		user.setImageUrl("default.png");
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		User result = userRepository.save(user);
		
		}catch(Exception e){
			e.printStackTrace();
			session.setAttribute("message", new Message("Samething went wrong, "+e.getMessage(),"alert-error"));
//			System.out.println(session.message);
			return "singUp";
			
		}
		
		
		

		model.addAttribute("user",new User());
		session.setAttribute("message", new Message("You have successfully registered...! ","alert-success"));
		Message msg = (Message) session.getAttribute("message");
		System.out.println(msg.getMessage());
		return "singUp";
		//		return "singUp";
	}
	
	
	@GetMapping("/login")
	public String login(){
		
		
		return "login";
	}
	
	@GetMapping("/resetPassword")
	public String resetPassword(){
		
		
		return "resetPasswordPage";
	}
	@PostMapping("/do_resetPassword")
	public String resetPasswordHendeler(@RequestParam String email,
			RedirectAttributes redirectAttributes,HttpSession session){
		
		String sentOtp = "";
		
		User user = userRepository.findByEmail(email);
		if(user==null) {
			redirectAttributes.addFlashAttribute("messageError", true);
			redirectAttributes.addFlashAttribute("message", "Your email is not registered !!!");
			
			return "redirect:/register";
		}
		session.setAttribute("user", user);
		try {
			sentOtp = sendResetEmailService.sendResetPasswordOTPEmail(email);System.out.println("Sent OTP: " + sentOtp);
		    session.setAttribute("sentOtp", sentOtp);
			redirectAttributes.addFlashAttribute("otpstatusTrue", "true");
			redirectAttributes.addFlashAttribute("otpmessage", "OTP sent successfully!");
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("otpstatusFalse", "true");
			redirectAttributes.addFlashAttribute("otpmessage", "Unable to send OTP !!!");
			return "redirect:/resetPassword";
		}
		
		
		
		
		
		return "redirect:/resetPassword";
	}
	
	

	@PostMapping("/otpverify")
	public String otpverify(@RequestParam String otp,
			RedirectAttributes redirectAttributes,HttpSession session){
		String sentOtp = (String) session.getAttribute("sentOtp");
		
		
		if(otp!=null && !(otp.isEmpty())) {
			 otp = otp.replaceAll("\\s+", "");  // Removes all whitespace
			 System.out.println(otp);
			 
			 if(otp.equals(sentOtp)) {
				 
				 redirectAttributes.addFlashAttribute("newpasswordform", true);
				 return "redirect:/resetPassword"; 
			 }
		}
		
		System.out.println("Invalid OTP !!!");
		redirectAttributes.addFlashAttribute("otpstatusFalse", "true");
		redirectAttributes.addFlashAttribute("otpmessage", "Invalid OTP !!!");
		 return "redirect:/resetPassword";
	}
	
	
	@PostMapping("/newpasswordsave")
	public String saveNewpassword(@RequestParam String newPassword,@RequestParam String confirmNewPassword,RedirectAttributes redirectAttributes,HttpSession session){
		
		if(!newPassword.equals(confirmNewPassword)) {
			 redirectAttributes.addFlashAttribute("newpasswordform", true);
			 redirectAttributes.addFlashAttribute("otpstatusFalse", "true");
				redirectAttributes.addFlashAttribute("otpmessage", "New Password and Confirm New Password should be same");
				 return "redirect:/resetPassword";
		}
		
		User user = (User) session.getAttribute("user");
		if(user!=null) {
			
//			user.setPassword(newPassword);
			String encodedPassword = passwordEncoder.encode(newPassword);
			user.setPassword(encodedPassword);
			userRepository.save(user);
			redirectAttributes.addFlashAttribute("message", "New Password has been changed successfully!");
			return "redirect:/login";
		}
		
		
		
		return "redirect:/resetPassword";
	}
}
