package manager;

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
	
	/** The filename of this project. */
	private String _filename; 
	
	/** The object that keeps track of recent changes to the diagram.
	 * It is useful for undo and redo features. */
	private DiagramHistory _history;

	/**
	 * Creates a new blank diagram project with initially a blank diagram.
	 */
	public DiagramProject() {
		_filename = null;
		_history = new DiagramHistory(MAX_HISTORY);
	}
	
	/**
	 * Creates a diagram project from the given file name.
	 */
	public DiagramProject(String filename) {
		// TODO: not yet implemented
		throw new UnsupportedOperationException("Not yet implemented.");
	}
	
	/**
	 * 
	 */
	
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
