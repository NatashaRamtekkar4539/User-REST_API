package com.cybage.emsys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cybage.emsys.model.User;
import com.cybage.emsys.service.EmailService;
import com.cybage.emsys.service.UserService;
import com.cybage.emsys.utility.UserUtility;

import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService service;
	
	@Autowired 
    EmailService emailService;
	
	@GetMapping("/getAllUsers")
	public ResponseEntity<List<User>> getAllUsers(){
		
		return new ResponseEntity<List<User>>(service.getAllUsers(), HttpStatus.OK);
		
	}
	
	@PostMapping("/addUser")
	public ResponseEntity<String> addUser(@Valid @RequestBody User user){
		if(service.getUserByEmail(user.getEmail())!=null) {
			return new ResponseEntity<String>("user exists already",HttpStatus.OK);
		}
		
		if(service.addUser(user))
		return new ResponseEntity<String>("added successfully",HttpStatus.CREATED);
		
		else
			return new ResponseEntity<String>("could not add user",HttpStatus.OK);	
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody User user){
		String message = "Something went wrong";
		User storedUser = service.getUserById(user.getId());
		storedUser = service.getUserByEmail(user.getEmail());
		
		if(storedUser==null)
			message = UserUtility.USER_NOT_FOUND;
		
		else if(storedUser.getLoginAttempts()==3)
			message = UserUtility.ACCOUNT_BLOCKED+"due to 3 invalid attempts";
		
		else if(service.isPasswordCorrect(user) && storedUser.getRole().equals(user.getRole())) {
			message="login successfull "+UserUtility.OTP_SENT;
			Integer otp = UserUtility.generateOTP();
			String otpString  = otp.toString();
			emailService.sendOTP(user, otpString);
			service.updateOTP(user.getId(), otp);
			service.updateLoginAttempts(storedUser.getId(), storedUser.getLoginAttempts()+1);
		}
		else {
			message = UserUtility.WRONG_PASSWORD;
			service.updateLoginAttempts(storedUser.getId(), storedUser.getLoginAttempts()+1);
			
		}
			return new ResponseEntity<String>(message,HttpStatus.OK);
		
	}
	
	@PostMapping("/otp/{email}/{otp}")
	public ResponseEntity<String> verifyOTP(@PathVariable String email,@PathVariable Integer otp){
		if(service.getUserByEmail(email).getOtp()==otp) {
			return new ResponseEntity<String>("login with otp successfull",HttpStatus.OK);			
		}
		System.out.println(service.getUserByEmail(email).getOtp()+" "+otp);
		return new ResponseEntity<String>("Invalid OTP",HttpStatus.OK);
	}
	
	@GetMapping("/activateAccount/{id}")
	public ResponseEntity<String> activateAccount(@PathVariable Integer id){
		
		service.updateStatus(id, 1);
		return new ResponseEntity<String>("account has been activated",HttpStatus.OK);
	}
	
	@PutMapping("/updateUser/{id}")
	public ResponseEntity<String> updateUser(@PathVariable Integer id,@RequestBody User user){
		if(service.updateUserById(id,user)!=null)
		return new ResponseEntity<String>("updated",HttpStatus.OK);
		else
			return new ResponseEntity<String>("not updated",HttpStatus.NOT_FOUND);	
	}
	
	@DeleteMapping("/deleteUser/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable Integer id){
		if(service.deleteUser(id)) 
			return new ResponseEntity<String>("deleted successfully",HttpStatus.OK);
		return new ResponseEntity<String>("could not delete",HttpStatus.OK);
	}

}