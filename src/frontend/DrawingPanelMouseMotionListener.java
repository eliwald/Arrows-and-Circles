package frontend;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.*;

import backend.Edge;
import backend.Node;

/**
 * This MouseMotionListener is used in the MainFrame to detect when the user acts on the canvas.
 * 
 * It is added to the DrawingPanel in the MainFrame.
 * 
 * @author ewald
 *
 */
public class DrawingPanelMouseMotionListener extends MouseMotionAdapter {
	private MainFrame _frame;
	
	public DrawingPanelMouseMotionListener(MainFrame frame) {
		_frame = frame;
	}
	
	public void mouseDragged(java.awt.event.MouseEvent evt) {
		drawingPanelMouseDragged(evt);
	}
	public void mouseMoved(java.awt.event.MouseEvent evt) {
		drawingPanelMouseMoved(evt);
	}
	
	/**
	 * The Mouse Dragged method handles dragging the mouse.  Handles four basic functionality: dragging single or multiple nodes,
	 * drawing the progressLine for an edge in the middle of creation, resizing a node, and resizing an edge.
	 * @param evt	The MouseEvent associated with the mouse being dragged.
	 */
	private void drawingPanelMouseDragged(java.awt.event.MouseEvent evt) {
		Point temp = _frame.getMouseLoc();
		_frame.setMouseLoc(evt.getPoint());

		//If we are in the middle of creating an edge, then handle drawing the progress edge on the screen.
		if (_frame.getEdgeStart() != null) {
			Node con = null;

			//Check to see if there is a close node to which we can snap the end of the progress line.
			for (Node n : _frame.getDrawing().getDiagram().getNodes()) {
				if (n.getCircle().contains(_frame.getMouseLoc())) {
					con = n;
				}
			}

			//If we are in the middle of a node, then snap the progress line to the closest point on that node.
			double difX, difY, vecX, vecY;
			Point2D.Double point_start, point_end = null;

			//Determine the vector based on either the mouse location, or the center of the node to which we're drawing.
			if (con != null) {
				difX = con.getCenter().x - _frame.getEdgeStart().getCenter().x;
				difY = con.getCenter().y - _frame.getEdgeStart().getCenter().y;
			}
			else {
				difX = _frame.getMouseLoc().x - _frame.getEdgeStart().getCenter().x;
				difY = _frame.getMouseLoc().y - _frame.getEdgeStart().getCenter().y;
			}

			vecX = difX/Math.sqrt((difX*difX+difY*difY));
			vecY = difY/Math.sqrt((difX*difX+difY*difY));

			//Start from the same place; if mouse is inside a node, set the end point to be the middle of that node.
			point_start = new Point2D.Double(_frame.getEdgeStart().getCenter().x+(_frame.getEdgeStart().getRadius()*vecX),
					_frame.getEdgeStart().getCenter().y+(_frame.getEdgeStart().getRadius()*vecY));
			if (con != null)
				point_end = new Point2D.Double(con.getCenter().x-(con.getRadius()*vecX),con.getCenter().y-(con.getRadius()*vecY));

			//Draw the lines. First case is if it's a self loop; second case is
			//if it's to a node that the mouse is in the middle of; third case
			//is if we just want to draw to a mouse point.
			if (con != null && _frame.getEdgeStart() == con)
				_frame.getDrawing()._progressLine = new Arc2D.Double(con.getCenter().getX(), con.getCenter().getY(),
						con.getRadius() * 2, con.getRadius() * 2, 180, 540, Arc2D.OPEN);
			else if (con != null)
				_frame.getDrawing()._progressLine = new Line2D.Double(point_start, point_end);
			else
				_frame.getDrawing()._progressLine = new Line2D.Double(point_start, _frame.getMouseLoc());

		}
		else if (_frame.getSelectPoint() != null){
			Point p = evt.getPoint();
			_frame.getDrawing().setSelectRectangle( new Rectangle(Math.min(_frame.getSelectPoint().x, p.x), Math.min(_frame.getSelectPoint().y, p.y), 
					Math.abs(_frame.getSelectPoint().x - p.x), Math.abs(_frame.getSelectPoint().y - p.y)));
			_frame.resetSelected();
			for (Node n : _frame.getDrawing().getDiagram().getNodes()){
				if (_frame.getDrawing().getSelectRectangle().contains(n.getCenter()))
					_frame.addSelectedNode(n);
			}
			for (Edge e : _frame.getDrawing().getDiagram().getEdges()){
				if (e.getStartNode().isSelected() && e.getEndNode().isSelected())
					_frame.addSelectedEdge(e);
			}
		}
		
		//Else if _nodeDragged, which is set when someone presses down on a node (in MousePressed), is not null, then drag the appropriate nodes:
		else if (_frame.getNodeDragged() != null && _frame.getDrawing().contains(_frame.getMouseLoc())){
			
			//If the node is not selected, then just move that one node.
			if (!_frame.getNodeDragged().isSelected() || _frame.getNodesSelected().size() == 1){
				double snappedX = _frame.getMouseLoc().getX() - _frame.getNodeDragged().getOffset().getX();
				double snappedY = _frame.getMouseLoc().getY() - _frame.getNodeDragged().getOffset().getY();
				for (Node n : _frame.getDrawing().getDiagram().getNodes()) {
					if (n != _frame.getNodeDragged()) {
						if (Math.abs(_frame.getMouseLoc().getX() - _frame.getNodeDragged().getOffset().getX() - n.getCenter().getX()) <= MainFrame.SNAP_DIFFERENCE) {
							snappedX = n.getCenter().getX();
						}
						if (Math.abs(_frame.getMouseLoc().getY() - _frame.getNodeDragged().getOffset().getY() - n.getCenter().getY()) <= MainFrame.SNAP_DIFFERENCE) {
							snappedY = n.getCenter().getY();
						}
					}
				}
				_frame.getNodeDragged().setCenter(snappedX, snappedY);
				_frame.getNodeDragged().resetCircle();
			}


			//Otherwise, move all the selected nodes.
			else {
				for (Node n : _frame.getNodesSelected()){
					int difX = _frame.getMouseLoc().x - temp.x;
					int difY = _frame.getMouseLoc().y - temp.y;
					n.setCenter(n.getCenter().x + difX, n.getCenter().y + difY);
					n.resetCircle();
				}
			}
		}

		//Otherwise, if we are in the middle of resizing, then correctly resize the node.
		else if (_frame.getResizing() != null) {
			Rectangle2D rec = _frame.getResizing().getResize();

			double dif = Math.max(_frame.getMouseLoc().x - rec.getCenterX(),_frame.getMouseLoc().y - rec.getCenterY());

			double newR = _frame.getResizing().getRadius() + dif/2;
			if (newR < Node.MIN_RADIUS) {
				return;
			}
			_frame.getResizing().setRadius(newR);
		}

		//Otherwise, if we are in the middle of resizing an edge, then correctly resize the edge.
		else if (_frame.getEdgeDragged() != null) {
			
			//Set the help text of the bottom bar
			_frame.setHelpText(3);
			
			// When the start node and end node are different.
			if(_frame.getEdgeDragged().getStartNode() != _frame.getEdgeDragged().getEndNode()) {
			
				// Find the vector A from mouse to start node.
				double[] vectorA = {
					_frame.getEdgeDragged().getStartNode().getCenter().getX() - _frame.getMouseLoc().getX(),
					_frame.getEdgeDragged().getStartNode().getCenter().getY() - _frame.getMouseLoc().getY()
				};
				double vectorASize = Math.sqrt(vectorA[0] * vectorA[0] + vectorA[1] * vectorA[1]);
					
				// Find the vector B from the mouse to the end
				double[] vectorB = {
					_frame.getEdgeDragged().getEndNode().getCenter().getX() - _frame.getMouseLoc().getX(),
					_frame.getEdgeDragged().getEndNode().getCenter().getY() - _frame.getMouseLoc().getY()
				};
				double vectorBSize = Math.sqrt(vectorB[0] * vectorB[0] + vectorB[1] * vectorB[1]);
					
				// Find the vector C from start to end.
				double[] vectorC = {
					_frame.getEdgeDragged().getEndNode().getCenter().getX() - _frame.getEdgeDragged().getStartNode().getCenter().getX(),
					_frame.getEdgeDragged().getEndNode().getCenter().getY() - _frame.getEdgeDragged().getStartNode().getCenter().getY()
				};
				double vectorCSize = Math.sqrt(vectorC[0] * vectorC[0] + vectorC[1] * vectorC[1]);
					
				// Find the cross product of A and B
				double crossAB = vectorA[0] * vectorB[1] - vectorA[1] * vectorB[0];
				_frame.getEdgeDragged().setTurn(crossAB > 0);
				
				// Find the cosine of the angle between vector A and B
				double cosTheta = (vectorASize * vectorASize + vectorBSize * vectorBSize - vectorCSize * vectorCSize) / (2 * vectorASize * vectorBSize);
				
				double height; 
				if(Math.abs(crossAB) < Double.MIN_VALUE) {
					height = -1000000.0;
				}
				else {
					double radius = (vectorASize * vectorBSize * vectorCSize) / (Math.abs(crossAB) * 2);
					height = Math.sqrt(radius * radius - (vectorCSize / 2) * (vectorCSize / 2));
				}
	
				_frame.getEdgeDragged().setHeight(height * ((crossAB * cosTheta > 0) ? 1 : -1));
			}
			// The start node and the end node are the same (doing self-loop).
			else {

				double[] vectorA = {
					_frame.getMouseLoc().getX() - _frame.getEdgeDragged().getStartNode().getCenter().getX(),
					_frame.getMouseLoc().getY() - _frame.getEdgeDragged().getStartNode().getCenter().getY()
				};
				double vectorAAngle = Math.atan2(vectorA[1], vectorA[0]);
				_frame.getEdgeDragged().setAngle(vectorAAngle + _frame.getEdgeDragged().getOffset());
			}
		}
		//Repaint at the end
		_frame.getDrawing().repaint();
	}
	
	/**
	 * The Mouse Moved method checks to see whether we are currently hovering over a node and
	 * whether shift is being pressed; if it is, it sets that node to _edgeStart.
	 * @param evt	The MouseEvent associated with the mouse being moved.
	 */
	private void drawingPanelMouseMoved(java.awt.event.MouseEvent evt) {
		_frame.setMouseLoc(evt.getPoint());
		_frame.setEdgeStart(null);
		boolean changed_help_text = false;
		for (Edge e : _frame.getDrawing().getDiagram().getEdges()) {
			if (e.intersects(_frame.getMouseLoc().getX(), _frame.getMouseLoc().getY())) {
				//Set the help text of the bottom bar
				_frame.setHelpText(3);
				changed_help_text = true;
			}
		}
		for (Node n : _frame.getDrawing().getDiagram().getNodes()) {
			if (n.getCircle().contains(_frame.getMouseLoc()) || (n.isStart() && n.getStartSymbol().contains(_frame.getMouseLoc()))) {
				if (n.isSelected())
					_frame.setHelpText(2);
				else
					_frame.setHelpText(1);
				changed_help_text = true;
			}
		}
		if (!changed_help_text) 
			_frame.setHelpText(0);
	}
}
