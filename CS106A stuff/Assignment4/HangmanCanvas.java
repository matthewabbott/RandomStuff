/*
 * File: HangmanCanvas.java
 * ------------------------
 * This file keeps track of the Hangman display.
 */

import acm.graphics.*;
import java.awt.*;

public class HangmanCanvas extends GCanvas {

	private int numIncorrect = 0;	// counts the number of incorrect guesses to determine which body part to add
	private GLabel incorrect;		// the label that has all the incorrect guesses. gets reset by the reset method
	private String wrongLetters = "";	//string containing the letters for the label "incorrect"

/** Resets the display so that only the scaffold appears 
 * What it actually does is add a large white rectangle that covers anything that might already be there up.
 */
	public void reset() {
		GRect reset = new GRect(0, 0, getX(), 1000);	// for some reason getY() wouldn't work, so I gave up and just went with a large number.
		reset.setColor(Color.WHITE);
		reset.setFilled(true);
		reset.setFillColor(Color.WHITE);
		add(reset);
		incorrect = new GLabel("",0,0);
		add(incorrect);
		add(new GLine(getX() / 2 - BEAM_LENGTH, OFFSET_FROM_TOP, getX() / 2 - BEAM_LENGTH, OFFSET_FROM_TOP + SCAFFOLD_HEIGHT));
		add(new GLine(getX() / 2 - BEAM_LENGTH, OFFSET_FROM_TOP, getX() / 2, OFFSET_FROM_TOP));
		add(new GLine(getX() / 2, OFFSET_FROM_TOP, getX() / 2, OFFSET_FROM_TOP + ROPE_LENGTH));
	}

/**
 * Updates the word on the screen to correspond to the current
 * state of the game.  The argument string shows what letters have
 * been guessed so far; unguessed letters are indicated by hyphens.
 * 
 * I changed this method to return the appropriate GLabel so that I could remove that GLabel when I needed to add the next.
 */
	public GLabel displayWord(String word) {
		GLabel firstLabel = new GLabel(word, getX()/2, I_GIVE_UP);
		firstLabel.setFont("SansSerif-20");
		GLabel displayLabel = new GLabel(word, getX() / 2 - firstLabel.getWidth() / 2, I_GIVE_UP + firstLabel.getHeight());
		displayLabel.setFont("SansSerif-20");
		return displayLabel;
	}

/**
 * Updates the display to correspond to an incorrect guess by the
 * user.  Calling this method causes the next body part to appear
 * on the scaffold and adds the letter to the list of incorrect
 * guesses that appears at the bottom of the window.
 */
	public void noteIncorrectGuess(char letter) {
		if (numIncorrect == 0) {
			GOval head = new GOval(getX() / 2 - HEAD_RADIUS, OFFSET_FROM_TOP + ROPE_LENGTH, 2 * HEAD_RADIUS, 2 * HEAD_RADIUS);
			add(head);
		} else if (numIncorrect == 1) {
			GLine body = new GLine(getX() / 2, OFFSET_FROM_TOP + ROPE_LENGTH + 2 * HEAD_RADIUS, getX() / 2, OFFSET_FROM_TOP + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH);
			add(body);
		} else if (numIncorrect == 2) {
			GLine upperLeftArm = new GLine(getX() / 2 - UPPER_ARM_LENGTH, OFFSET_FROM_TOP + ROPE_LENGTH + 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD, getX() / 2, OFFSET_FROM_TOP + ROPE_LENGTH + 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD);
			add(upperLeftArm);
			GLine lowerLeftArm = new GLine(getX() / 2 - UPPER_ARM_LENGTH, OFFSET_FROM_TOP + ROPE_LENGTH + 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD,getX() / 2 - UPPER_ARM_LENGTH, OFFSET_FROM_TOP + ROPE_LENGTH + 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD + LOWER_ARM_LENGTH);
			add(lowerLeftArm);
		} else if (numIncorrect == 3) {
			GLine upperRightArm = new GLine(getX() / 2 + UPPER_ARM_LENGTH, OFFSET_FROM_TOP + ROPE_LENGTH + 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD, getX() / 2, OFFSET_FROM_TOP + ROPE_LENGTH + 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD);
			add(upperRightArm);
			GLine lowerRightArm = new GLine(getX() / 2 + UPPER_ARM_LENGTH, OFFSET_FROM_TOP + ROPE_LENGTH + 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD, getX() / 2 + UPPER_ARM_LENGTH, OFFSET_FROM_TOP + ROPE_LENGTH + 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD + LOWER_ARM_LENGTH);
			add(lowerRightArm);
		} else if (numIncorrect == 4) {
			GLine leftHip = new GLine(getX() / 2 - HIP_WIDTH, OFFSET_FROM_TOP + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH, getX() / 2, OFFSET_FROM_TOP + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH);
			add(leftHip);
			GLine leftLeg = new GLine(getX() / 2 - HIP_WIDTH, OFFSET_FROM_TOP + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH, getX() / 2 - HIP_WIDTH, I_GIVE_UP);
			add(leftLeg);
		} else if (numIncorrect == 5) {
			GLine rightHip = new GLine(getX() / 2 + HIP_WIDTH, OFFSET_FROM_TOP + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH, getX() / 2, OFFSET_FROM_TOP + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH);
			add(rightHip);
			GLine rightLeg = new GLine(getX() / 2 + HIP_WIDTH, OFFSET_FROM_TOP + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH, getX() / 2 + HIP_WIDTH, I_GIVE_UP);
			add(rightLeg);
		} else if (numIncorrect == 6) {
			GLine leftFoot = new GLine(getX() / 2 - HIP_WIDTH - FOOT_LENGTH, I_GIVE_UP, getX() / 2 - HIP_WIDTH, I_GIVE_UP);
			add(leftFoot);
		} else if (numIncorrect == 7) {
			GLine rightFoot = new GLine(getX() / 2 + HIP_WIDTH + FOOT_LENGTH, I_GIVE_UP, getX() / 2 + HIP_WIDTH, I_GIVE_UP);
			add(rightFoot);
		}
		
		numIncorrect += 1;
		addWrongLettersLabel(letter);
		
	}
	
	
	/*
	 * private method removes the previous label with all the wrong letters, changes it
	 * then replaces it
	 */
	private void addWrongLettersLabel(char letter){
		remove(incorrect);
		String strLetter = "" + letter + "";
		wrongLetters = wrongLetters.concat(strLetter);
		GLabel firstLabel = new GLabel(wrongLetters, getX()/2, I_GIVE_UP + 20);
		firstLabel.setFont("SansSerif-14");
		incorrect = new GLabel(wrongLetters, getX() / 2 - firstLabel.getWidth() / 2, I_GIVE_UP + 38);
		incorrect.setFont("SansSerif-14");
		add(incorrect);
		
	}
		

	private static final int OFFSET_FROM_TOP = 50;
	
/* Constants for the simple version of the picture (in pixels) */
	private static final int SCAFFOLD_HEIGHT = 360;
	private static final int BEAM_LENGTH = 144;
	private static final int ROPE_LENGTH = 18;
	private static final int HEAD_RADIUS = 36;
	private static final int BODY_LENGTH = 144;
	private static final int ARM_OFFSET_FROM_HEAD = 28;
	private static final int UPPER_ARM_LENGTH = 72;
	private static final int LOWER_ARM_LENGTH = 44;
	private static final int HIP_WIDTH = 36;
	private static final int LEG_LENGTH = 108;
	private static final int FOOT_LENGTH = 28;
	
	private static final int I_GIVE_UP = OFFSET_FROM_TOP + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH + LEG_LENGTH;	// there were just too many constants in this y value, so I had to make it into its own constant. I couldn't handle it.

}
