package manager;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import backend.Diagram;
import backend.Node;

/**
 * Keeps track of some recent changes to the diagram.
 * @author ajanthon
 */
public class HistoryStack {
	
	/** The number of maximum history. */
	private static final int MAX_HISTORY = 42; 
	
	/** The number of maximum history allowed. */
	private int _capacity;
	
	/** The previous diagrams before the current. */
	private LinkedList<HistoryStackElement> _undoStack = new LinkedList<HistoryStackElement>();
	
	/** The future diagrams after the current */
	private LinkedList<HistoryStackElement> _redoStack = new LinkedList<HistoryStackElement>();

	/**
	 * Constructs a new diagram history manager with the given maximum number
	 * of history allowed. Use this method if opening a new blank diagram.
	 * @param capacity The maximum number of history allowed.
	 */
	public HistoryStack() {
		_capacity = MAX_HISTORY;
	}

	/**
	 * Constructs a new diagram history manager with the given maximum number
	 * of history allowed. Use this method if opening a new blank diagram.
	 * @param capacity The maximum number of history allowed.
	 */
	public HistoryStack(int capacity) {
		_capacity = capacity;
	}
	
	/** 
	 * Adds a new diagram into the history stack.
	 * @param newDiagram The new diagram to replace the current.
	 * @param message The message indicating the change.
	 */
	public void add(Diagram newDiagram, String message) {
		
		// Put the current diagram into the undo history stack, and replace
		// with the given new diagram.
		HistoryStackElement newElement = new HistoryStackElement(newDiagram, message);
		_undoStack.addLast(newElement);

		// Get rid of very old history if the undo stack exceeds the limit.
		while(_undoStack.size() > _capacity) {
			_undoStack.removeFirst();
		}
		
		// Clear all redo history.
		_redoStack.clear();
	}
	
	/**
	 * Returns the boolean whether undo is possible.
	 * @return A Boolean indicating whether there is a history to undo.
	 */
	public boolean hasNextUndo() {
		return !_undoStack.isEmpty();
	}
	
	/**
	 * Returns the boolean whether redo is possible.
	 * @return A Boolean indicating whether there is a history to redo.
	 */
	public boolean hasNextRedo() {
		return !_redoStack.isEmpty();
	}
	
	/**
	 * Obtains the most recent message log for undo. 
	 */
	public String nextUndoMessage() {
		if(hasNextUndo()) {
			return _undoStack.getLast().getMessage();
		}
		throw new NoSuchElementException("Undo history stack is already empty.");
	}
	
	/**
	 * Obtains the most recent message log for redo.
	 */
	public String nextRedoMessage() {
		if(hasNextRedo()) {
			return _redoStack.getLast().getMessage();
		}
		throw new NoSuchElementException("Redo history stack is already empty.");
	}
	
	/**
	 * Undo the diagram and return it.
	 * @param diagram 
	 * @return The Diagram undone.
	 */
	public Diagram nextUndo(Diagram diagram) {
		if(hasNextUndo()) {
			// Obtain the previous diagram and the log message.
			HistoryStackElement element = _undoStack.removeLast();
			Diagram oldDiagram = element.getDiagram();
			String message = element.getMessage();

			// Put the current diagram into the redo stack.
			_redoStack.addLast(new HistoryStackElement(diagram, message));
			
			// Set the current diagram to the old diagram.
			return oldDiagram;
		}
		throw new NoSuchElementException("Undo history stack is already empty.");
	}
	
	/**
	 * Redo the diagram and return it.
	 * @param diagram 
	 * @return The Diagram undone.
	 */
	public Diagram nextRedo(Diagram diagram) {
		if(hasNextRedo()) {
			// Obtain the previous diagram and the log message.
			HistoryStackElement element = _redoStack.removeLast();
			Diagram newDiagram = element.getDiagram();
			String message = element.getMessage();
			
			// Put the current diagram into the redo stack.
			_undoStack.addLast(new HistoryStackElement(diagram, message));
			
			// Set the current diagram to the old diagram.
			return newDiagram;
		}
		throw new NoSuchElementException("Redo history stack is already empty.");
	}
	
}
