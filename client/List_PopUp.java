package client;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

// 마우스 오른쪽 클릭 했을때 팝업의 내용을 셋팅하는 메서드
public class List_PopUp extends JPopupMenu {
	Main_Event event;
	
	JMenuItem dialog;
	JMenuItem message;
	JMenuItem info;
	
	public List_PopUp(Main_Event event) {
		this.event = event;
		
		this.add(dialog = new JMenuItem("대화하기"));
		this.add(message = new JMenuItem("쪽지보내기"));
		this.add(info = new JMenuItem("사용자 정보 보기"));
		
		dialog.addActionListener(event);
		message.addActionListener(event);
		info.addActionListener(event);
		
	}
}