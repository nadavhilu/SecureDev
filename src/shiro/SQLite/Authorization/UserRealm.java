package shiro.SQLite.Authorization;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SaltedAuthenticationInfo;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.SimpleByteSource;

import model.Logger;
import shiro.SQLite.service.UserService;
import shiro.SQLite.service.UserServiceImpl;

public class UserRealm extends AuthorizingRealm {

	private UserService userService = new UserServiceImpl();
	private final static int waitingTime = 15;

	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String username = (String)principals.getPrimaryPrincipal();
		
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		authorizationInfo.setRoles(userService.findRoles(username));
		authorizationInfo.setStringPermissions(userService.findPermissions(username));
		
		return authorizationInfo;
	}

	
	/*
	 * 
	 * 
	 *  Authentication Realm Implementation
	 * 
	 */
	protected Connection dataSource;
	protected String connection;
	protected static HashedCredentialsMatcher cm = new HashedCredentialsMatcher(Sha256Hash.ALGORITHM_NAME);
	public UserRealm() {
		super(cm);
		connection = "jdbc:sqlite:resource/db.sqlite";
		
	}

/*
	public void setJndiDataSourceName() {
		
		 default connection
		 
		connection = "jdbc:sqlite:resource/db.sqlite";
	}*/
	
	public void setConnection(String connection)
	{
		this.connection = connection;
	}

	

	@Override
	protected SaltedAuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//identify account to log to
		UsernamePasswordToken userPassToken = (UsernamePasswordToken) token;
		String username = userPassToken.getUsername();
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		if(!isLocked(session))
		{
	
			if (username == null) {
			/*
			 * 	log.debug("Username is null.");
			 */
				unauthorizedAccessTry(session);
				return null;
			}
	
			// read password hash and salt from db 
			PasswdSalt passwdSalt = getPasswordForUser(username);
	
			if (passwdSalt == null) {
			/*
			 * 	log.debug("No account found for user [" + username + "]");
			 */
				unauthorizedAccessTry(session);
				return null;
			}
			
			
			// return salted credentials
			SaltedAuthenticationInfo info = new SimpleAuthenticationInfo(username,passwdSalt.password, passwdSalt.salt, getName());
		//	info.setCredentialsSalt(new SimpleByteSource(passwdSalt.salt));
			Sha256Hash userPassword = new Sha256Hash(userPassToken.getPassword(),passwdSalt.salt);
			if(passwdSalt.password.equals(userPassword.toString()))
			{
				authorizedAccess(session);
				SecureRandom secureRandom = new SecureRandom();
				byte[] csrfToken = new byte[32];
				secureRandom.nextBytes(csrfToken);
				Sha256Hash sha256hash = new Sha256Hash("myCsrfSalt",csrfToken);
				sha256hash.toBase64();
				sha256hash.toHex();
				sha256hash.toString();
				session.setAttribute("csrf", sha256hash);
				session.setAttribute("username", username);
				Logger.getInstance().write(username+" has logged into the system", Level.INFO);
				return info;
			}
			else
			{
				unauthorizedAccessTry(session);
				return info;
			}
		}
		else
		//	org.apache.shiro.web.tags.UserTag tag;
			return null;
		
	}

	private PasswdSalt getPasswordForUser(String username) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		SecureRandom csprng = new SecureRandom();
		byte[] values = new byte[32];
		csprng.nextBytes(values);
		
		try {
			dataSource = DriverManager.getConnection(connection);
			statement = dataSource.prepareStatement("SELECT password,salt from tblusers where username = ? ;");
			statement.setString(1, username);

			resultSet = statement.executeQuery();

			boolean hasAccount = resultSet.next();
			if (!hasAccount)
				return null;

			byte[] salt = null;
			String password = resultSet.getString(1);
			resultSet.getMetaData().getColumnCount();
			if (resultSet.getMetaData().getColumnCount() > 1)
				salt = resultSet.getBytes(2);
			Sha256Hash sha256hash = new Sha256Hash(password,values);
			sha256hash.toBase64();
			sha256hash.toHex();
			sha256hash.toString();
			if (resultSet.next()) {
				throw new AuthenticationException("More than one user row found for user [" + username + "]. Usernames must be unique.");
			}

			return new PasswdSalt(password, salt);
		} catch (SQLException e) {
			final String message = "There was a SQL error while authenticating user [" + username + "]";
		/*	if (log.isErrorEnabled()) {
				log.error(message, e);
			}*/
			throw new AuthenticationException(message, e);
			
		} finally {
		/*	JdbcUtils.closeResultSet(resultSet);
			JdbcUtils.closeStatement(statement);
			JdbcUtils.closeConnection(conn);*/
			try {
				dataSource.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				throw new AuthenticationException("There was an error while trying to close the db connection",e);
			}
		}
	}



	class PasswdSalt {
	
		public String password;
		public SimpleByteSource salt;
	
		public PasswdSalt(String password, byte[] salt) {
			super();
			this.password = password;
			this.salt = new SimpleByteSource(salt);
		}
	}
	
	/*
	 * 
	 * 
	 *  end of Authentication Realm Implementation
	 * (non-Javadoc)
	 * @see org.apache.shiro.realm.AuthorizingRealm#clearCachedAuthorizationInfo(org.apache.shiro.subject.PrincipalCollection)
	 */
	
	@Override
	public void clearCachedAuthorizationInfo(PrincipalCollection principals)
	{
		super.clearCachedAuthorizationInfo(principals);
	}
	
	
	@Override
	public void clearCachedAuthenticationInfo(PrincipalCollection principals)
	{
		super.clearCachedAuthenticationInfo(principals);
	}
	
	@Override
	public void clearCache(PrincipalCollection principals)
	{
		super.clearCache(principals);
	}
	
	public void clearAllCachedAuthorizationInfo()
	{
		getAuthorizationCache().clear();
	}
	
	public void clearAllCachedAuthenticationInfo()
	{
		getAuthenticationCache().clear();
	}
	
	public void clearAllCache()
	{
		clearAllCachedAuthenticationInfo();
		clearAllCachedAuthorizationInfo();
	}
	
	public void unauthorizedAccessTry(Session session)
	{
		String result = "Wrong Username or Password";
			
		if(session.getAttribute("tries") == null )
		{
			session.setAttribute("tries", 1);
		}
		else
		{
			if((int)session.getAttribute("tries") == 3)
			{
				if(session.getAttribute("timer")==null)
				{					
					DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
					Date date = new Date();
					
					result = "You failed to login 3 times already, you need to wait "+waitingTime+":"+0+ "0 minutes to try again";
					session.setAttribute("timer", dateFormat.format(date));
				}
			}
			else
				session.setAttribute("tries", (int)session.getAttribute("tries")+1);
		}
		session.setAttribute("loginFailed", "visibility:visible");		
		session.setAttribute("message", result);
	}
		
	public void authorizedAccess(Session session)
	{
		if (session.getAttribute("timer")==null) 
		{
			if(session.getAttribute("tries")!=null)
				session.removeAttribute("tries");
			
			
			session.setAttribute("loginFailed", "visibility:hidden");
			
		} 
	}
	
	
	
	public boolean isLocked(Session session)
	{
		int seconds = 0;
		int minutes = 0;
		String result;
		if(session.getAttribute("timer")!=null)
		{
			DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			Date date = new Date();
			String currentTime = dateFormat.format(date);
			int hours = Integer.parseInt(currentTime.substring(0, 2));
			minutes = Integer.parseInt(currentTime.substring(3, 5));
			seconds = Integer.parseInt(currentTime.substring(6, 8));
			seconds = seconds-Integer.parseInt(((String)session.getAttribute("timer")).substring(6, 8));
			if(seconds < 0)
			{
				minutes--;
				seconds += 60;
			}
			minutes = minutes-Integer.parseInt(((String)session.getAttribute("timer")).substring(3, 5));
			if(minutes < 0)
			{
				hours--;
				minutes += 60;
			}
			if(minutes > 15 && hours - Integer.parseInt(((String)session.getAttribute("timer")).substring(0, 2)) >-1)
			{
				session.removeAttribute("tries");
				session.removeAttribute("timer");
			}
			minutes = waitingTime - minutes;
			if(seconds>0)
			{
				minutes--;
				seconds = 60 - seconds;
			}
			result = "You failed to login 3 times already, you need to wait "+minutes+":"+seconds+ "minutes to try again";
			session.setAttribute("message", result);
			return true;
		}
		return false;
	}

}
