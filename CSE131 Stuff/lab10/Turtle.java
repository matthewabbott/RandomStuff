/**
 * File: Turtle.java
 * ------------------------
 * Written by Jack Paladin and Matthew Abbott
 * 
 * Turtles are nearly identical to logs. They do everything logs
 * do, but disappear periodically. They have three stages. In
 * stage 1, they are drawn at full size and prevent the frog from
 * dying as long as it is touching the turtle. At stage 2, the
 * frog is still protected, but the turtle is drawn smaller. It
 * still has the same collision, it is just drawn as a warning to
 * the player to get off soon. At stage 3, the turtle is not drawn
 * and does not protect the player. Turtles have a position and
 * dimensions, as well as a speed. They additionally have a property
 * that determines how frequently they change stage that is shared
 * between all instances of the turtle class.
 */

package lab10;

import java.awt.Color;
import sedgewick.StdDraw;

public class Turtle {
	/** location of turtle */
	private double x;
	private double y;

	/** dimensions of turtle */
	private double length;
	private double width;

	/** how much the turtle moves over any step */
	private double speed;

	/**
	 * what stage of motion the turtle is on: Stage 0: turtle appears at full
	 * size and can support a frog. Stage 1: turtle appears to be partially
	 * submerged but can still support a frog. Stage 2: turtle is no longer
	 * drawn on the screen and cannot support the frog.
	 */
	private int stage = 0;

	/** how many steps the turtle has gone through since its last stage change */
	private int numSteps = 0;

	/** How many steps a tutle will go through before changing to the next stage */
	private static final int STAGE_CHANGE_CONSTANT = 70;

	public Turtle(double x, double y, double length, double width, double speed) {
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.length = length;
		this.width = width;
	}

	/**
	 * @return the x coordinate of the center
	 */
	public double getX() {
		return x;
	}

	/**
	 * Alters the x coordinate to the given value
	 * 
	 * @param a
	 *            the new x coordinate
	 */
	public void setX(double a) {
		x = a;
	}

	/**
	 * @return the y coordinate of the center
	 */
	public double getY() {
		return y;
	}

	/**
	 * Alters the y coordinate to the given value
	 * 
	 * @param a
	 *            the new y coordinate
	 */
	public void setY(double a) {
		y = a;
	}

	/**
	 * @return the distance moved by the turtle when move is called
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * Alters the speed to the given value
	 * 
	 * @param a
	 *            the new speed
	 */
	public void setSpeed(double a) {
		speed = a;
	}
	
	public void modSpeed(double a) {
		speed += a;
	}

	/**
	 * @return the horizontal length of the rectangular turtle
	 */
	public double getLength() {
		return length;
	}

	/**
	 * @return the vertical height of the rectangular turtle
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * changes the stage of the frog to its appropriate value based on how many
	 * steps have happened since it last changed and the stage change constant
	 */
	private void setStage() {
		if (numSteps > STAGE_CHANGE_CONSTANT) {

			numSteps = 0;

			stage++;
			if (stage > 2) {
				stage = 0;
			}

		}
	}

	/**
	 * draws the turtle onto the screen according to its dimensions, location
	 * and stage. In stage 0, the turtle is full sized. In stage 1 it is
	 * slightly smaller, while in stage 2 it is not drawn at all.
	 */
	public void draw() {
		if (stage == 0) {
			StdDraw.setPenColor(Color.CYAN);
			StdDraw.filledRectangle(x, y, length / 2, width / 2);

		} else if (stage == 1) {
			StdDraw.setPenColor(Color.CYAN);
			StdDraw.filledRectangle(x, y, length / 2 - .005, width / 2 - .005);

		}

	}

	/**
	 * moves the turtle according to its speed, called once per step in the
	 * Frogger file
	 */
	public void move() {

		x += speed;
		numSteps++;
		setStage();

	}

	/**
	 * Determines whether the frog is on the log or not. If the turtle is in
	 * stage 2 then the frog can never be on the turtle. If not, this method
	 * checks to see if any of the topmost, rightmost, leftmost or bottommost
	 * points of the frog are within the area of the turtle.
	 * 
	 * @param frogX
	 *            the frog's x position
	 * @param frogY
	 *            the frog's y position
	 * @param frogRadius
	 *            the radius of the circular frog
	 * @return true if any of the salient points of the frog are within the turtle
	 */
	public boolean hitsFrog(double frogX, double frogY, double frogRadius) {
		if (stage == 2) {
			return false;
		}

		double frogTop = frogY + frogRadius;
		double frogBottom = frogY - frogRadius;

		double frogRight = frogX + frogRadius;
		double frogLeft = frogX - frogRadius;

		return (isWithin(frogX, frogTop) || isWithin(frogX, frogBottom)
				|| isWithin(frogLeft, frogY) || isWithin(frogRight, frogY));
	}

	/**
	 * helper method for hitsFrog which checks if a specific point is within the
	 * boundaries of the turtle.
	 * 
	 * @param frogCollisionX
	 *            x coordinate of the point being checked
	 * @param frogCollisionY
	 *            y coordinate of the point being checked
	 * @return true if the point is within the turtle's boundaries
	 */
	private boolean isWithin(double frogCollisionX, double frogCollisionY) {
		double topY = y + width / 2;
		double bottomY = y - width / 2;

		double rightX = x + length / 2;
		double leftX = x - length / 2;

		return (leftX < frogCollisionX && frogCollisionX < rightX
				&& bottomY < frogCollisionY && frogCollisionY < topY);

	}

}
