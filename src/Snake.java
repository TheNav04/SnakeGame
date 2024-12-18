import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;
import javax.swing.JPanel;


//initialize entities in this class
//this class is a subclass of JPanel
//will behave as a JPanel
public class Snake extends JPanel implements ActionListener, KeyListener{
	//properties of board
	int boardHeight = 600;
	int boardLength = 600;
	int tileSize = 25;
	boolean gameOver = false;
	
	int velocityX; //- goes to the left, + goes to the right
	int velocityY; //- goes upwards, + goes downward
	
	Tile snakeHead;
	Tile food;
	
	Random rand;
	
	//Need TIMER to Animate the frames and makes things move
	//GameLogic
	Timer gameLoop;
	
	//each part of the snake will be connected to its neighbour
	//this is probably best to use
	//contains the object and address for the next and previous object
	ArrayList<Tile> snakeBody;
	//what methods are available
		//get method for index
		//set method for index
		//add last
		//add first
		//clear all
		//get size	
		
	public Snake(int boardH, int boardL) {
		this.boardHeight = boardH;
		this.boardLength = boardL;

		setPreferredSize(new Dimension(this.boardHeight, this.boardLength)); 
		setBackground(Color.black);
		//to allow listener to listen
		//add method
		addKeyListener(this);
		setFocusable(true);
		
		snakeHead = new Tile(5,5);
		
		//must be placed inside so it can update
		snakeBody = new ArrayList<Tile>();
		food = new Tile(10,10);		
		rand = new Random();
		foodPlacer();
		
		//creates a tick every 100ms
		gameLoop = new Timer(100, this);
		gameLoop.start();
		
		
		//to update the x and y position we need timer
		//then action needs to be capable of being rePainted
		//then we need the ability to change snake location
		velocityX = 0;
		velocityY = 0;
	}
	
	//draw components within JPanel
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		//grid lines
		//square grip with 25 by 25 tile per one space
		int row = 0;
		for(int i = 0; i < boardHeight/tileSize; i++) {
			g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);
			g.drawLine(0, i*tileSize, boardLength, i*tileSize);
			
		}
		
		//snake
		g.setColor(Color.green);
		g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize);
		
		//snakeBody
		for(int i = 0; i < snakeBody.size(); i++) {
			Tile snakePart = snakeBody.get(i);
			g.fillRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize);
		}
		
		//food
		g.setColor(Color.red);
		g.fillRect(food.x * 25, food.y * 25, tileSize, tileSize);
	}
	
	//Random Food Location
	public void foodPlacer () {
		//should not place food where snake already is
		food.x = rand.nextInt(boardHeight/tileSize);
		food.y = rand.nextInt(boardHeight/tileSize);
	}
	
	
	public void foodMucher() {}
	
	public boolean collisionCheck(Tile tile1, Tile tile2) {
		return tile1.y*25 == tile2.y*25 && tile1.x*25 == tile2.x*25;
	}
	
	//just needed to update x and y position
	//happens every tick
	public void moveIt() {
		//snake body
		if(collisionCheck(snakeHead, food) == true) {
			snakeBody.add(new Tile(food.x, food.y));
			foodPlacer();
		}
		
		//collision with boarder 
		if(snakeHead.x < 0 || snakeHead.y < 0 || 
				snakeHead.x * tileSize >= boardHeight || snakeHead.y * tileSize >= boardHeight) {
			
			gameOver = true;
		}
		
		//if collision with body
		//collision method already exists
		//iterate through all snakeBody and check if it shares a location with snakeHead
		for(int i = snakeBody.size() - 2; i > 0; i--) {
			Tile partOfSnake = snakeBody.get(i);
				if(collisionCheck(partOfSnake, snakeHead)) {
					gameOver = true;
				}					
		}
		
		
		for(int i = snakeBody.size() - 1; i >= 0; i--) {
			Tile snakePart = snakeBody.get(i);
			if(i == 0) {
				snakePart.x = snakeHead.x;
				snakePart.y = snakeHead.y;
			}
			else {
				Tile prevSnakeBit = snakeBody.get(i-1);
				snakePart.x = prevSnakeBit.x;
				snakePart.y = prevSnakeBit.y; 
			}
		}
		
		//snakeHead
		snakeHead.x += velocityX;
		snakeHead.y += velocityY;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//makes the draw function go again
		if(gameOver == true) {
			gameLoop.stop();
			System.out.println("Game Over: You Suck!");
		}
		else {
			moveIt();
			this.repaint();			
		}

		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
			velocityX = 0;
			velocityY = -1;
		}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
			velocityX = 0;
			velocityY = 1;
		}
		else if(e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
			velocityX = -1;
			velocityY = 0;
		}
		else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1){
			velocityX = 1;
			velocityY = 0;
		}
		else if (e.getKeyCode() == KeyEvent.VK_SPACE && gameOver == true) {
			snakeBody.clear();
			velocityX = 0;
			velocityY = 0;
			snakeHead.x = 5 * tileSize;
			snakeHead.y = 5 * tileSize;
			gameOver = false;
			gameLoop.start();
			
		}
	}
	
	//will not be used
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}
	
	
}
