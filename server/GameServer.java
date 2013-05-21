package server;

import java.io.IOException;			//입출력 에러를 제어
import java.net.ServerSocket;		//서버 소켓
import java.net.Socket;				//소켓을 작성
import java.util.ArrayList;			//리스트 인터페이스
import java.util.HashMap;			//map 인터페이스의 해시 테이블에 근거

import etc.Protocol;
import etc.User;

public class GameServer extends Thread {

	ArrayList<CopyClient> allUserList; // 접속자들이 저장되는 곳
	ArrayList<User> allUserInfo; // 접속자들의 정보가 저장되는 곳
	ArrayList<GameRoom> roomList; // 게임방 리스트가 저장되는 곳
	HashMap<String, User> db;
	ServerSocket server;
	
	
	public GameServer() {
		try {
			server = new ServerSocket(7717);				//지정된 포트에 바인드 되고 있는 서버소켓을 돌려줌
			System.out.println("서버 시작!");					//서버 시작을 알리는 메세지
			allUserList = new ArrayList<CopyClient>();		//접속자들이 저장되는 곳
			allUserInfo = new ArrayList<User>();			//접속자들의 정보가 저장되는 곳
			roomList = new ArrayList<GameRoom>();			//게임방 리스트가 저장되는 곳
			db = new HashMap<String, User>();
		} catch (Exception e) {
			e.printStackTrace();							//결과를 string객체에 저장하여 반환
		}
	}

	public static void main(String[] args) {
		new GameServer().start();					//서버 시작
	}

	public void run() {
		while (true) {
			try {
				// 접속자가 발생 할 때까지 대기!
				Socket socket = server.accept();	//소켓에 대한 접속 요구를 대기
				String ip = socket.getInetAddress().getHostAddress();	//소켓의 로컬 주소를 돌려줌
				CopyClient client = new CopyClient(socket, this);		//CopyClient형의 변수 client 생성
				System.out.println(ip + ": 접속");
				client.start(); 										// 스레드 시작!
			} catch (Exception e) {
				e.printStackTrace();					//실행 결과를 System.out 객체가 아닌 String 객체에 저장하여 반환
			}
		}
	}

	// 회원등록 기능
	public void addUser(User user) {
		db.put(user.getId(), user);
	}

	// 로그인 기능
	public void loginUser(CopyClient client, String id, String ps) {
		try {
			
			//입력한 id가 없을시
			if (db.get(id) == null) {
				System.out.println("login false... id check false!");
				client.out.writeObject(new Protocol(250));				//로그인 실패
				client.out.flush();
			} else {
				User user = db.get(id);
				
				//패스워드가 틀렸을시 
				if (user.getPassword().equals(ps) == false) {
					System.out.println("login false... ps check false!");	//패스워드 틀렸다는 메시지
					client.out.writeObject(new Protocol(250));				//로그인 실패
					client.out.flush();
				} else {
					System.out.println("login OK!!");			//로그인 완료 메세지
					client.setUser(user); 						//client에 사용자정보 등록
					client.out.writeObject(new Protocol(200));	//로그인 성공
					this.setList(client, user); 				//접속자 리스트에 등록
					this.sendAllUserInfo(); 					//접속자 리스트 전송
					this.send_room(); 							//방 리스트 전송
				}
			}
		} catch (Exception e) {
			e.printStackTrace();					//실행 결과를 System.out 객체가 아닌 String 객체에 저장하여 반환
		}
	}
	
	// 접속자 리스트에 접속자 등록 기능
	public void setList(CopyClient client, User user) {
		System.out.println("addUser...");
		this.allUserList.add(client);				//client측 접속자 리스트에 접속자 등록
		this.allUserInfo.add(user);					//user측 접속자 리스트에 접속자 등록
	}
	
	// 접속자 리스트에 접속자 삭제 기능
	public void delList(CopyClient client, User user) {
		System.out.println("delUser...");
		this.allUserList.remove(client);			//client측 접속자 리스트에서 접속자 삭제
		this.allUserInfo.remove(user);				//user측 접속자 리스트에서 접속자 삭제
	}
	// 전체 접속자에게 프로토콜 전송 기능
	public void sendProtocol(Protocol ptc){
		for (CopyClient client : allUserList) {
			try {
				client.out.writeObject(ptc);		//ptc객체를 전송
				client.out.flush();
				System.out.println("sendProtocol OK!!");
			} catch (Exception e) {
				e.printStackTrace();				//실행 결과를 System.out 객체가 아닌 String 객체에 저장하여 반환
			}
		}
	}
	
	// 전체 접속자에게 접속자 정보 리스트 전송 기능
	public void sendAllUserInfo(){
		
		// 접속자 리스트를 배열로 변환
		User[] userList = new User[allUserInfo.size()];
		allUserInfo.toArray(userList);
		
		for (CopyClient client : allUserList) {
			try {
				client.out.writeObject(new Protocol(500, userList));	//접속자 리스트 수신
				client.out.flush();
				System.out.println("sendAllUserInfo OK!!");
			} catch (Exception e) {
				e.printStackTrace();				//실행 결과를 System.out 객체가 아닌 String 객체에 저장하여 반환
			}
		}
	}
	public void send_room(){
		// 방의 정보를 배열에 넣어서 배열을 전송
		String[] room_Send = getRoomList(); 
		
		for (CopyClient client : allUserList) {
			try {
				client.out.writeObject(new Protocol(600, room_Send));	//방 리스트 수신
				client.out.flush();
				System.out.println("sendRoomList OK!!");
			} catch (IOException e) {
				e.printStackTrace();				//실행 결과를 System.out 객체가 아닌 String 객체에 저장하여 반환
			}
		}
	}
	public String[] getRoomList(){
		
		// 대기실의 방 이름을 반환하는 메서드
		String[] value = null;
		
		if (roomList.size() > 0) {
			value = new String[roomList.size()];
			int i = 0;
			
			for (GameRoom room : roomList) {
				value[i++] = room.toString();
			}
		}
		return value;
	}
	// 방 추가 기능
	public void addRoom(GameRoom room){
		System.out.println("roomAdd...");
		roomList.add(room);						//방 리스트에 방 추가
	}
	
	// 방 삭제 기능
	public void delRoom(GameRoom room){
		System.out.println("roomDel...");
		roomList.remove(room);					//방 리스트에서 방 삭제
	}
	
	// 사용자가 선택한 방을 찾는 기능
	public GameRoom checkRoom(int idx){
		System.out.println("roomCheck...");
		return roomList.get(idx);				//방 리스트 반환
	}
	
	// 접속자 종료 기능
	public void exit(CopyClient client){
		try {
			System.out.println("userExit...");
			client.in.close();
			client.out.close();
			client.socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
       
}
