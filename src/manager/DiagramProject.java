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
	private DiagramHistory _history;

	/**
	 * The factory that a new diagram project object with blank diagram.
	 * @return The diagram project.
	 * @throws IOException
	 */
	public static DiagramProject newBlankProject() {
		// TODO: implement opening the project file and populate data.
		throw new UnsupportedOperationException("Not yet implemented.");
	}

	/**
	 * The factory that opens an existing diagram project object by reading in
	 * the file from the specified filename and populating the data.
	 * @param filename The path to the project file.
	 * @return The diagram project.
	 * @throws IOException 
	 */
	public static DiagramProject openExistingProject(String filename) throws IOException {
		// TODO: implement opening the project file and populate data.
		throw new UnsupportedOperationException("Not yet implemented.");
	}

	/**
	 * The factory that saves the current diagram project object by writing down 
	 * the data to the file.
	 * @param dp The current diagram project.
	 * @return The diagram project.
	 * @throws IOException 
	 */
	public static void saveCurrentProject(DiagramProject dp) throws IOException {
		// TODO: implement opening the project file and populate data.
		throw new UnsupportedOperationException("Not yet implemented.");
	}
	
	
	
	/**
	 * Modifies the current diagram object by using the specified action object
	 * which implements the DiagramModifyAction interface.
	 * @param action The action object that would modify the current diagram
	 * 		with its modify method (interface of DiagramModifyAction). 
	 * 		The implemented modify method must be robust and do not break.
	 * @return A boolean indicating whether the modification of the current 
	 * 		diagram is a success or not.
	 */
	public boolean modify(DiagramModifyAction action) {
		
		try {
			// Create a new clone of the current diagram.
			Diagram newDiagram = (Diagram) _history.getCurrentDiagram().clone();
			
			// Try to modify the diagram, and see if it is successful.
			boolean success = action.modify(newDiagram);
			if(success) {
				_history.add(newDiagram, action.message());
			}
			
			return success;
			
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return false;
		}
	}
}
