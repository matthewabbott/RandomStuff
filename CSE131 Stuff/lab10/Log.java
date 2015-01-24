/**
 * File: Log.java
 * -------------------
 * Written by Matthew Abbott and Jack Paladin
 * 
 * Logs are the reverse of obstacles. Where cars destroy a frog
 * on contact, logs prevent the destruction of a frog while in
 * contact with that frog. If a frog is above the center safe
 * zone of the screen, it will be destroyed unless it is in
 * contact with a log or turtle. Logs can check to see if they
 * are hitting the frog and move across the screen. The frog
 * also moves at the speed of the log while on top of the log,
 * but that is controlled by the modX frog method and getSpeed
 * in the log.
 */

package lab10;

import java.awt.Color;

import sedgewick.StdDraw;

public class Log {
	
	/** location of log */
	private double x;
	private double y;

	/** dimensions of log */
	private double length;
	private double width;

	/** how much the log moves over any step */
	private double speed;

	/**
	 * Initializes the log with the given location, dimensions and speed
	 * 
	 * @param x
	 *            x position of center
	 * @param y
	 *            y position of center
	 * @param length
	 *            horizontal distance from one end of the log to the other
	 * @param width
	 *            vertical distance from one end of the log to another
	 * @param speed
	 *            distance that the log moves on the stdDraw canvas during each
	 *            move method
	 */
	public Log(double x, double y, double length, double width, double speed) {
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
	 * @return the distance moved by the log when move is called
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
	 * @return the horizontal length of the rectangular log
	 */
	public double getLength() {
		return length;
	}

	/**
	 * @return the vertical height of the rectangular log
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * draws the log onto the screen according to its dimensions and location
	 */
	public void draw() {
		StdDraw.setPenColor(Color.RED);
		StdDraw.filledRectangle(x, y, length / 2, width / 2);
	}

	/**
	 * moves the log according to its speed, called once per step in the Frogger
	 * file
	 */
	public void move() {

		x += speed;

	}

	/**
	 * Determines whether the frog is on the log or not. Checks to see if any of
	 * the topmost, rightmost, leftmost or bottommost points of the frog are
	 * within the area of the log
	 * 
	 * @param frogX
	 *            the frog's x position
	 * @param frogY
	 *            the frog's y position
	 * @param frogRadius
	 *            the radius of the circular frog
	 * @return true if any of the salient points of the frog are within the log
	 */
	public boolean hitsFrog(double frogX, double frogY, double frogRadius) {

		double frogTop = frogY + frogRadius;
		double frogBottom = frogY - frogRadius;

		double frogRight = frogX + frogRadius;
		double frogLeft = frogX - frogRadius;

		return (isWithin(frogX, frogTop) || isWithin(frogX, frogBottom)
				|| isWithin(frogLeft, frogY) || isWithin(frogRight, frogY));
	}

	/**
	 * helper method for hitsFrog which checks if a specific point is within the
	 * boundaries of the log.
	 * 
	 * @param frogCollisionX
	 *            x coordinate of the point being checked
	 * @param frogCollisionY
	 *            y coordinate of the point being checked
	 * @return true if the point is within the log's boundaries
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
