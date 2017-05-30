package match.com.messaging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import match.com.IO.IO;
import match.com.gui.GUIDetails;

public class MessageSender {
	private WebDriver driver;
	private WebDriverWait wait;
	private GUIDetails details;
	private IO io;
	private ArrayList<User> userList = new ArrayList<User>();
	
	public MessageSender(IO io) {
		this.io = io;
		driver = new FirefoxDriver();
		wait = new WebDriverWait(driver, 30);
		userList = io.getUserList();
	}
	public void login(GUIDetails details) throws InterruptedException {
		this.details = details;
		//going to home page
		//https://uk.match.com/search/process_result.php
		driver.get("https://match.com");
		//click on login button
		driver.findElement(By.xpath(".//*[@id='main-frame']/div[1]/div[2]/div/div[2]/div[1]/a")).click();
		//finding email field and type email address in it
		driver.findElement(By.id("log")).sendKeys(details.getEmail());
		//getting password in a String object
		String password = "";
		char[] pass = details.getPassword();
		for (int i=0; i<pass.length; i++) {
			password += pass[i];
		}
		//finding password field and type password in it
		driver.findElement(By.id("pwd")).sendKeys(password);
		//finding form element
		WebElement temp = driver.findElements(By.className("simple-form")).get(1);
		temp.submit();
		Thread.sleep(5000);
		//switch frame
		driver.switchTo().frame("meeticmain");
	}
	public void getPeople() throws InterruptedException {
		if (userList != null) return;
		userList = new ArrayList<>();
		closePopup();
		boolean moreToLoad = true;
		//clicking on search button
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//*[@id='menu_search']/a/span"))).click();
		try {
			//clicking on my saved searches button
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//*[@id='i_show_save_search_history_btn']/span"))).click();
		}
		catch (WebDriverException wde) {
			driver.switchTo().frame("meeticmain");
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//*[@id='i_show_save_search_history_btn']/span"))).click();
		}
		//clicking on first result
		driver.findElement(By.xpath(".//*[@id='i_saved_search_history']/div[2]/div/div[1]/div[1]/a")).click();
		Thread.sleep(5000);
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		while (moreToLoad) {
			for (int i=0; i<70; i++) {
				jse.executeScript("window.scrollBy(0,250)", "");
				Thread.sleep(100);
			}
			try {
				driver.findElement(By.xpath(".//*[@id='i_is_see_more']/span/a")).click();
			}
			catch(ElementNotVisibleException enve) {
				moreToLoad = false;
			}
		}
		String src = driver.getPageSource();
		ArrayList<User> users = new ArrayList<User>();
		String[] srcLines = src.split("\n");
		String temp;
		String id = "",userUrl = "",username ="";
		for (int i=0; i<srcLines.length; i++) {
			temp = srcLines[i];
			if (temp.contains("<div class=\"i-block-content-list\"")) {
				id = temp.substring(temp.indexOf("id="));
				id = id.substring(id.indexOf("\"")+1, id.lastIndexOf("\""));
			}
			if (temp.contains("<a href=\"https://uk.match.com/members/index.php?ref=VIEW.")) {
				userUrl = temp.substring(temp.indexOf("href="));
				userUrl = userUrl.substring(userUrl.indexOf('"')+1);
				userUrl = userUrl.substring(0, userUrl.indexOf('"'));
				temp = srcLines[i+1];
				username = temp.trim();
				User u = new User();
				u.setId(id);
				u.setPageUrl(userUrl);
				u.setUsername(username);
				users.add(u);
			}
		}
		System.out.println(users.size());
		io.storeUsers(users);
		userList.addAll(users);
	}
	public void sendMessages() throws InterruptedException {
		File logFile = new File("log.txt");
		//Getting list of users who already have been messaged
		ArrayList<String> messageSent = io.getMessageSentUserList();
		System.out.println(messageSent.size());
		System.out.println(userList.size());
		//number of message counter
		int numOfMessegesSent = 0;
		//walk through users
		userList:
		for (int i=0; i<userList.size(); i++) {
			//if user has already been messaged jump to the next user
			if (messageSent.contains(userList.get(i).getUsername())) continue;
			userList.get(i).setPageUrl(userList.get(i).getPageUrl().replace("amp;", ""));
			System.out.println(userList.get(i).getPageUrl());
			System.out.println(userList.get(i).getUsername());
			//get user's page
			driver.get(userList.get(i).getPageUrl());
			//wait 5 seconds before switching to the relevant frame
			Thread.sleep(5000);
			driver.switchTo().frame("meeticmain");
			closePopup();
			//clicking on message button
			try {
				driver.findElement(By.id("i_profile_menu_buttons")).findElement(By.className("i_js_actionbar_messagebox")).click();
			}
			catch (NoSuchElementException nsee) {
				//user does not exist anymore
				messageSent.add(userList.get(i).getUsername());
				continue;
			}
			catch (WebDriverException wde) {
				wde.printStackTrace();
				messageSent.add(userList.get(i).getUsername());
				continue;
			}
			catch (Exception e) {
				try {
					logFile.createNewFile();
					BufferedWriter bw = new BufferedWriter(new FileWriter(logFile, true));
					bw.append(userList.get(i).getPageUrl());
					bw.newLine();
					bw.append(userList.get(i).getUsername());
					bw.newLine();
					bw.append(e.getMessage());
					bw.newLine();
					bw.close();
					continue;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			//wait a second before start typing message
			Thread.sleep(1000);
			try {
				driver.findElement(By.id("i_email_sender_text")).sendKeys(details.getMessage().replace("'name'",userList.get(i).getUsername()));
			}
			catch (ElementNotVisibleException enve) {
				enve.printStackTrace();
				messageSent.add(userList.get(i).getUsername());
				continue;
			}
			catch (Exception e) {
				try {
					logFile.createNewFile();
					BufferedWriter bw = new BufferedWriter(new FileWriter(logFile, true));
					bw.append(userList.get(i).getPageUrl());
					bw.newLine();
					bw.append(userList.get(i).getUsername());
					bw.newLine();
					bw.append(e.getMessage());
					bw.newLine();
					bw.close();
					continue;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			//getting all div element that is a child of 
			List<WebElement> tempList = driver.findElement(By.id("i_profile_picture")).findElements(By.tagName("div"));
			String tempString;
			for (int k=0; k<tempList.size(); k++) {
				tempString = tempList.get(k).getAttribute("role");
				if (tempString != null && tempString.contentEquals("sendButton")) {
					tempList.get(k).click();
					messageSent.add(userList.get(i).getUsername());
					numOfMessegesSent++;
					if (numOfMessegesSent == details.getNumOfMessages()) break userList;
					break;
				}
			}
		}
		io.storeMessageSentUserList(messageSent);
		driver.close();
		driver.quit();
		System.exit(0);
	}
	private void closePopup() {
		try {
			driver.findElement(By.id("i_close_layer")).click();
		}
		catch (NoSuchElementException nsee) {
			
		}
	}
}
