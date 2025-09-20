package com.smartContacts.smartContactsManeger.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.smartContacts.smartContactsManeger.dao.ContactRepository;
import com.smartContacts.smartContactsManeger.dao.UserRepository;
import com.smartContacts.smartContactsManeger.entites.Contacts;
import com.smartContacts.smartContactsManeger.entites.User;
import com.smartContacts.smartContactsManeger.helper.Message;

import jakarta.servlet.http.HttpSession;

import java.nio.file.Path;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	ContactRepository contactRepository;
	
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@ModelAttribute
	public void addCommonData(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		String email = userDetails.getUsername();
		System.out.println("Email:  " + email);
		User user = userRepository.findByEmail(email);
		System.out.println("User:  " + user);

		model.addAttribute("user", user);
	}

	@RequestMapping("/userDashboard")
	public String User_dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
		System.out.println("User_dashboard working...");

		return "normal/user_dashboard";
	}

	@RequestMapping("/addContact")
	public String addContactHandeler(@AuthenticationPrincipal UserDetails userDetails, Model model,
			HttpSession session) {
		System.out.println("addContact wrking...");

		Message message = (Message) session.getAttribute("message");

		// ✅ success message

		if (message != null) {
			model.addAttribute("message", message);
			session.removeAttribute("message"); // clear after showing once
		}

		return "normal/add_contact_page";
	}

	@PostMapping("/process-contact")
	public String processContactHeandeler(@ModelAttribute("contacts") Contacts contact,
			@AuthenticationPrincipal UserDetails userDetails, @RequestParam("image") MultipartFile file,
			HttpSession session) throws IOException {

		Random rand = new Random();
		// Get current date and time
		LocalDateTime now = LocalDateTime.now();

		// Format it (e.g. 2025-08-23_14-35-59)
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");// yyyyMMdd
		String formattedDateTime = now.format(formatter);
		System.out.println("process-contact wrking...");

/////////////////////////////////////////  Handel img
		String originalFilename = file.getOriginalFilename(); // example: "mypic.jpeg"
		String extension = "";
		// Handle null safely
		if (originalFilename != null && originalFilename.contains(".")) {
			// substring from the last dot → ".jpeg"
			extension = originalFilename.substring(originalFilename.lastIndexOf("."));
			System.out.println("Extension: " + extension);
		}

		if (!file.isEmpty()) {
			// Get file name
//	        String fileName = file.getOriginalFilename();
			String fileName = "SCM_ContImg_" + formattedDateTime + "_" + rand.nextInt(2147483000) + extension;// new
																												// file
																												// name
																												// generation

			// Define upload folder (absolute path)
			String uploadDir = "F:/Users/Documents/Spring/SpringBoot/uploads/contactsProfilesImgs";

			File saveDir = new File(uploadDir);
			if (!saveDir.exists()) {
				saveDir.mkdirs(); // create folder if not exists
			}

			Path path = (Path) Paths.get(saveDir.getAbsolutePath() + File.separator + fileName);

			Files.copy(file.getInputStream(), path, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

//	        contact.setImage(fileName);
			contact.setImageURL(fileName);
		} else {
			contact.setImageURL("default.jpg"); // fallback
		}

		///////////////////////////////////////// DB
		try {

			String email = userDetails.getUsername();
			User user = userRepository.findByEmail(email);
			user.getContacts().add(contact);
			contact.setUser(user);
			userRepository.save(user);
//		contact.setUser(user);
			System.out.println("data: " + contact);

			session.setAttribute("message", new Message("Contact has been successfully saved...!!! ", "alert-success"));
		} catch (Exception e) {
			session.setAttribute("message", new Message("Samething went wrong, " + e.getMessage(), "alert-error"));
			e.printStackTrace();
		}

//		contactsRepository.save(contact);

		return "redirect:/user/addContact";
	}
	
	
	
	

	@GetMapping("/show-contact/{page}")
	public String showContacts(@PathVariable("page") int page, @AuthenticationPrincipal UserDetails userDetails,
			Model model, RedirectAttributes redirectAttributes,HttpSession session) {

		int pageSize = 10; // contacts per page

		System.out.println("show-contact wrking...");
		

		String userEmail = userDetails.getUsername();
		User user = userRepository.findByEmail(userEmail);

		List<Contacts> contacts = contactRepository.findByUser_Id(user.getId());
		Pageable pageable = PageRequest.of(page, pageSize);
		Page<Contacts> contactPage = contactRepository.findByUser_Id(user.getId(), pageable);

		System.out.println("Contacts:------------------ " + contacts);
		redirectAttributes.addFlashAttribute("toastMessage", "Contact added successfully!");

		model.addAttribute("contacts", contactPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contactPage.getTotalPages());

		// Tosty
		if (contactPage.getContent().isEmpty()) {
			model.addAttribute("toastMessage", "No contacts found!");
			model.addAttribute("toastType", "info"); // success | danger | warning | info

		} else {
			model.addAttribute("toastMessage", contactPage.getContent().size() + " contacts loaded successfully!");
			model.addAttribute("toastType", "success"); // example
			// success | danger | warning | info

		}

		return "normal/show_contact";

	}

	@GetMapping("/show-contact/contactDetails/{cId}")
	public String contactDetails(@PathVariable("cId") int cId, @AuthenticationPrincipal UserDetails userDetails,
			Model model) {
		
		System.out.println("cId-------------->>>>>>>>>>" + cId);
		Optional<Contacts> contactOps = contactRepository.findById(cId);
		Contacts contact = contactOps.get();
		
		String userEmail = userDetails.getUsername();
		User currUser = userRepository.findByEmail(userEmail);
		if(currUser.equals(contact.getUser())) {
			model.addAttribute("contact",contact);
		}
		
		return "normal/contactDetails";
	}
	
	
	@GetMapping("/show-contact/deleteContact/{cId}")
	public String deleteContact(@PathVariable("cId") int cId, @AuthenticationPrincipal UserDetails userDetails,
			Model model,HttpSession Session) {
		
		System.out.println("cId-------------->>>>>>>>>>" + cId);
		Optional<Contacts> contactOps = contactRepository.findById(cId);
		Contacts contact = contactOps.get();
		System.out.println("Contact----------> "+contact.getCid());
		String userEmail = userDetails.getUsername();
		User currUser = userRepository.findByEmail(userEmail);
		
		
		if(currUser.equals(contact.getUser())) {
 
			System.out.println("is Same user : "+currUser.equals(contact.getUser()));

			
			currUser.getContacts().removeIf(c -> c.getCid() == cId);
			
			userRepository.save(currUser);

//			Session.setAttribute("message", new Message("Contact deleted successfully!","success"));

			
			
			//delete contact img from folder
			String uploadDir = "F:/Users/Documents/Spring/SpringBoot/uploads/contactsProfilesImgs/";
			String fileName="";
			 boolean deleted =false;
			fileName = contact.getImageURL();
			Path path = Paths.get(uploadDir + fileName);
			 try {
				 if(!fileName.equalsIgnoreCase("default.jpg")){
					  deleted = Files.deleteIfExists(path);
				 }
		            
		            if(deleted) {
		                System.out.println( "File deleted successfully: " + fileName);
		            } else {
		            	 System.out.println( "File not found: " + fileName);
		            }
		        } catch (IOException e) {
		            e.printStackTrace();
		            System.out.println( "Failed to delete file: " + fileName + e.getMessage());
		        }

		}
		
		return "redirect:/user/show-contact/0";

	}
	
	@PostMapping("/show-contact/updateContact/{cid}")
	public String updateContact(@PathVariable("cid") int cid,Model model 
            ){
		
		System.out.println("/show-contact/updateContact working....................");
		
//		String userEmail = userDetails.getUsername();
//		User user = userRepository.findByEmail(userEmail);
		
		Optional<Contacts> contactOpt = contactRepository.findById(cid);
		Contacts contact = contactOpt.get();
		
		model.addAttribute("contacts",contact);
		
		
		
		return "normal/updateContact";
	}
	
//	process-Updatecontact
	@PostMapping("/process-Updatecontact")
	public String process_Updatecontact(@ModelAttribute Contacts contact,@RequestParam("image") MultipartFile file,Model model, 
            @AuthenticationPrincipal UserDetails userDetails,@RequestParam(value = "page", defaultValue = "0") int page){
		
		LocalDateTime now = LocalDateTime.now();
		// Format it (e.g. 2025-08-23_14-35-59)
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");// yyyyMMdd
		String formattedDateTime = now.format(formatter);
	System.out.println("/show-contact/process-Updatecontact.....................................");
	String userEmail = userDetails.getUsername();
	User user = userRepository.findByEmail(userEmail);
	
	contact.setUser(user);
	
	  Optional<Contacts> oldContactOpt = contactRepository.findById(contact.getCid());
	  Contacts oldContact = oldContactOpt.get();
	
	  
	// Define upload folder (absolute path)
				String path = "F:/Users/Documents/Spring/SpringBoot/uploads/contactsProfilesImgs/";
	  // If user uploads new image
    if (!file.isEmpty()) {
        // Delete old image from "uploads" folder
        if (oldContact.getImageURL() != null && !oldContact.getImageURL().equalsIgnoreCase("default.jpg")) {
            File deleteFile = new File(path + oldContact.getImageURL());
            if (deleteFile.exists()) {
                deleteFile.delete();
            }
        }
        
        String originalFilename = file.getOriginalFilename(); // example: "mypic.jpeg"
		String extension = "";
		// Handle null safely
		if (originalFilename != null && originalFilename.contains(".")) {
			// substring from the last dot → ".jpeg"
			extension = originalFilename.substring(originalFilename.lastIndexOf("."));
			System.out.println("Extension: " + extension);
		}
        // Save new file
//        String fileName = file.getOriginalFilename();
        String fileName="";
         fileName = "SCM_ContImg_" + formattedDateTime + "_" + oldContact.getCid() + extension;
        
        Path newPath = Paths.get(path + fileName);
        try {
			Files.copy(file.getInputStream(), newPath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        contact.setImageURL(fileName);
    } else {
        // Keep old image if no new upload
        contact.setImageURL(oldContact.getImageURL());
    }
    
	
	
	contactRepository.save(contact);
	System.out.println("Updated.....................................");
	
		model.addAttribute("contact",contact);
		
		return "redirect:/user/show-contact/contactDetails/" + contact.getCid() + "?page=" + page;

	}
	
	@RequestMapping("/profile")
	public String profileHendaler(@AuthenticationPrincipal UserDetails userDetails,
			Model model){
		String userEmail = userDetails.getUsername();
		User user = userRepository.findByEmail(userEmail);
		System.out.println(user);
		model.addAttribute("user",user);
		
		
		
		return "normal/profile_page";
	}
	
	
	@PostMapping("/searchContacts")
	@ResponseBody
	public List<Contacts> searchContacts(@RequestBody Map<String, String> body,@AuthenticationPrincipal UserDetails userDetails,
			Model model){
		String userEmail = userDetails.getUsername();
		User user = userRepository.findByEmail(userEmail);
		System.out.println(user);
		String keyword = body.get("keyword");
		System.out.println(keyword);
		
		
		List<Contacts> contacts = contactRepository.findByUserIdAndNameContainingIgnoreCase(user.getId(), keyword);
		System.out.println(contacts.size());
		return contacts ;
	}
	
	
	@RequestMapping("/settings")
	public String settingsHendaler(@AuthenticationPrincipal UserDetails userDetails,
			Model model){
		
		
		
		
		return "normal/settings_page";
	}
	
	
	
	@PostMapping("/doChangePassword")
	public String doChangePassword(@RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,Model model, @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes){
		System.out.println("doChangePassword..........................");
		String email = userDetails.getUsername();
		
		
		User user = userRepository.findByEmail(email);
		String password = user.getPassword();
		System.out.println("password: "+ password);
		
		model.asMap().remove("success");
		model.asMap().remove("error");
		if(passwordEncoder.matches(currentPassword, password) && newPassword.equals(confirmPassword) ) {
			user.setPassword(passwordEncoder.encode(newPassword));
			userRepository.save(user);
			redirectAttributes.addFlashAttribute("success", "Password updated successfully!");  // ✅ Success message

			System.out.println("pw updated..........................");
		}else {
			redirectAttributes.addFlashAttribute("error", "Current password is incorrect or new passwords do not match");  // ❌ Error message

		}
		
//		return "normal/settings_page";
		return "redirect:settings";

	}
	
	@RequestMapping("/getPremium")
	public String getPremium() {
		
		
		return "/normal/getPremium";
	}
	
}
