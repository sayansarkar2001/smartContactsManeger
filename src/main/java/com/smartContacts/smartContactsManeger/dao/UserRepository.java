package com.smartContacts.smartContactsManeger.dao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartContacts.smartContactsManeger.entites.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
//	User findByUsername(String username);

	User findByEmail(String username);
	

	
}



