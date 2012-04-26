/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frontend;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.JPanel;

import manager.DiagramModifyAction;
import manager.DiagramProject;
import backend.*;

import java.awt.BasicStroke;
import java.awt.Graphics;

import java.awt.Polygon;
import java.awt.geom.Ellipse2D;

import java.awt.geom.Line2D;
import java.awt.geom.QuadCurve2D;

/**
 *
 * @author Eddie
 */

public class DrawingPanel extends JPanel {
    
    private Diagram _diagram;
    public Line2D.Double _progressLine;

    public DrawingPanel() {
        _diagram = new Diagram();
        this.setBackground(Color.WHITE);
    }
    
    public Diagram getDiagram() {
        return _diagram;
    }

    public void clearAll(){
        for (Node n : _diagram.getNodes()){
            n.setSelected(false);
        }
        for (Edge e : _diagram.getEdges()){
            e.setSelected(false);
        }
    }

    public Node addNode(Point p) {
        clearAll();
        Node n = new Node(p.x,p.y, this);
        _diagram.addNode(n);
        if (_diagram.getNodes().size() == 1) {
            n.setStart(true);
        }
        repaint();
        return n;
        
    }

    public void addEdge(Node n1, Node n2) {
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
    		Edge e = new Edge(n1,n2,n1.getCenter(),n2.getCenter(), this);
            n1.addConnected(e);
            n2.addConnected(e);
    		_diagram.addEdge(e);
	        repaint();
    	}
    }

    
    
    @Override
    public void paintComponent(Graphics g) {
       super.paintComponent(g); 
       Graphics2D g2 = (Graphics2D)g;
       for (Node n : _diagram.getNodes()){
    	   g2.setColor(java.awt.Color.BLACK);
           g2.setStroke(new BasicStroke(1));
           
           if (n.isSelected()){
               g2.setColor(java.awt.Color.BLUE);
               g2.setStroke(new BasicStroke(3));
               g2.fill(n.getResize());
               g2.draw(n.getResize());
           }
           
           if (n.getCurrent()) {
               g2.setColor(java.awt.Color.PINK);
           }

           if (n.isStart()) {
                Polygon _startSymbol = new Polygon();
                _startSymbol.addPoint((int)(n.getCenter().x - n.getRadius()),(int) (n.getCenter().y));
                _startSymbol.addPoint((int)(n.getCenter().x - n.getRadius() - 20),(int) (n.getCenter().y + 10));
                _startSymbol.addPoint((int)(n.getCenter().x - n.getRadius() - 20),(int) (n.getCenter().y - 10));
                g2.draw(_startSymbol);
           }
           if (n.isEnd()) {
               double newRad = n.getRadius()-4;
               double x = n.getCenter().x;
               double y = n.getCenter().y;
               g2.draw(new Ellipse2D.Double(x-newRad,y-newRad,newRad*2,newRad*2));
           }
           g2.draw(n.resetCircle());

       }

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
           g2.draw(e.getForward());
           g2.draw(e.resetLine());
       }

       if (_progressLine != null) {
    	   g2.setColor(java.awt.Color.BLACK);
           g2.setStroke(new BasicStroke(1));
           g2.draw(_progressLine);
       }
       
    }
    
}
