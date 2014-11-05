/*
 * File: NameSurferGraph.java
 * ---------------------------
 * This class represents the canvas on which the graph of
 * names is drawn. This class is responsible for updating
 * (redrawing) the graphs whenever the list of entries changes
 * or the window is resized.
 */

import acm.graphics.*;

import java.awt.event.*;
import java.util.*;
import java.awt.*;

public class NameSurferGraph extends GCanvas
	implements NameSurferConstants, ComponentListener {

	/**
	* Creates a new NameSurferGraph object that displays the data.
	*/
	public NameSurferGraph() {
		update();
		addComponentListener(this);
	}
	
	
	/**
	* Clears the list of name surfer entries stored inside this class.
	*/
	public void clear() {
		entryList.clear();
		
	}
	
	
	/* Method: addEntry(entry) */
	/**
	* Adds a new NameSurferEntry to the list of entries on the display.
	* Note that this method does not actually draw the graph, but
	* simply stores the entry; the graph is drawn by calling update.
	*/
	public void addEntry(NameSurferEntry entry) {
		entryList.add(entry);
	}
	
	private void updateEntries(){
		Color[] colorArr = {Color.BLACK, Color.RED, Color.BLUE, Color.MAGENTA};
		int colorIndex = 0;
		double heightOfGraphArea = getHeight() - 2 * GRAPH_MARGIN_SIZE;
		
		for(int i = 0; i < entryList.size(); i++){
			NameSurferEntry entry = entryList.get(i);
			String name = entry.getName();
			
			double x1 = 0;	// x value of the previous decade, is zero if the decade is 1900
			double y1 = entry.getRank(0) * (heightOfGraphArea / 1000) + GRAPH_MARGIN_SIZE;	// y value of the previous decade except at decade 1900, where it is the y value of that decade
			double x2 = 0;	// x value of the current decade
			double y2;		// y value of the current decade
			
			for(int j = 0; j < NDECADES; j++){
				int rank = entry.getRank(j);
				if (rank == 0){
					y2 = heightOfGraphArea + GRAPH_MARGIN_SIZE;
				} else {
					y2 = rank * (heightOfGraphArea / 1000) + GRAPH_MARGIN_SIZE;		// y2 is 1/1000th of the way down the graph (past the margin) per number number rank, so rank 437 goes 437/1000 of the way down.
				}
				GLine line = new GLine(x1, y1, x2, y2);	// this line is the line from the previous decade to the current decade.
				line.setColor(colorArr[colorIndex]);
				add(line);
				
				if (rank == 0) {
					GLabel label = new GLabel(" " + name + " *", x2, y2 - 3);
					label.setColor(colorArr[colorIndex]);
					add(label);
				} else {
					GLabel label = new GLabel(" " + name + " " + rank, x2, y2 - 3);
					label.setColor(colorArr[colorIndex]);
					add(label);
				}
				
				
				y1 = y2;
				
				x2 += getWidth() / NDECADES;
				x1 = x2 - getWidth() / NDECADES;
				
			}
			
			colorIndex++;
			if (colorIndex > 3) {
				colorIndex = 0;
			}
		}
	}
	
	
	/**
	* Updates the display image by deleting all the graphical objects
	* from the canvas and then reassembling the display according to
	* the list of entries. Your application must call update after
	* calling either clear or addEntry; update is also called whenever
	* the size of the canvas changes.
	*/
	public void update() {
		removeAll();
		addBackgroundLines();
		addDecadeLabels();
		updateEntries();
	}
	
	/*
	 * adds the top and bottom lines offset by graph margin amount
	 * then adds a line for each decade
	 */
	private void addBackgroundLines(){
		add(new GLine(0, GRAPH_MARGIN_SIZE, getWidth(), GRAPH_MARGIN_SIZE));
		add(new GLine(0, getHeight() - GRAPH_MARGIN_SIZE, getWidth(), getHeight() - GRAPH_MARGIN_SIZE));
		
		double x = 0;
		for(int i = 0; i < NDECADES; i++){
			add(new GLine(x, 0, x, getHeight()));
			x += getWidth() / NDECADES;
		}
	}
	
	/*
	 * adds the year of each specific decade at the bottom of the window.
	 */
	private void addDecadeLabels(){
		double x = 0;
		int decade = 1900;
		String year;
		
		for(int i = 0; i < NDECADES; i++){
			year = " " + decade;
			add(new GLabel(year, x, getHeight() - 3));	//I wasn't sure about the exact correct location, so I just did what looked right.
			x += getWidth() / NDECADES;
			decade += 10;
		}
	}
	
	
	/* Implementation of the ComponentListener interface */
	public void componentHidden(ComponentEvent e) { }
	public void componentMoved(ComponentEvent e) { }
	public void componentResized(ComponentEvent e) { update(); }
	public void componentShown(ComponentEvent e) { }
	
	private ArrayList<NameSurferEntry> entryList = new ArrayList<NameSurferEntry>();
}
