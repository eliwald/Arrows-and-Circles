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

public class DrawingPanel extends JPanel {

	private DiagramProject _project;
	public Shape _progressLine;
	private Rectangle _selectRectangle;

	public DrawingPanel(DiagramProject project) {
		_project = project;
		setBackground(Color.WHITE);
	}

	public Diagram getDiagram() {
		return _project.getCurrentDiagram();
	}

	public DiagramProject getDiagramProject() {
		return _project;
	}
	
	public void setDiagramProject(DiagramProject project) {
		_project = project;
	}

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

	public void clearCurrent() {
		for (Node n : getDiagram().getNodes()){
			n.setCurrent(false);
		}
		for (Edge e : getDiagram().getEdges()){
			e.setCurrent(false);
		}
	}

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
				double newRad = n.getRadius()-4;
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

	public void setSelectRectangle(Rectangle selectRectangle) {
		_selectRectangle = selectRectangle;
	}

	public Rectangle getSelectRectangle() {
		return _selectRectangle;
	}

}
