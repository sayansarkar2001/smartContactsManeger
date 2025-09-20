package com.smartContacts.smartContactsManeger.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartContacts.smartContactsManeger.entites.MyOrder;

public interface OrdersRepo extends JpaRepository<MyOrder, Long> {
	
	// Optional: Add custom queries if needed
    MyOrder findByReceipt(String receipt);
    MyOrder findByRazorpayOrderId(String razorpay_order_id);

}
