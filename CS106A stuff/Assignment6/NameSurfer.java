/*
 * File: NameSurfer.java
 * ---------------------
 * When it is finished, this program will implements the viewer for
 * the baby-name database described in the assignment handout.
 */

import acm.program.*;
import java.awt.event.*;
import javax.swing.*;

public class NameSurfer extends Program implements NameSurferConstants {

/* Method: init() */
/**
 * This method has the responsibility for reading in the data base
 * and initializing the interactors at the top of the window.
 */
	private JTextField nameField;
	
	public void init() {
		graph = new NameSurferGraph();
		add(graph);
		
		nameField = new JTextField(10);
		nameField.setActionCommand("Graph");	// at first I had a getSource call in actionperformed, but then I realized that if this and the graph button had the same action command, I wouldn't need to.
		add(new JLabel("Name"), NORTH);
		add(nameField, NORTH);
		nameField.addActionListener(this);
		
	    add(new JButton("Graph"), NORTH);
	    add(new JButton("Clear"), NORTH);
	    addActionListeners();
	}

/* Method: actionPerformed(e) */
/**
 * This class is responsible for detecting when the buttons are
 * clicked, so you will have to define a method to respond to
 * button actions.
 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Graph")){
			NameSurferEntry entry = database.findEntry(nameField.getText().toUpperCase());
			if (entry == null){
				
			} else {
				graph.addEntry(entry);
				graph.update();
			}
			
		} else if (e.getActionCommand().equals("Clear")){
			graph.clear();
			graph.update();
		}
	}
	
	private NameSurferDataBase database = new NameSurferDataBase(NAMES_DATA_FILE);
	private NameSurferGraph graph;
}
