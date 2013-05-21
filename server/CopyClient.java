package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JOptionPane;
import etc.Message_Data;
import etc.Protocol;
import etc.User;
import java.io.Serializable;

public class CopyClient extends Thread implements Serializable{

	// 접속자의 정보를 의미하는 객체들
	Socket socket;
	GameServer server;
	ObjectOutputStream out;
	ObjectInputStream in;
	User user;
	GameRoom currentRoom;
	Message_Data m_Data;
	
	public CopyClient(Socket socket, GameServer server) {
		this.socket = socket;
		this.server = server;
		
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// 스레드가 해야할 일들
		bk : while(true) {
			try {
				// 입력 받을 때 까지 대기!
				Protocol ptc = (Protocol) in.readObject();
				if (ptc.getPtc() != 1000) {
					System.out.println(ptc.getPtc());
				}
				
				switch (ptc.getPtc()) {
				case 50: // 회원 가입 시
					server.addUser(ptc.getUser());
					System.out.println("Register OK!");
					out.writeObject(new Protocol(50));
					out.flush();
					break;
				case 100: // 중복 체크 시
					if (server.db.get(ptc.getMsg()) == null) {
						// 중복된 아이디가 없을 경우
						out.writeObject(new Protocol(100));
						out.flush();
					}else {
						// 이미 존재 할 경우
						out.writeObject(new Protocol(150));
						out.flush();
					}
					break;
				case 150: // 승인 거절 시
					break;
				case 200: // 로그인 시
					server.loginUser(this, ptc.getId(), ptc.getPs());
					break;
				case 250: // 로그인 실패 시
					break;
				case 300: // 로그아웃 시
					server.delList(this, user);
					user = null; // 다른사용자가 접속 할 수 있기에 null로 값 변경
					server.sendAllUserInfo();
					out.writeObject(new Protocol(300));
					break;
				case 350: // 로그아웃(프로그램 종료) 시
					// 대화방에서 접속 해제시 작동
					if (currentRoom != null) {
						// 대화방에 자신을 제거
						currentRoom.outUser(this);
						
						// 방에 접속인원이 없다면 방 삭제
						if (currentRoom.getUserCount() == 0) {
							server.delRoom(currentRoom);
							server.send_room(); // 최신화된 방 정보들을 전송
						}
						
						// 클라이언트에서 방 정보 삭제
						currentRoom = null;
					}
					server.delList(this, user);
					server.sendAllUserInfo();
					out.writeObject(new Protocol(350));
					break bk;
				case 400: // 메시지 수신 시
					currentRoom.SendMessage(new Protocol(400, user, ptc.getMsg()));
                                        if(ptc.getMsg().equals(currentRoom.question)){//정답과 같을 경우
                                            
                                            for(int i=0;i<currentRoom.roomUserInfo.size();i++){//이전 방장에게 보낸다.
                                                
                                                if(currentRoom.roomUserInfo.get(i).equals(currentRoom.roomcap)){
                                                    currentRoom.roomUserList.get(i).out.writeObject(new Protocol(1250,false));
                                                    out.flush();
                                                    break;
                                                }
                                            }
                                            currentRoom.roomcap=user;
                                            currentRoom.question=null;
                                            currentRoom.SendProtocol(new Protocol(1200));
                                            out.writeObject(new Protocol(1350, true));
                                        }
					break;
				case 500: // 접속자 리스트 수신 시
					break;
				case 600: // 방 리스트 수신
					break;
				case 650: // 방 접속자 리스트 수신
					break;
				case 700: // 방 만들기
					String roomName = ptc.getMsg();
                	currentRoom = new GameRoom(roomName, server);
                	
                	// 서버에 대화방 추가!
                	server.addRoom(currentRoom);

            		out.writeObject(new Protocol(700));
                        out.flush();
                        currentRoom.roomcap=user;
                        out.writeObject(new Protocol(720, user));
            		out.flush();
                	
                	// 대화방에 자신을 추가
                	currentRoom.joinUser(this);
                	
                	// 전체 접속자들에게 대화방 정보 전송
                	server.send_room();
                	break;
				case 750: // 방 접속
					int roomIdx = ptc.getRoomIdx();
					 out.writeObject(new Protocol(720, user));
                                         out.flush();
					// 전달받은 방의 위치를 서버에서 검색
					currentRoom = server.checkRoom(roomIdx);
					
					// 방 접속자가 초과(4명)하기 않았을 경우에만 진행
					if (currentRoom.getUserCount() < 4 ) {
						System.out.println("roomUser..:"+currentRoom.getUserCount());
						out.writeObject(new Protocol(750));
						out.flush();
						
						// 대화방에 자신을 추가
						currentRoom.joinUser(this);
					} else {
						// 초과하였을 경우
						out.writeObject(new Protocol(780));
						out.flush();
						
						// 클라이언트에서 방 정보 삭제
						currentRoom = null;
					}
					break;
				case 780: // 방 접속 실패
					break;
				case 800: // 방 나가기
					// 대화방에 자신을 제거
                                        
                                        currentRoom.outUser(this);
                                        
                                        currentRoom.ready_list.removeAll(currentRoom.ready_list);
                                         
                                         currentRoom.ready_list.trimToSize();
                                    if (currentRoom.roomcap == user) {
                                        
                                        currentRoom.roomcap = currentRoom.roomUserInfo.get(0);
                                        currentRoom.roomUserList.get(0).out.writeObject(new Protocol(1350, true));
                                        out.flush();
                                        currentRoom.question=null;
                                        currentRoom.SendProtocol(new Protocol(1450));
                                    }
                                    
                                     System.out.println("ready size : "+currentRoom.ready_list.size());
					currentRoom.SendProtocol(new Protocol(1450));
					// 방에 접속인원이 없다면 방 삭제
					if (currentRoom.getUserCount() == 0) {
						server.delRoom(currentRoom);
						server.send_room(); // 최신화된 방 정보들을 전송
					}
					
					// 클라이언트에서 방 정보 삭제
					currentRoom = null;
					
					out.writeObject(new Protocol(800));
					out.flush();
					
					break;
				case 900: // 쪽지 수신시
					System.out.println("message..");
					m_Data = ptc.getM_Data();
					for (int i = 0; i < server.allUserInfo.size(); i++) {
						if (server.allUserInfo.get(i).getId().equals(m_Data.getToUser().getId())) {
							m_Data.setFromUser(user);
							System.out.println("11");
							CopyClient toClient = server.allUserList.get(i);
							System.out.println("22");
							toClient.out.writeObject(new Protocol(900, m_Data));
							toClient.out.flush();
							System.out.println("33");
						}
					}
					break;
				case 1000: // 캔버스 값 수신시
					currentRoom.SendProtocol(new Protocol(1000, ptc.getValue()));
					break;
				case 1050: // 캔버스 클리어
					currentRoom.SendProtocol(new Protocol(1050));
					break;
				case 1100: // 게임 시작
                                    if(currentRoom.ready_list.size()<currentRoom.roomUserList.size()-1)//레디 인원이 없다면 시작 못함
                                        break;
                                    else if(currentRoom.roomUserList.size()-1==currentRoom.ready_list.size())//방장 자신을 제외한 인원수가 레디 인원수와 같다면
                                        currentRoom.question=currentRoom.setQuestion();//문제 생성
					out.writeObject(new Protocol(1100,currentRoom.question));//방장에게 문제 출제
                                         out.flush();
                                         currentRoom.SendProtocol(new Protocol(1400));//시작 메세지 전달
                                         currentRoom.ready_list.removeAll(currentRoom.ready_list);
                                         
                                         currentRoom.ready_list.trimToSize();
                                         System.out.println("게임시작");
                                        break;
                               
				
                                case 1300://레디 눌렀을 때
                                   
                                    if(ptc.isBb()==true){
                                        System.out.println("SERVER");
                                        
                                       
                                        for(int i=0;i<currentRoom.roomUserInfo.size();i++){//방의 유저들에게
                                            
                                            if(currentRoom.roomUserInfo.get(i).equals(user)){//레디한 유저의 인덱스 값을 방의 유저들에게 보내준다.
                                                currentRoom.ready(this);
                                                 System.out.println("ready room size"+currentRoom.ready_list.size());
                                                currentRoom.SendProtocol(new Protocol(1300,i,true));
                                                break;
                                            }
                                        }
                                              
                                    }
                                    else{
                                        
                                        //위의 반대
                                        for(int i=0;i<currentRoom.roomUserInfo.size();i++){
                                            if(currentRoom.roomUserInfo.get(i).equals(user)){
                                                currentRoom.unready(this); 
                                                currentRoom.SendProtocol(new Protocol(1300,i,false));
                                                break;
                                            }
                                        }
                                        
                                    }
                                    break;
                                case 1500:
                                    int cnt=currentRoom.roomUserInfo.indexOf(user);
                                    System.out.println("cnt: "+cnt);
                                    Protocol p=new Protocol(1500, cnt, ptc.getMsg());
                                   currentRoom.SendProtocol(p);
                                    
                                    break;
                                       
                                }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		server.exit(this);
	}

	public void setUser(User user) {//자신의 유저 정보 담기
		this.user = user;
	}
	 
       
}
