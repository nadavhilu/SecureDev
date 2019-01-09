package shiro.SQLite.service;

import java.util.Set;

import shiro.SQLite.entity.User;

public interface UserService {
	
	public void correlationRoles(Integer userId, Integer... roleIds);
	
	public User findByUsername(String username);
	
	public Set<String> findRoles(String username);
	
	public Set<String> findPermissions(String username);
	

}
