/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frontend;

import backend.Edge;
import backend.Node;
import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Eddie and Sandy
 */
public class MainFrame extends javax.swing.JFrame {

    private Node _selected;
    private Node _edgeStart;
    private Node _edgeEnd;
    private Node _resizing;
    private Point _mouseLoc;
    private boolean _edgePressed = false;
    private boolean _shift = false;
    private Robot _robot;
    private int _nodesSelected = 0;
    private int _edgesSelected = 0;

    /**
     * Creates new form MainFram
     */
    public MainFrame() {
        try {
            _robot = new Robot();
        } catch (AWTException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jSplitPane1 = new javax.swing.JSplitPane();
        jSplitPane2 = new javax.swing.JSplitPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        drawingPanel1 = new frontend.DrawingPanel();
        jSplitPane3 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jTextField1 = new javax.swing.JTextField();
        jSlider1 = new javax.swing.JSlider();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        jMenu7 = new javax.swing.JMenu();

        jMenuItem2.setText("jMenuItem2");

        jMenu6.setText("jMenu6");

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Arrows & Circles Alpha");

        jTabbedPane1.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent evt){
                if (((JTabbedPane)evt.getSource()).getSelectedComponent() != null) {
                    jScrollPane1 = (JScrollPane)((JTabbedPane)evt.getSource()).getSelectedComponent();
                    drawingPanel1 = (DrawingPanel)jScrollPane1.getViewport().getView();
                }
            }
        });

        drawingPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                drawingPanel1MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                drawingPanel1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                drawingPanel1MouseReleased(evt);
            }
        });
        drawingPanel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                drawingPanel1MouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                drawingPanel1MouseMoved(evt);
            }
        });
        drawingPanel1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                drawingPanel1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                drawingPanel1KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout drawingPanel1Layout = new javax.swing.GroupLayout(drawingPanel1);
        drawingPanel1.setLayout(drawingPanel1Layout);
        drawingPanel1Layout.setHorizontalGroup(
            drawingPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 617, Short.MAX_VALUE)
        );
        drawingPanel1Layout.setVerticalGroup(
            drawingPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 842, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(drawingPanel1);

        jTabbedPane1.addTab("Untitled", jScrollPane1);

        jSplitPane2.setRightComponent(jTabbedPane1);

        MouseMotionListener[] actions2 = jSplitPane3.getMouseMotionListeners();

        for (MouseMotionListener m : actions2){
            jSplitPane3.removeMouseMotionListener(m);
        }
        jSplitPane3.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane3.setResizeWeight(0.5);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jRadioButton1.setText("jRadioButton1");

        jRadioButton2.setText("jRadioButton2");

        jRadioButton3.setText("jRadioButton3");

        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel2.setText("Toolbox");
        jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jRadioButton1)
                .addContainerGap(151, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(189, Short.MAX_VALUE)
                .addComponent(jRadioButton2)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(99, 99, 99)
                .addComponent(jRadioButton3)
                .addContainerGap(102, Short.MAX_VALUE))
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButton2)
                .addGap(18, 18, 18)
                .addComponent(jRadioButton3)
                .addContainerGap(143, Short.MAX_VALUE))
        );

        jSplitPane3.setRightComponent(jPanel1);

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        jTextField1.setText("jTextField1");

        jButton1.setText("PLAY");

        jButton2.setText("PLAY");

        jButton4.setText("PLAY");

        jButton3.setText("PLAY");

        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setText("Simulation");
        jLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(48, 48, 48))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(46, Short.MAX_VALUE))
        );

        jSplitPane3.setLeftComponent(jPanel2);

        jSplitPane2.setLeftComponent(jSplitPane3);

        jMenu3.setText("File");

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setText("New");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem3);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setText("Open");
        jMenu3.add(jMenuItem4);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem5.setText("Save");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem5);

        jMenuItem7.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem7.setText("Save As...");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem7);

        jMenuBar2.add(jMenu3);

        jMenu4.setText("Edit");
        jMenuBar2.add(jMenu4);

        jMenu5.setText("View");
        jMenuBar2.add(jMenu5);

        jMenu7.setText("Help");
        jMenuBar2.add(jMenu7);

        setJMenuBar(jMenuBar2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 970, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 601, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
  
    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        javax.swing.JScrollPane newPane = new javax.swing.JScrollPane();
        DrawingPanel newPanel = new DrawingPanel();
        newPane.setViewportView(newPanel);
        jScrollPane1 = newPane;
        drawingPanel1 = newPanel;
        drawingPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                drawingPanel1MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                drawingPanel1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                drawingPanel1MouseReleased(evt);
            }
        });
        drawingPanel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                drawingPanel1MouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                drawingPanel1MouseMoved(evt);
            }
        });
        drawingPanel1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                drawingPanel1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                drawingPanel1KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout drawingPanel1Layout = new javax.swing.GroupLayout(drawingPanel1);
        drawingPanel1.setLayout(drawingPanel1Layout);
        drawingPanel1Layout.setHorizontalGroup(
            drawingPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 599, Short.MAX_VALUE)
        );
        drawingPanel1Layout.setVerticalGroup(
            drawingPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 778, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Untitled", new ImageIcon("frontend/ask.png"), jScrollPane1);
        jTabbedPane1.setSelectedIndex(jTabbedPane1.getTabCount() - 1);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void drawingPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_drawingPanel1MouseClicked
        // TODO add your handling code here:
        String mod = MouseEvent.getMouseModifiersText(evt.getModifiers());
        if (evt.getClickCount() >=2 && mod.equals("Button1")){
            _nodesSelected = 0;
            _edgesSelected = 0;
            Node add = drawingPanel1.addNode(evt.getPoint());
            for (Node n : drawingPanel1.getDiagram().getNodes()){
                n.setSelected(false);
            }
            for (Edge e : drawingPanel1.getDiagram().getEdges()){
                e.setSelected(false);
            }
            add.setSelected(true);

            add.getTextField().setVisible(true);
            add.getLabel().setVisible(false);
            _nodesSelected++;
        }
        else{
            
            if (mod.equals("Ctrl+Button1")) {
                for (Node n : drawingPanel1.getDiagram().getNodes()){
                    if (n.getCircle().contains(evt.getPoint())){
                        if (n.isSelected()){
                            n.setSelected(false);
                            _nodesSelected--;
                        }
                        else{
                            n.setSelected(true);
                            _nodesSelected++;
                        }
                        drawingPanel1.repaint();
                    }
                }
                for (Edge e : drawingPanel1.getDiagram().getEdges()){
                    if (e.getCurve().intersects(evt.getPoint().x,evt.getPoint().y, 2, 2)){
                        if (e.isSelected()){
                            e.setSelected(false);
                            _edgesSelected--;
                        }
                        else{
                            e.setSelected(true);
                            _edgesSelected++;
                        }
                        drawingPanel1.repaint();
                    }
                }
            }
            else{
                _nodesSelected = 0;
                for (Node n : drawingPanel1.getDiagram().getNodes()){
                    n.setSelected(false);
                    n.getTextField().setVisible(false);
                    n.getLabel().setVisible(true);
                }
                for (Edge e : drawingPanel1.getDiagram().getEdges()){
                    e.setSelected(false);
                    e.getTextField().setVisible(false);
                    e.getLabel().setVisible(true);
                }
                for (Node n : drawingPanel1.getDiagram().getNodes()){
                    if (n.getCircle().contains(evt.getPoint())){
                        n.setSelected(true);
                        n.getTextField().setVisible(true);
                        n.getLabel().setVisible(false);
                        _nodesSelected++;
                        drawingPanel1.repaint();
                        return;
                    }
                }
                for (Edge e : drawingPanel1.getDiagram().getEdges()){
                    if (e.getCurve().intersects(evt.getPoint().x,evt.getPoint().y, 2, 2)){
                        e.setSelected(true);
                        e.getTextField().setVisible(true);
                        e.getLabel().setVisible(false);
                        _edgesSelected++;
                        break;
                    }
                }
            }
        }
    }//GEN-LAST:event_drawingPanel1MouseClicked

    private void drawingPanel1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_drawingPanel1MouseDragged
        // TODO add your handling code here:
        _mouseLoc = evt.getPoint();
        if (_edgePressed && _edgeStart != null) {
            if (_shift) {
                Node n = _edgeStart;
                if (_edgeEnd != null) {
                    n = _edgeEnd;
                }
                Point loc = drawingPanel1.getLocationOnScreen();
                double difX = _mouseLoc.x - n.getCenter().x;
                double difY = _mouseLoc.y - n.getCenter().y;
                double vecX = difX/Math.sqrt((difX*difX+difY*difY));
                double vecY = difY/Math.sqrt((difX*difX+difY*difY));
                Point2D.Double curr = new Point2D.Double(n.getCenter().x+(n.getRadius()*vecX),n.getCenter().y+(n.getRadius()*vecY));
                _robot.mouseMove(loc.x+(int)curr.x, loc.y+(int)curr.y);
            }
            Node con = null;
            for (Node n : drawingPanel1.getDiagram().getNodes()) {
                if (n.getCircle().contains(_mouseLoc)) {
                    con = n;
                }
            }
            if (con != null) {
                double difX = con.getCenter().x - _edgeStart.getCenter().x;
                double difY = con.getCenter().x - _edgeStart.getCenter().y;
                double vecX = difX/Math.sqrt((difX*difX+difY*difY));
//                double vecX = Math.sqrt(con.getCenter().x*con.getCenter().x - _edgeStart.getCenter().x*_edgeStart.getCenter().x);
                double vecY = difY/Math.sqrt((difX*difX+difY*difY));
//                double vecY = Math.sqrt(con.getCenter().y*con.getCenter().y - _edgeStart.getCenter().y*_edgeStart.getCenter().y);

                Point2D.Double point_start = new Point2D.Double(_edgeStart.getCenter().x+(_edgeStart.getRadius()*vecX),_edgeStart.getCenter().y+(_edgeStart.getRadius()*vecY));
                Point2D.Double point_end = new Point2D.Double(con.getCenter().x-(con.getRadius()*vecX),con.getCenter().y-(con.getRadius()*vecY));
                System.out.println("new line");
                drawingPanel1._progressLine = new Line2D.Double(point_start, point_end);
            }
            
            else {
                double difX = _mouseLoc.x - _edgeStart.getCenter().x;
                double difY = _mouseLoc.y - _edgeStart.getCenter().y;
                double vecX = difX/Math.sqrt((difX*difX+difY*difY));
                double vecY = difY/Math.sqrt((difX*difX+difY*difY));
                Point2D.Double point_start = new Point2D.Double(_edgeStart.getCenter().x+(_edgeStart.getRadius()*vecX),_edgeStart.getCenter().y+(_edgeStart.getRadius()*vecY));
                drawingPanel1._progressLine = new Line2D.Double(point_start, _mouseLoc);
            }

            drawingPanel1.repaint();
        }
        else if (_selected != null && drawingPanel1.contains(evt.getPoint())){
            _selected.setCenter(evt.getX()-_selected.getOffset().x, evt.getY()-_selected.getOffset().y);
            _selected.resetCircle();
            drawingPanel1.repaint();
        }

        if (_resizing != null) {
            Rectangle2D rec = _resizing.getResize();

            double dif = Math.max(evt.getPoint().x - rec.getCenterX(),evt.getPoint().y - rec.getCenterY());

            double newX = _resizing.getCenter().x - _resizing.getRadius();
            double newY = _resizing.getCenter().y - _resizing.getRadius();
            double newR = _resizing.getRadius() + dif/2;
            if (newR < _resizing.MIN_RADIUS) {
                return;
            }




            //_resizing.setCenter(_resizing.getCenter().x + dif, _resizing.getCenter().y + dif);
            _resizing.setRadius(newR);

            drawingPanel1.repaint();
        }
        
    }//GEN-LAST:event_drawingPanel1MouseDragged

    private void drawingPanel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_drawingPanel1MousePressed
        // TODO add your handling code here:

        drawingPanel1.grabFocus();

        Point newp = new Point(evt.getX(),evt.getY());
        _selected = null;
        for (Node n : drawingPanel1.getDiagram().getNodes()) {
            n.getTextField().select(0, 0);
            if (n.getCircle().contains(newp)) {
                _selected = n;
                n.setOffset(evt.getX() - n.getCenter().x, evt.getY() - n.getCenter().y);

            }
            if (n.isSelected()) {
                Rectangle2D r = n.getResize();
                if (n.getResize().contains(newp)) {
                    _resizing = n;
                }
            }
        }
        String mod = MouseEvent.getMouseModifiersText(evt.getModifiers());
        if (mod.contains("Shift")) {
            _edgePressed = true;
        }

    }//GEN-LAST:event_drawingPanel1MousePressed

    private void drawingPanel1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_drawingPanel1KeyPressed
        // TODO add your handling code here:
        drawingPanel1.grabFocus();
        if(evt.getKeyCode() == KeyEvent.VK_DELETE || evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            ArrayList<Node> toRemoveNodes = new ArrayList<Node>();
            for (Node n : drawingPanel1.getDiagram().getNodes()){
                if (n.isSelected()){
                    toRemoveNodes.add(n);
                }
            }
            for (Node n : toRemoveNodes){
                for (Edge e : n.getConnected()){
                    drawingPanel1.getDiagram().getEdges().remove(e);
                }
                drawingPanel1.remove(n.getLabel());
                drawingPanel1.remove(n.getTextField());
                drawingPanel1.getDiagram().getNodes().remove(n);
            }
            ArrayList<Edge> toRemoveEdges = new ArrayList<Edge>();
            for (Edge e : drawingPanel1.getDiagram().getEdges()){
                if (e.isSelected()){
                    toRemoveEdges.add(e);
                }
            }
            for (Edge e : toRemoveEdges){
                drawingPanel1.remove(e.getLabel());
                drawingPanel1.remove(e.getTextField());
                drawingPanel1.getDiagram().getEdges().remove(e);
            }

        }
        if(evt.getKeyCode() == KeyEvent.VK_SHIFT) {
            _shift = true;
            Node currNode = null;

            int dist;
            int mindist = Integer.MAX_VALUE;
            for (Node n : drawingPanel1.getDiagram().getNodes()) {
                int difX = (int)n.getCenter().x - _mouseLoc.x;
                int difY = (int)n.getCenter().y - _mouseLoc.y;
                dist = (int)Math.sqrt(difX*difX + difY*difY);
                if (dist < mindist) {
                    mindist = dist;
                    currNode = n;
                }
            }
            if (currNode == null) {
                return;
            }
            if (_edgePressed) {
                _edgeEnd = currNode;
            }
            else {
                _edgeStart = currNode;
            }
            Point loc = drawingPanel1.getLocationOnScreen();
            double difX = _mouseLoc.x - currNode.getCenter().x;
            double difY = _mouseLoc.y - currNode.getCenter().y;
            double vecX = difX/Math.sqrt((difX*difX+difY*difY));
            double vecY = difY/Math.sqrt((difX*difX+difY*difY));
            Point2D.Double curr = new Point2D.Double(currNode.getCenter().x+(currNode.getRadius()*vecX),currNode.getCenter().y+(currNode.getRadius()*vecY));
            _robot.mouseMove(loc.x+(int)curr.x, loc.y+(int)curr.y);
            drawingPanel1.repaint();
        }
        else {
            _shift = false;
        }
    }//GEN-LAST:event_drawingPanel1KeyPressed

    private void drawingPanel1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_drawingPanel1MouseMoved
        // TODO add your handling code here:

        
        _mouseLoc = evt.getPoint();
        if (_shift) {
            Node currNode = null;
            int dist;
            int mindist = Integer.MAX_VALUE;
            for (Node n : drawingPanel1.getDiagram().getNodes()) {
                int difX = (int)n.getCenter().x - _mouseLoc.x;
                int difY = (int)n.getCenter().y - _mouseLoc.y;
                dist = (int)Math.sqrt(difX*difX + difY*difY);
                if (dist < mindist) {
                    mindist = dist;
                    currNode = n;
                }
            }
            if (currNode == null) {
                return;
            }
            Point loc = drawingPanel1.getLocationOnScreen();
            double difX = _mouseLoc.x - currNode.getCenter().x;
            double difY = _mouseLoc.y - currNode.getCenter().y;
            double vecX = difX/Math.sqrt((difX*difX+difY*difY));
            double vecY = difY/Math.sqrt((difX*difX+difY*difY));
            Point2D.Double curr = new Point2D.Double(currNode.getCenter().x+(currNode.getRadius()*vecX),currNode.getCenter().y+(currNode.getRadius()*vecY));
            _robot.mouseMove(loc.x+(int)curr.x, loc.y+(int)curr.y);
        }
    }//GEN-LAST:event_drawingPanel1MouseMoved

    private void drawingPanel1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_drawingPanel1KeyReleased
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_SHIFT) {
            _shift = false;
            _edgeEnd = null;
        }
        _resizing = null;
    }//GEN-LAST:event_drawingPanel1KeyReleased

    private void drawingPanel1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_drawingPanel1MouseReleased
        // TODO add your handling code here:
        if (_edgePressed) {
            _edgePressed = false;
            if (_edgeEnd != null) {
                drawingPanel1.addEdge(_edgeStart,_edgeEnd);
                _edgeStart = null;
                drawingPanel1._progressLine = null;
                drawingPanel1.repaint();
                _edgeEnd = null;
            }
            else {
                for (Node n : drawingPanel1.getDiagram().getNodes()) {
                    if (n.getCircle().contains(_mouseLoc)) {
                        drawingPanel1.addEdge(_edgeStart, n);
                        drawingPanel1._progressLine = null;
                        drawingPanel1.repaint();
                        break;
                     }
                }
                _edgeStart = null;
                drawingPanel1._progressLine = null;
                drawingPanel1.repaint();
            }
        }
        _edgePressed = false;
        _resizing = null;

    }//GEN-LAST:event_drawingPanel1MouseReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private frontend.DrawingPanel drawingPanel1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
