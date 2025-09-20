package com.smartContacts.smartContactsManeger.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


@Service
public class sendResetEmailService {
	
	@Autowired
	private JavaMailSender mailSender;

	public String sendResetPasswordOTPEmail(String email) throws MessagingException {
	    Random random = new Random();
//	    SimpleMailMessage message = new SimpleMailMessage();
	    String otp = "";
	    int randomNumber = random.nextInt(9000) + 1000;
	     otp = String.valueOf(randomNumber);
	    
//	    message.setTo(email);
//	    message.setSubject("Password Reset Request");
//	    message.setText("Your OTP to reset your password: " + otp);
	    
	    String emailMsg = """
	            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto; border: 1px solid #eee; border-radius: 10px; padding: 20px; background-color: #f9f9f9;">
	                <h2 style="color: #007bff; text-align: center;">Smart Contacts Manager</h2>
	                <p style="font-size: 16px; color: #333;">Hello,</p>
	                <p style="font-size: 16px; color: #333;">
	                    We received a request to reset your password. Please use the OTP below to complete the process:
	                </p>
	                 <div style="text-align: center; margin: 20px 0;">
	                    <span style="font-size: 28px; font-weight: bold; color: #007bff; letter-spacing: 4px;">%s</span>
	                </div>
	                 <p style="font-size: 14px; color: #555;">
	                    This OTP is valid for <strong>10 minutes</strong>. If you did not request this, please ignore this email.
	                </p>
	                <hr style="border: none; border-top: 1px solid #ddd; margin: 20px 0;">
	                
	                <p style="font-size: 12px; color: #888; text-align: center;">
	                    © 2025 Smart Contacts Manager | Keep your contacts safe 💙
	                </p>
	            </div>
	        """.formatted(otp);
	    
	    MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(email);
        helper.setSubject("🔑 Your SmartContacts OTP Code");
        helper.setText(emailMsg, true); // 👈 true = HTML content
	    
	    
	    mailSender.send(message);
	    
	    return otp;
	}

}
