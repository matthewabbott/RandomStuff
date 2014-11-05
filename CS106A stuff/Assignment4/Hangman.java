/*
 * File: Hangman.java
 * ------------------
 * This program plays the Hangman game from
 * Assignment #4.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.awt.*;
import java.io.*;
import java.util.*;

public class Hangman extends ConsoleProgram {

	private HangmanLexicon whyWontThisWork = new HangmanLexicon();	// the lexicon variable, called whyWontThisWork because I was extremely frustrated by getWord and getWordCount not working. I still don't understand why they are working now and were not before.
																	// I named it this before the arraylist and bufferedreader stuff. I have since encountered no errors, but am leaving the variable this anyway because it reflects my earlier frustration and (hopefully) doesn't detract form being able to understand my program.
	private RandomGenerator rgen = RandomGenerator.getInstance();
	private int numGuesses = 8;		// the variable that reflects the remaining guesses of the user. Changing the value here changes the number of guesses the user starts with.
	private String word;			// the word that was chosen from the lexicon
	private String display;			// the dashes and correctly guessed letters that the user sees.
	
	private HangmanCanvas canvas;
	private GLabel displayLabel;	// having this be an instance variable lets me remove it when I need to add a new one.
	
	public void init() {
		canvas = new HangmanCanvas();
		add(canvas);
	}
	
	public void run() {
		canvas.reset();
		println("Welcome to Hangman!");
		chooseWord();
		createDisplay();
		displayLabel = canvas.displayWord(display);
		canvas.add(displayLabel);
		playHangman();

	}

	/*
	 * uses rgen to select a random word from the hangman lexicon list
	 */
	private void chooseWord() {
		int numWord = rgen.nextInt(0, whyWontThisWork.getWordCount() - 1);
		word = whyWontThisWork.getWord(numWord);
	}

	/*
	 * creates the display string, which shows dashes for unguessed letters
	 * it will display a letter if that letter is correctly guessed by the user
	 */
	private void createDisplay() {
		display = "";
		for (int i = 0; i < word.length(); i ++){
			display = display.concat("-");
		}

	}

	/*
	 * this method is the code that prompts the user to input a character
	 * it then takes that character and converts it to upper case
	 * finally, it returns the character.
	 */

	private char getLetter() {
		String input = readLine("Your guess: ");
		while (1 != input.length()) {
			println("Guess 1 character.");
			input = readLine("Your guess: "); 
		}
		input = input.toUpperCase();
		return input.charAt(0);

	}


	/*
	 * this method determines if the character that the user input is correct
	 * it returns true if the input matches a letter from the word
	 */
	private boolean inputIsCorrect(char input) {
		for (int i = 0; i < word.length(); i++){
			if (input == word.charAt(i)){
				return true;
			}
		}

		return false;
	}


	/*
	 * this is the method that actually changes the display string to indicate that the user has 
	 * correctly guessed a character
	 * 
	 * this is a somewhat convoluted method which I will do my best to exlain here
	 * the placeholder string is created so that I can change display partially.
	 * I need to access characters that were part of display prior to its alteration, and this is the easiest way I could think to do that.
	 * 
	 * strInput exists because you cannot typecast from char to string. I tried display.concat((String)input), and it didn't work. If there is
	 * a better way to make a char a string that I didn't think of, I would like to know, because this was a little over the top and makes this method much more confusing.
	 * 
	 * String placeholderChar is a little poorly named, I suppose. My reasoning is: it is a string created from the charAt method, and represents a single character from placeholder.
	 * I couldn't really think of a better name for it, but there probably is one.
	 * It exists to concatenate display as well, since I can typecast the placeholder.charAt() method.
	 */
	private void modifyDisplay(char input) {
		String placeholder = display;
		display = "";
		String strInput = "" + input + "";

		for (int i = 0; i < word.length(); i++){	//this for loop executes for the length of the letter in the main word
			if (input == word.charAt(i)){			//if the letter chosen is the letter at position i, that letter is now concatenated onto the new display
				display = display.concat(strInput);

			} else {
				String placeholderChar = "" + placeholder.charAt(i) + "";	// if the letter chosen is not the letter at position i, the character that was previously at that position is concatenated on
				display = display.concat(placeholderChar);					// this is what necessitates the use of placeholder. I can't just append dashes unless this is the first correct guess.

			}
		}
		canvas.remove(displayLabel);
		displayLabel = canvas.displayWord(display);
		canvas.add(displayLabel);
	}
	
	/*
	 * contains the while loop that runs the actual game
	 * displays win or loss messages on win or loss.
	 */
	
	private void playHangman() {
		while (numGuesses > 0){		// if the user guesses wrong too many times, the loop ends.
			println("The word now looks like this: " + display);
			
			if (word.equals(display)) {	// if the display has been modified to the point that it is equal to the starting word, the loop breaks.
				println("You Win!");
				break;
			}
			
			println("You have " + numGuesses + " guesses left");
			char input = getLetter();
			if (inputIsCorrect(input)) {
				modifyDisplay(input);


			} else {
				numGuesses--;	// if the user guesses wrong, they lose a guess
				canvas.noteIncorrectGuess(input);
				println("There are no " + input + "'s in this word.");

			}
		}
		if (numGuesses == 0){	// the loop ends if the player wins or loses, so this is here to indicate that the player has lost, because if numGuesses is 0, the player did not win, but instead ran out of guesses.
			println("The word was: " + word);
			println("You lose!");
		}
	}

}
