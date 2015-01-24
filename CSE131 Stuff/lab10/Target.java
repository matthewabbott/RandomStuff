/**
 * File: Target.java
 * ---------------------
 * Written by Jack Paladin and Matthew Abbott
 * 
 * The target is the end zone that the frog is trying to reach.
 * If a target is active, that means that it is still open for
 * the frog to score in, if not, then it will be filled by a 
 * frog representation and destroy any frog that touches it. If
 * it is inactive, then the player will score upon making contact
 * with the target. There are always 5 targets at the top of the
 * screen, which are always blue squares of the same size.
 */

package lab10;

import java.awt.Color;
import sedgewick.StdDraw;
import lab10.ArcadeKeys;

public class Target {
	
	/** location of the target */
	private double x;
	private double y;

	/** side length of the target */
	private double length;

	/**
	 * Whether the target has been reached or not. The target begins as active
	 * and becomes inactive once it has been filled by a frog
	 */
	private boolean active = true;

	/**
	 * The radius of the frog, stored for the purposes of drawing the filled
	 * goal once a frog has reached the target
	 */
	private double storedFrogRadius;

	/**
	 * The color of the target is blue so it appears to be an extension of the
	 * water
	 */
	private final Color COLOR = Color.BLUE;

	/**
	 * Initializes a target with the given location and side length
	 * 
	 * @param x
	 *            x position of center
	 * @param y
	 *            y position of center
	 * @param length
	 *            length of one side of the square
	 */
	public Target(double x, double y, double length) {
		this.x = x;
		this.y = y;
		this.length = length;
	}

	/**
	 * @return the x coordinate of the center
	 */
	public double getX() {
		return x;
	}

	/**
	 * @return the y coordinate of the center
	 */
	public double getY() {
		return y;
	}

	/**
	 * returns whether or not the target is still active
	 * 
	 * @return active: the variable determining whether the target is active
	 */
	public boolean getActive() {
		return active;
	}

	/**
	 * allows active to be changed, usually to the opposite value (but not
	 * necessarily always), which is why the parameter is called flip
	 * 
	 * @param flip
	 *            the new booolean value for active
	 */
	public void setActive(boolean flip) {
		active = flip;
	}

	/**
	 * draws the target onto the screen according to ts color, position, and
	 * length. If the target is not active, that means it has been filled by a
	 * frog and so a representation of the frog is also drawn in the target.
	 * This frog is a circle that appears larger than the actual frog the player
	 * uses, concurrent with the original Frogger game.
	 */
	public void draw() {
		StdDraw.setPenColor(COLOR);
		StdDraw.filledRectangle(x, y, length / 2, length / 2);
		if (!active) {
			StdDraw.setPenColor(Color.GREEN);
			StdDraw.filledCircle(x, y, storedFrogRadius * 2);
		}
	}

	/**
	 * Determines whether the frog is on the target or not. Checks to see if any
	 * of the topmost, rightmost, leftmost or bottommost points of the frog are
	 * within the area of the target
	 * 
	 * @param frogX
	 *            the frog's x position
	 * @param frogY
	 *            the frog's y position
	 * @param frogRadius
	 *            the radius of the circular frog
	 * @return true if any of the salient points of the frog are within the
	 *         target
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
	 * boundaries of the target.
	 * 
	 * @param frogCollisionX
	 *            x coordinate of the point being checked
	 * @param frogCollisionY
	 *            y coordinate of the point being checked
	 * @return true if the point is within the target's boundaries
	 */
	private boolean isWithin(double frogCollisionX, double frogCollisionY) {
		double topY = y + length / 2;
		double bottomY = y - length / 2;

		double rightX = x + length / 2;
		double leftX = x - length / 2;

		return (leftX < frogCollisionX && frogCollisionX < rightX
				&& bottomY < frogCollisionY && frogCollisionY < topY);

	}

	/**
	 * Deactivates the target and stores the radius of the frog used to
	 * deactivate the target so that a representation of it can be drawn in the
	 * target during future calls to the drawTarget method
	 * 
	 * @param frogRadius
	 *            the radius of the frog that deactivated the target
	 */
	public void deactivate(double frogRadius) {
		storedFrogRadius = frogRadius;

		active = false;
	}

}
