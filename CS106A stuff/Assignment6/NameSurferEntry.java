/*
 * File: NameSurferEntry.java
 * --------------------------
 * This class represents a single entry in the database.  Each
 * NameSurferEntry contains a name and a list giving the popularity
 * of that name for each decade stretching back to 1900.
 */

import acm.util.*;
import java.util.*;

public class NameSurferEntry implements NameSurferConstants {

/* Constructor: NameSurferEntry(line) */
/**
 * Creates a new NameSurferEntry from a data line as it appears
 * in the data file.  Each line begins with the name, which is
 * followed by integers giving the rank of that name for each
 * decade.
 */
	public NameSurferEntry(String line) {
		name = line.substring(0, line.indexOf(" "));	
		
		rankArr = new int[11];
		int numStart = line.indexOf(" ") + 1;
		int numEnd = line.indexOf(" ", numStart);
		for(int i = 0; i <= 10; i++){	// adds each integer to an array, each integer starts at a space and ends at the next
			int rank = Integer.parseInt(line.substring(numStart, numEnd));
			
			numStart = numEnd + 1;	//the starting point is now one past the previous ending point
			numEnd = line.indexOf(" ", numStart);	//the ending point is now the next space
			if (numEnd == -1) numEnd = line.length();	//if there is no next space, then the ending point is the end of the line.
			
			rankArr[i] = rank;
		}
	}

/* Method: getName() */
/**
 * Returns the name associated with this entry.
 */
	public String getName() {
		
		return name;
	}

/* Method: getRank(decade) */
/**
 * Returns the rank associated with an entry for a particular
 * decade.  The decade value is an integer indicating how many
 * decades have passed since the first year in the database,
 * which is given by the constant START_DECADE.  If a name does
 * not appear in a decade, the rank value is 0.
 */
	public int getRank(int decade) {
		
		return rankArr[decade];
	}

/* Method: toString() */
/**
 * Returns a string that makes it easy to see the value of a
 * NameSurferEntry.
 */
	public String toString() {
		String entry = name;
		entry = entry.concat(" [");
		for (int i = 0; i <= 10; i++){		//adds each rank to the entry string, with a space in between.
			String rankString = (rankArr[i] + " ");
			entry = entry.concat(rankString);
		}
		entry = entry.substring(0, entry.length() - 1);	//removes the extra space from the for loop
		entry = entry.concat("]");
		return entry;
	}
	
	private String name;
	private int[] rankArr;
}

