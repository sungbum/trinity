package etc;

import java.io.Serializable;
import server.CopyClient;

import server.GameRoom;

public class Protocol implements Serializable{
	
	int ptc;
	int roomIdx;
	int roomUserIdx;
        boolean bb;
	String msg;
	String id;
	String ps;
	User user;
	User[] userInfo;
	String[] roomList;
	Canvas_Value value;
	Message_Data m_Data;
        CopyClient[] copylist;
        CopyClient copy;
	Game_Data g_Data;
        
	
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
	public Protocol(int ptc,CopyClient copy){
            this.ptc=ptc;
            this.copy=copy;
        }
	public Protocol(int ptc) {
		// 명령 만을 보내는 생성자
		this.ptc = ptc;
	}

	public Protocol(int ptc, String msg) {
		// 메시지를 전달하는 생성자(클라이언트->서버(CopyClient)
		this.ptc = ptc;
		this.msg = msg;
	}
	public Protocol(int ptc, int roomUserIdx, String msg) {
		// 메시지를 전달하는 생성자(서버(room)->클라이언트)
		this.ptc = ptc;
		this.roomUserIdx = roomUserIdx;
		this.msg = msg;
	}
	public Protocol(int ptc, User user, String msg) {
		// 메시지를 전달하는 생성자(서버(CopyClient)->서버(room)
		this.ptc = ptc;
		this.user = user;
		this.msg = msg;
	}
	public Protocol(int ptc, User user) {
		// 유저 정보를 전달하는 생성자
		this.ptc = ptc;
		this.user = user;
	}
	public Protocol(int ptc, String id, String ps){
		// 로그인시 id와 ps를 전달하는 생성자
		this.ptc = ptc;
		this.id = id;
		this.ps = ps;
	}
	public Protocol(int ptc, User[] userInfo) {
		// 사용자 정보들을 전달하는 생성자
		this.ptc = ptc;
		this.userInfo = userInfo;
	}
	public Protocol(int ptc, String[] roomList) {
		// 방 정보들을 전달하는 생성자
		this.ptc = ptc;
		this.roomList = roomList;
	}
	public Protocol(int ptc, int roomIdx){
		// 방의 위치를 전달하는 생성자
		this.ptc = ptc;
		this.roomIdx = roomIdx;
	}
       
	public Protocol(int ptc, User[] userInfo  ,String msg) {
		// 사용자들과 문자열을 전달하는 생성자
		this.ptc = ptc;
		this.userInfo = userInfo;
		this.msg = msg;
                this.copylist=copylist;
                
	}
       //레디한 유저 얻는 생성자
         public Protocol(int ptc,int roomUserIdx,boolean bb){
            this.ptc=ptc;
            this.bb=bb;
            this.roomUserIdx=roomUserIdx;
        }
	public Protocol(int ptc, Canvas_Value value) {
		// 캔버스 값을 전달하는 생성자
		this.ptc = ptc;
		this.value = value;
	}
	public Protocol(int ptc,Message_Data m_Data) {
		// 쪽지를 전달하는 생성자
		this.ptc = ptc;
		this.m_Data = m_Data;
	} 
	public Protocol(int ptc, Game_Data g_Data){
		// 게임을 시작을 전달하는 생성자
		this.ptc = ptc;
		this.g_Data = g_Data;
	}
        public Protocol(int ptc,boolean bb){
            this.ptc=ptc;
            this.bb=bb;
        }
	public int getPtc() {
		return ptc;
	}
	public int getRoomIdx() {
		return roomIdx;
	}
	public String getMsg() {
		return msg;
	}
	public User[] getUserInfo() {
		return userInfo;
	}
	public String[] getRoomList() {
		return roomList;
	}
	public User getUser() {
		return user;
	}
	public String getId() {
		return id;
	}
	public String getPs() {
		return ps;
	}
	public Canvas_Value getValue() {
		return value;
	}
	public Message_Data getM_Data() {
		return m_Data;
	}
	public Game_Data getG_Data() {
		return g_Data;
	}
	public int getRoomUserIdx() {
		return roomUserIdx;
	}
        public boolean isBb() {
        return bb;
    }
        public CopyClient[] getCopylist() {
        return copylist;
    }
         public CopyClient getCopy() {
        return copy;
    }
}
