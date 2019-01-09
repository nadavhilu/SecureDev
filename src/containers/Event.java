package containers;

import java.sql.Timestamp;

/*
 * 
 * class that defines Event Object
 * 
 */

public class Event {
	
	public Event()
	{
		super();
	}
	
	private Integer id;
	private String creatingUser;
	private String eventTitle;
	private Timestamp happeningDate;
	private Integer userLimit;
	private Integer participating;
	private String eventDescription;
	private String eventLocation;
	private String link;
	
	public Event(Integer id, String creatingUser, String eventTitle, Timestamp happeningDate, Integer userLimit, Integer participating,
			String eventDescription, String eventLocation) {
		
		this.id = id;
		this.creatingUser = creatingUser;
		this.eventTitle = eventTitle;
		this.happeningDate = happeningDate;
		this.userLimit = userLimit;
		this.participating = participating;
		this.eventDescription = eventDescription;
		this.eventLocation = eventLocation;
	}
	
	public Event(Integer id, String creatingUser, String eventTitle, Timestamp happeningDate, Integer userLimit, Integer participating,
			String eventDescription, String eventLocation,String link) {
		
		this.id = id;
		this.creatingUser = creatingUser;
		this.eventTitle = eventTitle;
		this.happeningDate = happeningDate;
		this.userLimit = userLimit;
		this.participating = participating;
		this.eventDescription = eventDescription;
		this.eventLocation = eventLocation;
		this.link = link;
	}
	
	public Integer getId() {
		return id;
	}



	public String getCreatingUser() {
		return creatingUser;
	}

	public void setCreatingUser(String creatingUser) {
		this.creatingUser = creatingUser;
	}

	public String getEventTitle() {
		return eventTitle;
	}

	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	public Timestamp getHappeningDate() {
		return happeningDate;
	}

	public void setHappeningDate(Timestamp happeningDate) {
		this.happeningDate = happeningDate;
	}

	public Integer getUserLimit() {
		return userLimit;
	}

	public void setUserLimit(Integer userLimit) {
		this.userLimit = userLimit;
	}

	public Integer getParticipating() {
		return participating;
	}

	public void setParticipating(Integer participating) {
		this.participating = participating;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public String getEventLocation() {
		return eventLocation;
	}

	public void setEventLocation(String eventLocation) {
		this.eventLocation = eventLocation;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
	
	

}
