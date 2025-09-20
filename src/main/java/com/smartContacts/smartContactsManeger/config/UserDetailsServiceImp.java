package com.smartContacts.smartContactsManeger.config;

//import java.util.Collections;


import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.smartContacts.smartContactsManeger.dao.UserRepository;
import com.smartContacts.smartContactsManeger.entites.User;


@Service
public class UserDetailsServiceImp implements UserDetailsService {
	
		@Autowired
	   private UserRepository repo;


	    
	    @Override
	    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//	        User user = repo.findByUsername(username);ssssssssss
	    	System.out.println(email);
	    	System.out.println("DB checking...");
	    	User user = repo.findByEmail(email);
	    	System.out.println("DB cheched... ");
//	    	System.out.println("User : " + user.getEmail());
	    	System.out.println("working...............");
	        if (user == null) {
	            throw new UsernameNotFoundException("User not found: " + email);
	        }
	    	System.out.println("password : " + user.getPassword());


	        return new org.springframework.security.core.userdetails.User(
	                user.getEmail(),
	                user.getPassword(),
	                Collections.singleton(new SimpleGrantedAuthority( user.getRole())));
	    }

}
