package etc;

import java.io.Serializable;

public class Game_Data implements Serializable{
	int gameCount;		//game 수
	String gameAnswer;	//game정답
	User userAnswer;	//user의 답
	
	//생성자 메소드
	public Game_Data(int gameCount, String gameAnswer) {
		this.gameCount = gameCount;
		this.gameAnswer = gameAnswer;
	}

	public void setUserAnswer(User userAnswer) {
		this.userAnswer = userAnswer;
	}

	public User getUserAnswer() {
		return userAnswer;
	}

	public int getGameCount() {
		return gameCount;
	}

	public String getGameAnswer() {
		return gameAnswer;
	}
	
	
}
