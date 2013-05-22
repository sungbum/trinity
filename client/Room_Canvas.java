 
package client;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

public class Room_Canvas extends Canvas{

	Room_Form room;
	Image img;

	// 그림을 그리기 위한 정보
	Point point; 
    int g_Size;  
    Color color;
    
    public Room_Canvas(Room_Form room) {
        this.room = room;
        point = new Point(-10, -10);
        g_Size = 10;
        color = Color.BLACK;
    }
    
    @Override
    public void paint(Graphics g) {
         g.setColor(color);
         g.fillOval(point.x-(g_Size/2), point.y-(g_Size/2), g_Size, g_Size );
    }
    

	public void setPoint(Point point) {
		this.point = point;
	}

	public void setColor(Color color) {
    	this.color = color;
    }
    
    public void setG_Size(int g_Size) {
    	this.g_Size = g_Size;
    }

    public int getG_Size(){
    	return g_Size;
    }


	public Color getColor() {
		return color;
	}


	@Override
    public void update(Graphics g) {
        paint(g);
    }
}