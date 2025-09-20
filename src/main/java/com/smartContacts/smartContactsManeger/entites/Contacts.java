package com.smartContacts.smartContactsManeger.entites;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "contacts")
public class Contacts {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int cid;
	private String name;
	private String nickName;
	private String work;
	private String email;
	private String imageURL;
	 @Column(length = 1000)
	private String description;
	private String phone;
	
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	 @JsonBackReference
	private User user;

	
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getWork() {
		return work;
	}
	public void setWork(String work) {
		this.work = work;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Contacts(int cid, String name, String nickName, String work, String email, String imageURL,
			String description, String phone) {
		super();
		this.cid = cid;
		this.name = name;
		this.nickName = nickName;
		this.work = work;
		this.email = email;
		this.imageURL = imageURL;
		this.description = description;
		this.phone = phone;
	}
	public Contacts() {
		super();
		// TODO Auto-generated constructor stub
	}
//	@Override
//	public String toString() {
//		return "Contacts [cid=" + cid + ", name=" + name + ", nickName=" + nickName + ", work=" + work + ", email="
//				+ email + ", imageURL=" + imageURL + ", description=" + description + ", phone=" + phone + ", user="
//				+ user + "]";
//	}
	
	
	

}
