/*
 * File: BreakoutExtra.java
 * -------------------
 * Name: Matthew Abbott
 * Section Leader: Meredith Marks
 * 
 * This file implements an "upgraded" version of the game breakout
 * I added:
 * the ball speeds up if it hits the orange layer (only once, resets after death)
 * the bal x velocity changes based on where the ball hits the paddle
 * chance to add a penalty row to the main body of bricks every time the player destroys a brick. 
 * chance to add a savior row below the paddle every time the player destroys a brick
 * that color changing stuff on win and loss is still in here too, but is also in my basic version
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class BreakoutExtra extends GraphicsProgram {

	/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

	/** Dimensions of game board
	 *  Should not be used directly (use getWidth()/getHeight() instead).
	 *  * */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

	/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

	/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

	/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

	/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

	/** Separation between bricks */
	private static final int BRICK_SEP = 2;

	/** Width of a brick */
	private static final int BRICK_WIDTH =
		(WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

	/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

	/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

	/** Number of turns */
	private static final int NTURNS = 3;

	// the height of one row + it's separation from the next
	private static final int ROW_HEIGHT = BRICK_HEIGHT + BRICK_SEP;


	/* Method: run() */
	public void init(){
		addMouseListeners();
	}

	/** Runs the Breakout program. */
	public void run() {
		setUpBricks();
		rowLevel = getHeight() - 5;		// if a new row of bricks is created, the top of each brick will be 5 pixels off the bottom of the screen. This applies much later in the program
		setUpPaddle();
		setUpBall();
		waitForClick();		//the program doesn't start the motion of the ball until the player clicks
		setBallVelocity();
		while(ball.getY() <= getHeight()) {
			moveBall();
			GObject collider = getCollidingObject();
			if (collider == paddle){
				ball.setLocation(ball.getX(), paddle.getY() - 3 * BALL_RADIUS - 4); //moves the ball right above the paddle. This is for cases where the ball hits the side of the paddle.
				vy = -vy;
				if (ball.getX() <= paddle.getX() + PADDLE_WIDTH / 3) {
					vx -=1;
				} else if (ball.getX() >= paddle.getX() + (2 * PADDLE_WIDTH) / 3) {	// if the ball lands on the left third of the paddle, vx is incremented 1 to the left, and if it lands on the right 3rd, it is incremented 1 to the right
					vx +=1;
				}

			} else if (collider != null){
				if (collisionOnSide) { // tells the program which side the ball hit
					vx = -vx;
				} else {
					vy = -vy;
				}
				if (collider.getColor() == Color.ORANGE) {	//if the ball hits an orange block, its y speed increases to 4 from 3. this brings us up to a greater speed once the player has reached the orange layer.
					if (vy < 0) {
						vy = -4;
					} else {
						vy = 4;
					}
				}
			
				if (collider.getColor() != Color.GRAY) {
					bricksRemaining -=1;
				}
				
				remove(collider);
				int powerUp = rgen.nextInt(1,100);	// on brick hit, the ball has a 1/100 chance of earning a power up, which adds a layer of bricks to the bottom of the screen
				if (powerUp == 100) {
					if (rowLevel >= getHeight() - (PADDLE_Y_OFFSET - 8)) { // limits the number of savior rows that can be added
						addBrickRow();
						rowLevel -= BRICK_HEIGHT + BRICK_SEP;		//this increases the row level by the height and separation of one brick in case a new row of power up bricks is created
						GLabel powerUpLabel = new GLabel ("Power Up: Savior row added. You don't have to destroy these to win.", 0, 11);
						powerUpLabel.setColor(Color.GRAY);
						add(powerUpLabel);
					}

				} else if (powerUp == 1) {	//if 1 shows up, that means the player has randomly critically failed, and a new row of bricks is added immediately below the primary set of bricks
					int placeHolder = rowLevel; //I didn't think this through very well, so this int holds the powerup value for rowlevel while i change it to the critical failure value
					critFails += 1;
					rowLevel = BRICK_Y_OFFSET + (9 + critFails) * ROW_HEIGHT;
					if (rowLevel <= BRICK_Y_OFFSET + 15 * ROW_HEIGHT) {// limits the number of extra rows that can be added
						addBrickRow();
					}
					rowLevel = placeHolder;
					GLabel powerDownLabel = new GLabel ("Power Down: Penalty row added. You don't have to destroy these to win.", 0, 24);
					powerDownLabel.setColor(Color.GRAY);
					add(powerDownLabel);
				}
			}
			if (bricksRemaining <= 0) { //end condition: breaks while loop
				break;
			}
			if (ballsRemaining <= 0) {  //end condition: breaks while loop
				break;
			}

			pause(20);		// reducing the pause time increases the speed of the ball. increasing it does the opposite, but the game looks choppier with a high pause time, so it's better to decrease speed a different way.
		}


		//this last bit of stuff is extra and unrelated to the game's function
		if (bricksRemaining <= 0){		//if the bricks being destroyed is what ended the game, the player wins
			paddle.setFillColor(Color.GREEN);
			ball.setFillColor(Color.BLUE);		//the ball and paddle change colors in celebration.
			GLabel victory = new GLabel ("You Win.", getWidth() / 2, getHeight() / 2);
			victory = new GLabel ("You Win.", getWidth() / 2 - victory.getWidth() / 2, getHeight() / 2 - victory.getHeight() / 2);
			add(victory);
		} else if (ballsRemaining <= 0){ // otherwise, the player loses
			GLabel loss = new GLabel ("You Lose.", getWidth() / 2, getHeight() / 2);
			loss = new GLabel ("You Lose.", getWidth() / 2 - loss.getWidth() / 2, getHeight() / 2 - loss.getHeight() / 2);
			add(loss);
			paddle.setFillColor(Color.RED);
			pause(1000);
			paddle.setFillColor(Color.WHITE);
			paddle.setColor(Color.RED);
			pause(1000);
			paddle.setColor(Color.WHITE);		// the paddle appears to be destroyed
		}
	}

	/*
	 * setUpBricks is a private method I created that does everything necessary
	 * to create the bricks in the correct location
	 * it first sets the height of the 1st row to the BRICK_Y_OFFSET constant, then
	 * runs a for loop that adds a row of bricks, then shifts the height of the next row down
	 * by the height of one brick + the length of separation between bricks
	 * pre-condition: no bricks
	 * post-condition: yes bricks
	 */

	private void setUpBricks() {
		rowLevel = BRICK_Y_OFFSET;
		for (int i = 0; i < NBRICK_ROWS; i++) {
			addBrickRow();
			rowLevel += (ROW_HEIGHT);
		}
	}

	/*
	 * addBrickRow adds a single row of bricks
	 * it starts by resetting the x value of the brick (brickX) to it's initial value, usually 0
	 * next, it runs a loop that defines the location and dimensions of the brick,
	 * set's the brick as filled,
	 * calls a method that sets the brick's color
	 * then adds the brick
	 * finally, it increments the x value of the brick for the next iteration of the loop
	 */

	private void addBrickRow() {
		int brickX = (getWidth() - WIDTH) /2;	
		for (int i = 0; i < NBRICKS_PER_ROW; i++) {
			brick = new GRect(brickX, rowLevel, BRICK_WIDTH, BRICK_HEIGHT);
			brick.setFilled(true);	
			setBrickColor();
			add(brick);
			brickX += BRICK_WIDTH + BRICK_SEP;
		}
	}

	/*
	 * setBrickColor changes the color of the bricks to whatever is appropriate
	 * for the row that that brick is in
	 * it uses the rowLevel variable, checking it's current value to determine which row the brick is
	 */
	private void setBrickColor(){
		if (rowLevel <= BRICK_Y_OFFSET + ROW_HEIGHT) {
			brick.setColor(Color.RED);
			brick.setFillColor(Color.RED);
		} else if (rowLevel <= BRICK_Y_OFFSET + 3 * ROW_HEIGHT) {
			brick.setColor(Color.ORANGE);
			brick.setFillColor(Color.ORANGE);
		} else if (rowLevel <= BRICK_Y_OFFSET + 5 * ROW_HEIGHT) {
			brick.setColor(Color.YELLOW);
			brick.setFillColor(Color.YELLOW);
		} else if (rowLevel <= BRICK_Y_OFFSET + 7 * ROW_HEIGHT) {
			brick.setColor(Color.GREEN);
			brick.setFillColor(Color.GREEN);
		} else if (rowLevel <= BRICK_Y_OFFSET + 9 * ROW_HEIGHT) {
			brick.setColor(Color.CYAN);
			brick.setFillColor(Color.CYAN);
		} else {
			brick.setColor(Color.GRAY);		// this is the color of power up bricks that line the bottom of the screen, preventing the ball from falling
		}
	}


	private GRect brick; // instance variable: the bricks that make up the top of the breakout game
	private int rowLevel; // instance variable: the height of the current row of bricks, doubles as the y coordinate for the bricks
	private int bricksRemaining = NBRICK_ROWS * NBRICKS_PER_ROW; //number of bricks left in the game, starts at this value.
	private int critFails = 0;	// this is the number of critical failures the player has gotten with the power up rgen so far. It changes the location of the next critical fail brick row.

	/*
	 * setUpPaddle adds the paddle to the program
	 */

	private void setUpPaddle() {
		paddle = new GRect((getWidth() / 2) - (PADDLE_WIDTH / 2), (getHeight() - PADDLE_Y_OFFSET), PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFilled(true);
		paddle.setColor(Color.BLACK);
		paddle.setFillColor(Color.BLACK);
		add(paddle);
	}

	/*
	 * this mouse moved method changes the x coordinate of the paddle to that off the mouse (sor of. it is offset by helf the paddle width so the mouse cursor is on the middle of the paddle)
	 * if the paddle would go off screen, the two else if situations set its location to touching the side but not off it
	 */
	public void mouseMoved(MouseEvent e) {
		if ((PADDLE_WIDTH/2) < e.getX() && (getWidth() - PADDLE_WIDTH/2) > e.getX()) {
			paddle.setLocation((e.getX() - (PADDLE_WIDTH / 2)), (getHeight() - PADDLE_Y_OFFSET));
		} else if ((PADDLE_WIDTH/2) > e.getX()) {
			paddle.setLocation(0, getHeight() - PADDLE_Y_OFFSET);
		} else if ((getWidth() - PADDLE_WIDTH/2) < e.getX()) {
			paddle.setLocation(getWidth() - PADDLE_WIDTH, getHeight() - PADDLE_Y_OFFSET);
		}
	}

	private GRect paddle;


	/*
	 * setUpBall creates the game ball and puts it in the center of the window.
	 */
	private void setUpBall() {
		ball = new GOval(getWidth() / 2 - BALL_RADIUS, getHeight() / 2 - BALL_RADIUS, 2 * BALL_RADIUS, 2 * BALL_RADIUS);
		ball.setFilled(true);
		ball.setColor(Color.BLACK);
		ball.setFillColor(Color.BLACK);
		add(ball);
	}

	private GOval ball;

	/*
	 * this sets the variables vx and vy
	 * it is separate from the moveBall method because if it were not, the x velocity would constantly be changed 
	 */
	private void setBallVelocity(){
		vx = rgen.nextDouble(1, 3);
		if (rgen.nextBoolean(.5)) vx = -vx;
		vy = 3;
	}

	/*
	 * startBall moves the ball, velocity vx & vy
	 */
	private void moveBall(){
		ball.move(vx, vy);
		if (ball.getX() + 2 * BALL_RADIUS >= getWidth()){
			vx = -vx;
		}
		if (ball.getX() <= 0) {
			vx = -vx;
		}
		if (ball.getY() <= 0) {
			vy = -vy;
		}
		if (ball.getY() >= getHeight()) {
			ballsRemaining -=1;			// this counts the number of turns the player still has. Once it reaches 0, the game ends.
			if (ballsRemaining > 0) {   // if there are no balls left, the ball does not reset. instead, a loss message is displayed after the while loop breaks
				resetBall();
			}
		}
	}

	private double vx, vy;
	private int ballsRemaining = NTURNS;

	private RandomGenerator rgen = RandomGenerator.getInstance();


	/*
	 * moves the ball back to its starting location if the ball has been moved below the bottom of the screen
	 */
	private void resetBall() {
		ball.setLocation(getWidth() / 2 - BALL_RADIUS, getHeight() / 2 - BALL_RADIUS);
		vx = rgen.nextDouble(1, 3);
		if (rgen.nextBoolean(.5)) vx = -vx;
		waitForClick();

	}


	/*
	 * getCollidingObject checks if the ball is 2 pixels from an object on the left, right, top or bottom. If it is, that object is considered touching the ball
	 * if it is touching an object, the method will return that object to the run method where the variable collider
	 * will become that object
	 * if collider is the paddle, the ball is teleported so it is tangent to the paddle and begins moving up
	 * if collider is a brick, the ball changes direction appropriately and the brick is removed
	 * this isn't done by this method, but happens instead immediately after and is facilitated by the object returned by this method,
	 * 
	 * Note: getCollidingObject checks for objects 2 pixels away. When the ball hits the paddle, it is moved to 3 pixes above the paddle for this reason
	 */
	private GObject getCollidingObject() {

		if (getElementAt(ball.getX() + BALL_RADIUS, ball.getY() - 2) != null) {
			collisionOnSide = false;
			return getElementAt(ball.getX() + BALL_RADIUS, ball.getY() - 2);

		} else if (getElementAt(ball.getX() + BALL_RADIUS, ball.getY() + 2 * BALL_RADIUS + 2) != null) {
			collisionOnSide = false;
			return getElementAt(ball.getX() + BALL_RADIUS, ball.getY() + 2 * BALL_RADIUS + 2);

		} else if (getElementAt(ball.getX() - 2, ball.getY() + BALL_RADIUS) != null) {
			collisionOnSide = true;
			return getElementAt(ball.getX() - 2, ball.getY() + BALL_RADIUS);

		} else if (getElementAt(ball.getX() + 2 * BALL_RADIUS + 2, ball.getY() + BALL_RADIUS) != null) {
			collisionOnSide = true;
			return getElementAt(ball.getX() + 2 * BALL_RADIUS + 2, ball.getY() + BALL_RADIUS);

		} else {
			return null;
		}
		/*
		 * 
		if(getElementAt(ball.getX(), ball.getY()) != null) {
			return getElementAt(ball.getX(), ball.getY());

		} else if(getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY()) != null){
			return getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY());

		} else if(getElementAt(ball.getX(), ball.getY() + 2 * BALL_RADIUS) != null){
			return getElementAt(ball.getX(), ball.getY() + 2 * BALL_RADIUS);

		} else if (getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY() + 2 * BALL_RADIUS) != null) {
			return getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY() + 2 * BALL_RADIUS);

		 * I have commented out this dead code but left it in because it is what the assignment originally called for
		 * however, I think that the system I use up above this is better because it allows me to determine whether the ball was hit from the top/bottom or left/right
		 * in this way, i can have only the x or y direction change appropriately, which I couldn't figure out a good way to do with the original points of collision
		 */

	}

	/*
	 * if collisionOnSide is true, that means that the ball just hit an object with its side, so vx should change
	 * if it is false, that means the ball just collided with something with its top or bottom, so vy should change
	 */
	private boolean collisionOnSide;
}