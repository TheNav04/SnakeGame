import java.awt.Color;

import javax.swing.*;

//this App Class is to initialize the window
public class App {
	public static void main(String[] args) {
		//Window needs dimensions
		//Going to be a square
		int boardLength = 600;
		
		JFrame window = new JFrame("Snake Game");
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(boardLength, boardLength);
		window.setResizable(false);	
		window.setLocationRelativeTo(null);
		window.setBackground(Color.BLACK);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//components are going to be added in separate class
		Snake snakeGame = new Snake(boardLength, boardLength);
		window.add(snakeGame);
		//.pack() so that top part of window does not interfere with dimensions
		window.pack();
		window.setVisible(true);
		
		//gives input focus to snakeGame JPanel
		snakeGame.requestFocus();
	}
}
