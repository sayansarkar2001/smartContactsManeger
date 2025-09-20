package com.smartContacts.smartContactsManeger.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartContacts.smartContactsManeger.entites.Contacts;
import com.smartContacts.smartContactsManeger.entites.User;

import java.util.List;

import org.springframework.data.domain.Page;


@Repository
public interface ContactRepository extends JpaRepository<Contacts, Integer> {

	
	// Find all contacts for a given user id
    List<Contacts> findByUser_Id(Integer userId);
    
    Page<Contacts> findByUser_Id(Integer userId, Pageable pageable);
    
 // Custom finder
    List<Contacts> findByUserIdAndNameContainingIgnoreCase(Integer userId,String name);
	
//    // Fetch all contacts belonging to a specific user
//    List<Contacts> findByUserId( int userId);

//    // Search contacts by name for a given user
//    List<Contact> findByNameContainingAndUser(String name, User user);
//
//    // Search contacts by email for a given user
//    List<Contact> findByEmailContainingAndUser(String email, User user);
//
//    // Search contacts by phone for a given user
//    List<Contact> findByPhoneContainingAndUser(String phone, User user);
}
