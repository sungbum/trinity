package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import server.GameRoom;

import etc.Canvas_Value;
import etc.Message_Data;
import etc.Protocol;
import etc.User;
import java.awt.Color;
import server.CopyClient;

public class Main_Thread extends Thread {
	Main client;
	Socket socket;
        String q;
	ObjectInputStream in;
	ObjectOutputStream out;
        int useridx;
        User user;
	public Main_Thread(Main client, Socket socket) {
		this.client = client;
		this.socket = socket;

		try {
			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		bk: while (true) {
			try {
				// 읽을 때까지 대기!!
				Protocol ptc = (Protocol) in.readObject();

				if (ptc.getPtc() != 1000) {
					System.out.println(ptc.getPtc());
				}

				// 프로토콜 정의
				// 50 -> 회원가입
				// 100 -> id 중복체크
				// 150 -> id 중복체크 오류
				// 200 -> 로그인
				// 250 -> 로그인 실패
				// 300 -> 로그아웃
				// 350 -> 로그아웃(프로그램 종료)
				// 400 -> 메시지
				// 500 -> 접속자 리스트 수신
				// 600 -> 방 리스트 수신
				// 650 -> 방 접속자 리스트 수신
				// 700 -> 방 만들기
				// 750 -> 방 접속
				// 780 -> 방 접속 실패
				// 800 -> 방 나가기
				// 900 -> 쪽지 
				// * 게임 프로토콜 *
				// 1000 -> 캔버스 값
				// 1050 -> 캔버스 클리어
				// 1100 -> 게임 시작
				
				switch (ptc.getPtc()) {
				case 50: // 회원 가입 시
					System.out.println("Register : OK");
					new JOptionPane().showMessageDialog(client, "회원가입이 완료 되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
					client.register.dispose();
					break;
				case 100: // 중복 체크 시
					System.out.println("ID Check : OK");
					int ch = new JOptionPane().showConfirmDialog(client,"중복된 아이디가 없습니다. 사용하시겠습니까?", "성공", JOptionPane.YES_NO_OPTION);
					if (ch == JOptionPane.YES_OPTION) {
						client.register.r_event.setId_chk(true);
					}
					break;
				case 150: // 승인 거절 시
					System.out.println("ID Check : False");
					new JOptionPane().showMessageDialog(client, "중복된 아이디가 있습니다.", "경고", JOptionPane.ERROR_MESSAGE);
					
					break;
				case 200: // 로그인 시
					System.out.println("login : OK");
					client.card.show(client.getContentPane(), "lobby");
					break;
				case 250: // 로그인 실패 시
					System.out.println("login : False");
					new JOptionPane().showMessageDialog(client, "입력한 정보가 잘못됬습니다. ", "실패", JOptionPane.ERROR_MESSAGE);
					break;
				case 300: // 로그아웃 시
					System.out.println("logOut!");
					client.card.show(client.getContentPane(), "login");
					break ;
				case 350: // 로그아웃(프로그램 종료) 시
					break bk;
				case 400: // 메시지 수신 시
					System.out.println(ptc.getMsg());
					client.room.addMsg(ptc.getRoomUserIdx(), ptc.getMsg());
					client.room.addMsg1(ptc.getMsg());
					break;
				case 500: // 접속자 리스트 수신 시
					User[] allUserInfo = ptc.getUserInfo();
					System.out.println("list update!");
					client.setUserList(allUserInfo);
					break;
				case 600: // 방 리스트 수신 시
					String[] roomList = ptc.getRoomList();
					System.out.println("roomList update!");
					// 받아온 방 정보가 Null이 아닐경우에만 리스트 셋팅
					if (roomList != null && roomList.length > 0) {
						client.setRoomList(roomList);
					} else { 
						// Null을 받아왔을 경우 비어있는 모델로 셋팅
						client.room_List_View.setModel(new DefaultListModel());
					}
					break;
				case 650: // 방 접속자 리스트 수신 시
					System.out.println("User Join!!");
					client.room.addMsg1(ptc.getMsg());
					User[] roomUserList = ptc.getUserInfo();
					System.out.println("roomUserList update!");                                       
					client.room.setRoomUserList(roomUserList);
					client.room.setUserInfo(roomUserList);
                                        System.out.println("roomuserlist"+roomUserList.length);
                                        for(int i=0;i<client.roomUserInfo.length;i++)//룸 리스트에서 자신의 정보와 같은 인덱스 값 얻기
                                        if(client.roomUserInfo[i].equals(user)){
                                            useridx=i;
                                        }
                                        System.out.println("i :"+useridx);
                                        
					break;
				case 700: // 방 만들기
					System.out.println("roomAdd : OK!!");
                                        boolean bb=true;//방장 확인
					client.room = new Room_Form(client,bb);
                                        client.room.getCap();
                                        client.room.setTitle("CatchMind");
					client.card.show(client.getContentPane(), "room");
					client.setVisible(false);
					break;
                                case 720://자신의 user 받아오기
                                    this.user=ptc.getUser();
                                    System.out.println("copycopy");
                                    break;
				case 750: // 방 접속
                                         boolean bb1=false;//접속 유저 확인
					System.out.println("roomJoin : OK!!");
					client.room = new Room_Form(client,bb1);
                                        client.room.giveCap();
					client.card.show(client.getContentPane(), "room");
					client.setVisible(false);
					break;
				case 780: // 방 접속 실패
					System.out.println("roomJoin : False!");
					new JOptionPane().showMessageDialog(client, "정원을 초과하였습니다.", "실패", JOptionPane.ERROR_MESSAGE);
					break;
				case 800: // 방 나가기
					System.out.println("roomOut : OK!!");
					client.area.setText(null);
					client.room.dispose();
                                        client.room.time=0;
					client.card.show(client.getContentPane(), "lobby");
					client.setVisible(true);
					break;
				case 900: // 쪽지 수신시
					System.out.println("message r : ok!!");
					Message_Data m_Data = ptc.getM_Data();
					Message_Form m_Form = new Message_Form(client, m_Data.getFromUser());
					m_Form.area.setText(m_Data.getMsg());
					break;
				case 1000: // 캔버스 값 수신시
					client.room.drawCanvas(ptc.getValue());
					break;
				case 1050: // 캔버스 클리어
					client.room.canvasClear();
					break;
				case 1100: // 게임 시작시
                                        q=ptc.getMsg();
					client.room.answer_Lab.setText(q);
                                        System.out.println("시작"+q);
                                        System.out.println("ddd"+q);
                                    break;
                               
                                case 1200://정답시
                                    JOptionPane.showMessageDialog(client.room, "정답");
                                    client.room.time_bb=false;//시간 스레드 멈추기
                                    client.room.time=180;//시간 0으로 셋팅
                                    client.room.area[useridx].setText("");
                                    client.room.area[useridx].setBackground(Color.white);
                                    client.room.canvasClear();//기존의 그림 지우기
                                    break;
                                case 1250://방장권한 주기
                                  
                                   client.room.giveCap();
                                    break;
                                    
                                case 1300://레디버튼
                                    int r_idx=ptc.getRoomUserIdx();
                                     if(ptc.isBb()==true){//레디 했을때의 세팅
                                         
                                        client.room.area[r_idx].setText("READY!");
                                        client.room.area[r_idx].setBackground(Color.red);
                                        
                                    }
                                    else{//레디 안했을 때의 세팅
                                          
                                         client.room.area[r_idx].setText("");
                                        client.room.area[r_idx].setBackground(Color.white);
                                        
                                    }
                                    break;
                                case 1350://방장 권한 받기
                                   client.room.point+=100;//자신의 점수에 더하기 100
                                   client.room.getCap();//방장 권한 받기
                                   client.room.setTitle("방장");
                                   client.thread.out.writeObject(new Protocol(1500,String.valueOf(client.room.point)));
                                   client.thread.out.flush();       //자신의 점수를 String으로 변환해서 서버 보내기
                                    break;
                                case 1400://게임이 시작하는 순간 각 유저들의 Area를 비워준다.
                                    client.room.time_bb=true;//시간 스레드 시작 가능하게 만듬
                                    client.room.thread=new Thread(client.room);
                                    client.room.thread.start();//시간 스레드 시작
                                    client.room.count_Lab.setText("게임시작");
                                    for(int i=0;i<client.roomUserInfo.length;i++){//자신의 창의 각 유저들의 말 칸 비우기
                                     client.room.area[i].setText("");
                                    client.room.area[i].setBackground(Color.white);
                                    }
                                    break;
                                    
                                case 1450:
                                     for(int i=0;i<4;i++){//자신의 창의 각 유저들의 말 칸 비우기
                                     client.room.area[i].setText("");
                                    client.room.area[i].setBackground(Color.white);
                                    }
                                   
                                    break;
                                case 1500:
                                    client.room.point_tf[ptc.getRoomUserIdx()].setText(ptc.getMsg());//정답을 맞춘 유저 점수 올리기
                                    break;
                                    
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}// while문의 끝
		client.exit();
	}
        
}
