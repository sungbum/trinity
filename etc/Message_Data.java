package etc;

import java.io.Serializable;

public class Message_Data implements Serializable{
	User fromUser, toUser;
	String msg;
	
	//생성자
	public Message_Data(User toUser, String msg)
	{
		this.toUser = toUser;
		this.msg = msg;
	}
	public void setFromUser(User fromUser) {
		this.fromUser = fromUser;
	}
	public User getFromUser() {
		return fromUser;
	}
	public User getToUser() {
		return toUser;
	}
	public String getMsg() {
		return msg;
	}
}
