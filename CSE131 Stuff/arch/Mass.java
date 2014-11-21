/**
 * File: Mass.java
 * ------------------------
 * Written by Matthew Abbott
 * 
 * The Mass class stores all the information about a mass,
 * including its position, mass and size, and provides the
 * methods for that information to be accessed and modified
 * according to kinematics and force rules
 */

package arch;

import sedgewick.*;
import java.awt.Color;

public class Mass {

	/** mass of the Mass */
	private double mass;
	/** side length of the mass (all masses are squares) */
	private double halfSideLength;

	/** variables that determine the horizontal motion and location of the mass */
	private double x;
	private double vx = 0;
	private double ax = 0;
	/*
	 * There should, theoretically, be no x motion at any point, since the
	 * catenary is symmetrical, so vx and ax should always be zero
	 */

	/** variables that determine the vertical motion and location of the mass */
	private double y; // center point of the mass
	private double vy = 0;
	private double ay = 0;

	private double storeY;

	public Mass(double mass, double halfWidth, double initialX, double initialY) {

		this.mass = mass;
		halfSideLength = halfWidth;

		x = initialX;
		y = initialY;

	}

	/**
	 * calculates the instantaneous acceleration of the mass using the forces
	 * currently being applied to it by the two springs attached to it and
	 * gravity
	 * 
	 * @param springForce1
	 * @param springForce2
	 * @param gravity
	 */
	public void setYAcceleration(double springForce1, double springForce2,
			double gravity) {
		// Fnet = ma
		ay = springForce1 / mass + springForce2 / mass + gravity;
	}

	/**
	 * calculates the approximate average acceleration over a predetermined
	 * interval by using the instantaneous acceleration of the mass at the
	 * beginning of the interval (ay) and the forces on the mass at its
	 * projected end location, which was computed using the instantaneous
	 * acceleration as a constant acceleration for the mass over the time
	 * interval.
	 * 
	 * @param springForce1
	 *            what the force on the mass from one of the springs would be at
	 *            its previously projected end location
	 * @param springForce2
	 * @param gravity
	 *            still constant: the mass doesn't move far enough with respect
	 *            to the Earth (or whatever other gravitational body) for it to
	 *            change
	 */
	public void recomputeAY(double springForce1, double springForce2,
			double gravity) {
		double storeAY = springForce1 / mass + springForce2 / mass + gravity;
		ay = (storeAY + ay) / 2;

	}

	/**
	 * moves the mass according to basic kinematic motion equations which assume
	 * a constant acceleration.
	 * 
	 * @param stepLength
	 *            time interval over which the mass is being moved. Ideally is
	 *            small enough such that acceleration is approximately constant
	 */
	public void move(double stepLength) {
		storeY = y;
		y += vy * stepLength + ay * stepLength * stepLength / 2;

	}

	/**
	 * returns y to its pre-computation value, so that y can be computed
	 * approximately, then ay can be reevaluated using the approximate future y
	 * and y can be returned to its pre-reevaluation state so it can be
	 * re-reevaluated using the approximate ay
	 */
	public void restoreY() {

		y = storeY;

	}

	/**
	 * Changes the velocity of the mass according to its acceleration
	 * 
	 * @param stepLength
	 *            the time interval over which the approximately constant
	 *            acceleration is being applied
	 */
	public void nextStepVelocity(double stepLength) {
		vy += ay * stepLength;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getSideLength() {
		return halfSideLength;
	}

	public void drawMass(boolean end) {
		if (end) {
			StdDraw.setPenColor(Color.BLACK);
		} else {
			StdDraw.setPenColor(Color.GRAY);
		}

		StdDraw.filledRectangle(x, y, halfSideLength, halfSideLength);
	}

}
