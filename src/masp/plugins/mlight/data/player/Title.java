package masp.plugins.mlight.data.player;

public final class Title implements Comparable<Title> {
	
	private String title;
	private String id;
	private int priority;
	
	public Title(String title, String id, int priority) {
		this.title = title;
		this.id = id;
		this.priority = priority;
	}
	
	public String getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public int getPriority() {
		return priority;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Title) {
			return ((Title) o).getId().equalsIgnoreCase(getId());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return getId().hashCode();
	}
	
	@Override
	public int compareTo(Title title) {
		return title.getPriority() - this.getPriority();
	}
	
}
