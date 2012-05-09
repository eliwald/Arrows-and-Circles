/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frontend;

import java.awt.*;
import java.awt.geom.Ellipse2D;

import javax.swing.JPanel;

import manager.DiagramProject;

import backend.*;

/**
 *
 * @author Eddie
 */

@SuppressWarnings("serial")
public class DrawingPanel extends JPanel {
	
	/*
	 * Instance variables:
	 * SMALLER_RADIUS is the value by which the inner circle of an accept state's radius should decrease from the outer circle's.
	 * _project is the DiagramProject associated with this drawingPanel; it stores most of the information associated with drawing the nodes and edges on the canvas.
	 * _progressLine is the line displayed when the user is in the process of forming an edge.
	 * _selectRectangle is the rectangle displayed when the user is dragging the mouse to select on the canvas.
	 *  
	 */

	private static final double SMALLER_RADIUS = 4;
	private DiagramProject _project;
	public Shape _progressLine;
	private Rectangle _selectRectangle;

	/**
	 * Constructor: sets _project and BGColor
	 * @param project
	 */
	public DrawingPanel(DiagramProject project) {
		_project = project;
		setBackground(Color.WHITE);
	}

	/**
	 * Returns diagram
	 * @return
	 */
	public Diagram getDiagram() {
		return _project.getCurrentDiagram();
	}

	/**
	 * returns DiagramProject
	 * @return
	 */
	public DiagramProject getDiagramProject() {
		return _project;
	}
	
	/**
	 * Sets current DiagramProject
	 * @param project
	 */
	public void setDiagramProject(DiagramProject project) {
		_project = project;
	}

	/**
	 * Sets node and edge selected values to false
	 */
	public void clearSelected() {
		for (Node n : getDiagram().getNodes()){
			n.setSelected(false);
			n.getTextField().setVisible(false);
			n.getLabel().setVisible(true);
		}
		for (Edge e : getDiagram().getEdges()){
			e.setSelected(false);
			e.getTextField().setVisible(false);
			e.getLabel().setVisible(true);
		}
	}

	/**
	 * clears current node or edge (in simulation)
	 */
	public void clearCurrent() {
		for (Node n : getDiagram().getNodes()){
			n.setCurrent(false);
		}
		for (Edge e : getDiagram().getEdges()){
			e.setCurrent(false);
		}
	}

	/**
	 * Creates node at point p and adds it to the Diagram's list
	 * @param p
	 * @return
	 */
	public Node addNode(Point p) {
		clearSelected();
		Node n = new Node(p.x,p.y, this);
		getDiagram().addNode(n);
		if (getDiagram().getNodes().size() == 1) {
			n.setStart(true);
		}
		repaint();
		return n;

	}

	/**
	 * PAINTS EVERYTHING.
	 * Options for if selected, start, end, etc.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g); 
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		for (Edge e: getDiagram().getEdges()) {
			g2.setColor(java.awt.Color.BLACK);
			g2.setStroke(new BasicStroke(1));
			if(e.isSelected()){
				g2.setColor(java.awt.Color.BLUE);
				g2.setStroke(new BasicStroke(2));
			}
			if (e.getCurrent()) {
				g2.setStroke(new BasicStroke(2));
				g2.setColor(new java.awt.Color(204, 0, 51));
			}
			if (e.getDirection() == EdgeDirection.SINGLE) {
				Shape end = e.getForward();
				g2.fill(end);
			}
			else if (e.getDirection() == EdgeDirection.DOUBLE) {
				g2.fill(e.getForward());
				g2.fill(e.getBackward());
			}
			g2.draw(e.resetArc());
			g2.setStroke(new BasicStroke(1));
		}
		if (_progressLine != null) {
			g2.setColor(java.awt.Color.BLACK);
			g2.setStroke(new BasicStroke(1));
			g2.draw(_progressLine);
		}
		for (Node n : getDiagram().getNodes()){
			g2.setColor(java.awt.Color.WHITE);
			g2.setStroke(new BasicStroke(1));
			Ellipse2D.Double ellipse = n.resetCircle();
			g2.fill(ellipse);
			g2.setColor(java.awt.Color.BLACK);
			g2.setStroke(new BasicStroke(1));


			if (n.isSelected()){
				float dash1[] = {3.0f};
				g2.setColor(java.awt.Color.BLACK);
				g2.setStroke(new BasicStroke(1.0f,
						BasicStroke.CAP_BUTT,
						BasicStroke.JOIN_MITER,
						3.0f, dash1, 0.0f));
				g2.draw(n.getStartSymbol());
				g2.setColor(n.getCurrent() ? 
						new java.awt.Color(204, 0, 51) : java.awt.Color.BLUE);
				g2.setStroke(new BasicStroke(3));
				g2.fill(n.getResize());
				g2.draw(n.getResize());
			}

			if (n.getCurrent()) {
				g2.setStroke(new BasicStroke(2));
				g2.setColor(new java.awt.Color(204, 0, 51));
			}

			if (n.isEnd()) {
				double newRad = n.getRadius() - SMALLER_RADIUS;
				double x = n.getCenter().x;
				double y = n.getCenter().y;
				g2.draw(new Ellipse2D.Double(x-newRad,y-newRad,newRad*2,newRad*2));
			}


			g2.draw(ellipse);
			g2.setStroke(new BasicStroke(1));
			if (n.isStart()) {
				if (n.isSelected()){
					g2.setColor(n.getCurrent() ? 
							new java.awt.Color(204, 0, 51) : java.awt.Color.BLUE);
					g2.setStroke(new BasicStroke(3));
				}
				else{
					g2.setColor(n.getCurrent() ? 
							new java.awt.Color(204, 0, 51) : java.awt.Color.BLACK);
					g2.setStroke(new BasicStroke(1));
				}
				g2.draw(n.getStartSymbol());

			}


		}
		if (_selectRectangle!=null){
			g2.setColor(java.awt.Color.black);
			g2.setStroke(new BasicStroke(1));
			g2.draw(_selectRectangle);
			g2.setColor(new java.awt.Color(0,0,0,10));
			g2.fill(_selectRectangle);
		}

	}

	/**
	 * Sets selectRectangle
	 * @param selectRectangle
	 */
	public void setSelectRectangle(Rectangle selectRectangle) {
		_selectRectangle = selectRectangle;
	}

	/**
	 * returns select rectangle
	 * @return
	 */
	public Rectangle getSelectRectangle() {
		return _selectRectangle;
	}

}
