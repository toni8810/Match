package match.com.gui;

import java.io.Serializable;

public class GUIDetails implements Serializable {
	
	private static final long serialVersionUID = 6496423202846290372L;
	private String email;
	private char[] password;
	private String message;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	private boolean saveBox;
	private int numOfMessages;
	
	public int getNumOfMessages() {
		return numOfMessages;
	}
	public void setNumOfMessages(int numOfMessages) {
		this.numOfMessages = numOfMessages;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public char[] getPassword() {
		return password;
	}
	public void setPassword(char[] password) {
		this.password = password;
	}
	public boolean isSaveBox() {
		return saveBox;
	}
	public void setSaveBox(boolean saveBox) {
		this.saveBox = saveBox;
	}
	
}
