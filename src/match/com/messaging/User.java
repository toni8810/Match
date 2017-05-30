package match.com.messaging;

import java.io.Serializable;

public class User implements Serializable {
	private static final long serialVersionUID = 4066820304381158629L;
	private String id;
	private String username;
	private String pageUrl;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPageUrl() {
		return pageUrl;
	}
	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}
}
