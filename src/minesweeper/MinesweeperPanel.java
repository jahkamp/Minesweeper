package minesweeper;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
/**
 * @coauthor NaShea Wiesner
 * @coauthor Jared Kamp
 */
public class MinesweeperPanel extends JPanel
{
    
    private int size;
    private int squares;
    private Minesweeper game;
    private String diff;    
    private File clickingFile;
    public MinesweeperPanel(Minesweeper game1)
    {
        clickingFile = new File("C:\\Projects\\Java\\Minesweeper\\wav\\Click.wav");
        game = game1;
        diff = game.getDiff();
        switch(diff) //3=expert, 2=intermediate, 1=beginner
        {
            case "Expert": size = 500; break;
            case "Intermediate": size = 250; break;
            case "Beginner": size = 150; break;
            default: size = 150; 
        }
        squares = size/25;
        setPreferredSize(new Dimension(size, size));
        setMaximumSize(new Dimension(size, size)); //lock the window
        addMouseListener(new MinesweeperMouseListener());          
    }    
    public void paint(Graphics g)
    {
        //background
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());        
        //lines (to form squares) ** must be variable based on difficulty lvl(size of window/number of squares) **
        g.setColor(Color.BLACK);
        for(int i = 0; i < squares; i++)
        { 
            int increment = i * 25;            
            g.drawLine(0, increment, size-1, increment);           
            g.drawLine(increment, 0, increment, size-1); 
        }
        g.drawLine(0, size-1, size-1, size-1);
        g.drawLine(size-1, 0, size-1, size-1);
        Font f = new Font("Times", Font.BOLD, 12);
        g.setFont(f);
        FontMetrics fm = g.getFontMetrics();
        
        int a = fm.getAscent();
        int h = fm.getHeight();

        for (int i = 0; i < squares; i++) 
        {
            for (int j = 0; j < squares; j++) 
            {
                String curSquare = game.getSquare(i,j);                
                if(!curSquare.equals(" "))
                {
                    if(curSquare.equals("F"))
                    {
                        g.setColor(Color.BLUE);
                        g.fillRect(i*25+1, j*25+1, 24, 24);
                        g.setColor(Color.WHITE);
                        int w = fm.stringWidth(curSquare);
                        g.drawString(curSquare, i*25 + 12 - w/2, j*25 + 12 + a - h/2);
                    }
                    else if(curSquare.equals("B"))
                    {
                        g.setColor(Color.RED);
                        g.fillRect(i*25+1, j*25+1, 24, 24);
                        g.setColor(Color.BLACK);
                        int w = fm.stringWidth(curSquare);
                        g.drawString(curSquare, i*25 + 12 - w/2, j*25 + 12 + a - h/2);
                    }
                    else
                    {
                        g.setColor(Color.WHITE);
                        g.fillRect(i*25+1, j*25+1, 24, 24);
                        g.setColor(Color.BLACK);
                        int w = fm.stringWidth(curSquare);
                        g.drawString(curSquare, i*25 + 12 - w/2, j*25 + 12 + a - h/2);                        
                    }                     
                }
            }
        }
    }
    private class MinesweeperMouseListener implements MouseListener 
    {
        @Override
        public void mousePressed(MouseEvent e) 
        {            
        }
        public void mouseReleased(MouseEvent e) 
        {            
        }
        public void mouseEntered(MouseEvent e) 
        {
        }
        public void mouseExited(MouseEvent e) 
        {
        }
        public void mouseClicked(MouseEvent e) 
        {
            if(!game.checkFullBoard())
            {
                game.playSound(clickingFile);
                int x = e.getX() / 25;
                int y = e.getY() / 25;
                if(e.getButton() == MouseEvent.BUTTON3)
                    game.setFlag(x,y);
                else if (game.getSquare(x,y).equals(" "))      
                    floodCheck(x,y);    
                repaint(); 
                game.checkForGameEnd(x,y);       
            }
        }
        private void floodCheck(int x, int y)
        {
            //x,y = blank. Every surrounding square needs to be set until no blanks
            //touch unset squares  
            if(game.getSquare(x,y).equals(" "))
            {
                game.setSquare(x,y);
                repaint();                                            
                if(game.getSquare(x,y).equals("  "))
                {                                
                    try
                    {
                        if(y != 0) //check space up one
                            floodCheck(x,y-1);                
                        if(y < squares - 1) //check space down one            
                            floodCheck(x,y+1);
                        if(x != 0)          
                        {
                            floodCheck(x-1,y);  
                            if(y != 0)                
                                floodCheck(x-1,y-1);                
                            if(y < squares - 1)                
                                floodCheck(x-1,y+1);
                        }
                        if(x < squares - 1)      
                        {
                            floodCheck(x+1,y);    
                            if(y != 0)                
                                floodCheck(x+1,y-1);                
                            if(y < squares - 1)                
                                floodCheck(x+1,y+1);
                        }  
                    }
                    catch(Exception e)
                    {                  
                        System.out.println(e.getMessage());
                    }            
                  repaint();
                }
            }                
        }
    }
}
