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

/**
 *
 * @author Eddie
 */

public class DrawingPanel extends JPanel {
    
    private Diagram _diagram;
    
    public DrawingPanel() {
        _diagram = new Diagram();
        this.setBackground(Color.WHITE);
    }
    
    public Diagram getDiagram() {
        return _diagram;
    }
    
    public void addNode(Point p) {
        System.out.println("shit");
        Node n = new Node(p.x,p.y);
        _diagram.addNode(n);
        repaint();
        
    }
    
    
    @Override
    public void paintComponent(Graphics g) {
       super.paintComponent(g); 
       Graphics2D g2 = (Graphics2D)g;
       for (Node n : _diagram.getNodes()){
           g2.draw(n.resetCircle());
       }
       
    }
    
}
