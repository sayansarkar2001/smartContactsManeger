package com.smartContacts.smartContactsManeger.controller;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.*;
import com.smartContacts.smartContactsManeger.dao.OrdersRepo;
import com.smartContacts.smartContactsManeger.dao.UserRepository;
import com.smartContacts.smartContactsManeger.entites.MyOrder;
import com.smartContacts.smartContactsManeger.entites.User;


@RestController
public class PaymentController {

	@Autowired
	OrdersRepo ordersRepo;
	@Autowired
	UserRepository userRepository;
	
	@PostMapping("/createOrder")
    @ResponseBody
    public String createOrder(@RequestBody Map<String, Object> paymentData, @AuthenticationPrincipal UserDetails userDetails) throws RazorpayException {
		System.out.println	("----------------------------------->>>>createOrder" + paymentData);	
//		System.out.println	("----------------------------------->>>>createOrder");	
		
		// Use your Razorpay test keys
        RazorpayClient client = new RazorpayClient("rzp_test_RJm6KmTkomgmDR", "tEYDm2NYKog648VvU5YOu45t");

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", Integer.parseInt(paymentData.get("amount").toString()) * 100); 
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "OD"+System.currentTimeMillis());

        Order order = client.orders.create(orderRequest);
        
		System.out.println	("----------------------------------->>>>rayzerpay response " + orderRequest);	

		System.out.println	("----------------------------------->>>>rayzerpay response " + order);	
		
		
		//Insert into orders table
		MyOrder myOrder =new MyOrder();
		
		Double ammount_in_rs = (Integer)order.get("amount") / 100.00;
		myOrder.setAmount( ammount_in_rs.toString());
		myOrder.setReceipt(order.get("receipt"));
		myOrder.setRazorpayOrderId(order.get("id"));
		myOrder.setCurrency(order.get("currency"));
		myOrder.setStatus(order.get("status"));
		myOrder.setMonthsOfPlan(paymentData.get("monthsOfPlan").toString());
		
		String useremail = userDetails.getUsername();
		User user = userRepository.findByEmail(useremail);
		myOrder.setUser(user);
		
		ordersRepo.save(myOrder);

        return order.toString(); // Send order details to frontend
		
		
	}
	
	@PostMapping("/verifyPayment")
    @ResponseBody
	public Boolean verifyPayment(@RequestBody Map<String, Object> verifyPaymentdata){
		System.out.println("verifyPaymentdata : "+verifyPaymentdata);
		
		 try {
		        JSONObject options = new JSONObject();
		        options.put("razorpay_order_id", verifyPaymentdata.get("razorpay_order_id"));
		        options.put("razorpay_payment_id", verifyPaymentdata.get("razorpay_payment_id"));
		        options.put("razorpay_signature", verifyPaymentdata.get("razorpay_signature"));

		        Utils.verifyPaymentSignature(options, "tEYDm2NYKog648VvU5YOu45t");
		        
		       MyOrder 	currOrder =  ordersRepo.findByRazorpayOrderId(verifyPaymentdata.get("razorpay_order_id").toString());
		       currOrder.setRazorpayPaymentId(verifyPaymentdata.get("razorpay_payment_id").toString());
		       currOrder.setRazorpaySignature(verifyPaymentdata.get("razorpay_signature").toString());
		       currOrder.setStatus("Paid");
		       
		       ordersRepo.save(currOrder);//save order
		       
		        return true;
		    } 
		 catch (Exception e) {
		        e.printStackTrace();
		        return false;
		    }
		
	}
}
