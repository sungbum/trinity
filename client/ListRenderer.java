package client;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;

import etc.User;

// 리스트의 아이콘을 설정하는 클래스
public class ListRenderer extends DefaultListCellRenderer{

	ImageIcon online;
	
	public ListRenderer() {
		online = new ImageIcon("src/images/user_online.png");
	}
	
	// List의 요소만큼 불러오는 메서드
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			
			if (getObject(value) instanceof User) {
				setIcon(online);
			}
			
			return this;
	}
	
	private Object getObject(Object value){
		// 전달된 인자(value)는 리스트의 요소이다.
		User user = (User) value;
		return user;
	}
	
}