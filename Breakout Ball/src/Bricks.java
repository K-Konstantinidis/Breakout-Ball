import java.awt.Color;
import java.awt.Graphics;

public class Bricks 
{
	int bricks[][], brickWidth, brickHeight, i, j;
	static final int WIDTH = 540; //540 pixels for the width of all the bricks combined 
	static final int HEIGHT = 200; //200 pixels for the height of all the bricks combined 
	
	public Bricks (int row, int col) {
		bricks = new int[row][col];	
		
		for(i = 0; i < row; i++) {
			for(j = 0; j < col; j++) {
				bricks[i][j] = 1; //Value = 1 to show that this brick is still active and has not been hit
			}
		}
		
		brickWidth = WIDTH / col; //Width of brick
		brickHeight = HEIGHT / row; //Height of brick
	}	
	
	public void draw(Graphics g) {
		for(i = 0; i < bricks.length; i++) {
			for(j = 0; j < bricks[0].length; j++) {
				if(bricks[i][j] > 0) { //If brick value is 1 then draw it else it's been broken so no need to draw it
					g.setColor(Color.red);
					g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight); //Make brick red
					g.setColor(Color.black);
					g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, 1); //Add black lines between them to seperate them
					g.fillRect(j * brickWidth + 80, i * brickHeight + 50, 1, brickHeight);			
				}
			}
		}
	}
	
	public void setBrickValue(int row, int col) {
		bricks[row][col] = 0; //Set the value to 0 because the brick is broken
	}
}