package match.com.controller;

import match.com.IO.IO;
import match.com.messaging.MessageSender;

public class AppController {
	//calling I/O
	//calling Selenium
	
	public MessageSender getMs() {
		return new MessageSender(new IO());
	}
	public AppController() {
		System.out.println("init App Controller");
	}
	public IO getIo() {
		return new IO();
	}
}
