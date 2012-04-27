package manager;

import java.io.IOException;

import backend.Diagram;

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
	
	/**
	 * Constructs a new diagram project object with blank diagram.
	 * @return The diagram project.
	 * @throws IOException
	 */
	public DiagramProject() {
		_filename = null;
		_history = new HistoryStack(MAX_HISTORY);
	}

	/**
	 * Constructs a project from the given diagram and uses the specified filename.
	 * @param filename The absolute path to the project file.
	 * @return The diagram project.
	 * @throws IOException 
	 */
	public DiagramProject(Diagram diagram, String filename) {
		_filename = filename;
		_history = new HistoryStack(MAX_HISTORY, diagram);
	}

	/**
	 * The factory that saves the current diagram project object by writing down 
	 * the data to the file.
	 * @param dp The current diagram project.
	 * @return The diagram project.
	 * @throws IOException 
	 */
	public static void saveProject(DiagramProject dp) throws IOException {
		// TODO: implement opening the project file and populate data.
		throw new UnsupportedOperationException("Not yet implemented.");
	}
	
	/**
	 * 
	 * @param newDiagram
	 * @param message
	 * @return
	 * @throws CloneNotSupportedException
	 */
	public void applyChange(Diagram newDiagram, String message) throws CloneNotSupportedException {
		
		// Try to modify the diagram, and see if it is successful.
		_history.add(newDiagram, message);
	}
}
