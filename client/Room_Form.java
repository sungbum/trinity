package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import etc.Canvas_Value;
import etc.Game_Data;
import etc.Protocol;
import etc.User;
import javax.swing.JOptionPane;

//유저 창은 일단 생성만 해놓은 것이기 때문에 변수를 지정해야 쓸수 있어요

public class Room_Form extends JFrame implements Runnable{
	
    Main client;
    Room_Event roomEvent;
    boolean bb,time_bb;
    Room_Canvas can;
    int roomUserIdx;
    JPanel t1_Pn, t2_Pn, south_Pn, t1_1_Info_Pn, t1_2_Info_Pn, t2_1_Info_Pn, t2_2_Info_Pn, t1_Score_Pn, t2_Score_Pn, center_Pn, color_Pn, out_Pn;
    JPanel[] basic_p=new JPanel[4];
    JPanel[] point_p=new JPanel[4];
    JTextField point_Fld, send_Fld, t1_Score_Fld, t2_Score_Fld;
    JTextField[] point_tf=new JTextField[4];
    JLabel can_Lab, count_Lab, answer_Lab;
    JLabel[] user_Info = new JLabel[4];
    JTextArea[] area = new JTextArea[4];
    JButton red_Btn,gray_Btn,black_Btn,green_Btn,blue_Btn,eraser_Btn,reset_Btn, send_Btn, room_Exit_Btn,game_start_btn,ready_btn;
    int point=0,time=60;
    Thread thread;
    JLabel time_jl;
    public Room_Form(Main client,boolean bb) {
    	this.client = client;
        this.bb=bb;
        
    	can = new Room_Canvas(this);
    	roomEvent = new Room_Event(client, this, can,client.thread);
        
    	this.add(out_Pn=new JPanel(),BorderLayout.NORTH);//창 위쪽에 나가기와 게임 정보를 생성하기 위한 panel
        out_Pn.add(new JLabel("TIME :"));
        out_Pn.add(time_jl=new JLabel("     "));
        out_Pn.add(game_start_btn=new JButton("Game Start!"));
        
    	out_Pn.add(count_Lab = new JLabel("Round!!")); // 카운트 정보
    	out_Pn.add(room_Exit_Btn = new JButton("나가기")); 
    	out_Pn.add(new JLabel("정답 : ")); // 문제의 정보
        out_Pn.add(answer_Lab=new JLabel());//정답 칸
    	room_Exit_Btn.setBackground(Color.white);
    	out_Pn.setBackground(Color.white);
      
    	this.add(t1_Pn=new JPanel(new GridLayout(3,1,5,5)),BorderLayout.WEST);//좌측 유저들을 위한 panel
    	this.add(t2_Pn=new JPanel(new GridLayout(3,1,5,5)),BorderLayout.EAST);//우측 유저들을 위한 panel
    	t1_Pn.setBackground(Color.white);
    	t2_Pn.setBackground(Color.white);
      
    	t1_Pn.add(basic_p[0]=new JPanel(new BorderLayout()));
       basic_p[0].add(point_p[0]=new JPanel(),BorderLayout.SOUTH);
       point_p[0].add(new JLabel("POINT"));
       point_p[0].add(point_tf[0]=new JTextField(5));
       basic_p[0].add(t1_1_Info_Pn=new JPanel(new GridLayout(1, 2,5,5)));
       t1_1_Info_Pn.add(user_Info[0]=new JLabel("ID"));
       t1_1_Info_Pn.add(area[0]=new JTextArea());
       point_tf[0].setEnabled(false);
       
       t1_Pn.add(basic_p[2]=new JPanel(new BorderLayout()));
       basic_p[2].add(point_p[2]=new JPanel(),BorderLayout.SOUTH);
       point_p[2].add(new JLabel("POINT"));
       point_p[2].add(point_tf[2]=new JTextField(5));
       basic_p[2].add(t1_2_Info_Pn=new JPanel(new GridLayout(1, 2,5,5)));
       t1_2_Info_Pn.add(user_Info[2]=new JLabel("ID"));
       t1_2_Info_Pn.add(area[2]=new JTextArea());        
       point_tf[2].setEnabled(false);
       
    	
      
       
    	t1_Pn.add(t1_Score_Pn=new JPanel());// 좌측 팀의 총점수를 위한 panel
    	t1_Score_Pn.add(new JLabel("정답수"));
    	t1_Score_Pn.add(t1_Score_Fld=new JTextField(5));
    	t1_Score_Fld.setEnabled(false);
    	t1_Score_Pn.setBackground(Color.white);
       
    	t2_Pn.add(basic_p[1]=new JPanel(new BorderLayout()));
       basic_p[1].add(point_p[1]=new JPanel(),BorderLayout.SOUTH);
       point_p[1].add(new JLabel("POINT"));
       point_p[1].add(point_tf[1]=new JTextField(5));
       basic_p[1].add(t2_1_Info_Pn=new JPanel(new GridLayout(1, 2,5,5)));
       t2_1_Info_Pn.add(area[1]=new JTextArea());
       t2_1_Info_Pn.add(user_Info[1]=new JLabel("ID"));
       point_tf[1].setEnabled(false);
        
    	t2_Pn.add(basic_p[3]=new JPanel(new BorderLayout()));
       basic_p[3].add(point_p[3]=new JPanel(),BorderLayout.SOUTH);
       point_p[3].add(new JLabel("POINT"));
       point_p[3].add(point_tf[3]=new JTextField(5));
       basic_p[3].add(t2_2_Info_Pn=new JPanel(new GridLayout(1, 2,5,5)));
       t2_2_Info_Pn.add(area[3]=new JTextArea());
       t2_2_Info_Pn.add(user_Info[3]=new JLabel("ID"));
       point_tf[3].setEnabled(false);
       
    	t2_Pn.add(t2_Score_Pn=new JPanel());//우측 팀의 총 점수를 위한 panel
    	t2_Score_Pn.add(new JLabel("정답수"));
    	t2_Score_Pn.add(t2_Score_Fld=new JTextField(5));
    	t2_Score_Fld.setEnabled(false);
    	t2_Score_Pn.setBackground(Color.white);
        
       
    	this.add(south_Pn=new JPanel(),BorderLayout.SOUTH);//창 하단에 채팅필드를 위한 panel
    	south_Pn.add(new JLabel("CHAT"));
    	south_Pn.add(send_Fld=new JTextField(10));
    	south_Pn.add(send_Btn = new JButton("보내기"));
        south_Pn.add(new JLabel("      "));
        south_Pn.add(ready_btn=new JButton("READY!"));
    	south_Pn.setBackground(Color.white);
       
    	this.add(center_Pn=new JPanel(new BorderLayout()));//가운데에 사진과 색선택 버튼을 올리기 위한 panel
    	center_Pn.add(color_Pn=new JPanel(new GridLayout(9,1,0,5)),BorderLayout.WEST);//좌측에 색상버튼을 올리기 위한 panel
    	center_Pn.setBackground(Color.white);

    	color_Pn.add(red_Btn=new JButton());
    	red_Btn.setBackground(Color.red);

    	color_Pn.add(green_Btn=new JButton());
    	green_Btn.setBackground(Color.green);
       
    	color_Pn.add(blue_Btn=new JButton());
    	blue_Btn.setBackground(Color.blue);
       
    	color_Pn.add(gray_Btn=new JButton());
    	gray_Btn.setBackground(Color.LIGHT_GRAY);
       
    	color_Pn.add(black_Btn=new JButton());
    	black_Btn.setBackground(Color.black);
       
    	color_Pn.add(eraser_Btn=new JButton("지우개"));
    	color_Pn.add(reset_Btn=new JButton("CLEAR"));
    	color_Pn.setBackground(Color.white);
       
    	center_Pn.add(can_Lab=new JLabel());//가운데에 사진을 올리기 위한 label
       	can_Lab.setIcon(new ImageIcon("src/images/11.jpg"));
       	can_Lab.add(can);
       
       	can.setBounds(56,41, 503, 422);//캔버스 크기 조정
       	can.setBackground(Color.yellow);
       	
       	// Area 셋팅!
       	for (JTextArea a : area) {
			a.setEditable(false);
		}

       	// 이벤트 감지자 등록
       	red_Btn.addActionListener(roomEvent);
       	gray_Btn.addActionListener(roomEvent);
       	black_Btn.addActionListener(roomEvent);
       	green_Btn.addActionListener(roomEvent);
       	blue_Btn.addActionListener(roomEvent);
       	eraser_Btn.addActionListener(roomEvent);
       	reset_Btn.addActionListener(roomEvent);
       	send_Btn.addActionListener(roomEvent);
       	send_Fld.addKeyListener(roomEvent);
       	room_Exit_Btn.addActionListener(roomEvent);
        
      
      
       	// **** 대화방 ****
		client.room_exit_Btn.addActionListener(roomEvent);
		client.send_Btn.addActionListener(roomEvent);
		client.send_Fld.addKeyListener(roomEvent);
 
       	// Dialog 셋팅!
       	this.setBounds(200, 100, 900, 600);
       	this.setVisible(true);
       	this.setResizable(false);
       	
    }

	public void sendMsg(String msg){
		// 메시지 보내기 기능
		
		this.send_Fld.setText("");
		try {
			System.out.println("sendMsg.."+msg);
			client.thread.out.writeObject(new Protocol(400, msg));
			client.thread.out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void addMsg(int roomUserIdx, String msg){
		// area에 메시지 출력 
		System.out.println(roomUserIdx);
                this.roomUserIdx=roomUserIdx;
                this.area[roomUserIdx].setText("");
		this.area[roomUserIdx].append(msg);
      		
		
	}
	public void addMsg1(String msg){
		// area에 메시지 출력 (**** 채팅방 ****)
		client.area.append(msg);
		client.area.append("\r\n");
		client.area.setCaretPosition(client.area.getText().length()); // 커서를 밑으로
	}
	public void setRoomUserList(User[] roomUserInfo){
		// 방 접속자 목록 변경 (**** 채팅방 ****)
		client.roomUserInfo = roomUserInfo;
		client.game_User_View.setListData(roomUserInfo);
		client.game_List_Pn.updateUI();
	}
	public void setUserInfo(User[] roomUserInfo){
		// 방의 유저 정보를 배분하여 출력
		for (int i = 0; i < roomUserInfo.length; i++) {
			user_Info[i].setText(roomUserInfo[i].toString());
		}
		for (int i = roomUserInfo.length; i < 4; i++) {
			user_Info[i].setText("ID");
		}
	}
	// 게임 시작 기능
	
    // 캔버스의 그려진 정보를 서버에 전송
    public void sendValue(Point point, Color color, int g_Size){
    	Canvas_Value value = new Canvas_Value(point, color, g_Size);
    	try {
			client.thread.out.writeObject(new Protocol(1000, value));
			client.thread.out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    // 캔버스에 그리기 기능
    public void drawCanvas(Canvas_Value value){
    	can.setPoint(value.getPoint());
    	can.setColor(value.getColor());
    	can.setG_Size(value.getG_Size());
    	can.repaint();
    }
    // 캔버스 클리어 명령 전송
    public void sendClear(){
    	try {
    		System.out.println("sendClear....");
			client.thread.out.writeObject(new Protocol(1050));
			client.thread.out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    // 캔버스 클리어 기능
    public void canvasClear(){
		Graphics g = can.getGraphics(); 
		g.clearRect(0, 0, can.getWidth(), can.getHeight());
		can.repaint();
    }
	public void outRoom(){
		// 방 나가기 기능
		try {
			System.out.println("roomOut...");
			client.thread.out.writeObject(new Protocol(800));
			client.thread.out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
        public void getCap(){//방장권한 얻기
            can.addMouseMotionListener(roomEvent);//그림을 그릴수 있게 함
            game_start_btn.addActionListener(roomEvent);//스타트 버튼 사용 가능
            ready_btn.removeActionListener(roomEvent);//레디 버튼 액션 지우기
            answer_Lab.setText("");//정답 칸 세팅
            
        }
        public void giveCap(){//방장권한 주기
            can.removeMouseMotionListener(roomEvent);//그림 그릴수 있는 권한 없애기
           game_start_btn.removeActionListener(roomEvent);//스타트 버튼 사용 불가
           ready_btn.addActionListener(roomEvent);//레디버튼 사용 가능
            answer_Lab.setText("");
            send_Fld.setEnabled(true);
        }

    @Override
    public void run() {// 시간 스레드
        bk: while(time>0){
           if(time_bb==true){// 게임을 시작했을 때
            time--;
            time_jl.setText(String.valueOf(time));
            System.out.println("time"+time);
            try {
               thread.sleep(1000);
           } catch (Exception e) {
               e.printStackTrace();
           }
           }
           else{// 누군가 정답을 맞췄거나 시간이 다 되서 게임이 끝났을 때
              
               break bk;//반복문 나가기
           }
        }//while
         JOptionPane.showMessageDialog(this, "GAME OVER");
               canvasClear();
               time=60;
               time_jl.setText("");
    }//run 
}

        
