package etc;

import java.io.Serializable; //객체를 직렬화 시켜서 알맞은 형태로 바꿔서 전송하는 인터페이스

// 사용자의 정보를 저장하기 위한 클래스
public class User implements Serializable{

	//변수를 문자열로 선언
	String id, password, nickName, eMail, age, phone;
	
	//User 생성자
	public User(String id, String password, String nickName, String eMail,
			String age, String phone) {
		super();
		this.id = id;
		this.password = password;
		this.nickName = nickName;
		this.eMail = eMail;
		this.age = age;
		this.phone = phone;
	}

	//nickName을 반환하는 메소드
	public String toString() {
		return nickName;
	}
	
	//id를 반환하는 메소드
	public String getId() {
		return id;
	}

	//password를 반환하는 메소드
	public String getPassword() {
		return password;
	}

	//eMail을 반환하는 메소드
	public String geteMail() {
		return eMail;
	}

	//Age를 반환하는 메소드
	public String getAge() {
		return age;
	}

	//Phone을 반환하는 메소드
	public String getPhone() {
		return phone;
	}
}
