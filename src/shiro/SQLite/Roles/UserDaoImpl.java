package shiro.SQLite.Roles;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import model.Logger;
import shiro.SQLite.entity.User;

public class UserDaoImpl implements UserDao{

	protected Connection dataSource;
	protected String connection;
	public static final int USER_ROLE_ID = 3;
	public static final int MODERATOR_ROLE_ID = 2;
	public static final int ADMIN_ROLE_ID = 1;
	public static final String USER_ROLE = "User";
	public static final String MODERATOR_ROLE = "Moderator";
	public static final String ADMIN_ROLE = "Admin";
	
	public UserDaoImpl()
	{
		connection = "jdbc:sqlite:resource/db.sqlite";
	}
	
	@Override
	public void correlationRoles(Integer userId, Integer... roleIds) {
		if(roleIds == null || roleIds.length == 0){
			return;
		}
		String sql = "insert into sys_users_roles(user_id, role_id) values(?,?)";
		PreparedStatement statement = null;
		for(Integer roleId : roleIds)
			if(!exists(userId, roleId)) {
				try {
					dataSource = DriverManager.getConnection(connection);
					statement = dataSource.prepareStatement(sql);
					statement.setInt(1, userId);
					statement.setInt(2, roleId);
					
					statement.executeUpdate();
					
					
				} catch (SQLException e) {
					Logger.getInstance().write(e.getMessage(), Level.SEVERE);
				}
				try {
					dataSource.close();
				} catch (SQLException e) {
					Logger.getInstance().write(e.getMessage(), Level.SEVERE);
				}
				
			}
		
	}

	private boolean exists(Integer userId, Integer roleId) {
		String sql = "select * from sys_users_roles where user_id=? and role_id=?";
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		boolean answer = false;
		
		try {
			dataSource = DriverManager.getConnection(connection);
			statement = dataSource.prepareStatement(sql);
			statement.setInt(1, userId);
			statement.setInt(2, roleId);
			
			resultSet = statement.executeQuery();
			if(resultSet.next())
				answer = true;
		} catch (SQLException e) {
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);
		}
		try {
			dataSource.close();
		} catch (SQLException e) {
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);
		}
		return answer;
	}

	@Override
	public void uncorrelationRoles(Integer userId, Integer... roleIds) {
		if(roleIds == null || roleIds.length == 0){
			return;
		}
		String sql = "delete from sys_users_roles where user_id=? and role_id=?";
		PreparedStatement statement = null;
		for(Integer roleId : roleIds)
			if(exists(userId, roleId)) {
				try {
					dataSource = DriverManager.getConnection(connection);
					statement = dataSource.prepareStatement(sql);
					statement.setInt(1, userId);
					statement.setInt(2, roleId);
					
					statement.execute();
				} catch (SQLException e) {
					Logger.getInstance().write(e.getMessage(), Level.SEVERE);
				}
				try {
					dataSource.close();
				} catch (SQLException e) {
					Logger.getInstance().write(e.getMessage(), Level.SEVERE);
				}
				
			}
		
	}

	@Override
	public User findOne(Integer userId) {
		String sql = "select id, username, password, salt from tblusers where id=?";
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		User user = new User();
		try {
			dataSource = DriverManager.getConnection(connection);
			statement = dataSource.prepareStatement(sql);
			statement.setInt(1, userId);
				
			resultSet = statement.executeQuery();
			user.setId(userId);
			user.setPassword(resultSet.getString("password"));
			user.setUsername(resultSet.getString("username"));
			user.setSalt(resultSet.getString("salt"));

					
		} catch (SQLException e) {
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);
		}
		try {
			dataSource.close();
		} catch (SQLException e) {
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);
		}
		
		return user;
	}

	@Override
	public User findByUsername(String username) {
		String sql = "select id, username, password, salt from tblusers where username=?";
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		User user = new User();
		try {
			dataSource = DriverManager.getConnection(connection);
			statement = dataSource.prepareStatement(sql);
			statement.setString(1, username);
				
			resultSet = statement.executeQuery();
			user.setId(resultSet.getInt("id"));
			user.setPassword(resultSet.getString("password"));
			user.setUsername(username);
			user.setSalt(resultSet.getString("salt"));

					
		} catch (SQLException e) {
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);
		}
		try {
			dataSource.close();
		} catch (SQLException e) {
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);
		}
		
		return user;
	}

	@Override
	public Set<String> findRoles(String username) {
		String sql = "select role from tblusers as u, sys_roles as r,sys_users_roles as ur where u.username=? and u.id=ur.user_id and r.id=ur.role_id";
		PreparedStatement statement = null;
		HashSet<String> roles = new HashSet<String>();
		ResultSet resultSet = null;
		try {
			Class.forName("org.sqlite.JDBC");
			dataSource = DriverManager.getConnection(connection);
			statement = dataSource.prepareStatement(sql);
			statement.setString(1, username);
				
			resultSet = statement.executeQuery();

			while(resultSet.next())
			{
				roles.add(resultSet.getString("role"));
			}
					
		} catch (SQLException e) {
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);
		} catch (ClassNotFoundException e) {
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);;
		}
		try {
			dataSource.close();
		} catch (SQLException e) {
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);
		}
		return roles;
	}

	@Override
	public Set<String> findPermissions(String username) {
        String sql = "select permission from tblusers as u, sys_roles as r, sys_permissions as p, sys_users_roles as ur, sys_roles_permissions as rp where u.username=? and u.id=ur.user_id and r.id=ur.role_id and r.id=rp.role_id and p.id=rp.permission_id";
        PreparedStatement statement = null;
		HashSet<String> premissions = new HashSet<String>();
		ResultSet resultSet = null;
		try {
			dataSource = DriverManager.getConnection(connection);
			statement = dataSource.prepareStatement(sql);
			statement.setString(1, username);
				
			resultSet = statement.executeQuery();
			while(resultSet.next())
			{
				premissions.add(resultSet.getString("permission"));
			}
					
		} catch (SQLException e) {
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);
		}
		try {
			dataSource.close();
		} catch (SQLException e) {
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);
		}
		return premissions;
	}
	

}
