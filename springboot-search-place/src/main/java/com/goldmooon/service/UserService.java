package com.goldmooon.service;

import com.goldmooon.model.User;

public interface UserService
{
	void saveUser(User user, String[] roles);





	User findByUsername(String username);
}
