package com.smartContacts.smartContactsManeger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.smartContacts.smartContactsManeger")
public class SmartContactsManegerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartContactsManegerApplication.class, args);
	}

}
