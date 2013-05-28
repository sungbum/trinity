package client;  

    import javazoom.jl.player.Player;  
    import java.io.FileInputStream;  
      
    public class Bgm  
    {  
        public static void main(String[]args)  
        {  
            try  
            {  
                FileInputStream fis=new FileInputStream("./Girls On Fire Um Tom Baixo.wav");  
                Player playMp3=new Player(fis);  
      
                playMp3.play();  
            }  
            catch(Exception e)  
            {  
                System.out.println(e);  
            }  
        }  
    }  