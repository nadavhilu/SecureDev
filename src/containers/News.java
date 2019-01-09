package containers;

public class News {

	Integer id;
	String creator;
	String title;
	String content;
	String link;
	boolean visable;
	
	
	public News(Integer id, String creator,String title, String content,boolean visable, String link )
	{
		this.id = id;
		this.creator = creator;
		this.title = title;
		this.content = content;
		this.visable = visable;
		this.link = link; 
	}
	
	public Integer getId() {
		return id;
	}

	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public boolean isVisable() {
		return visable;
	}
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	
}
