package client;

import etc.Protocol;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;


public class Room_Event extends KeyAdapter implements ActionListener, MouseMotionListener {

	Main client;
	Room_Form room;
	Room_Canvas can;
	Main_Thread thread;
    boolean r_bb;
	
    public Room_Event(Main client, Room_Form room, Room_Canvas can,Main_Thread thread) {
		this.client = client;
		this.room = room;
		this.can = can;
        this.thread=thread;
        room.addWindowListener(new WindowAdapter() {

		public void windowClosing(WindowEvent e) {
	
			// 프로그램 종료 기능
			if (Room_Event.this.client.thread != null) {
				try {
					Room_Event.this.client.thread.out.writeObject(new Protocol(350));
					Room_Event.this.client.thread.out.flush();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		});
	}

	public void beepmusic(){

		InputStream in = null;
		try {
			in = new FileInputStream("src/bgm/2.wav");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		AudioStream as = null;
		try {
			as = new AudioStream(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		AudioPlayer.player.start(as);
	}

	public void keyPressed(KeyEvent e) {
		if (e.getSource() == room.send_Fld) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				// 메시지 창에서 엔터를 클릭했을 시
				room.sendMsg(room.send_Fld.getText().trim());
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == room.room_Exit_Btn) {
			// 방 나가기 버튼을 클릭했을 시
			room.outRoom();
			beepmusic();
		}
		if (e.getSource() == room.send_Btn) {
			// 보내기 버튼을 클릭했을 시
			room.sendMsg(room.send_Fld.getText().trim());
			beepmusic();
		}
		if (e.getSource() == room.red_Btn) {
			// 레드 색상을 클릭했을 시
			can.setColor(Color.red);
			can.setG_Size(10);
			beepmusic();
		}
		if (e.getSource() == room.green_Btn) {
			// 초록 색상을 클릭했을 시
			can.setColor(Color.green);
			can.setG_Size(10);
			beepmusic();
		}
		if (e.getSource() == room.blue_Btn) {
			// 파랑 색상을 클릭했을 시
			can.setColor(Color.blue);
			can.setG_Size(10);
			beepmusic();
		}
		if (e.getSource() == room.gray_Btn) {
			// 회색 색상을 클릭했을 시
			can.setColor(Color.lightGray);
			can.setG_Size(10);
			beepmusic();
		}
		if (e.getSource() == room.black_Btn) {
			// 검정 색상을 클릭했을 시
			can.setColor(Color.black);
			can.setG_Size(10);
			beepmusic();
		}
		if (e.getSource() == room.eraser_Btn) {
			// 지우개 버튼을 클릭했을 시
			can.setColor(Color.yellow);
			can.setG_Size(30);
			beepmusic();
		}
		if (e.getSource() == room.reset_Btn) {
			// 클리어 버튼을 클릭했을 시
			room.sendClear();
			beepmusic();
		}
                if(e.getSource()==room.game_start_btn){//게임 스타트
                    try {
                        thread.out.writeObject(new Protocol(1100));
                        thread.out.flush();
                        room.send_Fld.setEnabled(false);//게임중 채팅 불가
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                if(e.getSource()==room.ready_btn){//레디 버튼
                    try {
                        if(r_bb==false){//자신이 레디가 아닐때
                            r_bb=true;
                        Protocol p= new Protocol(1300, true);
                        thread.out.writeObject(p);
                        thread.out.flush();
                            
                    }
                    else{
                            r_bb=false;//자신이 레디일때
                        Protocol p=new Protocol(1300, false);
                            System.out.println("room-event");
                        thread.out.writeObject(p);
                        thread.out.flush();
                            
                    }
                        
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    
                        
                }
	}

	public void mouseDragged(MouseEvent e) {
		// 캔버스에서 마우스를 드래그했을 시 작동
		if (e.getSource() == can) {
			room.sendValue(e.getPoint(), can.getColor(), can.getG_Size());
		}
	}

	public void mouseMoved(MouseEvent e) {
		// 사용 안함
	}
}