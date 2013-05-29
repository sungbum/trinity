package client;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import etc.Message_Data;
import etc.Protocol;
import etc.User;

public class Message_Event implements ActionListener {

	Message_Form m_Form;
	Main client;
	User toUser;
	
	Message_Event(Message_Form m_Form, Main client, User toUser)
	{
		this.m_Form = m_Form;
		this.client = client;
		this.toUser = toUser; // 수정해야 함
		
	}

	public void actionPerformed(ActionEvent e) {
		// 쪽지 보내기 버튼을 클릭했을 시
		if(e.getSource() == m_Form.send_Btn)
		{	
			Message_Data m_Data = new Message_Data(toUser, m_Form.send_Fld.getText());
			try {
				System.out.println("sendMessage...");
				client.thread.out.writeObject(new Protocol(900, m_Data));
				client.thread.out.flush();
				m_Form.dispose();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}