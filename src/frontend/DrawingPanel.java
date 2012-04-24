/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frontend;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.JPanel;
import backend.*;
import java.awt.Graphics;
import java.awt.geom.Line2D;

/**
 *
 * @author Eddie
 */

public class DrawingPanel extends JPanel {
    
    private Diagram _diagram;

    public static Line2D.Double _progressLine;
    
    public DrawingPanel() {
        _diagram = new Diagram();
        this.setBackground(Color.WHITE);
    }
    
    public Diagram getDiagram() {
        return _diagram;
    }
    
    public void addNode(Point p) {
        Node n = new Node(p.x,p.y);
        _diagram.addNode(n);
        repaint();
        
    }

    public void addEdge(Node n1, Node n2) {
        Edge e = new Edge(n1,n2,n1.getCenter(),n2.getCenter());
        _diagram.addEdge(e);
        repaint();
    }

    
    
    @Override
    public void paintComponent(Graphics g) {
       super.paintComponent(g); 
       Graphics2D g2 = (Graphics2D)g;
       for (Node n : _diagram.getNodes()){
           g2.draw(n.resetCircle());
       }

       for (Edge e: _diagram.getEdges()) {
           g2.draw(e.resetLine());
       }

       if (_progressLine != null)
            g2.draw(_progressLine);
       
    }
    
}
