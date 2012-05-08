package frontend;

import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
			if (mod.equals("Ctrl+Button1")) {
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
		if (MouseEvent.getMouseModifiersText(evt.getModifiers()).contains("Shift")) {
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
					return;
				}
			}
			for (Node n : _frame.getDrawing().getDiagram().getNodes()) {
				if (n.isSelected() && n.getResize().contains(evt.getPoint())) {
					_frame.setResizing(n);
					return;
				}
			}
			for (Edge e : _frame.getDrawing().getDiagram().getEdges()) {
				if (e.intersects(evt.getPoint().x,evt.getPoint().y)) {
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
		//If we were drawing an edge
		if (_frame.getEdgeStart() != null) {
			//Find the node which the edge is ending at
			for (Node n : _frame.getDrawing().getDiagram().getNodes()) {
				if (n.getCircle().contains(evt.getPoint())) {
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
		_frame.getDrawing().setSelectRectangle(null);
		_frame.getDrawing().repaint();
	}
}
