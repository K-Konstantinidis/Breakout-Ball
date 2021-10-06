import java.awt.event.*;
import java.util.Random;

import javax.swing.*;

import java.awt.*;

public class Gameplay extends JFrame implements KeyListener, ActionListener {
	private static final long serialVersionUID = 1L;
	
	boolean play = false; //If game ends, play is equal to false else it is true
	boolean gameOver = false;
	
	Timer timer = new Timer(40, this);
	
	int playerX = 300; //X coordinate of the START of the paddle

	//Starting ball coordinates
	int ballposX = 350 - 10; //Ball X coordinates => 350 is the center of 700(Width) and - 10 because the ball is 20 pixels long (20/2=10)
	int ballposY = 350; //Ball Y coordinates
	
	int ballXdir; //Direction of ball X coordinates
	int ballYdir; //Direction of ball Y coordinates
	
	/*Nothing will happen while you are playing and you are pressing the [SPACE] key 
	& nothing will happen if you try to move the paddle before starting the game*/
	boolean nothing = false; 
	
	int score = 0;
	
	private Bricks bricks;
	
	Random row = new Random(); //Get random row number every time a new game starts
	Random column = new Random(); //Get random column numbber every time a new game starts
	int rows, columns, totalBricks;
	
	public Gameplay() {		
		createBricks();

		play = true; //Game ready to start 

		this.setSize(700, 600);
		this.setTitle("Breakout Ball");		
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.addKeyListener(this);
		this.setVisible(true);
	}
	
	public void paint(Graphics g) { 
		//Background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 700, 600);
		
		//Drawing the bricks
		bricks.draw(g);
		
		//Score	
		g.setColor(Color.WHITE);
		g.setFont(new Font("Times New Roman",Font.BOLD, 13));
		g.drawString("Your score is: " + score, 590, 45);
		
		//Ball
		g.setColor(Color.BLUE);
		g.fillOval(ballposX, ballposY, 20, 20);
		
		//Paddle
		g.setColor(Color.GREEN);
		g.fillRect(playerX, 550, 100, 8);

		//If you lost
		if(ballposY >= 600) {
			gameOver = true;
			end(g);
            g.setColor(Color.RED);
            g.drawString("Game Over!", 265, 100);
            showMessage(g);
        }
		
		//If you won
		if(totalBricks <= 0) {
			end(g);
            g.setColor(Color.GREEN);
            g.drawString("You Won!", 275, 100);
            showMessage(g);
		}
		
		g.dispose();
	}	
	
	public void end(Graphics g) {
		timer.stop();
		
		//Background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 700, 600);
		
		play = false; //Game ended
        ballXdir = 0; //Stop ball
 		ballYdir = 0; //Stop ball
 		
 		g.setFont(new Font("ARIAL",Font.PLAIN, 30));
	}
	
	public void showMessage(Graphics g) {
		g.setFont(new Font("ARIAL",Font.BOLD, 20));
		g.drawString("Your score is: " + score, 270, 300);
		g.setColor(Color.WHITE);
		g.setFont(new Font("ARIAL",Font.BOLD, 20));           
		g.drawString("Press [SPACE] to Restart", 230, 400);  
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
		{      
			if(nothing) { //If game is on
				if(playerX >= 590) //If paddle hits the right border
					playerX = 590;
				else
					moveRight();
			}
        }
		
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
		{         
			if(nothing) { //If game is on
				if(playerX <= 10) //If paddle hits the left border
					playerX = 10;
				else
					moveLeft();
			}
        }	
		 
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
		{          
			if(!play) //If it's after winning/losing a game  AN EXEIS KERDISEI NA MHN KANEI TO SCORE 0
			{
				play = true;
				playerX = 300;
				ballposX = 350 - 10;
				ballposY = 400;
				ballDir();
				if(gameOver) //If you lost make score 0 else keep adding
					score = 0;
				createBricks();
				
				timer.start();
			}
			else if(!nothing) //If it's the first time playing
			{
				nothing = true;
				ballDir();
				timer.start();
			}
        }		
	}
	
	public void ballDir() {
		ballXdir = 0;
		ballYdir = 10;
	}
	
	public void createBricks() {
		rows = 3 + row.nextInt(2); //Random rows between(3-4)
		columns = 4 + column.nextInt(3); //Random columns between(4-6)
		
		bricks = new Bricks(rows, columns);
		totalBricks = rows * columns; //Get the total number of the bricks
	}
	
	public void moveRight() {
		play = true;
		playerX += 20;	//Move 20 pixels right
	}
	
	public void moveLeft() {
		play = true;
		playerX -= 20; //Move 20 pixels left	 	
	}
	
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}

	public void actionPerformed(ActionEvent e) {
		boolean flag = false;
		
		if(play) {
			int brickWidth = bricks.brickWidth; //Get brick width
			int brickHeight = bricks.brickHeight; //Get brick height
			int brickX;
			int brickY;
			Rectangle ball = new Rectangle(ballposX, ballposY, 20, 20);
			
			//If ball hits the paddle somewhere in the first 30 pixels
			if(ball.intersects(new Rectangle(playerX, 550, 30, 8))) {
				ballYdir = -ballYdir; //Make ball go uwards
				ballXdir -= 2; //Make ball go more left
			}
			//If ball hits the paddle somewhere in the last 30 pixels
			else if(ball.intersects(new Rectangle(playerX + 70, 550, 30, 8))) {
				ballYdir = -ballYdir; //Make ball go uwards
				ballXdir++; //Make ball go more right
			}
			//If ball hits the paddle somewhere between the pixels 30 - 70
			else if(ball.intersects(new Rectangle(playerX + 30, 550, 40, 8)))
				ballYdir = -ballYdir; //Make ball go uwards
			
			//Check bricks collision with the ball		
			for(int i = 0; i < bricks.bricks.length; i++) {
				for(int j = 0; j < bricks.bricks[0].length; j++) {				
					if(bricks.bricks[i][j] > 0) {
						brickX = j * brickWidth + 80; //Get brick x position
						brickY = i * brickHeight + 50; //Get brick y position
						
						Rectangle brickRect = new Rectangle(brickX, brickY, brickWidth, brickHeight); //Make a brick
						
						if(ball.intersects(brickRect)) {
							bricks.setBrickValue(i, j); //Make brick value from 1 => 0
							score += 5; //Add +5 for every broken brick	
							totalBricks--; //Remove a brick from the total
							
							if(ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) //If ball hits left or right of a brick
								ballXdir = -ballXdir; //Change the X direction of the ball
							else //If ball hits top or bottom of a brick
								ballYdir = -ballYdir; //Change the Y direction of the ball
							
							flag = true;
							break;
						}
					}
				}
				if(flag)
					break;
			}
			
			//Change ball position
			ballPos();
			
			//If ball hits the 3 walls change its directions
			if(ballposX < 0) 
				ballXdir = -ballXdir;
			else if(ballposX > 680)
				ballXdir = -ballXdir;
			
			if(ballposY < 20)
				ballYdir = -ballYdir;
			
			repaint();
		}
	}
	
	public void ballPos() {
		ballposX += ballXdir;
		ballposY += ballYdir;
	}
}