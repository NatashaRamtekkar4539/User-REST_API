package com.cybage.emsys.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import com.ems.model.Booking;
import com.cybage.emsys.model.User;
import com.cybage.emsys.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	EmailService emailService;
	
	public String getNameById(int id) {
		return userRepository.findById(id).get().getName();
	}

	public User getUserById(int id) {
		try {
		return userRepository.findById(id).get();}
		catch(Exception e) {
			System.out.println("id with that user not found getUserById in UserService");
			return null;
		}
	}

	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	
	/**
	 * @return true if user get stored ,
	 * otherwise false
	 * */
	public boolean addUser(User user) {
		
		try {
			boolean flag = false;
			int id = userRepository.save(user).getId();
		System.out.println("id "+id);
		flag = true;
		emailService.sendVerificationEmail(user, "http://localhost:8089/user/activateAccount/"+id);
		return flag;}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("error while adding user");
			return false;
		}	
	}

	public boolean deleteUser(int id) {
		try {
		userRepository.delete(userRepository.findById(id).get());
		return true;}
		catch(Exception exception) {
			return false;
		}
	}
	
	
	public boolean updateStatus(int id,int status) {
		User user = userRepository.findById(id).get();
		user.setStatus(status);
		userRepository.save(user);
		return true;
	}
	
	public boolean updateLoginAttempts(int id,int attempts) {
		User user = userRepository.findById(id).get();
		user.setLoginAttempts(attempts);
		userRepository.save(user);
		return true;
	}
	
	public boolean updateOTP(int id,Integer otp) {
		User user = userRepository.findById(id).get();
		user.setOtp(otp);
		userRepository.save(user);
		return true;
	}
	/**
	 * *
	 * 
	 * @param id   : int
	 * @param user : User
	 * @return User updated object else Generate error if user does not find
	 */
	public User updateUserById(int id, User user) {
		User user1 = userRepository.findById(id).get();
		userRepository.save(user);

		if (user.getName() != null)
			user1.setName(user.getName());

		if (user.getMobile_number() != null)
			user1.setMobile_number(user.getMobile_number());

		if (user.getPassword() != null)
			user1.setPassword(user.getPassword());

		if (user.getEmail() != null)
			user1.setEmail(user.getEmail());

		if (user.getRole() != null)
			user1.setRole(user.getRole());

		if (user.getLoginAttempts() != -1)
			user1.setLoginAttempts(user.getLoginAttempts());

		if (user.getStatus() != -1)
			user1.setStatus(user.getStatus());

		return userRepository.save(user1);
	}

	/**
	 * returns bookings done by user of role "user"
	 */

	/**
	 * returns the list of all users
	 */
	public List<User> getAllUsers() {
		return userRepository.findAll();

	}

	/**
	 * return the list of user by their role
	 */
	public List<User> getUsersByRole(String role) {
		return userRepository.findByRole(role);
	}

	public boolean isPasswordCorrect(User user) {
		return this.getUserByEmail(user.getEmail()).getPassword().equals(user.getPassword());
		
	}

}