/**
 * File: Frog.java
 * --------------------------
 * Written by Jack Paladin and Matthew Abbott
 */

package lab10;

import java.awt.Color;

import sedgewick.StdDraw;
import lab10.ArcadeKeys;

public class Frog {

	private double xLocation;
	private double yLocation;

	private final double radius;

	private int lives;

	private final double speed;

	private int score = 0;

	public Frog(double xLocation, double yLocation, double diameter, int lives,
			double speed) {

		this.xLocation = xLocation;

		this.yLocation = yLocation;

		radius = diameter / 2;

		this.lives = lives;

		this.speed = speed;

	}

	public double getX() {

		return xLocation;

	}

	public double getY() {

		return yLocation;

	}

	public void setX(double x) {

		xLocation = x;

	}
	
	public void modX(double a) {
		xLocation += a;
	}

	public void setY(double y) {

		yLocation = y;

	}

	public int getLives() {

		return lives;

	}

	public double getSpeed() {

		return speed;

	}

	public double getRadius() {
		return radius;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int x) {
		score = x;
	}

	public void modScore(int x) {
		score += x;
	}

	public void draw() {
		StdDraw.setPenColor(Color.GREEN);
		StdDraw.filledCircle(xLocation, yLocation, radius);
	}

	public void move() {

		boolean on = true;

		if (ArcadeKeys.isKeyPressed(1, ArcadeKeys.KEY_UP)) {

			if (on) {

				this.yLocation += speed;

				on = false;

			} else {

				on = true;

			}

		} else if (ArcadeKeys.isKeyPressed(1, ArcadeKeys.KEY_DOWN)) {

			if (on) {

				this.yLocation -= speed;

				on = false;

			}

			if (!on) {

				on = true;

			}

		} else if (ArcadeKeys.isKeyPressed(1, ArcadeKeys.KEY_RIGHT)) {

			if (on) {

				this.xLocation += speed;

				on = false;

			}

			if (!on) {

				on = true;

			}

		} else if (ArcadeKeys.isKeyPressed(1, ArcadeKeys.KEY_LEFT)) {

			if (on) {

				this.xLocation -= speed;

				on = false;

			}

			if (!on) {

				on = true;

			}

		}

	}

	public void die() {

		lives--;

		StdDraw.setPenColor(Color.RED);
		StdDraw.filledCircle(xLocation, yLocation, radius);

		resetPosition();

	}
	
	public void resetPosition() {
		StdDraw.show(750);

		setX(.5);

		setY(.05);
	}

}