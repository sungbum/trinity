package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;

import etc.Protocol;
import etc.User;

public class Register_Event implements ActionListener {

	Register_Form form;
	Main client;
	String id; // 중복체크후 아이디를 변경 하였을 경우를 대비
	Boolean id_chk = false; // 중복체크 성공시 true

	public Register_Event(Register_Form form, Main client) {
		this.form = form;
		this.client = client;
	}


	public void actionPerformed(ActionEvent e) {
		// 중복체크 버튼을 클릭시 작동
		if (e.getSource() == form.check_btn) {
			id = form.id_field.getText().trim();
			try {
				// 중복체크 프로토콜과 id 를 보냄
				Protocol ptc = new Protocol(150, id);
				System.out.println(ptc.getPtc());
				client.thread.out.writeObject(new Protocol(100, id));
				client.thread.out.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		// 회원가입 버튼을 클릭시 작동
		if (e.getSource() == form.register_btn) {
			if (id_chk == true && form.id_field.getText().trim().equals(id)) {
				User user = new User(form.id_field.getText().trim(),
						form.password_field.getText().trim(),
						form.name_field.getText().trim(), form.mail_field.getText().trim(),
						form.age_field.getText().trim(), form.phone_field.getText().trim());
				try {
					client.thread.out.writeObject(new Protocol(50, user));
					client.thread.out.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else {
				new JOptionPane().showMessageDialog(client, "중복체크를 하시오.", "경고", JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	public void setId_chk(Boolean id_chk) {
		this.id_chk = id_chk;
	}
}
