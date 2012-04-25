/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package drawing;

/**
 *
 * @author sstudent
 */
public class Arc extends DrawingShape {
	public Arc (javax.swing.JPanel panel) {
		super(panel, new java.awt.geom.Arc2D.Double(4, 4, 44, 44, 180, -180, 2));
	//Constructs a new arc, initialized to the specified location, size, angular extents, and closure type.
	}
}
