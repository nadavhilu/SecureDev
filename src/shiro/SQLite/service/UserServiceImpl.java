package shiro.SQLite.service;

import java.util.Set;

import shiro.SQLite.Roles.UserDao;
import shiro.SQLite.Roles.UserDaoImpl;
import shiro.SQLite.entity.User;

public class UserServiceImpl implements UserService{
	
	private UserDao userDao = new UserDaoImpl();
//	private PasswordHelper passwordHelper = new PasswordHelper();
	
	@Override
	public void correlationRoles(Integer userId, Integer... roleIds){
        userDao.correlationRoles(userId, roleIds);
    }
	
/*	public void uncorrelationRoles(Long userId, Long... roleIds) {
        userDao.uncorrelationRoles(userId, roleIds);
    }*/

	public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }
	public Set<String> findRoles(String username) {
        return userDao.findRoles(username);
    }

	public Set<String> findPermissions(String username) {
        return userDao.findPermissions(username);
    }


}
