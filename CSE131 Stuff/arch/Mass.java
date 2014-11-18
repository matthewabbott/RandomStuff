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

	public Mass(double mass, double halfWidth, double initialX, double initialY) {

		this.mass = mass;
		halfSideLength = halfWidth;

		x = initialX;
		y = initialY;

	}

	public void setYAcceleration(double springForce1, double springForce2,
			double gravity) {
		//Fnet = ma
		ay = springForce1 / mass + springForce2 / mass + gravity;
	}

	public void move(double stepLength) {
		y += vy * stepLength + ay * stepLength * stepLength / 2;

	}

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
