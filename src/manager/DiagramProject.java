package manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import backend.Diagram;

import com.google.gson.stream.*;

/**
 * Represents a single project (single tab) of the FSM/Graph Diagram.
 * It consists of History Stack as well as utilities to save, open,
 * and export the diagram.
 * 
 * @author ajanthon
 */
public class DiagramProject {
	
	/** The number of maximum history. */
	private static final int MAX_HISTORY = 42; 
	
	/** The filename of this project. This could be null if it is a newly
	 * created diagram that has never been saved yet. */
	private String _filename; 
	
	/** The object that keeps track of recent changes to the diagram.
	 * It is useful for undo and redo features. */
	private HistoryStack _history;

	/** Boolean indicating whether the project is from a file, not a newly created one. */
	private boolean _loaded;
	
	/** Last saved revision number */
	private int _savedRevision; 
	
	/**
	 * Make sure the the constructor is private.
	 */
	private DiagramProject() {
		// do nothing
	}
	
	/**
	 * Factory method that creates a new project. 
	 * @return The new project.
	 */
	public static DiagramProject newProject() {
		DiagramProject project = new DiagramProject();
		project._filename = "";
		project._history = new HistoryStack(MAX_HISTORY);
		project._loaded = false;
		project._savedRevision = -1;
		return project;
	}
	
	/** 
	 * Factory method that loads the saved project from a file. 
	 * @throws IOException 
	 */
	public static DiagramProject openProject(Reader reader) throws IOException {
		
		// Creates a JSON Reader based on the given reader.
		JsonReader jsonReader = new JsonReader(reader);
		
		// Create a new diagram to construct.
		Diagram diagram = new Diagram();
		
		// Start reading data.
		jsonReader.beginObject();
		
		// First, expect the name of the 
		
		
		
		
		return null;
	}

	private static Diagram getDiagramFromReader(Reader reader) {
		JsonReader jsonReader = new JsonReader(reader);
		try {
			while(jsonReader.hasNext()) {
				
			}
		} catch (IOException e) {
			return null;
		}
		return null;
	}
	
	/**
	 * This procedure pops up a file chooser so that the user can select (multiple) files to load,
	 * then it returns a list of File objects from the selected files.
	 * @return List of File objects to open.
	 */
	private static List<File> openFileChooser() {
		JFileChooser chooser = new JFileChooser();
		FileFilter filter = new FileNameExtensionFilter("Diagram JSON File", "json");
		chooser.addChoosableFileFilter(filter);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setMultiSelectionEnabled(true);
		
		int returnVal = chooser.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			return Arrays.asList(chooser.getSelectedFiles());
		} else {
			return null;
		}
	}

	/**
	 * This procedure pops up a file chooser so that the user can select one file to save to,
	 * then it returns a File object to write data to.
	 * @return File object to save to.
	 */
	private static File saveFileChooser() {
		JFileChooser chooser = new JFileChooser();
		FileFilter filter = new FileNameExtensionFilter("Diagram JSON File", "json");
		chooser.addChoosableFileFilter(filter);
		chooser.setAcceptAllFileFilterUsed(false);
		
		int returnVal = chooser.showSaveDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile();
		} else {
			return null;
		}
	}

	
	
}
