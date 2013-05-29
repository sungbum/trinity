package client;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import etc.User;

// 쪽지창 폼
public class Message_Form extends JDialog{
	// 대화창에 필요한 객체
	JTextArea area;
	JPanel send_Pn;
	JTextField send_Fld;
	JButton send_Btn;
	
	// 쪽지에 필요한 객체
	Main client;
	User user;
	Message_Event m_Event;
	
	public Message_Form(Main client, User user) {
		this.client = client;
		this.user = user;
		
		m_Event = new Message_Event(this, client, user);
		
		this.add(new JScrollPane(area = new JTextArea()));
		area.setEditable(false); // area에 커서가 못들어가게 설정
		area.setBackground(new Color(200, 200, 200));
		this.add(send_Pn = new JPanel(), BorderLayout.SOUTH);
		send_Pn.add(send_Fld = new JTextField(16));
		send_Pn.add(send_Btn = new JButton("보내기"));
		
		// 이벤트 감지자 등록
		this.send_Btn.addActionListener(m_Event);
		
		// 기본 셋팅!
		this.setTitle(user+" 님 - Message"); // 쪽지 송신자의 객체를 받아와야함.
		this.setBounds(400, 300, 300, 200);
		this.setVisible(true);
	}
}