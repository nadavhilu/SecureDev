package containers;

public class UserRolesInfo {

	Integer id;
	String username;
	boolean admin;
	boolean moderator;
	boolean user;
	
	public UserRolesInfo(Integer id,String username, boolean admin, boolean moderator, boolean user) {
		super();
		this.id = id;
		this.username = username;
		this.admin = admin;
		this.moderator = moderator;
		this.user = user;
	}

	
	public Integer getId() {
		return id;
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isModerator() {
		return moderator;
	}

	public void setModerator(boolean moderator) {
		this.moderator = moderator;
	}

	public boolean isUser() {
		return user;
	}

	public void setUser(boolean user) {
		this.user = user;
	}
	
	
	
}
