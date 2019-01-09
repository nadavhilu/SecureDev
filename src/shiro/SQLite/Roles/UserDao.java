package shiro.SQLite.Roles;

import java.util.Set;

import shiro.SQLite.entity.User;

public interface UserDao {

	public void correlationRoles(Integer userId, Integer... roleIds);
	public void uncorrelationRoles(Integer userId, Integer... roleIds);

	User findOne(Integer userId);

	User findByUsername(String username);

	Set<String> findRoles(String username);

	Set<String> findPermissions(String username);


}
