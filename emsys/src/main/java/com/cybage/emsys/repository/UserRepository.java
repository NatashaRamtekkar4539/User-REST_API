package com.cybage.emsys.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cybage.emsys.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	List<User> findByRole(String role);

	User findByEmail(String email);

}