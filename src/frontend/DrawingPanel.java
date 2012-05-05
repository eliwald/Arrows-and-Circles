/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frontend;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import manager.DiagramProject;
import backend.*;

import java.awt.BasicStroke;
import java.awt.Graphics;

import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import java.awt.geom.Line2D;
import java.awt.geom.QuadCurve2D;

/**
 *
 * @author Eddie
 */

public class DrawingPanel extends JPanel {
    
    private Diagram _diagram;
    public Shape _progressLine;

    public DrawingPanel() {
        _diagram = new Diagram();
        this.setBackground(Color.WHITE);
    }
    
    public Diagram getDiagram() {
        return _diagram;
    }

    public void clearSelected(){
        for (Node n : _diagram.getNodes()){
            n.setSelected(false);
            n.getTextField().setVisible(false);
            n.getLabel().setVisible(true);
        }
        for (Edge e : _diagram.getEdges()){
            e.setSelected(false);
            e.getTextField().setVisible(false);
            e.getLabel().setVisible(true);
        }
    }
    
    public void clearCurrent() {
    	for (Node n : _diagram.getNodes()){
            n.setCurrent(false);
        }
        for (Edge e : _diagram.getEdges()){
            e.setCurrent(false);
        }
    }

    public Node addNode(Point p) {
        clearSelected();
        Node n = new Node(p.x,p.y, this);
        _diagram.addNode(n);
        if (_diagram.getNodes().size() == 1) {
            n.setStart(true);
        }
        repaint();
        return n;
        
    }

    /*public void addEdge(Node n1, Node n2) {
    	if (n1 != null && n2 != null) {
    		clearAll();
//    		DiagramProject dp = new DiagramProject();
//    		dp.modify(new DiagramModifyAction() {
//
//				@Override
//				public boolean modify(Diagram diagram) {
//					Edge e = new Edge(n1,n2,n1.getCenter(),n2.getCenter());
//					diagram.addEdge(e);
//					return false;
//				}
//
//				@Override
//				public String message() {
//					// TODO Auto-generated method stub
//					return null;
//				}
//			});
    		Edge e = new Edge(n1,n2,n1.getCenter(),n2.getCenter(), this,null);
            n1.addConnected(e);
            n2.addConnected(e);
    		_diagram.addEdge(e);
	        repaint();
    	}
    }*/

    
    
    @Override
    public void paintComponent(Graphics g) {
       super.paintComponent(g); 
       Graphics2D g2 = (Graphics2D) g;
       g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
       for (Edge e: _diagram.getEdges()) {
    	   g2.setColor(java.awt.Color.BLACK);
           g2.setStroke(new BasicStroke(1));
           if(e.isSelected()){
               g2.setColor(java.awt.Color.BLUE);
               g2.setStroke(new BasicStroke(2));
           }
           if (e.getCurrent()) {
               g2.setColor(java.awt.Color.PINK);
           }
           if (e.getDirection() == EdgeDirection.SINGLE && e.getStartNode() != e.getEndNode()) {
                Ellipse2D.Double end = e.getForward();
                g2.fill(end);
           }
           else if (e.getDirection() == EdgeDirection.DOUBLE && e.getStartNode() != e.getEndNode()) {
                g2.fill(e.getForward());
                g2.fill(e.getBackward());
           }
           g2.draw(e.resetArc());
       }
       if (_progressLine != null) {
    	   g2.setColor(java.awt.Color.BLACK);
           g2.setStroke(new BasicStroke(1));
           g2.draw(_progressLine);
       }
       for (Node n : _diagram.getNodes()){
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
               g2.setColor(java.awt.Color.BLUE);
               g2.setStroke(new BasicStroke(3));
               g2.fill(n.getResize());
               g2.draw(n.getResize());
           }
           
           if (n.getCurrent()) {
               g2.setColor(java.awt.Color.PINK);
           }

           if (n.isEnd()) {
               double newRad = n.getRadius()-4;
               double x = n.getCenter().x;
               double y = n.getCenter().y;
               g2.draw(new Ellipse2D.Double(x-newRad,y-newRad,newRad*2,newRad*2));
           }


           g2.draw(ellipse);
           if (n.isStart()) {
               if (n.isSelected()){
                   g2.setColor(java.awt.Color.BLUE);
                   g2.setStroke(new BasicStroke(3));
               }
               else{
                   g2.setColor(java.awt.Color.BLACK);
                   g2.setStroke(new BasicStroke(1));
               }
               g2.draw(n.getStartSymbol());

           }

       }

    }
    
}
