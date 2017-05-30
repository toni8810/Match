package match.com.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import match.com.controller.AppController;
import match.com.messaging.MessageSender;

public class GUI extends JFrame {
	private static final long serialVersionUID = -8415803973844278786L;

	public GUI() {
		final AppController controller = new AppController();
		GUIDetails details = controller.getIo().getDetailsFromFile();
		this.setTitle("Match.com Message sender");
		this.setSize(400, 500);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel jp = new JPanel();
		
		final Font f = new Font("Courier New", Font.PLAIN, 25);
		
		JLabel lEmail = new JLabel("Email: ");
		lEmail.setFont(f);
		JLabel lPassword = new JLabel("Password: ");
		lPassword.setFont(f);
		JLabel lNumOfMessages = new JLabel("Number of messages: ");
		lNumOfMessages.setFont(f);
		final JCheckBox save = new JCheckBox("Save Details");
		save.setFont(new Font("Courier New", Font.PLAIN, 20));
		save.setSelected(true);
		
		final JTextField tfEmail = new JTextField(15);
		tfEmail.setFont(f);
		if (details != null) tfEmail.setText(details.getEmail());
		final JPasswordField pfPassword = new JPasswordField(12);
		pfPassword.setFont(f);
		if (details != null) {
			String password = "";
			char[] pass = details.getPassword();
			for (int i=0; i<pass.length; i++) {
				password += pass[i];
			}
			pfPassword.setText(password);
		}
		final JTextField tfNumOfMessages = new JTextField(2);
		tfNumOfMessages.setFont(f);
		if (details != null) tfNumOfMessages.setText(""+details.getNumOfMessages());
		
		final JTextArea message = new JTextArea(15, 40);
		message.setLineWrap(true);
		message.setWrapStyleWord(true);
		message.setFont(new Font("Courier New", Font.PLAIN, 15));
		if (details != null) message.setText(details.getMessage());
		else message.setText("'name' will be replaced with actual username. Be sure that you have a saved search");
		
		JButton jb = new JButton("Lets Rock!!");
		jb.setFont(f);
		jb.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				GUIDetails details = new GUIDetails();
				details.setEmail(tfEmail.getText());
				details.setPassword(pfPassword.getPassword());
				try {
					int numOfMessages = Integer.parseInt(tfNumOfMessages.getText());
					details.setNumOfMessages(numOfMessages);
				}
				catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(null, "Number of messages field is invalid");
					return;
				}
				details.setMessage(message.getText());
				details.setSaveBox(save.isSelected());
				controller.getIo().storeDetailsInAFile(details);
				try {
					MessageSender ms = controller.getMs();
					ms.login(details);
					ms.getPeople();
					ms.sendMessages();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		
		jp.add(lEmail);
		jp.add(tfEmail);
		jp.add(lPassword);
		jp.add(pfPassword);
		jp.add(lNumOfMessages);
		jp.add(tfNumOfMessages);
		jp.add(message);
		jp.add(jb);
		jp.add(save);
		
		this.add(jp);
		this.setVisible(true);
	}
}
