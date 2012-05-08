package frontend;

import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collection;

import backend.Edge;
import backend.Node;

/**
 * This KeyListener is used in the MainFrame to detect when the user acts on the canvas.
 * 
 * It is added to the DrawingPanel in the MainFrame.
 * 
 * @author ewald
 *
 */
public class DrawingPanelKeyListener extends KeyAdapter {
	private MainFrame _frame;
	public DrawingPanelKeyListener(MainFrame frame) {
		_frame = frame;
	}
	
	public void keyPressed(java.awt.event.KeyEvent evt) {
		drawingPanelKeyPressed(evt);
	}
	public void keyReleased(java.awt.event.KeyEvent evt) {
		drawingPanelKeyReleased(evt);
	}
	
	/**
	 * The Key Pressed method handles the pressing of delete, shift, or s.  If "delete" is pressed, it deletes
	 * all the currently selected nodes and edges (and all edges to the nodes).  If "s" is pressed, it snaps
	 * the mouse to the nearest node.  If "Shift" is pressed, it gets ready to create an edge by setting
	 * _edgeStart.
	 * @param evt	The KeyEvent associated with the key being pressed.
	 */
	private void drawingPanelKeyPressed(java.awt.event.KeyEvent evt) {
		//If delete is pressed, delete all edges/nodes selected, and the edges connected to the selected nodes.
		if(evt.getKeyCode() == KeyEvent.VK_DELETE || evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			if (_frame.getNodesSelected().size() > 0 || _frame.getEdgesSelected().size() > 0)
				_frame.getDrawing().getDiagramProject().pushCurrentOntoHistory("Added Node");
			
			for (Node n : _frame.getNodesSelected()){
				Node connectedNode;
				Collection<Edge> edges = n.getConnected();
				for (Edge e : edges){
					connectedNode = e.getStartNode() == n ? e.getEndNode() : e.getStartNode();
					_frame.getDrawing().getDiagram().getEdges().remove(e);
					_frame.removeSelectedEdge(e);
					_frame.getDrawing().remove(e.getLabel());
					_frame.getDrawing().remove(e.getTextField());
					connectedNode.removeConnected(e);
				}
				_frame.getDrawing().remove(n.getLabel());
				_frame.getDrawing().remove(n.getTextField());
				_frame.getDrawing().getDiagram().getNodes().remove(n);
			}
			for (Edge e : _frame.getEdgesSelected()){
				for (Node n : _frame.getDrawing().getDiagram().getNodes())
					n.removeConnected(e);
				_frame.getDrawing().remove(e.getLabel());
				_frame.getDrawing().remove(e.getTextField());
				_frame.getDrawing().getDiagram().getEdges().remove(e);
			}
			_frame.resetSelected();
		}
		//Otherwise if "s" is pressed, snap to the nearest node.
		else if(evt.getKeyCode() == KeyEvent.VK_S && evt.getModifiers() != 2) {
			Node currNode = null;

			int dist;
			int mindist = Integer.MAX_VALUE;
			for (Node n : _frame.getDrawing().getDiagram().getNodes()) {
				int difX = (int)n.getCenter().x - _frame.getMouseLoc().x;
				int difY = (int)n.getCenter().y - _frame.getMouseLoc().y;
				dist = (int)Math.sqrt(difX*difX + difY*difY);
				if (dist < mindist) {
					mindist = dist;
					currNode = n;
				}
			}
			if (currNode == null) {
				return;
			}
			Point loc = _frame.getDrawing().getLocationOnScreen();
			double difX = _frame.getMouseLoc().x - currNode.getCenter().x;
			double difY = _frame.getMouseLoc().y - currNode.getCenter().y;
			double vecX = difX/Math.sqrt((difX*difX+difY*difY));
			double vecY = difY/Math.sqrt((difX*difX+difY*difY));
			if (difX == 0 && difY == 0)
				_frame.getRobot().mouseMove(loc.x + (int) currNode.getCenter().x, loc.y + (int) (currNode.getCenter().y - currNode.getRadius()));
			else
				_frame.getRobot().mouseMove(loc.x + (int) (currNode.getCenter().x+(currNode.getRadius()*vecX)), loc.y + (int) (currNode.getCenter().y+(currNode.getRadius()*vecY)));
		}
		_frame.getDrawing().repaint();
	}

	/**
	 * The Key Released method handles resetting the _edgeStart variable to null if
	 * shift is released.
	 * @param evt	The KeyEvent associated with the key being released.
	 */
	private void drawingPanelKeyReleased(java.awt.event.KeyEvent evt) {
		if (evt.getKeyCode() == KeyEvent.VK_SHIFT) {
			if (_frame.getDrawing()._progressLine == null)
				_frame.setEdgeStart(null);
		}
	}
}
