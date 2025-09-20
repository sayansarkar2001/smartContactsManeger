package com.smartContacts.smartContactsManeger.helper;

public class Message {
	
	public String message;
	public String type;
	public Message(String message, String type) {
		super();
		this.message = message;
		this.type = type;
	}
	public Message() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
	

}
