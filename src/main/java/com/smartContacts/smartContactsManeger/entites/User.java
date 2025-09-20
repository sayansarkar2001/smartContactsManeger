package com.smartContacts.smartContactsManeger.entites;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity
@Table(name = "users")  // optional, but recommended to avoid reserved word issues
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;
    private String imageUrl;

    @Column(length = 500)
    private String about;

    private String role;

    private boolean enabled;
    
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Contacts> contacts = new ArrayList<>();

    

    // --- Getters and Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public List<Contacts> getContacts() {
  		return contacts;
  	}

  	public void setContacts(List<Contacts> contacts) {
  		this.contacts = contacts;
  	}
  	
  	
    // --- Constructors ---

  

	public User() {
        super();
    }

    public User(int id, String name, String email, String password, String imageUrl, String about, String role,
                boolean enabled) {
        super();
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.imageUrl = imageUrl;
        this.about = about;
        this.role = role;
        this.enabled = enabled;
    }

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", imageUrl="
				+ imageUrl + ", about=" + about + ", role=" + role + ", enabled=" + enabled + ", contacts=" + contacts
				+ "]";
	}

	

    // --- toString() ---

    
    
}
