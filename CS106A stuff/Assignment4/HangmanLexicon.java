/*
 * File: HangmanLexicon.java
 * -------------------------
 * This file now contains a full implementation of hangman lexicon, drawing on the associated text file.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import acm.util.*;

public class HangmanLexicon {
	private ArrayList<String> wordList = new ArrayList<String>();
	
	public HangmanLexicon() {
		try {
			BufferedReader rd = new BufferedReader(new FileReader("HangmanLexicon.txt"));
			while(true) {
				String line = rd.readLine();
				if (line == null) break;
				wordList.add(line);
			}
		} catch (IOException ex) {
			throw new ErrorException(ex);
		}
	}

/** Returns the number of words in the lexicon. */
	public int getWordCount() {
		return wordList.size();
	}

/** Returns the word at the specified index. */
	public String getWord(int index) {
		return wordList.get(index);
	};
}
