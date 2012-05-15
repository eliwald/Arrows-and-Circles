package frontend;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.HashMap;

import backend.*;

/**
 * This MouseListener is used in the MainFrame to detect when the user acts on the canvas.
 * 
 * It is added to the DrawingPanel in the MainFrame.
 * 
 * @author ewald
 *
 */
public class DrawingPanelMouseListener extends MouseAdapter {
	
	//Needs a reference to the main frame's objects.
	private MainFrame _frame;
	private HashMap<Node, Point2D.Double> _tempPoint;
	private Point _tempPointMoved;
	private double tempRadius;
	private double tempHeight;
	private double tempAngle;
	private boolean tempTurn;
	
	public DrawingPanelMouseListener(MainFrame frame) {
		_frame = frame;
	}
	
	public void mouseClicked(java.awt.event.MouseEvent evt) {
		drawingPanelMouseClicked(evt);
	}
	
	public void mousePressed(java.awt.event.MouseEvent evt) {
		drawingPanelMousePressed(evt);
	}
	
	public void mouseReleased(java.awt.event.MouseEvent evt) {
		drawingPanelMouseReleased(evt);
	}
	
	/**
	 * The Mouse Clicked method handles functionality on mouse clicks.  Functionality handled includes: creating a new node,
	 * toggling end state of a new node (double clicks), ctrl-selecting multiple edges or nodes, selecting a start state
	 * of a node, or selecting a new node/edge.
	 * @param evt	The MouseEvent associated with the mouse being clicked.
	 */
	private void drawingPanelMouseClicked(java.awt.event.MouseEvent evt) {
		String mod = MouseEvent.getMouseModifiersText(evt.getModifiers());
		//If there is a double click
		if (evt.getClickCount() >=2 && mod.equals("Button1")){
			_frame.getDrawing().getDiagramProject().pushCurrentOntoHistory("Added Node");
			_frame.setStar();
			
			//If double click inside a node, toggle end state.
			for (Node n : _frame.getDrawing().getDiagram().getNodes()){
				if (n.getCircle().contains(evt.getPoint())){
					n.setEnd(!n.isEnd());
					return;
				}
			}

			//Otherwise, reset all the selected nodes, and add a new node centered at the click.
			_frame.resetSelected();
			Node add = _frame.getDrawing().addNode(evt.getPoint());
			_frame.addSelectedNode(add);
			add.getTextField().setVisible(true);
			add.getLabel().setVisible(false);
			_frame.setHelpText(2);
		}
		//Otherwise, it's a single click
		else{
			//If Ctrl is clicked, we are selecting multiple nodes/edges
			if (evt.isControlDown()) {
				//Iterate over all edges and nodes, toggling selection for each node and edge
				for (Node n : _frame.getDrawing().getDiagram().getNodes()){
					n.getTextField().setVisible(false);
					n.getLabel().setVisible(true);
					if (n.getCircle().contains(evt.getPoint())){
						if (n.isSelected())
							_frame.removeSelectedNode(n);
						else
							_frame.addSelectedNode(n);
						_frame.getDrawing().repaint();
						return; //If we find a node or edge, just return; we don't want to ctrl
						//select multiple things if nodes are stacked on top of each other.
					}
				}
				for (Edge e : _frame.getDrawing().getDiagram().getEdges()){
					if (e.intersects(evt.getPoint().x,evt.getPoint().y)){
						if (e.isSelected())
							_frame.removeSelectedEdge(e);
						else
							_frame.addSelectedEdge(e);
						_frame.getDrawing().repaint();
						return;
					}
				}
			}
			// Else ctrl isn't clicked
			else{
				//If we are setting a start node, don't do anything else except toggle start icon
				for (Node n : _frame.getDrawing().getDiagram().getNodes()){
					if (n.getStartSymbol().contains(evt.getPoint()) && n.isSelected()){
						n.setStart(!n.isStart());
						return;
					}
				}

				//Otherwise, reset all the selected nodes and edges, iterate through each node/edge to
				//change the variables which manage how they are drawn, and see if a new node/edge is selected
				_frame.resetSelected();

				for (Node n : _frame.getDrawing().getDiagram().getNodes()){
					if (n.getCircle().contains(evt.getPoint()) || (n.isStart() && n.getStartSymbol().contains(evt.getPoint()))){
						_frame.addSelectedNode(n);
						n.getTextField().setVisible(true);
						n.getTextField().select(0,0);
						n.getLabel().setVisible(false);
						_frame.getDrawing().repaint();
						_frame.setHelpText(2);
						if (n.getTextField().getText().equals("")){
							n.getTextField().setVisible(true);
							n.getLabel().setVisible(false);
							n.getTextField().grabFocus();
						}
						return;
					}
				}
				for (Edge e : _frame.getDrawing().getDiagram().getEdges()){
					Rectangle boundingBox = e.getLabel().getBounds();
					if (e.intersects(evt.getPoint().x,evt.getPoint().y)){
						_frame.addSelectedEdge(e);					
						EdgeDirection dir = e.getDirection();
						_frame.setSelectedEdgeType(dir);
						_frame.getDrawing().repaint();
						if (e.getTextField().getText().equals("")){
							e.getTextField().setVisible(true);
							e.getLabel().setVisible(false);
							e.getTextField().grabFocus();
						}
						return;
					}
					else if (boundingBox.contains(evt.getPoint().x, evt.getPoint().y)){
						_frame.resetSelected();
						e.getLabel().setVisible(false);
						e.getTextField().setVisible(true);
						e.getTextField().grabFocus();
						e.getTextField().selectAll();
						_frame.addSelectedEdge(e);
						return;
					}
				}
			}
		}
	}
	
	/**
	 * The Mouse Pressed method sets the appropriate helper variables used in Mouse Dragged.  If you press down
	 * on an edge, node, or resizing square, it selects the appropriate things.
	 * @param evt	The MouseEvent associated with the mouse being pressed.
	 */
	private void drawingPanelMousePressed(java.awt.event.MouseEvent evt) {
		_frame.deselectAllEdgeText();

		//Set the edge, node, and resizing box currently being dragged to null.
		_frame.getDrawing().grabFocus();
		_frame.resetDrawingVariables();
		
		//Check if we are trying to draw another edge
		if (evt.isShiftDown()) {
			for (Node n : _frame.getDrawing().getDiagram().getNodes()) {
				if (n.getCircle().contains(evt.getPoint())) {
					_frame.setEdgeStart(n);
					break;
				}
			}
		}
		
		//Set the node/resizing/edge being dragged to the thing being pressed on.
		if (_frame.getEdgeStart() == null) {
			for (Node n : _frame.getDrawing().getDiagram().getNodes()) {
				if (n.getCircle().contains(evt.getPoint())) {
					_frame.setNodeDragged(n);
					n.setOffset(evt.getX() - n.getCenter().x, evt.getY() - n.getCenter().y);
					_tempPoint = new HashMap<Node, Point2D.Double>();
					for (Node n2 : _frame.getNodesSelected()) {
						Point2D.Double curCenter = new Point2D.Double();
						curCenter.x = n2.getCenter().getX();
						curCenter.y = n2.getCenter().getY();
						_tempPoint.put(n2, curCenter);
					}
					if (!_tempPoint.containsKey(_frame.getNodeDragged())) {
						Point2D.Double curCenter = new Point2D.Double();
						curCenter.x = _frame.getNodeDragged().getCenter().getX();
						curCenter.y = _frame.getNodeDragged().getCenter().getY();
						_tempPoint.put(_frame.getNodeDragged(), curCenter);
					}
					
					_tempPointMoved = evt.getPoint();
					return;
				}
			}
			for (Node n : _frame.getDrawing().getDiagram().getNodes()) {
				if (n.isSelected() && n.getResize().contains(evt.getPoint())) {
					_frame.setResizing(n);
					tempRadius = n.getRadius();
					_tempPointMoved = evt.getPoint();
					return;
				}
			}
			for (Edge e : _frame.getDrawing().getDiagram().getEdges()) {
				if (e.intersects(evt.getPoint().x,evt.getPoint().y)) {
					tempAngle = e.getAngle();
					tempHeight = e.getHeight();
					tempTurn = e.getTurn();
					_tempPointMoved = evt.getPoint();
					_frame.setEdgeDragged(e);
					double dx = evt.getX() - e.getStartNode().getCenter().getX();
					double dy = evt.getY() - e.getStartNode().getCenter().getY();
					e.setOffset(e.getAngle() - Math.atan2(dy, dx));
					return;
				}
			}
		}
		_frame.setSelectPoint(evt.getPoint());
	}
	
	/**
	 * When the mouse is released, if we are currently drawing an edge, we have to create the edge between two nodes.
	 * @param evt	The MouseEvent associated with the mouse being released.
	 */
	private void drawingPanelMouseReleased(java.awt.event.MouseEvent evt) {
		if (_tempPointMoved != null && (_tempPointMoved.getX() != evt.getPoint().getX() || _tempPointMoved.getY() != evt.getPoint().getY())) {
			if (_frame.getNodeDragged() != null) {
				HashMap<Node, Point2D.Double> tempCenterPoints = new HashMap<Node, Point2D.Double>();
				for (Node n : _frame.getNodesSelected()) {
					Point2D.Double tempCenter = new Point2D.Double();
					tempCenter.x = n.getCenter().getX();
					tempCenter.y = n.getCenter().getY();
					tempCenterPoints.put(n, tempCenter);
				}
				if (!tempCenterPoints.containsKey(_frame.getNodeDragged())) {
					Point2D.Double curCenter = new Point2D.Double();
					curCenter.x = _frame.getNodeDragged().getCenter().getX();
					curCenter.y = _frame.getNodeDragged().getCenter().getY();
					tempCenterPoints.put(_frame.getNodeDragged(), curCenter);
				}
				for (Node n : _frame.getNodesSelected()) {
					Point2D.Double old_center = _tempPoint.get(n);
					if (n != null)
						n.setCenter(old_center.getX(), old_center.getY());
				}
				Point2D.Double old_center_on_drag = _tempPoint.get(_frame.getNodeDragged());
				_frame.getNodeDragged().setCenter(old_center_on_drag.getX(), old_center_on_drag.getY());
				
				_frame.getDrawing().getDiagramProject().pushCurrentOntoHistory("Moving Node");
				_frame.setStar();
				
				Point2D.Double new_center_on_drag = tempCenterPoints.get(_frame.getNodeDragged());
				_frame.getNodeDragged().setCenter(new_center_on_drag.getX(), new_center_on_drag.getY());
				for (Node n : _frame.getNodesSelected()) {
					Point2D.Double new_center = tempCenterPoints.get(n);
					n.setCenter(new_center.getX(), new_center.getY());
				}
			}
			if (_frame.getEdgeDragged() != null) {
				double newHeight = _frame.getEdgeDragged().getHeight();
				double newAngle = _frame.getEdgeDragged().getAngle();
				boolean newTurn = _frame.getEdgeDragged().getTurn();
				
				_frame.getEdgeDragged().setHeight(tempHeight);
				_frame.getEdgeDragged().setAngle(tempAngle);
				_frame.getEdgeDragged().setTurn(tempTurn);
				_frame.getDrawing().getDiagramProject().pushCurrentOntoHistory("Moving Edge");
				_frame.setStar();
				
				_frame.getEdgeDragged().setHeight(newHeight);
				_frame.getEdgeDragged().setAngle(newAngle);
				_frame.getEdgeDragged().setTurn(newTurn);
			}
			if (_frame.getResizing() != null) {
				double newRadius = _frame.getResizing().getRadius();
				
				_frame.getResizing().setRadius(tempRadius);
				_frame.getDrawing().getDiagramProject().pushCurrentOntoHistory("Resizing Node");
				_frame.setStar();
				_frame.getResizing().setRadius(newRadius);
			}
		}
		
		//If we were drawing an edge
		if (_frame.getEdgeStart() != null) {
			//Find the node which the edge is ending at
			for (Node n : _frame.getDrawing().getDiagram().getNodes()) {
				if (n.getCircle().contains(evt.getPoint())) {
					//Undo/Redo
					_frame.getDrawing().getDiagramProject().pushCurrentOntoHistory("Added Edge");
					_frame.setStar();
					
					//Reset selected edges/nodes
					_frame.resetSelected();
					//Create the new edge, reset all variables associated with maintaining the edge being drawn.
					Edge newEdge = new Edge(_frame.getEdgeStart(),n,_frame.getDrawing(),_frame.getEdgeType());
					newEdge.getTextField().grabFocus();
					newEdge.getLabel().setVisible(false);
					_frame.getEdgeStart().addConnected(newEdge);
					n.addConnected(newEdge);
					_frame.getDrawing().getDiagram().addEdge(newEdge);
					_frame.addSelectedEdge(newEdge);
					_frame.setEdgeStart(null);
					_frame.getDrawing()._progressLine = null;
					_frame.getDrawing().repaint();
					return;
				}
			}
			//If no end point was found, reset the line.
			_frame.getDrawing()._progressLine = null;
			_frame.setEdgeStart(null);
		}
		
		
		
		//If no edge is being drawn, reset all the nodes/resizing boxes/edges being dragged, upon mouse release
		_frame.resetDrawingVariables();
		_tempPointMoved = null;
		_frame.getDrawing().setSelectRectangle(null);
		_frame.getDrawing().repaint();
	}
}
