package manager;

import backend.Diagram;

/**
 * This interface requires the action of a single modify to the given diagram.
 * @author ajanthon
 */
public interface DiagramModifyAction {

	/**
	 * Modify the diagram.
	 * @param diagram The diagram to be modified.
	 * @return True if success, or false if failed.
	 */
	public boolean modify(Diagram diagram);
	
	/**
	 * Message log of the modification. This is useful
	 * for printing undo messages.
	 * @param diagram The diagram to be modified.
	 * @return True if success, or false if failed.
	 */
	public String message();
}
