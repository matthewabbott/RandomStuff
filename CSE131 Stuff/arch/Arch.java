/**
 * File: Arch.java
 * -----------------------
 * Modified by Matthew Abbott
 * This class was not created by me. I created all of the code inside Round
 * and all of the methods that aren't StdDraw methods which were called.
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
	private final double STEP_INTERVAL = .4;

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
		projectedMotion();
		midpointRecomputation();
		applyForces();

		drawMasses();
		drawStrings();

		StdDraw.show(0);
	}

	/**
	 * computeYForces is a helper method for round that figures out what the
	 * forces on each mass should be based on how much the strings are currently
	 * stretched. It does this by using the given spring constant and the
	 * distance of the mass from its neighbors. It does not compute forces on
	 * the initial and final masses, as those are fixed. It also accounts for
	 * the force of gravity.
	 */
	private void computeYForces() {
		double leftSpringForceY = 0;
		double rightSpringForceY = 0;
		for (int i = 1; i < allMasses.length - 1; i++) {

			// double leftDistance = distance(allMasses[i].getX(),
			// allMasses[i].getY(), allMasses[i - 1].getX(),
			// allMasses[i - 1].getY());
			// double rightDistance = distance(allMasses[i].getX(),
			// allMasses[i].getY(), allMasses[i + 1].getX(),
			// allMasses[i + 1].getY());

			double leftDeltaY = allMasses[1].getY() - allMasses[i - 1].getY();
			double rightDeltaY = allMasses[i].getY() - allMasses[i + 1].getY();

			// cosine of theta is the adjacent side of a triangle over the
			// hypotenuse
			// the hypotenuse is the total distance
			// double leftCosTheta = leftDeltaY / leftDistance;
			// double rightCosTheta = rightDeltaY / rightDistance;

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

	/**
	 * applyForces is a helper method for round that causes the masses to change
	 * location and velocity based on what the acceleration of that mass is, a
	 * value determined by computeYforces and the methods in the mass class.
	 */
	private void applyForces() {
		for (int i = 1; i < allMasses.length - 1; i++) {

			allMasses[i].move(STEP_INTERVAL);
			allMasses[i].nextStepVelocity(STEP_INTERVAL);

		}
	}

	/**
	 * projectedMotion is a modified version of the applyForces method that
	 * simply doesn't recompute the velocity. It exists so that the program can
	 * do the midpoint recomputation of the acceleration for the mass. It shows
	 * where the mass would be if it continued to accelerate and changes the y
	 * position. Then midpointRecomputation is called.
	 */
	private void projectedMotion() {
		for (int i = 1; i < allMasses.length - 1; i++) {

			allMasses[i].move(STEP_INTERVAL);

		}
	}

	/**
	 * midpointRecomputation is similar to the computeYForces method, but
	 * instead it uses the new Y value to figure out what the acceleration of
	 * the mass would be in the next step and changes ay so that it is the
	 * average of the acceleration of the mass in its existing position and the
	 * acceleration at what its next position would have been if its
	 * acceleration remained constant. This acceleration is an approximation of
	 * the instantaneous acceleration of the mass at any time over the interval
	 * STEP_LENGTH. Performing this approximation prevents the arch from failing
	 * catasrophically by additively overestimating each force and adding energy
	 * to the system, to the point where the masses in the simulation fly out of
	 * control.
	 * 
	 * The previous version of this simulation could be run with smaller step
	 * lengths and take a long time to fail, but it would fail eventually. The
	 * previous version, in spite of the changes to the mass class, can still be
	 * run if the projectedMotion and midPointRecomputation method calls are
	 * removed.
	 */
	private void midpointRecomputation() {

		double rightSpringForceY = 0;
		double leftSpringForceY = 0;
		for (int i = 1; i < allMasses.length - 1; i++) {

			double leftDeltaY = allMasses[1].getY() - allMasses[i - 1].getY();
			double rightDeltaY = allMasses[i].getY() - allMasses[i + 1].getY();

			leftSpringForceY = -1 * rightSpringForceY;
			if (i == 1) {
				leftSpringForceY = -1 * SPRING_CONSTANT * leftDeltaY;
			}
			rightSpringForceY = -1 * SPRING_CONSTANT * rightDeltaY;

			allMasses[i].recomputeAY(leftSpringForceY, rightSpringForceY,
					GRAVITY);
			allMasses[i].restoreY();

		}
	}

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
