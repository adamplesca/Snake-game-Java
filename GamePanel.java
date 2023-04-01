//Java snake game
// Adam Plesca
// 1/4/23

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener{
	//everything we need to declare before the constructor
	static final int SCREEN_WIDTH = 800;
	static final int SCREEN_HEIGHT= 800;
	//how big the objects/items are in this game
	static final int UNIT_SIZE = 40;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	//higher delay slower the snake moves
	static final int DELAY = 75;
	// these arrays holds the whole body co-ordinates of the snake including the head
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	//begin with 6 body parts on the snake
	int bodyParts = 6;

	int applesEaten;
	int appleX;
	int appleY;
	//direaction which the snake starts moving in
	char direaction = 'R';
	boolean running = false;
	//cdeclaring object
	Timer timer;
	Random random;
	//constructor (creates an object)
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	public void startGame(){
		//creates an apple object
		newApple();
		running = true;
		//creates timer when game starts
		timer = new Timer(DELAY, this);
		timer.start();
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		draw(g);
	}
	public void draw(Graphics g){
		//does everything in the if statement if the games running
		if(running) {
			//creating a grid/matrix to see what we want easier
			/*for(int i = 0;i<SCREEN_HEIGHT/UNIT_SIZE;i++){
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
			}*/
			//drawing the apple
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			//drawing the head of the snake + body
			for(int i = 0; i < bodyParts; i++){
				if(i == 0){
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE); //fill rectangle method to fill the color for the head
				}
				else{
					g.setColor(new Color(45, 180, 0));
					//g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255))); //makes the snake randomise colors
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE); //fill rectangle method to fill the color for the body
				}
			}
			//same code but alters slightly to give live update of apples being eaten when playing
			g.setColor(Color.red);
			g.setFont(new Font("Ink Free", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize()); //this will give us the string "Score : " in the top center of the screen
		}
		else {
			gameOver(g); //g = graphics that we receive from using the "Graphics g" parameter
		}
	}
	public void newApple(){
		//creating the code to randomise the apples position on the screen
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;

	}
	//moving the snake with this method
	public void move(){
		//basically shifting all the bodyparts of the snake around by shuffling the array
		for(int i = bodyParts; i>0; i--){
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		//creating a switch that changes the direaction of the snake
		switch(direaction){
			case 'U':
				y[0] = y[0] - UNIT_SIZE; //y co-ordinate of the head of the snake (y[0])
				break;
			case 'D':
				y[0] = y[0] + UNIT_SIZE;
				break;
			case 'L':
				x[0] = x[0] - UNIT_SIZE;
				break;
			case 'R':
				x[0] = x[0] + UNIT_SIZE;
				break;
		}
	}
	public void checkApple(){
		if((x[0] == appleX) && (y[0] == appleY)){ // if the x[0] or y[0] head touches an apple it does the following:
			bodyParts++; //makes the body bigger if an apple is eaten
			applesEaten++; //keeps track of apples eaten
			newApple(); //generates new apple
		}
	}
	public void checkCollisions(){
		//checks if the head collides with body
		for(int i = bodyParts; i > 0; i--){
			if((x[0] == x[i]) && (y[0] == y[i])){ // x[0] + y[0] is the snakes head + x[i] y[i] is the body IF the head hits the body the game stops (GAMEOVER)
				running = false;
			}
		}
		//check if head touches touches left border
		if(x[0] < 0) {
			running = false;
		}
		//check if head touches touches right border
		if(x[0] > SCREEN_WIDTH) {
			running = false;
		}
		//check if head touches touches top border
		if(y[0] < 0) {
			running = false;
		}
		//check if head touches touches bottem border
		if(y[0] > SCREEN_WIDTH) {
			running = false;
		}
		//if not running/ hits something timer stops
		if(!running) {
			timer.stop();
		}
	}
	public void gameOver(Graphics g){
		//final score text
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		FontMetrics finalMetrics = getFontMetrics(g.getFont());
		g.drawString("Final Score: " + applesEaten, (SCREEN_WIDTH - finalMetrics.stringWidth("Final Score: " + applesEaten))/2, g.getFont().getSize()); //this will give us the string "Score : " in the top center of the screen

		//Game over text
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Game Over :(", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2); //this will give us the string "Game Over :(" in the center of the screen when the player dies
	}
	public void actionPerformed(ActionEvent e){

		if(running){
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e){
		//code that allows you to control the snake
			switch(e.getKeyCode()) {
				case KeyEvent.VK_LEFT: //if direaction does not equal RIGHT then let them go LEFT
					if(direaction != 'R') {
						direaction = 'L';
					}
					break;
				case KeyEvent.VK_RIGHT: //if direaction does not equal LEFT then let them go RIGHT
					if(direaction != 'L') {
						direaction = 'R';
					}
					break;
				case KeyEvent.VK_UP: //if direaction does not equal DOWN then let them go UP
					if(direaction != 'D') {
						direaction = 'U';
					}
					break;
				case KeyEvent.VK_DOWN: //if direaction does not equal UP then let them go DOWN
					if(direaction != 'U') {
						direaction = 'D';
					}
					break;
			}
		}
	}
}