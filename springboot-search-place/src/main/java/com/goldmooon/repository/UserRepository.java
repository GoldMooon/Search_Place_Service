package com.goldmooon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goldmooon.model.User;

public interface UserRepository extends JpaRepository<User, Long>
{
	public User findByUsername(String username);

}
