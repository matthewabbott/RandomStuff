/**
 * File: Car.java
 * ---------------------
 * Written by Jack Paladin and Matthew Abbott
 * 
 * The Car object is used as an obstacle in the frogger game.
 * Cars destroy the frog when they hit the frog and move 
 * horizontally, either to the right or left. Cars have a 
 * position and movement speed, as well as a color that they 
 * are drawn with. They also have a length and width.
 */

package lab10;

import java.awt.*;
import sedgewick.*;

public class Car {

	/** The x and y position of the car */
	private double xLocation;
	private double yLocation;

	/** The width and length of the car */
	private final double length;
	private final double width;

	/** The color of the car */
	private final Color color;

	/** How much the car moves on any step */
	private double speed;

	public Car(double x, double y, double length, double width, Color color,
			double speed) {
		xLocation = x;
		yLocation = y;

		this.length = length;
		this.width = width;

		this.color = color;

		this.speed = speed;
	}

	/**
	 * Draws the car based on its current position. The car is a filled
	 * rectangle with the given length, width and color. This method assumes
	 * that the rectangle drawn at the car's previous location has been removed.
	 */
	public void draw() {

		StdDraw.setPenColor(color);
		StdDraw.filledRectangle(xLocation, yLocation, length / 2, width / 2);

	}

	/**
	 * changes the car's position based on its speed, called after each step.
	 */
	public void move() {

		xLocation += speed;

	}

	public void modSpeed(double increase) {
		if (speed > 0) {
			speed += increase;
		} else {
			speed -= increase;
		}
	}

	/**
	 * @return the car's x position
	 */
	public double getX() {
		return xLocation;
	}

	/**
	 * Changes the car's location directly to a specific x value, usually called
	 * once the car reaches the end of the screen to move it to the beginning of
	 * the screen.
	 * 
	 * @param x
	 *            the new x position of the car
	 */
	public void setX(double x) {
		xLocation = x;
	}

	/**
	 * returns half the length of the car. Since cars are oriented such that
	 * they drive on the road, the length of a car is actually the horizontal
	 * distance from one end of the car to the other, from the player's
	 * perspective.
	 * 
	 * @return half the length of the car
	 */
	public double getHalfLength() {
		return length / 2;
	}

	/**
	 * Determines if this specific car has hit the frog and returns true if so.
	 * The car is determined to have hit the car if the any of the top, bottom,
	 * left or right -most points of the frog are within the car. This means
	 * that the car is not actually a circle for the purposes of collision with
	 * cars, but rather a generous diamond-ish shape. If the top right point of
	 * the frog-circle is inside a car (the point that would be at theta = 45
	 * degrees), the frog can still be considered 'not run over'
	 * 
	 * @param frogX
	 *            the x position of the frog
	 * @param frogY
	 *            the y position of the frog
	 * @param frogRadius
	 *            the radius of the from
	 * @return whether the top, bottom, left or right of the frog is within the
	 *         car
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
	 * isWithin is a helper method for hitsFrom that checks to see if a point is
	 * inside of the car in question. Specifically, one of the salient points of
	 * a given frog
	 * 
	 * @param frogCollisionX
	 * @param frogCollisionY
	 * @return
	 */
	private boolean isWithin(double frogCollisionX, double frogCollisionY) {
		double topY = yLocation + width / 2;
		double bottomY = yLocation - width / 2;

		double rightX = xLocation + length / 2;
		double leftX = xLocation - length / 2;

		return (leftX < frogCollisionX && frogCollisionX < rightX
				&& bottomY < frogCollisionY && frogCollisionY < topY);

	}
}