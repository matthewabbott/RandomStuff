/**
 * File: Arch.java
 * -----------------------
 * Modified by Matthew Abbott
 * Most of the code is mine, but there are some things that were provided
 * for me since this was a project created for students in CSE131 at WUSTL
 * 
 * The arch class initializes all of the masses for an arch, draws them,
 * then modifies them as per the gravity and spring forces acting on them.
 */

package arch;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Timer;

import lab0.Vector;

import sedgewick.StdDraw;
import java.awt.Color;

/**
 * Models an arch using masses and springs.
 * 
 */
public class Arch implements ActionListener {

	final private int CANVAS_WIDTH = 512;
	final private int CANVAS_HEIGHT = 512;
	final private int TOTAL_MASS = 100; // combined mass of the rectangles
	final private Timer timer;

	private final double GRAVITY = -9.8; // acceleration due to gravity (m/s^2)
	private final double SPRING_CONSTANT = 10; // k in newtons/meter
	private final double STEP_INTERVAL = .005;

	private Mass[] allMasses;

	public Arch(int numMasses) {
		// initiate Sedgewick StdDraw
		StdDraw.setCanvasSize(CANVAS_WIDTH, CANVAS_HEIGHT);
		StdDraw.setXscale(0, CANVAS_WIDTH);
		StdDraw.setYscale(0, CANVAS_HEIGHT);

		timer = new Timer(20, this);

		/**
		 * Below, set up the simulation by depositing the specified number of
		 * masses onto the panel, with springs between each adjacent pair of
		 * masses.
		 */

		initializeMasses(numMasses);
		drawMasses();
		drawStrings();
	}

	private void initializeMasses(int numMasses) {

		double halfMassWidth = (CANVAS_WIDTH / (double) (numMasses * 2 + 1)) / 2;
		double singleMass = TOTAL_MASS / (double) (numMasses);

		double y0 = CANVAS_HEIGHT - halfMassWidth;
		double firstX = halfMassWidth * 3;

		allMasses = new Mass[numMasses];
		for (int i = 0; i < numMasses; i++) {
			allMasses[i] = new Mass(singleMass, halfMassWidth, firstX + 4
					* halfMassWidth * i, y0);
		}
	}

	private void drawMasses() {
		for (int i = 0; i < allMasses.length; i++) {
			allMasses[i].drawMass(i == 0 || i == allMasses.length - 1);
		}
	}

	private void drawStrings() {
		StdDraw.setPenColor(Color.RED);
		for (int i = 0; i < allMasses.length - 1; i++) {

			StdDraw.line(allMasses[i].getX(), allMasses[i].getY(),
					allMasses[i + 1].getX(), allMasses[i + 1].getY());
		}

	}

	/**
	 * Run one round of the simulation. Between the FIXME lines below, place
	 * your code which should 1) Compute for each mass (but don't apply) the
	 * force that the mass experiences 2) Then for each mass, apply the force
	 * over a period of time of your choice This will cause the mass to
	 * experience acceleration, which will cause it to move. 3) When the masses
	 * have all moved, relocate the springs so that they connect the masses as
	 * they should.
	 */
	public void round() {
		StdDraw.show(0);
		StdDraw.clear();

		computeYForces();
		applyForces();

		drawMasses();
		drawStrings();
		
		StdDraw.show(0);
	}

	private void computeYForces() {
		double leftSpringForceY = 0;
		double rightSpringForceY = 0;
		for (int i = 1; i < allMasses.length - 1; i++) {

//			double leftDistance = distance(allMasses[i].getX(),
//					allMasses[i].getY(), allMasses[i - 1].getX(),
//					allMasses[i - 1].getY());
//			double rightDistance = distance(allMasses[i].getX(),
//					allMasses[i].getY(), allMasses[i + 1].getX(),
//					allMasses[i + 1].getY());

			double leftDeltaY = allMasses[1].getY() - allMasses[i - 1].getY();
			double rightDeltaY = allMasses[i].getY() - allMasses[i + 1].getY();

			// cosine of theta is the adjacent side of a triangle over the
			// hypotenuse
			// the hypotenuse is the total distance
//			double leftCosTheta = leftDeltaY / leftDistance;
//			double rightCosTheta = rightDeltaY / rightDistance;

			// Fspring = -k x
			leftSpringForceY = -1 * rightSpringForceY;
			if (i == 1) {
				leftSpringForceY = -1 * SPRING_CONSTANT * leftDeltaY;
			}
			rightSpringForceY = -1 * SPRING_CONSTANT * rightDeltaY;

			allMasses[i].setYAcceleration(leftSpringForceY, rightSpringForceY,
					GRAVITY);

		}
	}

	private double distance(double x0, double y0, double x1, double y1) {
		return Math.sqrt((x0 - x1) * (x0 - x1) + (y0 - y1) * (y0 - y1));
	}

	private void applyForces() {
		for (int i = 1; i < allMasses.length - 1; i++) {

			allMasses[i].move(STEP_INTERVAL);
			allMasses[i].nextStepVelocity(STEP_INTERVAL);
		}
	}

	//everything below here was provided for me and is accordingly not written by me
	
	/**
	 * Start the swing timer
	 */
	public void run() {
		timer.start();
	}

	/**
	 * Stop the swing timer
	 */
	public void stop() {
		timer.stop();
	}

	/**
	 * Each clock tick, perform one round of the simulation.
	 */
	public void actionPerformed(ActionEvent e) {
		round();
	}
}
