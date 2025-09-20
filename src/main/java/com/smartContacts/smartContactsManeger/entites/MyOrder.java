package com.smartContacts.smartContactsManeger.entites;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class MyOrder {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;
    
    private String amount; // in paise
    private String currency;
    private String receipt;
    
    private String status; // CREATED, PAID, FAILED
    private String monthsOfPlan;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // assuming you already have User entity

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRazorpayOrderId() {
		return razorpayOrderId;
	}

	public void setRazorpayOrderId(String razorpayOrderId) {
		this.razorpayOrderId = razorpayOrderId;
	}

	public String getRazorpayPaymentId() {
		return razorpayPaymentId;
	}

	public void setRazorpayPaymentId(String razorpayPaymentId) {
		this.razorpayPaymentId = razorpayPaymentId;
	}

	public String getRazorpaySignature() {
		return razorpaySignature;
	}

	public void setRazorpaySignature(String razorpaySignature) {
		this.razorpaySignature = razorpaySignature;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getReceipt() {
		return receipt;
	}

	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMonthsOfPlan() {
		return monthsOfPlan;
	}

	public void setMonthsOfPlan(String monthsOfPlan) {
		this.monthsOfPlan = monthsOfPlan;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public MyOrder() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
    
	
}
