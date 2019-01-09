package containers;

import java.sql.Timestamp;

public class ForumData 
{
	public ForumData()
	{
		super();
	}
	
	/**
	 * New Thread
	 * @param ThreadName
	 * @param CreatingUser
	 * @param CreationTime
	 * @param Content
	 */
	public ForumData(Integer ID, String ThreadName, String CreatingUser, String CreationTime, String Content)
	{
		id = ID;
		threadName = ThreadName;
		creatingUser = CreatingUser;
		creationTime = CreationTime;
		content = Content;
	}
	
	/**
	 * New Post
	 * @param CreatingUser
	 * @param CreationTime
	 * @param Content
	 */
	public ForumData(Integer ID, String CreatingUser, String CreationTime, String Content)
	{
		id = ID;
		threadName = null;
		creatingUser = CreatingUser;
		creationTime = CreationTime;
		content = Content;
	}
	
	private String threadName;
	private String creatingUser;
	private String creationTime;
	private String content;
	private Integer id;
		
	public Integer getId()
	{
		return id;
	}
	
	public String getThreadName() 
	{
		return threadName;
	}
	public void setThreadName(String threadName)
	{
		this.threadName = threadName;
	}
	public String getCreatingUser()
	{
		return creatingUser;
	}
	public void setCreatingUser(String creatingUser)
	{
		this.creatingUser = creatingUser;
	}
	public String getCreationTime() 
	{
		return creationTime;
	}
	public void setCreationTime(String CreationTime)
	{
		this.creationTime = CreationTime;
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String Content)
	{
		this.content = Content;
	}
}
