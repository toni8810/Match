package match.com.IO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import match.com.gui.GUIDetails;
import match.com.messaging.User;

public class IO {
	File file;
	File users;
	File messageSent;
	public IO() {
		System.out.println("initializing IO");
		file = new File("details.dat");
		users = new File("users.dat");
		messageSent = new File("messageSent.dat");
	}
	public void storeDetailsInAFile(GUIDetails details) {
		if (details.isSaveBox()) {
			try {
				file.createNewFile();
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
				oos.writeObject(details);
				oos.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public GUIDetails getDetailsFromFile() {
		GUIDetails details;
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			details = (GUIDetails) ois.readObject();
			ois.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return details;
		
	}
	public void storeUsers(ArrayList<User> userList) {
		try {
			users.createNewFile();
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(users));
			oos.writeObject(userList);
			oos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public ArrayList<User> getUserList() {
		ArrayList<User> returnList = new ArrayList<User>();
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(users));
			returnList = (ArrayList<User>) ois.readObject();
			ois.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return returnList;
	}
	public ArrayList<String> getMessageSentUserList() {
		ArrayList<String> returnList = new ArrayList<String>();
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(messageSent));
			returnList = (ArrayList<String>) ois.readObject();
			ois.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return returnList;
	}
	public void storeMessageSentUserList(ArrayList<String> sent) {
		try {
			messageSent.createNewFile();
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(messageSent));
			oos.writeObject(sent);
			oos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
