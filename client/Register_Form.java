package client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Register_Form extends JDialog{
	
	Main client;
	Register_Event r_event;
	
	JPanel south_pn, center_pn;
	JPanel[] info_pn = new JPanel[6];
	JTextField id_field, password_field, name_field, mail_field, age_field, phone_field;
	JButton check_btn, register_btn;
	public Register_Form(Main client) {
		this.client = client;
		r_event = new Register_Event(this, client);
		
		// 회원 가입 창
		// 하단부 셋팅!
		this.add(south_pn = new JPanel(), BorderLayout.SOUTH);
		south_pn.add(register_btn = new JButton("등록"));
		// 중심부 셋팅!
		this.add(center_pn = new JPanel(new GridLayout(6, 1)));
		for (int i = 0; i < info_pn.length; i++) {
			info_pn[i] = new JPanel(new FlowLayout(FlowLayout.LEFT));
			center_pn.add(info_pn[i]);
		}
		info_pn[0].add(new JLabel("I-D: "));
		info_pn[0].add(id_field = new JTextField(7));
		info_pn[0].add(check_btn = new JButton("중복체크"));
		info_pn[1].add(new JLabel("PS: "));
		info_pn[1].add(password_field = new JTextField(7));
		info_pn[2].add(new JLabel("Name: "));
		info_pn[2].add(name_field = new JTextField(14));
		info_pn[3].add(new JLabel("E-Mail: "));
		info_pn[3].add(mail_field = new JTextField(14));
		info_pn[4].add(new JLabel("Age---: "));
		info_pn[4].add(age_field = new JTextField(14));
		info_pn[5].add(new JLabel("Phone: "));
		info_pn[5].add(phone_field = new JTextField(14));
		
		// 이벤트 감지자 등록
		check_btn.addActionListener(r_event);
		register_btn.addActionListener(r_event);
		
		// Dialog 셋팅!
		this.setTitle("Register");
		this.setBounds(300, 200, 250, 400);
		this.setVisible(true);
	}
}