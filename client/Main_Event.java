package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import etc.Protocol;
import etc.User;

public class Main_Event extends MouseAdapter implements ActionListener{
	Main client;
	List_PopUp popUp;
	Message_Form message;
	int list_idx;
	
	public Main_Event(Main client) {
		this.client = client;

		popUp = new List_PopUp(this);
		
		client.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				// 프로그램 종료 기능
				if (Main_Event.this.client.thread != null) {
					try {
						Main_Event.this.client.thread.out.writeObject(new Protocol(350));
						Main_Event.this.client.thread.out.flush();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == client.Register_Btn) {
			// 회원가입 버튼을 클릭했을 시
			client.register = new Register_Form(client);
		}
		if (e.getSource() == client.login_Btn) {
			// 로그인 버튼을 클릭했을 시
			client.login(client.id_Fld.getText(), client.ps_Fld.getText());
		}
		if (e.getSource() == client.room_Add_Btn) {
			// 방 만들기 버튼을 클릭했을 시
			client.addRoom();
		}
		if (e.getSource() == client.room_Join_Btn) {
			// 방 접속하기 버튼을 클릭했을 시
			client.joinRoom();
		}
		if (e.getSource() == client.logOut_Btn) {
			// 로그아웃 버튼을 클릭했을 시
			client.logOut();
		}
		if (e.getSource() == popUp.dialog) {
			// 미구현 기능
		}
		if (e.getSource() == popUp.message) {
			User user = client.allUserInfo[list_idx];
			message = new Message_Form(client, user);
		}
		if (e.getSource() == popUp.info) {
			// 미구현 기능
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// 전체 접속자 리스트에 마우스 오른쪽 클릭 했을 시 작동
		if (e.getSource() == client.all_User_View && e.getButton() == 3) {
			list_idx = client.all_User_View.getSelectedIndex();
			System.out.println(list_idx);
			if (list_idx != -1) {
			popUp.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}
	
}