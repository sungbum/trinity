package client;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import etc.Protocol;
import etc.User;
import server.CopyClient;

import server.GameRoom;

public class Main extends JFrame {

	Main_Event event;
	Main_Thread thread;
	Register_Form register;
	Room_Form room;
	Room_Event roomEvent;

	// UI에 필요한 객체들
	CardLayout card;
	JPanel login_Pn, info_Pn, lobby_Pn, room_List_Pn, all_User_Pn, room_Btn_Pn, room_Pn, game_List_Pn, send_Pn;
	JTextField id_Fld, ps_Fld, send_Fld;
	JButton login_Btn, Register_Btn, room_Add_Btn, room_Join_Btn, logOut_Btn, send_Btn, room_exit_Btn;
	JTextArea area;
	JList room_List_View, all_User_View, game_User_View;

	// 채팅에 필요한 객체들
	User[] allUserInfo, roomUserInfo;
	String[] roomList;
        CopyClient[] copy;
	public Main() {
		event = new Main_Event(this);
		conneted(); // 서버에 접속

		this.setLayout(card = new CardLayout());
		// 로그인 카드 셋팅!
		this.add("login", login_Pn = new JPanel(new BorderLayout()));
		login_Pn.add(new JLabel(new ImageIcon("src/images/01.jpg")));
		login_Pn.add(info_Pn = new JPanel(new GridLayout(2, 3)), BorderLayout.SOUTH);
		info_Pn.setBackground(new Color(255, 255, 255)); // 패널 색상 조절
		info_Pn.add(new JLabel("ID : "));
		info_Pn.add(id_Fld = new JTextField(5));
		info_Pn.add(login_Btn = new JButton("접속하기"));
		info_Pn.add(new JLabel("PassWord"));
		info_Pn.add(ps_Fld = new JTextField(5));
		info_Pn.add(Register_Btn = new JButton("회원가입"));
		login_Btn.setBackground(new Color(255, 255, 255)); // 버튼 색상 조절
		Register_Btn.setBackground(new Color(255, 255, 255));

		// 대기화면 카드 셋팅!
		this.add("lobby", lobby_Pn = new JPanel(new BorderLayout(5, 0)));
		lobby_Pn.add(room_List_Pn = new JPanel(new BorderLayout()));
		room_List_Pn.add(new JLabel("*** 대화방 목록 ***"), BorderLayout.NORTH);
		room_List_Pn.add(room_List_View = new JList()); // 대화방 목록 설정
		lobby_Pn.add(all_User_Pn = new JPanel(new BorderLayout()), BorderLayout.EAST);
		all_User_Pn.add(new JLabel("- 전체 접속자 -"), BorderLayout.NORTH);
		all_User_Pn.add(all_User_View = new JList()); // 전체 접속자 목록 설정
		all_User_Pn.add(room_Btn_Pn = new JPanel(new GridLayout(3, 1)), BorderLayout.SOUTH); // 채팅방 버튼 설정
		room_Btn_Pn.add(room_Add_Btn = new JButton("방 만들기"));
		room_Btn_Pn.add(room_Join_Btn = new JButton("방 참여"));
		room_Btn_Pn.add(logOut_Btn = new JButton("로그아웃"));

		// 게임방 카드 셋팅!
		this.add("room", room_Pn = new JPanel(new BorderLayout()));
		room_Pn.add(new JScrollPane(area = new JTextArea()));
		area.setEditable(false); // area에 커서가 못들어가게 설정
		room_Pn.add(game_List_Pn = new JPanel(new BorderLayout()), BorderLayout.EAST);
		game_List_Pn.add(new JLabel("- 채팅방 접속자 -"), BorderLayout.NORTH);
		game_List_Pn.add(game_User_View = new JList()); // JList 설정
		game_List_Pn.add(room_exit_Btn = new JButton("방 나가기"), BorderLayout.SOUTH);
		room_Pn.add(send_Pn = new JPanel(), BorderLayout.SOUTH);
		send_Pn.setBackground(new Color(245, 245, 245)); // 패널 색상 조절
		send_Pn.add(send_Fld = new JTextField(20));
		send_Pn.add(send_Btn = new JButton("보내기"));
		send_Btn.setBackground(new Color(255, 255, 255));// 버튼 색상 조절

		// 이벤트 감지자 등록!
		login_Btn.addActionListener(event);
		Register_Btn.addActionListener(event);

		room_Add_Btn.addActionListener(event);
		room_Join_Btn.addActionListener(event);
		logOut_Btn.addActionListener(event);

		all_User_View.addMouseListener(event);

		all_User_View.setCellRenderer(new ListRenderer());
		game_User_View.setCellRenderer(new ListRenderer());

		// Frame 셋팅!
		card.show(this.getContentPane(), "login"); // 프로그램 시작시 보여지는 창
		this.setTitle("내가그린 사과그림");
		this.setBounds(200, 200, 355, 400);
		this.setVisible(true);
	}

	public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
		new Main();
		//
		AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("src/bgm/0.wav"));
        Clip clip = AudioSystem.getClip();
        clip.open(inputStream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        Thread.sleep(10000); // looping as long as this thread is alive
        // 배경음악
	}

	public void conneted() {
		// 서버에 연결
		try {
			Socket socket = new Socket("localhost", 7717);
			thread = new Main_Thread(this, socket);
			thread.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void login(String id, String ps){
		// 로그인 기능
		try {
			System.out.println("login...."+id+":"+ps);
			thread.out.writeObject(new Protocol(200, id, ps));
			thread.out.flush();

		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
        public void setcopylist(CopyClient[] copy){
            this.copy=copy;
        }

	public void setUserList(User[] allUserInfo) {
		// 전체 접속자 목록 변경
		this.allUserInfo = allUserInfo;
		all_User_View.setListData(allUserInfo);
		all_User_Pn.updateUI();
	}
	public void setRoomList(String[] roomList){
		// 대화방 목록 변경
		this.roomList = roomList;
		room_List_View.setListData(roomList);
		room_List_Pn.updateUI();
	}
	public void addRoom(){
		// 방 만들기 기능
		String roomName = JOptionPane.showInputDialog(this, "방 제목을 입력하세요");
		if (roomName != null && roomName.length() > 0) {
			try {
				System.out.println("addRoom...");
				thread.out.writeObject(new Protocol(700, roomName));
				thread.out.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void joinRoom(){
		// 방 접속하기 기능
		int roomIdx = room_List_View.getSelectedIndex();
		if (roomIdx != -1) {
			try {
				System.out.println("roomJoin...");
				thread.out.writeObject(new Protocol(750, roomIdx));
				thread.out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void logOut(){
		// 로그아웃 기능
		try {
			System.out.println("logOut...");
			thread.out.writeObject(new Protocol(300));
			thread.out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void exit() {
		// 종료

		try {
			thread.in.close();
			thread.out.close();
			thread.socket.close();
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}