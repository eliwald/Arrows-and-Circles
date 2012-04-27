package manager;

import backend.Diagram;

/**
 * This is the element of the history stack.
 * @author ajanthon
 */
public class HistoryStackElement {

	/** The diagram of the element. */
	private Diagram _diagram;
	
	/** The log message describing the change to the diagram. */
	private String _message;
	
	/**
	 * Constructs a new element for the history stack.
	 * @param diagram The diagram of the element. 
	 * @param message The log message describing the change.
	 */
	public HistoryStackElement(Diagram diagram, String message) {
		_diagram = diagram;
		_message = message;
	}

	/**
	 * Obtains the diagram of the element.
	 * @return The diagram.
	 */
	public Diagram getDiagram() {
		return _diagram;
	}
	
	/**
	 * Obtains the log message of the element.
	 * @return The log message.
	 */
	public String getMessage() {
		return _message;
	}

}
