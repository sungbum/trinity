package server;

import java.io.Serializable;  //객체를 직렬화 시켜서 알맞은 형태로 바꿔서 전송하는 인터페이스
import java.util.ArrayList;	  //다수의 데이터를 리스트화

import etc.Game_Data;
import etc.Protocol;
import etc.User;

public class GameRoom implements Serializable{
	String roomName; 					// 방 제목
	GameServer server;					// GameServer 클래스 타입 server선언
	ArrayList<CopyClient> roomUserList; // 대화방 접속자들이 저장 되는 곳
	ArrayList<User> roomUserInfo; 		// 대화방 접속자들의 정보가 저장 되는 곳
	ArrayList<CopyClient> t1_UserList;  // 1팀 접속자들이 저장 되는 곳
	ArrayList<User> t1_UserInfo; 		// 1팀 접속자들의 정보가 저장되는 곳
	ArrayList<CopyClient> t2_UserList;  // 1팀 접속자들이 저장 되는 곳
	ArrayList<User> t2_UserInfo; 		// 2팀 접속자들의 정보가 저장되는 곳
    ArrayList<CopyClient> ready_list;	// ready한 user를 담기 위한 곳
	int gameCount; 			// 게임의 카운트
	String answer; 			// 게임의 정답
    String question;		// 문제
    User roomcap;			// 방장
	
	public GameRoom(String roomName, GameServer server) {
		this.roomName = roomName;
		this.server = server;
		
		roomUserList = new ArrayList<CopyClient>();	// 대화방 접속자들이 저장 되는 곳
		roomUserInfo = new ArrayList<User>();		// 대화방 접속자들의 정보가 저장 되는 곳
		t1_UserInfo = new ArrayList<User>();		// 1팀 접속자들이 저장 되는 곳
		t1_UserList = new ArrayList<CopyClient>();	// 1팀 접속자들의 정보가 저장되는 곳
		t2_UserInfo = new ArrayList<User>();		// 2팀 접속자들이 저장 되는 곳
		t2_UserList = new ArrayList<CopyClient>();	// 2팀 접속자들의 정보가 저장되는 곳
		ready_list = new ArrayList<CopyClient>();	// ready한 user를 담기 위한 곳	
	}
	
	// 대화방 접속 기능
	public void joinUser(CopyClient client){
		System.out.println("roomJoin..");		
		roomUserList.add(client);				//대화방 접속자들을 저장
		roomUserInfo.add(client.user);			//대화방 접속자들의 정보를 저장
		this.addTeamUser(client);				//들어온 순서마다 팀 배분
		System.out.println("client list"+roomUserList.size());
        System.out.println("user list"+roomUserInfo.size());
		
        // 입장 메시지 & 방 접속자 리스트 전달
		User[] user_Send = new User[roomUserInfo.size()];
        CopyClient[] copy=new CopyClient[roomUserList.size()];
		
        roomUserInfo.toArray(user_Send);		//방 접속자 정보 전달
        roomUserList.toArray(copy);				//방 접속자 리스트 전달
		this.SendProtocol(new Protocol(650, user_Send ,"*** "+client.user+"님이 입장하였습니다. ***"));
                
	} 
	// 대화방 퇴장 기능
	public void outUser(CopyClient client){
		System.out.println("roomExit..");
		roomUserList.remove(client);			//대화방 퇴장한사람 삭제
		roomUserInfo.remove(client.user);		//대화방 퇴장한사람 정보 삭제
		this.removeTeamUser(client);			//배분한 팀에서 삭제
		
		// 퇴장 메시지 & 방 접속자 리스트 전달
		User[] user_Send = new User[roomUserInfo.size()];
        CopyClient[] copy = new CopyClient[roomUserList.size()];
		
        roomUserInfo.toArray(user_Send);		//방 접속자 정보 전달
        roomUserList.toArray(copy);				//방 접속자 리스트 전달
		this.SendProtocol(new Protocol(650, user_Send ,"*** "+client.user+"님이 퇴장하였습니다. ***"));
               
	}
	// 들어온 순서마다 팀 배분
	public void addTeamUser(CopyClient client){
		System.out.println("TeamSet..add..");
		int userCount = this.getUserCount();	//user의 숫자를 저장하는 변수 선언
		
		switch (userCount) {
		//team 1에 배분
		case 1:			
			t1_UserList.add(client);			//1팀에 접속자 추가
			t1_UserInfo.add(client.user);		//1팀에 접속자 정보 추가
			System.out.println("TeamSet..t1");
			break;
		//team 2에 배분
		case 2:
			t2_UserList.add(client);			//2팀에 접속자 추가
			t2_UserInfo.add(client.user);		//2팀에 접속자 정보 추가
			System.out.println("TeamSet..t2");
			break;
		//team 3에 배분
		case 3:
			t1_UserList.add(client);			//1팀에 접속자 추가
			t1_UserInfo.add(client.user);		//1팀에 접속자 정보 추가
			System.out.println("TeamSet..t1");
			break;
		//team 4에 배분
		case 4:
			t2_UserList.add(client);			//2팀에 접속자 추가
			t2_UserInfo.add(client.user);		//2팀에 접속자 정보 추가
			System.out.println("TeamSet..t2");
			break;
		}
	}
	// 종료시 팀에서 나가기
	public void removeTeamUser(CopyClient client){
		System.out.println("TeamSet..remove..");
		
		if (t1_UserList.size() != 0) {					//1팀의 리스트가 0이 아닐 경우
			for (CopyClient user : t1_UserList) {
				if (user == client){
					System.out.println("TeamSet..t1..");
					t1_UserList.remove(client);			//1팀에서 사용자 삭제
					t1_UserInfo.remove(client.user);	//1팀에서 사용자 정보 삭제
					break;
					}
			}
		}
		if (t2_UserList.size() != 0) {					//2팀의 리스트가 0이 아닐 경우
			for (CopyClient user : t2_UserList) {
				if (user == client){
					System.out.println("TeamSet..t2..");
					t2_UserList.remove(client);			//2팀에서 사용자 삭제
					t2_UserInfo.remove(client.user);	//2팀에서 사용자 정보 삭제
					break;
					}
			}
		}
	}
	
	// 대화방 접속자들에게 메시지 전달
	public void SendMessage(Protocol ptc){
		int roomUserIdx = roomUserInfo.indexOf(ptc.getUser());
		System.out.println("SendMsg..."+roomUserIdx);
		this.SendProtocol(new Protocol(400, roomUserIdx, ptc.getMsg()));
	}
	
	// 대화방 접속자들에게 프로토콜 전달
	public void SendProtocol(Protocol ptc){
		try {
			for (CopyClient user : roomUserList) {
				user.out.writeObject(ptc);   		//ptc를 ObjectOutputStream에 기입
				user.out.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();					//실행 결과를 System.out 객체가 아닌 String 객체에 저장하여 반환
		}
	}
	// 게임 시작 기능
	public void startGame(){
		gameCount++;					//게임 카운트 증가
		answer = this.setQuestion();	//게임의 정답을 문제로 초기화
		System.out.println("GameStart...Count:"+gameCount+", Answer:"+answer);
		Game_Data g_Data = new Game_Data(gameCount, answer);		//Game_Data형 변수 g_Data생성
		//this.SendProtocol(new Protocol(1100, g_Data));
	}
        
        public void ready(CopyClient client){
            ready_list.add(client);				//레디한 유저 담기
        }
        public void unready(CopyClient client){
            ready_list.remove(client);			//레디 안한 유저
            ready_list.trimToSize();			//빈공간을 없앰
        }
	// 문제를 뽑는 기능
	public String setQuestion(){
		String[] question = {
					"사과", "바나나", "딸기", "수박", "포도", "당근", "메론","시계","토끼","호랑이","알로에","휴대폰","아기"};
			
		int choic = (int) (Math.random()*question.length);   //문제를 랜덤하게 고름
		return question[choic];								 //문제 반환
	}
	
	// 방 제목을 반환하는 기능
	public String toString() {
		return roomName;
	}
	
	// 대화방 참여 인원수를 반환하는 기능
	public int getUserCount(){
		return roomUserList.size();
	}
       
	
}
