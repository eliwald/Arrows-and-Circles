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
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.QuadCurve2D;
import javax.swing.JLabel;

/**
 *
 * @author Eddie
 */

public class DrawingPanel extends JPanel {
    
    private Diagram _diagram;
    public Line2D.Double _progressLine;
    private JLabel _startLabel;
    
    public DrawingPanel() {
        _diagram = new Diagram();
        this.setBackground(Color.WHITE);
        _startLabel = new JLabel("Start ->");
        _startLabel.setSize(50,10);
        _startLabel.setVisible(false);
        this.add(_startLabel);
    }
    
    public Diagram getDiagram() {
        return _diagram;
    }

    private void clearAll(){
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
        return n;
        
    }

    public void addEdge(Node n1, Node n2) {
    	if (n1 != null && n2 != null) {
    		for (Edge e : _diagram.getEdges()){
                e.setSelected(false);
            }
    		Edge e = new Edge(n1,n2,n1.getCenter(),n2.getCenter(),this);

            n1.addConnected(e);
            n2.addConnected(e);
    		_diagram.addEdge(e);
    	}
    }
    
    @Override
    public void paintComponent(Graphics g) {
       super.paintComponent(g); 
       Graphics2D g2 = (Graphics2D)g;
       for (Node n : _diagram.getNodes()){
           if (n.isSelected()){
               g2.setColor(java.awt.Color.BLUE);
               g2.setStroke(new BasicStroke(3));
               g2.fill(n.getResize());
               g2.draw(n.getResize());

           }
           if (n.isStart()) {
                _startLabel.setLocation((int)(n.getCircle().getX() - 20),(int)(n.getCircle().getY()));
                _startLabel.setVisible(true);
           }
           if (n.getCurrent()) {
               g2.setColor(java.awt.Color.PINK);
           }
           g2.draw(n.resetCircle());
           g2.setColor(java.awt.Color.BLACK);
           g2.setStroke(new BasicStroke(1));
           if (n.isEnd()) {
               double newRad = n.getRadius()-4;
               double x = n.getCenter().x;
               double y = n.getCenter().y;
               g2.draw(new Ellipse2D.Double(x-newRad,y-newRad,newRad*2,newRad*2));
           }
       }

       for (Edge e: _diagram.getEdges()) {
           if(e.isSelected()){
               g2.setColor(java.awt.Color.BLUE);
               g2.setStroke(new BasicStroke(2));
           }
           if (e.getCurrent()) {
               g2.setColor(java.awt.Color.PINK);
           }
           g2.draw(e.resetLine());

           g2.fill(e.getForward());

           g2.setColor(java.awt.Color.BLACK);
           g2.setStroke(new BasicStroke(1));
       }

       if (_progressLine != null)
            g2.draw(_progressLine);
       
    }
    
}
