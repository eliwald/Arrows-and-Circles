/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frontend;

import backend.DiagramObject;
import backend.Edge;
import backend.EdgeDirection;
import backend.InvalidDFSMException;
import backend.Node;
import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.Timer;

/**
 *
 * @author Eddie and Sandy
 */
public class MainFrame extends javax.swing.JFrame {

    private Node _edgeStart;
    private Node _resizing;
    private Point _mouseLoc;
    private Robot _robot;
    private Collection<Node> _nodesSelected;
    private Collection<Edge> _edgesSelected;
    private Node _nodeDragged;
    private Edge _edgeDragged;
    private List<DiagramObject> _sim;
    private ListIterator<DiagramObject> _iter;
    private Timer _simTimer;
    private EdgeDirection _edgeType;
    private boolean _shift = false;
    private static final String PLAY_FILEPATH = "./src/img/play.png";
    private static final String PAUSE_FILEPATH = "./src/img/pause.png";


    /**
     * Creates new form MainFram
     */
    public MainFrame() {
        _edgeType = EdgeDirection.SINGLE;
        _simTimer = new Timer(1000, new SimListener());
        try {
            _robot = new Robot();
        } catch (AWTException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        initComponents();
    }

    private void initComponents() {

        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jSplitPane1 = new javax.swing.JSplitPane();
        _edgeTypeGrp = new javax.swing.ButtonGroup();
        jSplitPane2 = new javax.swing.JSplitPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        drawingPanel1 = new frontend.DrawingPanel();
        jSplitPane3 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        _singlyBtn = new javax.swing.JRadioButton();
        _doublyBtn = new javax.swing.JRadioButton();
        _undirectedBtn = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jTextField1 = new javax.swing.JTextField();
        _slider = new javax.swing.JSlider();
        _rewindBtn = new javax.swing.JButton();
        _stopBtn = new javax.swing.JButton();
        _playPauseBtn = new javax.swing.JButton();
        _forwardBtn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
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
            .addGap(0, 951, Short.MAX_VALUE)
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

        _edgeTypeGrp.add(_singlyBtn);
        _singlyBtn.setSelected(true);
        _singlyBtn.setText("Singly");
        _singlyBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _singlyBtnActionPerformed(evt);
            }
        });

        _edgeTypeGrp.add(_doublyBtn);
        _doublyBtn.setText("Doubly");
        _doublyBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _doublyBtnActionPerformed(evt);
            }
        });

        _edgeTypeGrp.add(_undirectedBtn);
        _undirectedBtn.setText("Undirected");
        _undirectedBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _undirectedBtnActionPerformed(evt);
            }
        });

        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel2.setText("Toolbox");
        jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(_singlyBtn)
                .addGap(18, 18, 18)
                .addComponent(_doublyBtn)
                .addGap(18, 18, 18)
                .addComponent(_undirectedBtn)
                .addContainerGap(66, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(_doublyBtn)
                    .addComponent(_singlyBtn)
                    .addComponent(_undirectedBtn))
                .addContainerGap(135, Short.MAX_VALUE))
        );

        jSplitPane3.setRightComponent(jPanel1);

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        jTextField1.setText("Input string here");
        jTextField1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField1MouseClicked(evt);
            }
        });
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        _slider.setMaximum(5000);
        _slider.setValue(1000);
        _slider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                _sliderStateChanged(evt);
            }
        });

        _rewindBtn.setFont(new java.awt.Font("Bitstream Vera Sans", 0, 10)); // NOI18N
        _rewindBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/bwd.png"))); // NOI18N
        _rewindBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _rewindBtnActionPerformed(evt);
            }
        });

        _stopBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/stop.png"))); // NOI18N
        _stopBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _stopBtnActionPerformed(evt);
            }
        });

        _playPauseBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/play.png"))); // NOI18N
        _playPauseBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _playPauseBtnActionPerformed(evt);
            }
        });

        _forwardBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/fwd.png"))); // NOI18N
        _forwardBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _forwardBtnActionPerformed(evt);
            }
        });

        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setText("Simulation");
        jLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(_rewindBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(_stopBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(_playPauseBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(_forwardBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(_slider, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(48, 48, 48))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(_rewindBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(_stopBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(_playPauseBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(_forwardBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(_slider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(136, Short.MAX_VALUE))
        );

        jSplitPane3.setLeftComponent(jPanel2);

        jSplitPane2.setLeftComponent(jSplitPane3);

        jMenu3.setText("File");

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
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

        jMenuItem7.setText("Save As...");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem7);

        jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem6.setText("Close Tab");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem6);

        jMenuItem8.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem8.setText("Exit");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem8);

        jMenuBar2.add(jMenu3);

        jMenu4.setText("Edit");

        jMenuItem9.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem9.setText("Undo");
        jMenu4.add(jMenuItem9);

        jMenuItem10.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem10.setText("Redo");
        jMenu4.add(jMenuItem10);

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
            for (Node n : drawingPanel1.getDiagram().getNodes()){
                if (n.getCircle().contains(evt.getPoint())){
                    n.setEnd(!n.isEnd());
                    return;
                }
            }
            _nodesSelected = Collections.synchronizedSet(new HashSet<Node>());
            _edgesSelected = Collections.synchronizedSet(new HashSet<Edge>());
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
            _nodesSelected.add(add);
        }
        else{
            if (mod.equals("Ctrl+Button1")) {
                for (Node n : drawingPanel1.getDiagram().getNodes()){
                    if (n.getCircle().contains(evt.getPoint())){
                        if (n.isSelected()){
                            n.setSelected(false);
                            _nodesSelected.remove(n);
                        }
                        else{
                            n.setSelected(true);
                            _nodesSelected.add(n);
                        }
                    }
                }
                for (Edge e : drawingPanel1.getDiagram().getEdges()){
                    if (e.intersects(evt.getPoint().x,evt.getPoint().y)){
                        if (e.isSelected()){
                            e.setSelected(false);
                            _edgesSelected.remove(e);
                        }
                        else{
                            e.setSelected(true);
                            _edgesSelected.add(e);
                        }
                    }
                }
                drawingPanel1.repaint();
            }
            else{
                for (Node n : drawingPanel1.getDiagram().getNodes()){
                    if (n.getStartSymbol().contains(_mouseLoc) && n.isSelected()){
                        n.setStart(!n.isStart());
                        return;
                    }
                }
            	_edgesSelected = Collections.synchronizedSet(new HashSet<Edge>());
            	_nodesSelected = Collections.synchronizedSet(new HashSet<Node>());
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
                        _nodesSelected.add(n);
                        drawingPanel1.repaint();
                        return;
                    }
                }
                for (Edge e : drawingPanel1.getDiagram().getEdges()){
                    if (e.intersects(evt.getPoint().x,evt.getPoint().y)){
                        e.setSelected(true);
                        e.getTextField().setVisible(true);
                        e.getTextField().grabFocus();
                        e.getLabel().setVisible(false);
                        _edgesSelected.add(e);
                        drawingPanel1.repaint();
                        return;
                    }
                }
            }
        }
    }//GEN-LAST:event_drawingPanel1MouseClicked

    private void drawingPanel1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_drawingPanel1MouseDragged
        // TODO add your handling code here:
    	_mouseLoc = evt.getPoint();
        if (!_shift && _nodeDragged != null && drawingPanel1.contains(evt.getPoint())){
            if (this._nodesSelected.size() <= 1){
                _nodeDragged.setCenter(evt.getX()-_nodeDragged.getOffset().x, evt.getY()-_nodeDragged.getOffset().y);
                _nodeDragged.resetCircle();
            }
            else {
                for (Node n : drawingPanel1.getDiagram().getNodes()){
                    if (n.isSelected()) {
                        int difX = evt.getPoint().x - _mouseLoc.x;
                        int difY = evt.getPoint().y - _mouseLoc.y;
                        n.setCenter(n.getCenter().x + difX, n.getCenter().y + difY);
                        n.resetCircle();
                    }
                }
            }
            
        }
        else if (_edgeStart != null) {
            Node con = null;
            for (Node n : drawingPanel1.getDiagram().getNodes()) {
                if (n.getCircle().contains(_mouseLoc)) {
                    con = n;
                }
            }
            if (con != null) {
                double difX = con.getCenter().x - _edgeStart.getCenter().x;
                double difY = con.getCenter().y - _edgeStart.getCenter().y;
                double vecX = difX/Math.sqrt((difX*difX+difY*difY));
                double vecY = difY/Math.sqrt((difX*difX+difY*difY));

                Point2D.Double point_start = new Point2D.Double(_edgeStart.getCenter().x+(_edgeStart.getRadius()*vecX),_edgeStart.getCenter().y+(_edgeStart.getRadius()*vecY));
                Point2D.Double point_end = new Point2D.Double(con.getCenter().x-(con.getRadius()*vecX),con.getCenter().y-(con.getRadius()*vecY));
                if (_edgeStart == con) {
                    drawingPanel1._progressLine = Edge.getSelfLoop(_edgeStart);
                }
                else {
                    drawingPanel1._progressLine = new Line2D.Double(point_start, point_end);
                }
            }
            
            else {
                double difX = _mouseLoc.x - _edgeStart.getCenter().x;
                double difY = _mouseLoc.y - _edgeStart.getCenter().y;
                double vecX = difX/Math.sqrt((difX*difX+difY*difY));
                double vecY = difY/Math.sqrt((difX*difX+difY*difY));
                Point2D.Double point_start = new Point2D.Double(_edgeStart.getCenter().x+(_edgeStart.getRadius()*vecX),_edgeStart.getCenter().y+(_edgeStart.getRadius()*vecY));
                drawingPanel1._progressLine = new Line2D.Double(point_start, _mouseLoc);
            }
        }
        else if (_resizing != null) {
            Rectangle2D rec = _resizing.getResize();

            double dif = Math.max(evt.getPoint().x - rec.getCenterX(),evt.getPoint().y - rec.getCenterY());

            double newR = _resizing.getRadius() + dif/2;
            if (newR < Node.MIN_RADIUS) {
                return;
            }
            _resizing.setRadius(newR);
        }
        else if (_edgeDragged != null) {
        	double ax = _edgeDragged.getStartNode().getCenter().getX() - _mouseLoc.getX();
        	double ay = _edgeDragged.getStartNode().getCenter().getY() - _mouseLoc.getY();
        	double bx = _edgeDragged.getEndNode().getCenter().getX() - _mouseLoc.getX();
        	double by = _edgeDragged.getEndNode().getCenter().getY() - _mouseLoc.getY();
        	double cx = _edgeDragged.getEndNode().getCenter().getX() - _edgeDragged.getStartNode().getCenter().getX();
        	double cy = _edgeDragged.getEndNode().getCenter().getY() - _edgeDragged.getStartNode().getCenter().getY();
        	
        	double da = Math.sqrt(ax * ax + ay * ay);
        	double db = Math.sqrt(bx * bx + by * by);
        	double dc = Math.sqrt(cx * cx + cy * cy);
        	
        	double crossAB = ax * by - ay * bx;
        	// positive if mouse is on the left of vector c, negative if mouse is on the right of vector c
        	
        	if(Math.abs(crossAB) < 0.0000001) {
        		_edgeDragged.setHeight(0);
        	}
        	else {
            	double r = (da * db * dc) / (Math.abs(crossAB) * 2);
            	double h = Math.sqrt(r*r - (dc/2)*(dc/2));
            	
            	double lambda = da * da + db * db - dc * dc;
            	
            	_edgeDragged.setTurn(crossAB > 0);
            	if(crossAB * lambda > 0)
            		_edgeDragged.setHeight(h);
            	else 
            		_edgeDragged.setHeight(-h);
        	}
        }
        drawingPanel1.repaint();
        
    }//GEN-LAST:event_drawingPanel1MouseDragged

    private void drawingPanel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_drawingPanel1MousePressed
        // TODO add your handling code here:
        String mod = MouseEvent.getMouseModifiersText(evt.getModifiers());
        if (mod.contains("Shift")){
            _shift = true;
        }
        drawingPanel1.grabFocus();
        _edgeDragged = null;
        _nodeDragged = null;
        _resizing = null;
        for (Node n : drawingPanel1.getDiagram().getNodes()) {
            //n.getTextField().select(0, 0);
            if (n.isSelected()) {
                if (n.getResize().contains(evt.getPoint())) {
                    _resizing = n;
                    return;
                }
            }
            if (n.getCircle().contains(evt.getPoint())) {
                _nodeDragged = n;
                n.setOffset(evt.getX() - n.getCenter().x, evt.getY() - n.getCenter().y);
                return;
            }
        }
        for (Edge e : drawingPanel1.getDiagram().getEdges()) {
        	if (e.intersects(evt.getPoint().x,evt.getPoint().y)) {
        		_edgeDragged = e;
        		return;
        	}
        }
    }//GEN-LAST:event_drawingPanel1MousePressed

    private void drawingPanel1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_drawingPanel1KeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_DELETE || evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            for (Node n : _nodesSelected){
                for (Edge e : n.getConnected()){
                    drawingPanel1.getDiagram().getEdges().remove(e);
                    _edgesSelected.remove(e);
                    drawingPanel1.remove(e.getLabel());
                    drawingPanel1.remove(e.getTextField());
                }
                drawingPanel1.remove(n.getLabel());
                drawingPanel1.remove(n.getTextField());
                drawingPanel1.getDiagram().getNodes().remove(n);
            }
            for (Edge e : _edgesSelected){
            	for (Node n : drawingPanel1.getDiagram().getNodes())
            		n.removeConnected(e);
                drawingPanel1.remove(e.getLabel());
                drawingPanel1.remove(e.getTextField());
                drawingPanel1.getDiagram().getEdges().remove(e);
            }

        }
        if(drawingPanel1._progressLine == null && evt.getKeyCode() == KeyEvent.VK_SHIFT) {
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
            _edgeStart = currNode;
            Point loc = drawingPanel1.getLocationOnScreen();
            double difX = _mouseLoc.x - currNode.getCenter().x;
            double difY = _mouseLoc.y - currNode.getCenter().y;
            double vecX = difX/Math.sqrt((difX*difX+difY*difY));
            double vecY = difY/Math.sqrt((difX*difX+difY*difY));
            if (difX == 0 && difY == 0)
            	_robot.mouseMove(loc.x + (int) currNode.getCenter().x, loc.y + (int) (currNode.getCenter().y - currNode.getRadius()));
            else
            	_robot.mouseMove(loc.x + (int) (currNode.getCenter().x+(currNode.getRadius()*vecX)), loc.y + (int) (currNode.getCenter().y+(currNode.getRadius()*vecY)));
        }
        drawingPanel1.repaint();
    }//GEN-LAST:event_drawingPanel1KeyPressed
    
    private void drawingPanel1KeyReleased(java.awt.event.KeyEvent evt) {
    	if (drawingPanel1._progressLine == null && evt.getKeyCode() == KeyEvent.VK_SHIFT)
    		_edgeStart = null;
    }

    private void drawingPanel1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_drawingPanel1MouseMoved
        // TODO add your handling code here:
        _mouseLoc = evt.getPoint();
        String mod = MouseEvent.getMouseModifiersText(evt.getModifiers());
        if (mod.contains("Shift") && drawingPanel1._progressLine == null) {
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

    private void drawingPanel1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_drawingPanel1MouseReleased
        // TODO add your handling code here:
    	String mod = MouseEvent.getMouseModifiersText(evt.getModifiers());
        if (_edgeStart != null) {
        	for (Node n : drawingPanel1.getDiagram().getNodes()) {
        		if (n.getCircle().contains(_mouseLoc)) {
        			drawingPanel1.clearAll();
        			Edge newEdge = new Edge(_edgeStart,n,_edgeStart.getCenter(),_edgeStart.getCenter(),drawingPanel1,_edgeType);
        			for (Edge e : drawingPanel1.getDiagram().getEdges()){
                        e.setSelected(false);
                    }
        			_edgeStart.addConnected(newEdge);
                    n.addConnected(newEdge);
            		drawingPanel1.getDiagram().addEdge(newEdge);
                    _nodesSelected = Collections.synchronizedSet(new HashSet<Node>());
                    _edgesSelected = Collections.synchronizedSet(new HashSet<Edge>());
            		_edgesSelected.add(newEdge);
            		_edgeStart = null;
        			drawingPanel1._progressLine = null;
        			_resizing = null;
        	        _nodeDragged = null;
        	        _edgeDragged = null;
        	        drawingPanel1.repaint();
        			return;
        		}
        	}
        	if (mod.contains("Shift")) {
        		Point loc = drawingPanel1.getLocationOnScreen();
        		double difX = _mouseLoc.x - _edgeStart.getCenter().x;
        		double difY = _mouseLoc.y - _edgeStart.getCenter().y;
        		double vecX = difX/Math.sqrt((difX*difX+difY*difY));
        		double vecY = difY/Math.sqrt((difX*difX+difY*difY));
        		Point2D.Double curr = new Point2D.Double(_edgeStart.getCenter().x+(_edgeStart.getRadius()*vecX),_edgeStart.getCenter().y+(_edgeStart.getRadius()*vecY));
        		_robot.mouseMove(loc.x+(int)curr.x, loc.y+(int)curr.y);
        		drawingPanel1._progressLine = null;
        	}
        	else {
        		drawingPanel1._progressLine = null;
        		_edgeStart = null;
        	}
        }
        _resizing = null;
        _nodeDragged = null;
        _edgeDragged = null;
        drawingPanel1.repaint();
        _shift = false;
    }//GEN-LAST:event_drawingPanel1MouseReleased

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField1MouseClicked
        // TODO add your handling code here:
        if (jTextField1.getText().equals("Input string here")){
            jTextField1.setText("");
        }
    }//GEN-LAST:event_jTextField1MouseClicked

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        // TODO add your handling code here:
        exitPrompt();
        System.exit(0);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        // TODO add your handling code here:
        exitPrompt();
        int currIndex = jTabbedPane1.getSelectedIndex();
        jTabbedPane1.remove(currIndex);
        if (jTabbedPane1.getSelectedComponent() != null){
            jScrollPane1 = (JScrollPane)(jTabbedPane1.getSelectedComponent());
            drawingPanel1 = (DrawingPanel)jScrollPane1.getViewport().getView();
        }
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void _stopBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__stopBtnActionPerformed
        // TODO add your handling code here:
        _sim = null;
        _iter = null;
        _simTimer.stop();
        jTextArea1.setText("STOPPED");
        _playPauseBtn.setIcon(new ImageIcon(PLAY_FILEPATH));
        for (Node n : drawingPanel1.getDiagram().getNodes()) {
            n.setCurrent(false);
        }
        for (Edge e : drawingPanel1.getDiagram().getEdges()) {
            e.setCurrent(false);
        }
    }//GEN-LAST:event__stopBtnActionPerformed

    private void _playPauseBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__playPauseBtnActionPerformed
        // TODO add your handling code here:
        if (!_simTimer.isRunning()) {
        	_playPauseBtn.setIcon(new ImageIcon(PAUSE_FILEPATH));
            _forwardBtn.doClick();
            _simTimer.setDelay(_slider.getValue());
            _simTimer.start();
            return;
        }
        else {
        	_playPauseBtn.setIcon(new ImageIcon(PLAY_FILEPATH));
            _simTimer.stop();
        }
    }//GEN-LAST:event__playPauseBtnActionPerformed

    private void _forwardBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__forwardBtnActionPerformed
        // TODO add your handling code here:
        if (_sim == null) {
            try {
                _sim = drawingPanel1.getDiagram().deterministicSimulation(jTextField1.getText());
            } catch (InvalidDFSMException ex) {
            	_playPauseBtn.setIcon(new ImageIcon(PLAY_FILEPATH));
                jTextArea1.setText(ex.getMessage());
                if (_simTimer.isRunning()) {
                    _simTimer.stop();
                }
                return;
            }
            jTextArea1.setText("");
            _iter = _sim.listIterator();
        }
        for (Node n : drawingPanel1.getDiagram().getNodes()) {
            n.setCurrent(false);
        }
        for (Edge e : drawingPanel1.getDiagram().getEdges()) {
            e.setCurrent(false);
        }
        if (_iter.hasNext()) {
            DiagramObject e = _iter.next();
            e.setCurrent(true);
            if (jTextArea1.getText().equals("BACK TO START"))
            	jTextArea1.setText("");
            if (e instanceof Node && ((Node)e).isStart())
            	jTextArea1.append("Start ");
            jTextArea1.append(e.getName() + "\n");
            if (!_iter.hasNext()) {
            	_playPauseBtn.setIcon(new ImageIcon(PLAY_FILEPATH));
            	jTextArea1.append("FINISHED: Ended at node " + e.getName() + ".\n");
            	if (((Node)e).isEnd())
            		jTextArea1.append("FSM Accepted the input string.\n");
            	else
            		jTextArea1.append("FSM Rejected the input string.\n");
            	
            	_sim = null;
                _iter = null;
                if (_simTimer.isRunning()) {
                    _simTimer.stop();
                }
            }
        }
        drawingPanel1.repaint();
    }//GEN-LAST:event__forwardBtnActionPerformed

    private void _sliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event__sliderStateChanged
        // TODO add your handling code here:
        _simTimer.setDelay(_slider.getValue());
}//GEN-LAST:event__sliderStateChanged

    private void _doublyBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__doublyBtnActionPerformed
        // TODO add your handling code here:
        _edgeType = EdgeDirection.DOUBLE;
}//GEN-LAST:event__doublyBtnActionPerformed

    private void _undirectedBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__undirectedBtnActionPerformed
        // TODO add your handling code here:
        _edgeType = EdgeDirection.NONE;
}//GEN-LAST:event__undirectedBtnActionPerformed

    private void _rewindBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__rewindBtnActionPerformed
        // TODO add your handling code here:
        if (_simTimer.isRunning()) {
            _simTimer.stop();
        }
        if (_iter == null || _sim == null) {
            jTextArea1.setText("NOT IN SIMULATION");
            return;
        }
        for (Node n : drawingPanel1.getDiagram().getNodes()) {
            n.setCurrent(false);
        }
        for (Edge e : drawingPanel1.getDiagram().getEdges()) {
            e.setCurrent(false);
        }
        if (_iter.hasPrevious()) {
            DiagramObject e = _iter.previous();
            e.setCurrent(true);
        } else {
        	_playPauseBtn.setIcon(new ImageIcon(PLAY_FILEPATH));
            jTextArea1.setText("BACK TO START");
        }
        drawingPanel1.repaint();
}//GEN-LAST:event__rewindBtnActionPerformed

    private void _singlyBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__singlyBtnActionPerformed
        // TODO add your handling code here:
        _edgeType = EdgeDirection.SINGLE;
}//GEN-LAST:event__singlyBtnActionPerformed

    private class SimListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            _forwardBtn.doClick();
        }
    }

    private void exitPrompt(){
        //TODO fill this in to prompt for save

    }

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
    private javax.swing.JRadioButton _doublyBtn;
    private javax.swing.ButtonGroup _edgeTypeGrp;
    private javax.swing.JButton _forwardBtn;
    private javax.swing.JButton _playPauseBtn;
    private javax.swing.JButton _rewindBtn;
    private javax.swing.JRadioButton _singlyBtn;
    private javax.swing.JSlider _slider;
    private javax.swing.JButton _stopBtn;
    private javax.swing.JRadioButton _undirectedBtn;
    private frontend.DrawingPanel drawingPanel1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
