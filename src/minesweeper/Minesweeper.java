package minesweeper;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import javax.swing.*;
import java.util.Random;
import java.util.Scanner;
import javax.sound.sampled.*;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
/**
 * @coauthor NaShea Wiesner
 * @coauthor Jared Kamp
 */
public class Minesweeper extends JFrame
{          
    private File openingFile;
    private File closingFile;
    private File explodingFile;
    private File winningFile;
    private File flaggingFile;
    private File restartFile;
    private AudioStream stream;
    private AudioFormat format;
    private Random rand = new Random();
    private String[][] playingBoard;
    private String[][] mineBoard;
    private int size;
    private int squares;
    private int mineCount;
    private String diff;
    private JButton restartBtn;
    private MinesweeperPanel msP;
    private FileInputStream in;
    public Minesweeper()
    {
        super("Minesweeper");        
        try
        {                        
            //Should not hardcode file paths, but I couldn't seem to figure out a way
            //to access the wav folder without a complete path.
            openingFile = new File("C:\\Projects\\Java\\Minesweeper\\wav\\Open.wav");
            explodingFile = new File("C:\\Projects\\Java\\Minesweeper\\wav\\Bomb.wav");
            winningFile = new File("C:\\Projects\\Java\\Minesweeper\\wav\\Win.wav");
            flaggingFile = new File("C:\\Projects\\Java\\Minesweeper\\wav\\Flag.wav");
            restartFile = new File("C:\\Projects\\Java\\Minesweeper\\wav\\Restart.wav");
            playSound(openingFile);
        }
        catch(Exception e)
        {
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startUp();
    }
    private void startUp()
    {                 
        setConfigurationOptions();
        squares = size/25;
        playingBoard = new String[squares][squares]; // initialize playingBoard        
        mineBoard = new String[squares][squares]; // initialize playingBoard 
        for(int i = 0; i < squares; i++)
        {
            for(int j = 0; j < squares; j++)
            {
                playingBoard[i][j] = " ";
                mineBoard[i][j] = " ";
            }
        }
        placeMines(mineCount);
        restartBtn = new JButton("Restart");
        restartBtn.addActionListener(new ToggleState());
        msP = new MinesweeperPanel(this);
        add(msP, BorderLayout.CENTER);
        add(restartBtn, BorderLayout.SOUTH);       
        pack();        
        setLocationRelativeTo(null);
        setVisible(true); // Show the JFrame.
    }
    private class ToggleState
            implements ActionListener 
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            try
            {
                close();
                playSound(restartFile);
                startUp();            
            }
            catch(Exception ex)
            {            
            }
            System.out.println("Game Restarted. You lose.");
        }
    }
    private void close()
    {        
        try
        {
            this.dispose();
            this.remove(msP);
            this.remove(restartBtn);
        }
        catch(Exception e)
        {
        
        }
    }
    public String getDiff()
    {       
        return diff;
    }
    // The placeMines method must randomly place mines and set the numbers for whether the square touches a bomb(s).
    private void placeMines(int mineCount)
    {   
        //place bombs
        while(mineCount != 0)
        {
            int i = rand.nextInt(squares);
            int j = rand.nextInt(squares);
            //place mines
            mineBoard[i][j] = "B"; 
            mineCount--;        
        }
        //assign numbers
        int bombsTouching;
        for (int i = 0; i < squares; i++) 
        {
            for (int j = 0; j < squares; j++) 
            {           
                bombsTouching = 0;               
                if(mineBoard[i][j] != "B") //don't bother counting bombs for bomb squares
                {                    
                    if(j != 0) //array out of bounds
                        if(mineBoard[i][j-1] == "B") //one space up                    
                            bombsTouching++;                                                                                               
                    if(j < squares-1) //array out of bounds
                        if(mineBoard[i][j+1] == "B") //one space down                       
                                bombsTouching++;                                                                                                                                     
                    if (i != 0)
                    {
                        if(mineBoard[i-1][j] == "B") //one space back
                            bombsTouching++;  
                        if(j != 0)
                           if(mineBoard[i-1][j-1] == "B") //one space back & one space up                        
                                bombsTouching++; 
                        if(j < squares-1)
                            if(mineBoard[i-1][j+1] == "B") //one space back & one space down
                                bombsTouching++;                                                                                                                                                
                    }
                    if (i < squares-1)
                    {                        
                        if(mineBoard[i+1][j] == "B") //one space forward
                            bombsTouching++;  
                        if(j != 0)
                           if(mineBoard[i+1][j-1] == "B") //one space forward & one space up                        
                                bombsTouching++; 
                        if(j < squares-1)
                            if(mineBoard[i+1][j+1] == "B") //one space forward & one space down
                                bombsTouching++;                                                                                                                                               
                    }
                    //set number
                    String bt = ""+ bombsTouching + "";
                    if(bombsTouching > 0)
                    {
                        mineBoard[i][j] = bt; 
                    }
                    else
                        mineBoard[i][j] = "  ";
                }
            }
        }        
    }   
    public void setSquare(int x, int y) 
    {
        playingBoard[x][y] = mineBoard[x][y];    
        if(playingBoard[x][y].equals("B"))
            try
            {
                in = new FileInputStream(explodingFile);
                stream = new AudioStream(in);
                // play the audio clip with the audioplayer class
                AudioPlayer.player.start(stream);     
            }
            catch(Exception e)
            {        
                System.out.println(e.getMessage());
            }
    }
    public void playSound(File audioFile)
    {
        try
        {
            in = new FileInputStream(audioFile);
            stream = new AudioStream(in);
            // play the audio clip with the audioplayer class
            AudioPlayer.player.start(stream);   
        }
        catch(Exception e)
        {            
        }
    }
    public void setFlag(int x, int y) 
    {
        playSound(flaggingFile);
        //toggle the flag on/off        
        if(playingBoard[x][y] == " ")
            playingBoard[x][y] = "F";     
        else if(playingBoard[x][y] == "F")
            playingBoard[x][y] = " ";
    }
    public String getSquare(int x, int y) 
    {
        return playingBoard[x][y];
    }
    public void printBoard() 
    {
        for (int i = 0; i < squares; i++) 
        {
            for (int j = 0; j < squares; j++) 
            {
                System.out.print(" " + playingBoard[i][j]);
            }
            System.out.println();
        }
    }
    public void revealBoard() 
    {
        //show every square but maintain correct flags set by the user.
        for(int i = 0; i < squares; i++)
        {
            for(int j = 0; j < squares; j++)
            {
                if(!(playingBoard[i][j].equals("F") && mineBoard[i][j].equals("B")))
                    playingBoard[i][j] = mineBoard[i][j];
            }
        }
        repaint();
    }
    // check all columns in every row for any exposed bombs
    private String checkForWin()
    {
        int bombsBlown = 0;        
        for (int i = 0; i < squares; i++) 
        {
            for (int j = 0; j < squares; j++) 
            {                
                if(playingBoard[i][j] == "B")
                    bombsBlown++;
            }          
        }
        if(bombsBlown == 0)
        {
            playSound(winningFile);
            return "You win!";
        }
        else
            return "You lose.";        
    }
    // check all columns in every row for any remaining blank spaces
    public boolean checkFullBoard() 
    {
        for (int i = 0; i < squares; i++) {
            for (int j = 0; j < squares; j++) 
            {
                if(playingBoard[i][j] == " ")
                    return false;
            }
        }
        return true;
    }
    public void checkForGameEnd(int x, int y) 
    {
        if (checkFullBoard() || getSquare(x,y) == "B") 
        {
            revealBoard();
            JOptionPane.showMessageDialog(this, "Jared Alan Kamp, " + checkForWin());                                   
        }
    } 
    private void setConfigurationOptions()
    {        
        // Data Fields
        String[] selections = {"Beginner", "Intermediate", "Expert"};
        JOptionPane jOptPn = new JOptionPane();
        Object selectedValue = jOptPn.showInputDialog(this,
            "Please select a difficulty level.", "Difficulty Options",
            JOptionPane.QUESTION_MESSAGE, null,
            selections, selections[0]); 
        if(selectedValue==null)        
            System.exit(0);           
        else
            diff = selectedValue.toString().trim();
        switch(diff) //0=hard, 1=medium, 2=easy
        {           
            case "Expert": size = 500; mineCount = 99; break;
            case "Intermediate": size = 250; mineCount = 40; break;
            case "Beginner": size = 150; mineCount = 10; break;
            default: size = 150; mineCount = 10;
        }
    }   
    public static void main(String[] args) 
    {
        Minesweeper newGame = new Minesweeper();
    }    
}
