/**
 * File: Frogger.java
 * -------------------
 * Written by Matthew Abbott and Jack Paladin
 * 
 * This file contains the playgame method and all helper methods for it.
 * 
 * Extas stuff we did:
 * Player must cross the screen using logs and turtles that move
 * The player is moved along with the logs and turtles when on them
 * Turtles disappear periodically, with a visual indication before they do
 * Title and Game over screen: game over screen has text that increases in size (animation)
 * High score tracking: in an instance of frogger, scores are tracked across games
 * Note: high scores do not persist after the instance of froggermain has been ended
 * Difficult increases each time the player completes a level (fills all 5 goals)
 * This is accomplished with speed increases for cars, logs and turtles
 */

package lab10;

import java.awt.Color;
import java.awt.Font;
import java.util.*;

import sedgewick.StdDraw;

public class Frogger implements FroggerGame {

	/**
	 * Height of one zone: 1/17 the length of one side of the screen, since
	 * there are 17 zones.
	 */
	private final double ZONE_HEIGHT = 1.1 / 17.;

	/** Diameter of frog */
	private final double FROG_DIAMETER = ZONE_HEIGHT / 2;

	/** Initial lives of a player */
	private final int STARTING_LIVES = 3;

	/** The number input into StdDraw.show for each step */
	private final int STEP_LENGTH = 20;

	/** The side length of a target */
	private final double TARGET_LENGTH = 1. / 11;

	/** How many points reaching the end is worth */
	private final int GOAL_VALUE = 100;

	/** The base speed of the frog, used for log, car and turtle speeds as well */
	private final double SPEED_MULT = ZONE_HEIGHT * STEP_LENGTH / 100;

	/** The ArrayList that stores every car in the game */
	private ArrayList<Car> carList;

	/** The array of targets */
	private Target[] targetArray;

	/** The ArrayList that stores every log in the game */
	private ArrayList<Log> logList;

	private ArrayList<Turtle> turtleList;

	/** The frog of the player */
	private Frog player;
	private double difficultyMultiplier = 1;
	
	  /** starting values of high scores */
    private int highScore1 = 0;
    private int highScore2 = 0;
    private int highScore3 = 0;
    private int highScore4 = 0;
    private int highScore5 = 0;

	@Override
	public void playGame() {
		while (!ArcadeKeys.isKeyPressed(1, ArcadeKeys.KEY_DOWN)) {
			createStartScreen();
			StdDraw.show(100);
		}
		StdDraw.filledRectangle(.5, .5, .55, .55);
		StdDraw.show(100);
		initializeTargets();
		createBackground();

		player = new Frog(.5, -.05 + ZONE_HEIGHT * 1.5, FROG_DIAMETER,
				STARTING_LIVES, SPEED_MULT);
		player.draw();

		carList = initializeCars();
		logList = initializeLogs();
		turtleList = initializeTurtles();
		while (player.getLives() > -1) {
			levelFinishCheck();

			StdDraw.clear();
			createBackground();
			drawLives();
			moveEverything();
			collisionCheck();
            addHighScore();
			StdDraw.show(STEP_LENGTH);
		}
		for (int i = 1; i < 2; i++) {
			StdDraw.setPenColor(Color.BLACK);
			StdDraw.filledRectangle(.5, .5, .55, .55);
			createEndScreen();
		}
		while (!ArcadeKeys.isKeyPressed(1, ArcadeKeys.KEY_UP)) {
			StdDraw.clear();
		}
		playGame();
	}

	/**
	 * Calls all move methods. Used to simplify the playGame method, which was
	 * getting cluttered
	 */
	private void moveEverything() {
		moveLogs();
		moveTurtles();
		moveCars();
		moveFrog();
	}

	/**
	 * Moves the frog according to its move method, then draws the frog as well.
	 * Assumes that the screen was cleared in the previous step.
	 */
	private void moveFrog() {
		player.move();
		player.draw();
	}

	/**
	 * Creates every car in the game and returns the carList arrayList
	 * 
	 * @return carList, which holds every car in the game
	 */
	private ArrayList<Car> initializeCars() {
		ArrayList<Car> carList = new ArrayList<Car>();
		for (int i = 0; i < 5; i++) {
			addCarsInRow(i, carList);
		}
		return carList;
	}

	/**
	 * Generates the cars according to their row. The higher the row, the faster
	 * the car. All rows have 3 cars, which are all squares of length
	 * ZONE_HEIGHT * .75. They are spaced evenly based on the size of their path
	 * (1.1 + ZONE_HEIGHT * .75), which is the length of the screen plus the
	 * length of the car. This path length means the car will be exactly at the
	 * end of the path when it is exactly completely off the screen, and the
	 * beginning of the path is exactly completely off the screen on the other
	 * side as well.
	 * 
	 * The color of all cars is orange.
	 * 
	 * The speed of all cars is proportional to SPEED_MULT, which is the base
	 * speed of a frog. Cars have a constant term (SPEED_MULT / 10) and a term
	 * that scales with the row number (SPEED_MULT * (row + 1) / 20) * (-1)^row,
	 * which determines how fast and in what diretion the car travels. A car in
	 * row 0 will travel at speed (SPEED_MULT / 10 + SPEED_MULT / 20) * 1, for a
	 * total speed of 3 * SPEED_MULT / 20, or 3/20ths the base speed of a frog.
	 * This speed receives an increase of a constant value every time the player
	 * reaches all 5 targets, which is done in a separate method, since this is
	 * only the initialization of the cars
	 * 
	 * @param row
	 *            the row number of the three cars being created
	 * @param carList
	 *            the arrayList that stores all cars
	 */
	private void addCarsInRow(int row, ArrayList<Car> carList) {

		for (int i = 0; i < 3; i++) {
			// each car starts 1/3 of the length of the path it travels apart.
			// 1.1 + ZONE_HEIGHT = width of screen + length of car
			carList.add(new Car(-.05 + i * ((1.1 + (ZONE_HEIGHT * .75)) / 3),
					-.05 + ZONE_HEIGHT * (2.5 + row), ZONE_HEIGHT * .75,
					ZONE_HEIGHT * .75, Color.ORANGE,
					((SPEED_MULT / 10. + (SPEED_MULT * (row + 1) / 20.)) * Math
							.pow(-1, row))));
		}
	}

	/**
	 * Moves every car in the game according to its speed. If a car is
	 * completely off the screen, it is moved back to the start. In this way,
	 * the cars constantly cycle.
	 */
	private void moveCars() {
		Car current;
		for (int i = 0; i < carList.size(); i++) {
			current = carList.get(i);
			current.move();
			if (current.getX() - current.getHalfLength() > 1.05) {
				current.setX(-.05 - current.getHalfLength());
			} else if (current.getX() + current.getHalfLength() < -.05) {
				current.setX(1.05 + current.getHalfLength());
			}
			current.draw();
		}

	}

	/**
	 * Creates all logs and puts them in the arrayList that stores all logs.
	 * Logs are created at varying sizes and speeds that all need to be
	 * determined on a row by row basis, so no loop can be made, as with the
	 * cars, that creates each row of logs, only the logs in each row
	 * 
	 * @return the arrayList that stores all logs.
	 */
	private ArrayList<Log> initializeLogs() {
		ArrayList<Log> logList = new ArrayList<Log>();
		for (int i = 0; i < 3; i++) {
			// These logs have width ZONE_HEIGHT * 3 and height ZONE_HEIGHT *.75
			// These logs have path length (1.1 + (ZONE_HEIGHT * 3)
			// These logs have speed 3 * SPEED_MULT / 20
			// They are centered in zone 9 at height ZONE_HEIGHT * 9.5
			logList.add(new Log(-.05 + i * ((1.1 + (ZONE_HEIGHT * 3)) / 3),
					-.05 + ZONE_HEIGHT * 9.5, ZONE_HEIGHT * 3,
					ZONE_HEIGHT * .75, 3 * SPEED_MULT / 20.));
		}
		for (int i = 0; i < 2; i++) {
			// These logs have width ZONE_HEIGHT * 6 and height ZONE_HEIGHT *.75
			// These logs have path length (1.1 + (ZONE_HEIGHT * 6)
			// These logs have speed 7 * SPEED_MULT / 60
			logList.add(new Log(-.05 + i * ((1.1 + (ZONE_HEIGHT * 6)) / 2),
					-.05 + ZONE_HEIGHT * 10.5, ZONE_HEIGHT * 6,
					ZONE_HEIGHT * .75,
					((SPEED_MULT / 15. + (SPEED_MULT / 20.)))));
		}
		for (int i = 0; i < 4; i++) {
			// These logs have width ZONE_HEIGHT * 2 and height ZONE_HEIGHT *.75
			// These logs have path length (1.1 + (ZONE_HEIGHT * 2)
			// These logs have speed 7 * SPEED_MULT / 60
			logList.add(new Log(-.05 + i * ((1.1 + (ZONE_HEIGHT * 2)) / 4),
					-.05 + ZONE_HEIGHT * 12.5, ZONE_HEIGHT * 2,
					ZONE_HEIGHT * .75,
					((SPEED_MULT / 10. + (SPEED_MULT / 15.)))));
		}
		return logList;
	}

	/**
	 * moves every log according to its speed. If a log is completely off the
	 * screen, it is moved to the start of its path.
	 */
	private void moveLogs() {
		Log current;
		for (int i = 0; i < logList.size(); i++) {
			current = logList.get(i);
			current.move();
			if (current.getX() - current.getLength() / 2 > 1.05) {
				current.setX(-.05 - current.getLength() / 2);
			}
			current.draw();
		}
	}

	/**
	 * Creates all turtles and puts them in the arrayList that stores all
	 * turtles. Turtles are created at varying sizes and speeds that all need to
	 * be determined on a row by row basis, so no loop can be made, as with the
	 * cars, that creates each row of turtles, only the turtles in each row
	 * 
	 * @return the arrayList that stores all turtles.
	 */
	private ArrayList<Turtle> initializeTurtles() {
		ArrayList<Turtle> turtleList = new ArrayList<Turtle>();
		for (int i = 0; i < 3; i++) {
			turtleList.add(new Turtle(-.05 + i
					* ((1.1 + (ZONE_HEIGHT * 3)) / 3), -.05 + ZONE_HEIGHT
					* 8.5, ZONE_HEIGHT * 3, ZONE_HEIGHT * .75, -3 * SPEED_MULT
					/ 20.));
		}
		for (int i = 0; i < 4; i++) {
			turtleList.add(new Turtle(-.05 + i
					* ((1.1 + (ZONE_HEIGHT * 2)) / 4), -.05 + ZONE_HEIGHT
					* 11.5, ZONE_HEIGHT * 2, ZONE_HEIGHT * .75,
					-((SPEED_MULT / 15. + (SPEED_MULT / 20.)))));
		}
		return turtleList;
	}

	/**
	 * moves every turtle according to its speed. If a turtle is completely off
	 * the screen, it is moved to the start of its path.
	 */
	private void moveTurtles() {
		Turtle current;
		for (int i = 0; i < turtleList.size(); i++) {
			current = turtleList.get(i);
			current.move();
			if (current.getX() + current.getLength() / 2 < -.05) {
				current.setX(1.05 + current.getLength() / 2);
			}
			current.draw();
		}
	}

	/**
	 * checks to see if the frog has hit a car or goal. Kills the player or
	 * scores points for them accordingly. If not this method checks to see if
	 * the player has hit a log, if they have, then they are safe, if they have
	 * not, it checks to see if they have hit a turtle. If they have, then they
	 * are safe, but if not, they will die if they are above the middle safe
	 * zone
	 */
	private void collisionCheck() {
		offScreenCheck();
        carCollisionCheck();
		targetCollisionCheck();
		if (!logCollisionCheck()) {
			turtleCollisionCheck();
		}

	}

	/**
	 * uses the car hitsFrog method of every car to check if that car hits a
	 * frog at the given position with the given radius. If it does, the frog is
	 * killed
	 */
	private void carCollisionCheck() {
		for (int i = 0; i < carList.size(); i++) {
			if (carList.get(i).hitsFrog(player.getX(), player.getY(),
					player.getRadius())) {
				player.die();
			}
		}
	}

	/**
	 * checks each target to see if the player has hit the target using the
	 * hitsFrog method of the target. If the target is active, it is deactivated
	 * when hit and shows the frog filling the target, as well as pausing the
	 * game for a moment, as with player deaths. The player is then moved to the
	 * start again. If the target is no longer active, that is, it was already
	 * filled at some point, hitting it again will kill the frog.
	 */
	private void targetCollisionCheck() {
		for (int i = 0; i < 5; i++) {
			if (targetArray[i].hitsFrog(player.getX(), player.getY(),
					player.getRadius())) {
				if (!targetArray[i].getActive()) {
					player.die();
				} else {
					targetArray[i].deactivate(FROG_DIAMETER / 2);
					player.modScore(GOAL_VALUE);
					createBackground();
					drawLives();
					moveCars();
					moveLogs();
					moveTurtles();

					targetArray[i].draw();
					player.resetPosition();
				}
			}
		}
	}

	/**
	 * Checks to see if the player is touching a log. If a player is above the
	 * middle safe zone and touching a log according to the hitsFrog method logs
	 * have, that player is safe and their position is modified according to the
	 * log's speed to show them moving along with the log. If they are not
	 * touching a log, then turtleCollisionCheck will be called in the
	 * collisionCheck method, determining if the frog should be killed by the
	 * water
	 * 
	 * @return true if the player is safe
	 */
	private boolean logCollisionCheck() {
		boolean hitsLog = false;
		int relevantLogIndex = 0;
		for (int i = 0; i < logList.size(); i++) {
			if (logList.get(i).hitsFrog(player.getX(), player.getY(),
					player.getRadius())) {
				hitsLog = true;
				relevantLogIndex = i;
			}
		}
		if (player.getY() > ZONE_HEIGHT * 7 + FROG_DIAMETER) {
			if (!hitsLog) {
				return false;
			} else {
				player.modX(logList.get(relevantLogIndex).getSpeed());
				return true;
			}
		} else {
			return true;
		}
	}

	/**
	 * checks to see if the player is touching a turtle. If this has been
	 * called, the game has already determined that the player is not touching a
	 * log, so if the player is above the middle safe zone and not touching a
	 * turtle according to the hitsFrog method of turtles (which excludes
	 * submerged turtles), player.die is called and the frog is destroyed,
	 * causing the player to lose a life. If the player is hitting a turtle,
	 * they will be moved according to that turtle's speed by the modX method of
	 * frogs so that they move along with the turtle.
	 */
	private void turtleCollisionCheck() {
		boolean hitsTurtle = false;
		int relevantTurtleIndex = 0;
		for (int i = 0; i < turtleList.size(); i++) {
			if (turtleList.get(i).hitsFrog(player.getX(), player.getY(),
					player.getRadius())) {
				hitsTurtle = true;
				relevantTurtleIndex = i;
			}
		}
		if (player.getY() > ZONE_HEIGHT * 7 + FROG_DIAMETER) {
			if (!hitsTurtle) {
				player.die();
			} else {
				player.modX(turtleList.get(relevantTurtleIndex).getSpeed());
			}
		}
	}
	
	
    /**
     * This method checks to see if the player has gone off the screen. This can be
     * either off the sides or off the bottom. It does not check to see about the top
     * because we already check this in the logCollisionCheck() method.
     */
    private void offScreenCheck() {
        if(player.getX() > 1.05 - FROG_DIAMETER / 2 || player.getX() < -.05 + FROG_DIAMETER / 2) {
            player.die();
        }
        if(player.getY() < -.05 + ZONE_HEIGHT * 1) {
            player.die();
        }
    }

	/**
	 * Creates each of the zones in the screen with an appropriate color. The
	 * water is blue, the roads are black, the safe zone in between and starting
	 * safe zone is magenta. The goal zone is initialized as a green bar, but
	 * will also have the goals in it colored differently
	 */
	private void createBackground() {

		StdDraw.setPenColor(Color.BLACK);
		StdDraw.filledRectangle(.5, .5, .55, .55); // .5 * screen width = .55

		// starting zone
		StdDraw.setPenColor(Color.MAGENTA);
		StdDraw.filledRectangle(.5, -.05 + ZONE_HEIGHT * 1.5, .55,
				ZONE_HEIGHT / 2);

		// middle safe zone
		StdDraw.setPenColor(Color.MAGENTA);
		StdDraw.filledRectangle(.5, -.05 + ZONE_HEIGHT * 7.5, .55,
				ZONE_HEIGHT / 2);

		// turtles and logs are the next 5 zones, on blue water
		StdDraw.setPenColor(Color.BLUE);
		StdDraw.filledRectangle(.5, -.05 + ZONE_HEIGHT * 10.5, .55,
				ZONE_HEIGHT * 2.5);

		// home zones take up twice as much vertical space
		StdDraw.setPenColor(Color.GREEN);
		StdDraw.filledRectangle(.5, -.05 + ZONE_HEIGHT * 14, .55, ZONE_HEIGHT);
		// in the home zones, there are actual blue squares that are the goal

		drawTargets();

	}

	/**
	 * creates all of the targets in the game, according to the target length
	 * and zone height constants. There are always 5 targets which are always
	 * squares that are evenly spaced along the top of the screen. Tar
	 */
	private void initializeTargets() {
		targetArray = new Target[5];
		for (int i = 0; i < 5; i++) {
			targetArray[i] = new Target(TARGET_LENGTH / 2
					+ (2.5 * i * TARGET_LENGTH), ZONE_HEIGHT * 12.25
					+ TARGET_LENGTH / 2 - .0026, TARGET_LENGTH);
		}
	}

	/**
	 * draws the targets onto the screen according to their draw methods, which
	 * will draw a frog representation in the target if it has been filled
	 */
	private void drawTargets() {
		for (int i = 0; i < 5; i++) {
			targetArray[i].draw();
		}
	}

	/**
	 * if the player has filled all targets, each target will be reactivated and
	 * the cars will all speed up
	 */
	private void levelFinishCheck() {
		if (allTargetsReached()) {
			resetTargets();
			player.modScore(500);
			speedUpCars();
			speedUpLogs();
			speedUpTurtles();
		}
	}

	/**
	 * increases the speed of each car by 1/10 the base speed of the frog.
	 */
	private void speedUpCars() {
		for (int i = 0; i < carList.size(); i++) {
			carList.get(i).modSpeed(SPEED_MULT / 10.);
		}
	}
    
	/**
	 * increases the speed of each log by 1/20 the base speed of the frog
	 */
    private void speedUpLogs() {
        for(int i = 0; i < logList.size(); i++) {
            logList.get(i).modSpeed(SPEED_MULT / 20.);
        }
    }
    
    /**
	 * increases the speed of each turtle by 1/20 the base speed of the frog
	 */
    private void speedUpTurtles() {
    	for(int i = 0; i < turtleList.size(); i++) {
    		turtleList.get(i).modSpeed(SPEED_MULT / 20.);
    	}
    }

	/**
	 * Determines whether every target has been filled by checking whether any
	 * are still active. If any target is still active, the player has not yet
	 * beaten the level
	 * 
	 * @return
	 */
	private boolean allTargetsReached() {
		boolean allInactive = true;
		for (int i = 0; i < 5; i++) {
			if (targetArray[i].getActive()) {
				allInactive = false;
			}
		}
		return allInactive;
	}

	/**
	 * reactivates all targets
	 */
	private void resetTargets() {
		for (int i = 0; i < 5; i++) {
			targetArray[i].setActive(true);
		}
	}

	/**
	 * draws the start screen, which has a black background and a green title
	 * box with Frogger in black letters. It also has a high score list,
	 * although high scores are currently unimplemented. The player may start
	 * the game by pressing down, in the future, it is our intention to make it
	 * possible to play with multiple players by pressing a different direction.
	 */
	 private void createStartScreen() {
	        StdDraw.setPenColor(Color.BLACK);
	        StdDraw.filledRectangle(.5, .5, .55, .55);
	        StdDraw.setPenColor(Color.GREEN);
	        StdDraw.filledRectangle(.5, .82, .5, .10);
	        StdDraw.setPenColor(Color.BLACK);
	        StdDraw.setFont(FROGGER_FONT);
	        StdDraw.text(.5, .8, "FROGGER");
	        StdDraw.setPenColor(Color.WHITE);
	        StdDraw.setFont(FROGGER_1_FONT);
	        StdDraw.text(.5, .6, "High Scores");
	        StdDraw.setFont(FROGGER_2_FONT);
	        StdDraw.textLeft(.3, .53, "1) " + highScore1);
	        StdDraw.textLeft(.3, .48, "2) " + highScore2);
	        StdDraw.textLeft(.3, .43, "3) " + highScore3);
	        StdDraw.textLeft(.3, .38, "4) " + highScore4);
	        StdDraw.textLeft(.3, .33, "5) " + highScore5);
	        StdDraw.setFont(FROGGER_1_FONT);
	        StdDraw.text(.5, .2, "PRESS DOWN TO PLAY!");
	    }

    /**
     * Sets the high scores. If a player receives a high score, all high scores
     * below it are moved down one place
     */
    private void addHighScore() {
        if(player.getLives() < 0) {
            if(player.getScore() > highScore1) {
                highScore5 = highScore4;
                highScore4 = highScore3;
                highScore3 = highScore2;
                highScore2 = highScore1;
                highScore1 = player.getScore();
            } else if(player.getScore() > highScore2 && player.getScore() < highScore1) {
                highScore5 = highScore4;
                highScore4 = highScore3;
                highScore3 = highScore2;
                highScore2 = player.getScore();
            } else if(player.getScore() > highScore3 && player.getScore() < highScore2) {
                highScore5 = highScore4;
                highScore4 = highScore3;
                highScore3 = player.getScore();
            } else if(player.getScore() > highScore4 && player.getScore() < highScore3) {
                highScore5 = highScore4;
                highScore4 = player.getScore();
            } else if(player.getScore() > highScore5 && player.getScore() < highScore4) {
                highScore5 = player.getScore();
            }
        }
    }

	/**
	 * Creates the end screen which is a black box and then calls the method 
	 * bigger text to create the animation of the game over text.
	 */
	private void createEndScreen() {
		for (int i = 0; i < 85; i++) {
			StdDraw.setPenColor(Color.BLACK);
			StdDraw.filledRectangle(.5, .5, .55, .55);
			StdDraw.setPenColor(Color.RED);
			StdDraw.setFont(FROGGER_1_FONT);
			StdDraw.text(.5, .2, "PRESS UP TO CONTINUE");
			biggerText(i);
			StdDraw.show(10);
			StdDraw.clear();
		}
	}
	/**
	 * Draws the number of lives the player has left at the top of the screen.
	 * The lives are shown as green circles.
	 */
	 private void drawLives() {
	        StdDraw.setPenColor(Color.GREEN);
	        StdDraw.setFont(FROGGER_LIVES_FONT);
	        StdDraw.text(.045, 1.01, "Lives Left ");
	        StdDraw.text(.9, 1.01, "Score: " + player.getScore());
	        for (int i = 0; i < player.getLives(); i++) {
	            StdDraw.filledCircle((i / 30.), .97, .01);
	        }
	    }
	/**
	 * Creates a new bold font of size i and writes GAME OVER in that font
	 * @param i the size of the font
	 */
	private void biggerText(int i) {
		final Font BIGGER_FONT = new Font("SansSerif", Font.BOLD, i);
		StdDraw.setFont(BIGGER_FONT);
		StdDraw.text(.5, .5, "GAME OVER");
		
	}
	/**
	 * The fonts used in the start and end screens
	 */
	private static final Font FROGGER_FONT = new Font("SansSerif", Font.BOLD,
			60);
	private static final Font FROGGER_1_FONT = new Font("SansSerif",
			Font.PLAIN, 30);
	private static final Font FROGGER_2_FONT = new Font("SansSerif",
			Font.ITALIC, 20);
	private static final Font FROGGER_LIVES_FONT = new Font("SansSerif",
			Font.PLAIN, 15);

	@Override
	public String getGameName() {

		return null;
	}

	@Override
	public String[] getTeamMembers() {
		// TODO Auto-generated method stub
		return null;
	}

}
