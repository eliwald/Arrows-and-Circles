/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frontend;

import backend.Diagram;
import backend.DiagramObject;
import backend.Edge;
import backend.EdgeDirection;
import backend.InvalidDFSMException;
import backend.Node;
import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
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
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.table.TableColumn;
import javax.swing.Timer;

/**
 *
 * @author Eddie, Sandy, Eli, and Plane
 */
public class MainFrame extends javax.swing.JFrame {

	/*
	 * Instance variables of the MainFrame. Refer to the guide below:
	 * 
	 * _edgeStart is set to be the start node of the edge currently being drawn.  When
	 * 		the mouse is moved over a node, it is set as _edgeStart, so that when shift
	 * 		is pressed and the mouse is dragged, we know where to begin the progress line.
	 * 
	 * _resizing is the currently selected resizing box on a node.  If a resizing box
	 * 		is pressed, then this variable is set, so that when the mouse is dragged,
	 * 		we know which box was pressed.
	 * 
	 * _mouseLoc is updated every time the mouse is moved, and represents the current mouse
	 * 		location on the screen.
	 * 
	 * _robot is the Robot used to move the user's mouse when "s" is pressed.
	 * 
	 * _nodesSelected is the set of currently selected nodes in the diagram.  Make sure to
	 * 		call Collections.synchronizedSet if you need to re-instantiate this, because it
	 * 		is possible that two separate methods will try to access this set at once.
	 * 
	 * _edgesSelected is the currently selected edges, and has the same note as above.
	 * 
	 * _sim is the List returned by simulating the current FSM.  Used in all the simulation
	 * 		methods down towards the bottom of this file.
	 * 
	 * _iter is the Iterator used to iterate through the _sim list.
	 * 
	 * _simTimer is the timer used in simulation to step from one step in the simulation
	 * 		to the next.
	 * 
	 * _edgeType represents the current type of edge selected, so that when we make a new
	 * 		edge, we know what type of edge to create.
	 * 
	 * _simSlide is the Slider used to iterate through the simulation.  It is the bottom
	 * 		slider, and can be dragged forward or backward to step through the simulation.
	 * 
	 * _curr NOT SURE WHAT THIS DOES; EDDIE MADE THIS.
	 * 
	 * _autoChange is used when we want to change the slider's value without having
	 * 		to go through the slider changed method; used for the second slider.  If you want
	 * 		to change the value of the second slider without stopping simulation, set this to
	 * 		true, set the value of the slider, then reset it to false.
	 * 
	 * _backwardClicked is used in the interplay between clicking forward and backward.  If you
	 * 		make alternating calls to an Iterator's previous() and next(), the same object will
	 * 		be returned every time.  Thus this variable is used so that if we want to go to the
	 * 		next object after having just gone backwards, we will call next() one extra time;
	 * 		similarly, if we want to go backwards after calling next(), we have to call previous()
	 * 		one extra time.  If this variable is FALSE, then we are currently stepping BACKWARDS;
	 * 		similarly, if it is TRUE, we are currently stepping FORWARDS.  This is used in the
	 * 		simulation_move_forward/backward methods.
	 * 
	 * _currentInputString is used in simulation to alert the user when the input string changes
	 * 		during simulation.  It gets set the first time simulation starts, and then each time
	 * 		the simulation moves forwards or backwards, it checks to make sure that whatever is
	 * 		in the text field is equal to this instance variable.  Otherwise it alerts the user
	 * 		and gives them the opportunity to restart.
	 * 
	 * _helpText is the label displayed at the bottom of the main frame to alert the user to
	 * 		keybindings.
	 * 
	 * _shiftClicked is false if shift is not currently being held, and true otherwise.  We
	 * 		need this variable because Mac OS doesn't recognize shift in Mouse Modifiers for
	 * 		some reason.  TAKING THIS OUT FOR NOW.
	 */
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
	private javax.swing.JSlider _simSlide;
	private int _curr;
	private boolean _autoChange;
	private boolean _backwardClicked;
	private String _currentInputString;
	private JLabel _helpText;

	//The file paths of image resources, and other global static variables we want to define.
	private static final String PLAY_FILEPATH = "./src/img/play.png";
	private static final String PAUSE_FILEPATH = "./src/img/pause.png";
	private static final String FWD_FILEPATH = "./src/img/fwd.png";
	private static final String BWD_FILEPATH = "./src/img/bwd.png";
	private static final String STOP_FILEPATH = "./src/img/stop.png";
	
	private static final String help_message_text = "Double Click: New Node | \"S\": Snap Mouse To Nearest Node | Ctrl-Click Component: Add Component To Selected Components";
	private static final String help_message_text_in_node_unselected = "Click To Select | Double Click: Toggle Accept State | Shift-DragClick: New Edge | Click-Drag: Move It Around";
	private static final String help_message_text_in_node_selected = "Shift-DragClick: New Edge | Double Click: Toggle Accept | Delete/Backspace: Delete Node | Click Triangle: Toggle Start State";
	private static final String help_messate_text_in_edge = "To Delete: If Text Is Selected, Hit Enter; Then Delete | Toggle Direction: Lower Left Pane | To Rename: Single Click Edge";
	
	//If we are within 3 pixels of another node, then snap to that node.
	private static int SNAP_DIFFERENCE = 7;
	
	/*
	 * These are the GUI components.
	 */
	private javax.swing.JRadioButton _doublyBtn;
	private javax.swing.ButtonGroup _edgeTypeGrp;
	private javax.swing.JButton _forwardBtn;
	private javax.swing.JButton _playPauseBtn;
	private javax.swing.JButton _rewindBtn;
	private javax.swing.JRadioButton _singlyBtn;
	private javax.swing.JSlider _speedSlider, _slider;
	private javax.swing.JButton _stopBtn;
	private javax.swing.JRadioButton _undirectedBtn;
	private frontend.DrawingPanel drawingPanel1;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JMenu jMenu3;
	private javax.swing.JMenu jMenu4;
	private javax.swing.JMenu jMenuTools;
	private javax.swing.JMenu jMenu6;
	private javax.swing.JMenu jMenuHelp;
	private javax.swing.JMenuBar jMenuBar2;
	private javax.swing.JMenuItem jMenuItem1;
	private javax.swing.JMenuItem jMenuItemRedo;
	private javax.swing.JMenuItem jMenuItem2;
	private javax.swing.JMenuItem jMenuItem3;
	private javax.swing.JMenuItem jMenuItem4;
	private javax.swing.JMenuItem jMenuItem5;
	private javax.swing.JMenuItem jMenuItem6;
	private javax.swing.JMenuItem jMenuItem7;
	private javax.swing.JMenuItem jMenuItem8;
	private javax.swing.JMenuItem jMenuItemUndo;
	private javax.swing.JMenuItem jMenuItemSelectAll;
	private javax.swing.JMenuItem jMenuItemShowTrans;
	private javax.swing.JMenuItem jMenuItemAbout;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JSplitPane jSplitPane2;
	private javax.swing.JSplitPane jSplitPane3;
	private javax.swing.JTabbedPane jTabbedPane1;
	private javax.swing.JTextArea jTextArea1;
	private javax.swing.JTextField jTextField1;

	/**
	 * Creates new form MainFram.  Initializes the default edge type to SINGLE, and initiates the Timer.
	 * Sets backwardClicked to false, because initially we assume we want to go forward first in simulation.
	 * STILL NOT SURE WHAT AUTOCHANGE DOES.
	 */
	public MainFrame() {
		_edgeType = EdgeDirection.SINGLE;
		_simTimer = new Timer(1000, new SimListener());
		_autoChange = false;
		_backwardClicked = false;
		_helpText = new JLabel();
		_helpText.setHorizontalAlignment(JLabel.CENTER);
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
	// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
	private void initComponents() {

		jMenuItem2 = new javax.swing.JMenuItem();
		jMenu6 = new javax.swing.JMenu();
		jMenuItem1 = new javax.swing.JMenuItem();
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
		_speedSlider = new javax.swing.JSlider();
		_simSlide = new javax.swing.JSlider();
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
		jMenuItemUndo = new javax.swing.JMenuItem();
		jMenuItemRedo = new javax.swing.JMenuItem();
		jMenuItemSelectAll = new javax.swing.JMenuItem();
		jMenuItemShowTrans = new javax.swing.JMenuItem();
		jMenuItemAbout = new javax.swing.JMenuItem();
		jMenuTools = new javax.swing.JMenu();
		jMenuHelp = new javax.swing.JMenu();

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
				.addGap(0, 1900, Short.MAX_VALUE)
		);
		drawingPanel1Layout.setVerticalGroup(
				drawingPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 1900, Short.MAX_VALUE)
		);

		jScrollPane1.setViewportView(drawingPanel1);
		jScrollPane1.getVerticalScrollBar().setUnitIncrement(16);
		for (MouseWheelListener m : jScrollPane1.getMouseWheelListeners()){
			jScrollPane1.removeMouseWheelListener(m);
		}
		drawingPanel1.addMouseWheelListener(new MouseWheelListener(){

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				// TODO Auto-generated method stub
				if (e.getWheelRotation()>0){
					//zoom out
					for (Node n : drawingPanel1.getDiagram().getNodes()){
						Point2D.Double p = getNewCenter(n.getCenter(), e.getPoint(),false);
						n.setCenter(p.x, p.y);
						n.setRadius(n.getRadius()*0.9);
						drawingPanel1.setSize((int)(drawingPanel1.getWidth()*0.9), (int)(drawingPanel1.getHeight()*0.9));
					}
				}
				else{
					//zoom in
					for (Node n : drawingPanel1.getDiagram().getNodes()){
						Point2D.Double p = getNewCenter(n.getCenter(), e.getPoint(),true);
						n.setCenter(p.x, p.y);
						n.setRadius(n.getRadius()/0.9);
						drawingPanel1.setSize((int)(drawingPanel1.getWidth()/0.9), (int)(drawingPanel1.getHeight()/0.9));
					}
				}
			}
			
			public Point2D.Double getNewCenter(Point2D.Double oldCenter, Point center, boolean in){
				double newX, newY;
				if (oldCenter.x < center.x){
					if (in){
						newX = oldCenter.x*0.9;
					}
					else{
						newX = oldCenter.x/0.9;
					}
				}
				else if (oldCenter.x > center.x){
					if (in){
						newX = oldCenter.x/0.9;
					}
					else{
						newX = oldCenter.x*0.9;
					}
				}
				else{
					newX = oldCenter.x;
				}
				if (oldCenter.y < center.y){
					if (in){
						newY = oldCenter.y*0.9;
					}
					else{
						newY = oldCenter.y/0.9;
					}
				}
				else if (oldCenter.y > center.y){
					if (in){
						newY = oldCenter.y/0.9;
					}
					else{
						newY = oldCenter.y*0.9;
					}
				}
				else{
					newY= oldCenter.y;
				}
				return new Point2D.Double(newX, newY);
			}
			
		});
		
		jTabbedPane1.addTab("Untitled", jScrollPane1);

		jSplitPane2.setRightComponent(jTabbedPane1);

		MouseMotionListener[] actions2 = jSplitPane3.getMouseMotionListeners();

		for (MouseMotionListener m : actions2){
			jSplitPane3.removeMouseMotionListener(m);
		}
		jSplitPane3.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
		jSplitPane3.setResizeWeight(0.95);

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
		jLabel2.setText("Edges");
		jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
		jLabel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(
				jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
				.addGroup(jPanel1Layout.createSequentialGroup()
						.addGap(48, 48, 48)
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
								.addContainerGap(10, Short.MAX_VALUE))
		);

		jSplitPane3.setRightComponent(jPanel1);

		jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

		jTextArea1.setColumns(30);
		jTextArea1.setEditable(false);
		jTextArea1.setRows(5);
		jScrollPane2.setViewportView(jTextArea1);

		Font newTextFieldFont = new Font(jTextField1.getFont().getName(),Font.ITALIC,jTextField1.getFont().getSize());
		jTextField1.setText("Input string here");
		jTextField1.setFont(newTextFieldFont);
		jTextField1.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				jTextField1MousePressed(evt);
			}
		});
		jTextField1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jTextField1ActionPerformed(evt);
			}
		});

		_speedSlider.setMaximum(3000);
		_speedSlider.setValue(1500);
		_speedSlider.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				_sliderStateChanged(evt);
			}
		});

		_simSlide.setMaximum(100);
		_simSlide.setValue(0);
		_simSlide.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				_simSlideStateChanged(evt);
			}
		});

		_rewindBtn.setIcon(new javax.swing.ImageIcon(BWD_FILEPATH));
		_rewindBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				_rewindBtnActionPerformed(evt);
			}
		});

		_stopBtn.setIcon(new javax.swing.ImageIcon(STOP_FILEPATH));
		_stopBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				_stopBtnActionPerformed(evt);
			}
		});

		_playPauseBtn.setIcon(new javax.swing.ImageIcon(PLAY_FILEPATH));
		_playPauseBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				_playPauseBtnActionPerformed(evt);
			}
		});

		_forwardBtn.setIcon(new javax.swing.ImageIcon(FWD_FILEPATH));
		_forwardBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				_forwardBtnActionPerformed(evt);
			}
		});

		jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
		jLabel1.setText("Simulation");
		jLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		
		JLabel simTextAreaLabel = new JLabel();
		simTextAreaLabel.setHorizontalAlignment(JLabel.CENTER);
		simTextAreaLabel.setFont(newTextFieldFont);
		simTextAreaLabel.setText("Simulation Console");
		
		JLabel minWait = new JLabel();
		minWait.setHorizontalAlignment(JLabel.CENTER);
		minWait.setFont(newTextFieldFont);
		minWait.setText("3s");
		
		JLabel maxWait = new JLabel();
		maxWait.setHorizontalAlignment(JLabel.CENTER);
		maxWait.setFont(newTextFieldFont);
		maxWait.setText("0s");
		
		JLabel simulationTimeSliderLabel = new JLabel();
		simulationTimeSliderLabel.setHorizontalAlignment(JLabel.CENTER);
		simulationTimeSliderLabel.setFont(newTextFieldFont);
		simulationTimeSliderLabel.setText("Interval Between Simulation Steps");
		
		JLabel simulationStepSlider = new JLabel();
		simulationStepSlider.setFont(newTextFieldFont);
		simulationStepSlider.setHorizontalAlignment(JLabel.CENTER);
		simulationStepSlider.setText("Simulation Slider: Scroll Through Simulation");

		javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(
				jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
				.addGroup(jPanel2Layout.createSequentialGroup()
						.addGap(10, 10, 10)
						.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
								.addGroup(jPanel2Layout.createSequentialGroup()
										.addComponent(_rewindBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(_stopBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(_playPauseBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(_forwardBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
										.addComponent(simulationTimeSliderLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGroup(jPanel2Layout.createSequentialGroup()
												.addComponent(minWait, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(_speedSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(maxWait, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
												.addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
												.addComponent(simulationStepSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(_simSlide, javax.swing.GroupLayout.PREFERRED_SIZE,163,javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(simTextAreaLabel, javax.swing.GroupLayout.PREFERRED_SIZE,300,javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
												.addGap(10, 10, 10))
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
								.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(simulationTimeSliderLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
										.addGroup(jPanel2Layout.createParallelGroup()
												.addComponent(minWait, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(_speedSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(maxWait, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
												.addComponent(simulationStepSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(_simSlide, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(simTextAreaLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
												.addContainerGap())
		);
		jSplitPane3.setLeftComponent(jPanel2);
		final BasicSplitPaneUI splitPaneUI = (BasicSplitPaneUI) jSplitPane3.getUI();
		final BasicSplitPaneUI mainSplitPaneUI = (BasicSplitPaneUI) jSplitPane2.getUI();
		
		jPanel2.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
		jPanel1.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
		
		splitPaneUI.getDivider().addMouseMotionListener(new MouseMotionListener() {
			public void mouseMoved(MouseEvent evt) {
				splitPaneUI.getDivider().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			public void mouseDragged(MouseEvent evt) {
				jSplitPane3.setDividerLocation(splitPaneUI.getDividerLocation(jSplitPane3));
			}
		});
		mainSplitPaneUI.getDivider().addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				mainSplitPaneUI.getDivider().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));	
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				jSplitPane2.setDividerLocation(mainSplitPaneUI.getDividerLocation(jSplitPane2));
			}
		});

		jSplitPane2.setLeftComponent(jSplitPane3);
		jSplitPane2.setOneTouchExpandable(true);

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

		jMenuItemUndo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
		jMenuItemUndo.setText("Undo");
		jMenu4.add(jMenuItemUndo);

		jMenuItemRedo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
		jMenuItemRedo.setText("Redo");
		jMenu4.add(jMenuItemRedo);

		jMenuItemSelectAll.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
		jMenuItemSelectAll.setText("Select All");
		jMenuItemSelectAll.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				_nodesSelected = Collections.synchronizedSet(new HashSet<Node>());
				_edgesSelected = Collections.synchronizedSet(new HashSet<Edge>());
				Diagram diagram = drawingPanel1.getDiagram();
				for (Node n : diagram.getNodes()){
					n.setSelected(true);
					_nodesSelected.add(n);
				}
				for (Edge ed : diagram.getEdges()){
					ed.setSelected(true);
					_edgesSelected.add(ed);
				}
				drawingPanel1.repaint();
			}
			
		});
		jMenu4.add(jMenuItemSelectAll);
		
//		jMenuItemShowTrans.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
		jMenuItemShowTrans.setText("Show Transitions");
		jMenuTools.add(jMenuItemShowTrans);
		jMenuItemShowTrans.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				StringBuilder builder = new StringBuilder();
				for (Node node : drawingPanel1.getDiagram().getNodes()){
					for (Edge edge : node.getConnected()){
						if (edge.getStartNode() == node && edge.getDirection() == EdgeDirection.SINGLE){
							builder.append("<"+ node.getName() + ", edge labeled: " + edge.getTextField().getText() + " -> " + edge.getEndNode().getName() +  ">\n");
						}
					}
				}
				String disp = builder.toString();
				if (disp.equals("")) disp = "There are no transitions in this FSM.";
				String[] opts = {"OK"};

				JOptionPane.showOptionDialog(jSplitPane2, disp, "Transitions", JOptionPane.DEFAULT_OPTION, 
				JOptionPane.INFORMATION_MESSAGE, null, opts, opts[0]);
			}	
		});
		
		jMenuBar2.add(jMenu4);

		jMenuTools.setText("Tools");
		
		jMenuBar2.add(jMenuTools);

		jMenuHelp.setText("Help");
		jMenuItemAbout.setText("About");
		jMenuHelp.add(jMenuItemAbout);
		jMenuItemAbout.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame aboutFrame = new JFrame("About");
				aboutFrame.setVisible(true);
				JTabbedPane tabs = new JTabbedPane();
				tabs.setPreferredSize(new Dimension(400, 200));
				aboutFrame.add(tabs);
				JEditorPane edPane1 = new JEditorPane();
				edPane1.setEditable(false);
				edPane1.setContentType("text/html");
				edPane1.setText("<html> <p><b>Key Controls</b><table> <tbody><tr><td>Ctrl-A</td>" +
						"<td>Select all</td></tr><tr> <td>Ctrl-S</td> <td>Save current tab</td> " +
						"</tr> <tr> <td>Ctrl-O</td> <td>Open tab from file</td> </tr><tr> <td>Ctrl-T</td> " +
						"<td>Open new blank tab</td>  </tr>  <tr>" +
						" <td>Ctrl-W</td> <td>Close current tab</td> </tr> <tr> <td>Ctrl-Q</td> " +
						"<td>Quit program</td>" +
						"<tr> <td>Ctrl-Z</td> <td>Undo</td>" +
						"<tr> <td>Ctrl-Y</td> <td>Redo</td> </tr></tbody></table></p>" + 
						"<p><b>Mouse Click Modifiers</b><table> <tbody><tr></tr></tbody></table></p>");
				JScrollPane controls = new JScrollPane();
				controls.setViewportView(edPane1);
				controls.setName("Key Bindings");
				edPane1.setVisible(true);
				edPane1.setSize(400,200);
				
				JEditorPane edPane2 = new JEditorPane();
				edPane2.setEditable(false);
				edPane2.setContentType("text/html");
				edPane2.setText("<html> <b>Developers</b><br> <br>Abhabongse (Plane) Janthong<br>Edward (Eddie) Grystar"+
						"<br>Elias (Eli) Wald <br>Sanford (Sandy) Student<br><br><b>For CS32 Spring 2012</b>");
				
				JScrollPane dev = new JScrollPane(edPane2);
				dev.setName("Development");
				tabs.add(dev);
				tabs.add(controls);
				aboutFrame.pack();
			}
			
		});
		
		
		jMenuBar2.add(jMenuHelp);

		setJMenuBar(jMenuBar2);

		_helpText.setText(help_message_text);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
				.addComponent(_helpText, javax.swing.GroupLayout.DEFAULT_SIZE, 980, Short.MAX_VALUE)
				.addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE)
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
				.addGroup(layout.createSequentialGroup()
						.addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
						.addGap(1)
						.addComponent(_helpText, javax.swing.GroupLayout.DEFAULT_SIZE, 10, 10)
						.addGap(2))
		);

		//add zoom slider; totally separate from rest of UI (i.e. doesn't ever move, not part of any group or layout, etc).
//        _slider = new JSlider(JSlider.VERTICAL, -3, 3, 0);
//        _slider.setPaintTicks(true);
//        _slider.setSize(25, 100);
//        _slider.setName("Zoom");
//        this.add(_slider);
//        _slider.setVisible(true);
		
		pack();
	}                   

	/**
	 * Called when a new tab is opened.  Creates a new drawing panel and pane associated with the new tab.
	 * Sets all appropriate listeners, and adds the new tab.
	 * @param evt	The ActionEvent associated with the tab menu item being clicked.
	 */
	private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
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
				.addGap(0, 1900, Short.MAX_VALUE)
		);
		drawingPanel1Layout.setVerticalGroup(
				drawingPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 1900, Short.MAX_VALUE)
		);

		jTabbedPane1.addTab("Untitled", jScrollPane1);
		jTabbedPane1.setSelectedIndex(jTabbedPane1.getTabCount() - 1);
	}

	/**
	 * This is what happens when you click save.
	 * @param evt
	 */
	private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}

	/**
	 * This is what happens when you click save as.
	 * @param evt
	 */
	private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}

	/******************************************************************************************************************
	 * MOUSE CLICKING, MOUSE DRAGGING, MOUSE PRESSED, MOUSE MOVED, MOUSE RELEASED									  *
	 ******************************************************************************************************************/

	/**
	 * The Mouse Clicked method handles functionality on mouse clicks.  Functionality handled includes: creating a new node,
	 * toggling end state of a new node (double clicks), ctrl-selecting multiple edges or nodes, selecting a start state
	 * of a node, or selecting a new node/edge.
	 * @param evt	The MouseEvent associated with the mouse being clicked.
	 */
	private void drawingPanel1MouseClicked(java.awt.event.MouseEvent evt) {
		String mod = MouseEvent.getMouseModifiersText(evt.getModifiers());
		//If there is a double click
		if (evt.getClickCount() >=2 && mod.equals("Button1")){
			//If double click inside a node, toggle end state.
			for (Node n : drawingPanel1.getDiagram().getNodes()){
				if (n.getCircle().contains(evt.getPoint())){
					n.setEnd(!n.isEnd());
					return;
				}
			}

			//Otherwise, reset all the selected nodes, and add a new node centered at the click.
			resetSelected();
			Node add = drawingPanel1.addNode(evt.getPoint());
			add.setSelected(true);
			add.getTextField().setVisible(true);
			add.getLabel().setVisible(false);
			_nodesSelected.add(add);
			_helpText.setText(help_message_text_in_node_selected);
		}
		//Otherwise, it's a single click
		else{
			//If Ctrl is clicked, we are selecting multiple nodes/edges
			if (mod.equals("Ctrl+Button1")) {
				//Iterate over all edges and nodes, toggling selection for each node and edge
				for (Node n : drawingPanel1.getDiagram().getNodes()){
					n.getTextField().setVisible(false);
					n.getLabel().setVisible(true);
					if (n.getCircle().contains(evt.getPoint())){
						if (n.isSelected()){
							n.setSelected(false);
							_nodesSelected.remove(n);
						}
						else{
							n.setSelected(true);
							_nodesSelected.add(n);
						}
						drawingPanel1.repaint();
						return; //If we find a node or edge, just return; we don't want to ctrl
						//select multiple things if nodes are stacked on top of each other.
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
						drawingPanel1.repaint();
						return;
					}
				}
			}
			// Else ctrl isn't clicked
			else{
				//If we are setting a start node, don't do anything else except toggle start icon
				for (Node n : drawingPanel1.getDiagram().getNodes()){
					if (n.getStartSymbol().contains(_mouseLoc) && n.isSelected()){
						n.setStart(!n.isStart());
						return;
					}
				}

				//Otherwise, reset all the selected nodes and edges, iterate through each node/edge to
				//change the variables which manage how they are drawn, and see if a new node/edge is selected
				resetSelected();

				for (Node n : drawingPanel1.getDiagram().getNodes()){
					if (n.getCircle().contains(evt.getPoint()) || (n.isStart() && n.getStartSymbol().contains(evt.getPoint()))){
						n.setSelected(true);
						n.getTextField().setVisible(true);
						n.getLabel().setVisible(false);
						_nodesSelected.add(n);
						drawingPanel1.repaint();
						_helpText.setText(help_message_text_in_node_selected);
						return;
					}
				}
				for (Edge e : drawingPanel1.getDiagram().getEdges()){
					Rectangle boundingBox = e.getLabel().getBounds();
					if (e.intersects(evt.getPoint().x,evt.getPoint().y)){
						e.setSelected(true);
						e.getTextField().setVisible(true);
						e.getTextField().selectAll();
						e.getTextField().grabFocus();
						e.getLabel().setVisible(false);
						_edgesSelected.add(e);
						EdgeDirection dir = e.getDirection();
						switch (dir){
							case DOUBLE:
								_doublyBtn.setSelected(true);
								_edgeType = EdgeDirection.DOUBLE;
								break;
							case SINGLE:
								_singlyBtn.setSelected(true);
								_edgeType = EdgeDirection.SINGLE;
								break;
							default:
								_undirectedBtn.setSelected(true);
								_edgeType = EdgeDirection.NONE;
							
						}
						drawingPanel1.repaint();
						return;
					}
					else if (boundingBox.contains(evt.getPoint().x, evt.getPoint().y)){
						resetSelected();
						e.getLabel().setVisible(false);
						e.getTextField().setVisible(true);
						e.getTextField().grabFocus();
						e.getTextField().selectAll();
						_edgesSelected.add(e);
						e.setSelected(true);
						return;
					}
				}
			}
		}
	}

	/**
	 * The Mouse Dragged method handles dragging the mouse.  Handles four basic functionality: dragging single or multiple nodes,
	 * drawing the progressLine for an edge in the middle of creation, resizing a node, and resizing an edge.
	 * @param evt	The MouseEvent associated with the mouse being dragged.
	 */
	private void drawingPanel1MouseDragged(java.awt.event.MouseEvent evt) {
		Point temp = _mouseLoc;
		_mouseLoc = evt.getPoint();

		//If we are in the middle of creating an edge, then handle drawing the progress edge on the screen.
		if (_edgeStart != null) {
			Node con = null;

			//Check to see if there is a close node to which we can snap the end of the progress line.
			for (Node n : drawingPanel1.getDiagram().getNodes()) {
				if (n.getCircle().contains(_mouseLoc)) {
					con = n;
				}
			}

			//If we are in the middle of a node, then snap the progress line to the closest point on that node.
			double difX, difY, vecX, vecY;
			Point2D.Double point_start, point_end = null;

			//Determine the vector based on either the mouse location, or the center of the node to which we're drawing.
			if (con != null) {
				difX = con.getCenter().x - _edgeStart.getCenter().x;
				difY = con.getCenter().y - _edgeStart.getCenter().y;
			}
			else {
				difX = _mouseLoc.x - _edgeStart.getCenter().x;
				difY = _mouseLoc.y - _edgeStart.getCenter().y;
			}

			vecX = difX/Math.sqrt((difX*difX+difY*difY));
			vecY = difY/Math.sqrt((difX*difX+difY*difY));

			//Start from the same place; if mouse is inside a node, set the end point to be the middle of that node.
			point_start = new Point2D.Double(_edgeStart.getCenter().x+(_edgeStart.getRadius()*vecX),_edgeStart.getCenter().y+(_edgeStart.getRadius()*vecY));
			if (con != null)
				point_end = new Point2D.Double(con.getCenter().x-(con.getRadius()*vecX),con.getCenter().y-(con.getRadius()*vecY));

			//Draw the lines. First case is if it's a self loop; second case is
			//if it's to a node that the mouse is in the middle of; third case
			//is if we just want to draw to a mouse point.
			if (con != null && _edgeStart == con)
				drawingPanel1._progressLine = new Arc2D.Double(con.getCenter().getX(), con.getCenter().getY(),
						con.getRadius() * 2, con.getRadius() * 2, 180, 540, Arc2D.OPEN);
			else if (con != null)
				drawingPanel1._progressLine = new Line2D.Double(point_start, point_end);
			else
				drawingPanel1._progressLine = new Line2D.Double(point_start, _mouseLoc);

		}

		//Else if _nodeDragged, which is set when someone presses down on a node (in MousePressed), is not null, then drag the appropriate nodes:
		else if (_nodeDragged != null && drawingPanel1.contains(_mouseLoc)){

			//If the node is not selected, then just move that one node.
			if (!_nodeDragged.isSelected() || _nodesSelected.size() == 1){
				double snappedX = _mouseLoc.getX() - _nodeDragged.getOffset().getX();
				double snappedY = _mouseLoc.getY() - _nodeDragged.getOffset().getY();
				for (Node n : drawingPanel1.getDiagram().getNodes()) {
					if (n != _nodeDragged) {
						if (Math.abs(_mouseLoc.getX() - _nodeDragged.getOffset().getX() - n.getCenter().getX()) <= SNAP_DIFFERENCE) {
							snappedX = n.getCenter().getX();
						}
						if (Math.abs(_mouseLoc.getY() - _nodeDragged.getOffset().getY() - n.getCenter().getY()) <= SNAP_DIFFERENCE) {
							snappedY = n.getCenter().getY();
						}
					}
				}
				_nodeDragged.setCenter(snappedX, snappedY);
				_nodeDragged.resetCircle();
			}


			//Otherwise, move all the selected nodes.
			else {
				for (Node n : _nodesSelected){
					int difX = _mouseLoc.x - temp.x;
					int difY = _mouseLoc.y - temp.y;
					n.setCenter(n.getCenter().x + difX, n.getCenter().y + difY);
					n.resetCircle();
				}
			}

		}

		//Otherwise, if we are in the middle of resizing, then correctly resize the node.
		else if (_resizing != null) {
			Rectangle2D rec = _resizing.getResize();

			double dif = Math.max(_mouseLoc.x - rec.getCenterX(),_mouseLoc.y - rec.getCenterY());

			double newR = _resizing.getRadius() + dif/2;
			if (newR < Node.MIN_RADIUS) {
				return;
			}
			_resizing.setRadius(newR);
		}

		//Otherwise, if we are in the middle of resizing an edge, then correctly resize the edge.
		else if (_edgeDragged != null) {
			
			//Set the help text of the bottom bar
			_helpText.setText(help_messate_text_in_edge);
			
			// When the start node and end node are different.
			if(_edgeDragged.getStartNode() != _edgeDragged.getEndNode()) {
			
				// Find the vector A from mouse to start node.
				double[] vectorA = {
					_edgeDragged.getStartNode().getCenter().getX() - _mouseLoc.getX(),
					_edgeDragged.getStartNode().getCenter().getY() - _mouseLoc.getY()
				};
				double vectorASize = Math.sqrt(vectorA[0] * vectorA[0] + vectorA[1] * vectorA[1]);
					
				// Find the vector B from the mouse to the end
				double[] vectorB = {
					_edgeDragged.getEndNode().getCenter().getX() - _mouseLoc.getX(),
					_edgeDragged.getEndNode().getCenter().getY() - _mouseLoc.getY()
				};
				double vectorBSize = Math.sqrt(vectorB[0] * vectorB[0] + vectorB[1] * vectorB[1]);
					
				// Find the vector C from start to end.
				double[] vectorC = {
					_edgeDragged.getEndNode().getCenter().getX() - _edgeDragged.getStartNode().getCenter().getX(),
					_edgeDragged.getEndNode().getCenter().getY() - _edgeDragged.getStartNode().getCenter().getY()
				};
				double vectorCSize = Math.sqrt(vectorC[0] * vectorC[0] + vectorC[1] * vectorC[1]);
					
				// Find the cross product of A and B
				double crossAB = vectorA[0] * vectorB[1] - vectorA[1] * vectorB[0];
				_edgeDragged.setTurn(crossAB > 0);
				
				// Find the cosine of the angle between vector A and B
				double cosTheta = (vectorASize * vectorASize + vectorBSize * vectorBSize - vectorCSize * vectorCSize) / (2 * vectorASize * vectorBSize);
				
				double height; 
				if(Math.abs(crossAB) < Double.MIN_VALUE) {
					height = -1000000.0;
				}
				else {
					double radius = (vectorASize * vectorBSize * vectorCSize) / (Math.abs(crossAB) * 2);
					height = Math.sqrt(radius * radius - (vectorCSize / 2) * (vectorCSize / 2));
				}
	
				_edgeDragged.setHeight(height * ((crossAB * cosTheta > 0) ? 1 : -1));
			}
			// The start node and the end node are the same (doing self-loop).
			else {

				double[] vectorA = {
					_mouseLoc.getX() - _edgeDragged.getStartNode().getCenter().getX(),
					_mouseLoc.getY() - _edgeDragged.getStartNode().getCenter().getY()
				};
				double vectorAAngle = Math.atan2(vectorA[1], vectorA[0]);
				_edgeDragged.setAngle(vectorAAngle + _edgeDragged.getOffset());
			}
		}

		//Repaint at the end
		drawingPanel1.repaint();
	}

	/**
	 * The Mouse Pressed method sets the appropriate helper variables used in Mouse Dragged.  If you press down
	 * on an edge, node, or resizing square, it selects the appropriate things.
	 * @param evt	The MouseEvent associated with the mouse being pressed.
	 */
	private void drawingPanel1MousePressed(java.awt.event.MouseEvent evt) {
		//De-select any text in the edges
		if (_edgesSelected != null) {
			for (Edge e : _edgesSelected)
				e.getTextField().select(0, 0);
		}

		//Set the edge, node, and resizing box currently being dragged to null.
		drawingPanel1.grabFocus();
		_edgeDragged = null;
		_nodeDragged = null;
		_resizing = null;
		
		//Check if we are trying to draw another edge
		if (MouseEvent.getMouseModifiersText(evt.getModifiers()).contains("Shift")) {
			for (Node n : drawingPanel1.getDiagram().getNodes()) {
				if (n.getCircle().contains(evt.getPoint())) {
					_edgeStart = n;
					break;
				}
			}
		}
		
		//Set the node/resizing/edge being dragged to the thing being pressed on.
		if (_edgeStart == null) {
			for (Node n : drawingPanel1.getDiagram().getNodes()) {
				if (n.getCircle().contains(evt.getPoint())) {
					_nodeDragged = n;
					n.setOffset(evt.getX() - n.getCenter().x, evt.getY() - n.getCenter().y);
					return;
				}
			}
			for (Node n : drawingPanel1.getDiagram().getNodes()) {
				if (n.isSelected() && n.getResize().contains(evt.getPoint())) {
					_resizing = n;
					return;
				}
			}
			for (Edge e : drawingPanel1.getDiagram().getEdges()) {
				if (e.intersects(evt.getPoint().x,evt.getPoint().y)) {
					_edgeDragged = e;
					double dx = evt.getX() - e.getStartNode().getCenter().getX();
					double dy = evt.getY() - e.getStartNode().getCenter().getY();
					e.setOffset(e.getAngle() - Math.atan2(dy, dx));
					return;
				}
			}
		}
	}

	/**
	 * The Mouse Moved method checks to see whether we are currently hovering over a node and
	 * whether shift is being pressed; if it is, it sets that node to _edgeStart.
	 * @param evt	The MouseEvent associated with the mouse being moved.
	 */
	private void drawingPanel1MouseMoved(java.awt.event.MouseEvent evt) {
		_mouseLoc = evt.getPoint();
		_edgeStart = null;
		boolean changed_help_text = false;
		for (Node n : drawingPanel1.getDiagram().getNodes()) {
			if (n.getCircle().contains(_mouseLoc) || (n.isStart() && n.getStartSymbol().contains(_mouseLoc))) {
				if (n.isSelected())
					_helpText.setText(help_message_text_in_node_selected);
				else
					_helpText.setText(help_message_text_in_node_unselected);
				changed_help_text = true;
			}
		}
		for (Edge e : drawingPanel1.getDiagram().getEdges()) {
			if (e.intersects(_mouseLoc.getX(), _mouseLoc.getY())) {
				//Set the help text of the bottom bar
				_helpText.setText(help_messate_text_in_edge);
				changed_help_text = true;
			}
		}
		if (!changed_help_text) 
			_helpText.setText(help_message_text);
	}

	/**
	 * When the mouse is released, if we are currently drawing an edge, we have to create the edge between two nodes.
	 * @param evt	The MouseEvent associated with the mouse being released.
	 */
	private void drawingPanel1MouseReleased(java.awt.event.MouseEvent evt) {
		//If we were drawing an edge
		if (_edgeStart != null) {
			//Find the node which the edge is ending at
			for (Node n : drawingPanel1.getDiagram().getNodes()) {
				if (n.getCircle().contains(_mouseLoc)) {
					//Reset selected edges/nodes
					resetSelected();
					
					//Create the new edge, reset all variables associated with maintaining the edge being drawn.
					Edge newEdge = new Edge(_edgeStart,n,_edgeStart.getCenter(),_edgeStart.getCenter(),drawingPanel1,_edgeType);
					newEdge.getTextField().grabFocus();
					newEdge.getLabel().setVisible(false);
					_edgeStart.addConnected(newEdge);
					n.addConnected(newEdge);
					drawingPanel1.getDiagram().addEdge(newEdge);
					_edgesSelected.add(newEdge);
					_edgeStart = null;
					drawingPanel1._progressLine = null;
					drawingPanel1.repaint();
					return;
				}
			}
			//If no end point was found, reset the line.
			drawingPanel1._progressLine = null;
			_edgeStart = null;
		}
		//If no edge is being drawn, reset all the nodes/resizing boxes/edges being dragged, upon mouse release
		_resizing = null;
		_nodeDragged = null;
		_edgeDragged = null;
		drawingPanel1.repaint();
	}

	/******************************************************************************************************************
	 * KEY PRESSED, KEY RELEASED																					  *
	 ******************************************************************************************************************/

	/**
	 * The Key Pressed method handles the pressing of delete, shift, or s.  If "delete" is pressed, it deletes
	 * all the currently selected nodes and edges (and all edges to the nodes).  If "s" is pressed, it snaps
	 * the mouse to the nearest node.  If "Shift" is pressed, it gets ready to create an edge by setting
	 * _edgeStart.
	 * @param evt	The KeyEvent associated with the key being pressed.
	 */
	private void drawingPanel1KeyPressed(java.awt.event.KeyEvent evt) {
		//If delete is pressed, delete all edges/nodes selected, and the edges connected to the selected nodes.
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
			resetSelected();
		}
		//Otherwise if "s" is pressed, snap to the nearest node.
		else if(evt.getKeyCode() == KeyEvent.VK_S && evt.getModifiers() != 2) {
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
			if (difX == 0 && difY == 0)
				_robot.mouseMove(loc.x + (int) currNode.getCenter().x, loc.y + (int) (currNode.getCenter().y - currNode.getRadius()));
			else
				_robot.mouseMove(loc.x + (int) (currNode.getCenter().x+(currNode.getRadius()*vecX)), loc.y + (int) (currNode.getCenter().y+(currNode.getRadius()*vecY)));
		}
		drawingPanel1.repaint();
	}

	/**
	 * The Key Released method handles resetting the _edgeStart variable to null if
	 * shift is released.
	 * @param evt	The KeyEvent associated with the key being released.
	 */
	private void drawingPanel1KeyReleased(java.awt.event.KeyEvent evt) {
		if (evt.getKeyCode() == KeyEvent.VK_SHIFT) {
			if (drawingPanel1._progressLine == null)
				_edgeStart = null;
		}
	}

	/******************************************************************************************************************
	 * EXIT MENU, SIMULATION TEXT FIELD																				  *
	 ******************************************************************************************************************/
	
	/**
	 * Gets called when an action is performed on the simulation text
	 * field.  Nothing needs to happen; I think we can delete this.
	 */
	private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {
	}
	
	/**
	 * This gets called when the mouse is pressed inside the simulation
	 * text field.  Sets the text to be empty if the text is the
	 * default "Input string here".
	 */
	private void jTextField1MousePressed(java.awt.event.MouseEvent evt) {
		if (jTextField1.getText().equals("Input string here")){
			jTextField1.setText("");
		}
		Font newTextFieldFont = new Font(jTextField1.getFont().getName(),Font.PLAIN,jTextField1.getFont().getSize());
		jTextField1.setFont(newTextFieldFont);
	}
	
	/**
	 * This gets called when the user hits ctrl-Q to quit the program.
	 * @param evt
	 */
	private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {
		exitPrompt();
		System.exit(0);
	}
	
	/**
	 * This gets called when the close tab action is performed.
	 * @param evt
	 */
	private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {
		//TODO: Add to this if-statement the condition that checks whether or
		//not the currently selected index has already been saved
		if (jTabbedPane1.getSelectedIndex() != -1) {
			int answer = exitPrompt();
			if (answer == 1) {
				//TODO: Save the tab before closing it!
			}
			if (answer != 2) {
				int currIndex = jTabbedPane1.getSelectedIndex();
				jTabbedPane1.remove(currIndex);
				if (jTabbedPane1.getSelectedComponent() != null){
					jScrollPane1 = (JScrollPane)(jTabbedPane1.getSelectedComponent());
					drawingPanel1 = (DrawingPanel)jScrollPane1.getViewport().getView();
				}
			}
		}
		//If it has already been saved, then just copy above code to exit no matter what.
	}

	/******************************************************************************************************************
	 * SIMULATION METHODS: MOVE_FORWARD, MOVE_BACKWARD, STOP/PLAY/FWD/RWD CLICKED									  *
	 ******************************************************************************************************************/

	/**
	 * Helper method called when the simulation moves forward; use instead
	 * of calling "_fwdButton.doClick()".
	 */
	private void simulation_move_forward() {
		//If simulation is not currently running, try starting it up.
		if (_sim == null) {
			//If the FSM is invalid, catch the error, display the message, and return.
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
			//Else, start simulation, move the first step forward to the start node.
			jTextArea1.setText("");
			_iter = _sim.listIterator();
			_currentInputString = jTextField1.getText();
			if (!_autoChange) {
				_autoChange = true;
				_simSlide.setValue(0);
				_autoChange = false;
				_curr = 0;
			}
		}

		//After potentially starting up the simulation (or if it's already been started)
		//clear the selected objects.
		drawingPanel1.clearCurrent();

		//If we still aren't at the end of our simulation, move to the next node.
		if (_iter.hasNext()) {
			//If the input text has changed, alert the user.
			int answer = 1;
			if (!_currentInputString.equals(jTextField1.getText())) {
				String[] opts = {"No", "Yes"};
				answer = JOptionPane.showOptionDialog(this, "The input string has changed during simulation.\nIf you continue," +
						" you may get unexpected results.  Do you wish to continue anyways?", "Input Text Changed", JOptionPane.YES_NO_OPTION, 
						JOptionPane.INFORMATION_MESSAGE, null, opts, opts[0]);
				_currentInputString = jTextField1.getText();
			}
			if (answer == 1) {
				//If backward was the last one to be clicked, make an extra call to
				//next() so that we move on to the correct next node.  Set the current
				//index of the input character in the input string to select.
				if (_backwardClicked) {
					_backwardClicked = false;
					_iter.next();
				}
				int curIndex = _iter.nextIndex();

				//Get the next object, set the slider value without stopping simulation.
				DiagramObject e = _iter.next();
				e.setCurrent(true);
				if (!_autoChange) {
					_autoChange = true;
					_simSlide.setValue(_simSlide.getValue() + 100/_sim.size());
					_curr = _simSlide.getValue();
					_autoChange = false;
				}

				//Set the text area to be the appropriate text.
				if (jTextArea1.getText().equals("BACK TO START"))
					jTextArea1.setText("");
				if (jTextArea1.getText().equals(""))
					jTextArea1.setText("Start ");

				//Append the name of the next object in the simulation to the text area.
				jTextArea1.setText(jTextArea1.getText() + (e.getName() + "\n"));

				//Select the correct character in the input string, so the user knows which edge he's taking.
				jTextField1.grabFocus();
				if (curIndex < jTextField1.getText().length())
					jTextField1.select(curIndex, curIndex + 1);
				else
					jTextField1.select(0, 0);
				drawingPanel1.grabFocus();

				//If this is the last element in the list, then clean up and stop simulation.
				if (!_iter.hasNext()) {
					_playPauseBtn.setIcon(new ImageIcon(PLAY_FILEPATH));
					jTextArea1.setText(jTextArea1.getText() + ("FINISHED: Ended at " + e.getName() + ".\n"));
					if (((Node)e).isEnd())
						jTextArea1.setText(jTextArea1.getText() + ("FSM Accepted the input string.\n"));
					else
						jTextArea1.setText(jTextArea1.getText() + ("FSM Rejected the input string.\n"));

					_sim = null;
					_iter = null;
					_curr = 0;
					if (_simTimer.isRunning()) {
						_simTimer.stop();
					}
				}
			}
			else if (answer == 0) {
				_sim = null;
				_iter = null;
				_simTimer.stop();
				jTextArea1.setText("STOPPED");
				_playPauseBtn.setIcon(new ImageIcon(PLAY_FILEPATH));
				drawingPanel1.clearCurrent();
			}
		}
		//Repaint once at the end.
		drawingPanel1.repaint();
	}

	/**
	 * Helper method called when the simulation moves backward.  Use instead of calling
	 * "_rwdButton.doClick()".
	 */
	private void simulation_move_backward() {
		//If the simulation is running, stop simulation.
		if (_simTimer.isRunning()) {
			_playPauseBtn.setIcon(new ImageIcon(PLAY_FILEPATH));
			_simTimer.stop();
		}
		//If we are not in the simulation, alert the user.
		if (_iter == null || _sim == null) {
			jTextArea1.setText("NOT IN SIMULATION");
			return;
		}

		//Clear all the currently selected (PINK) nodes.
		drawingPanel1.clearCurrent();

		//If there is a previous node to go to
		if (_iter.hasPrevious()) {
			//If the input text has changed, alert the user.
			int answer = 1;
			if (!_currentInputString.equals(jTextField1.getText())) {
				String[] opts = {"No", "Yes"};
				answer = JOptionPane.showOptionDialog(this, "The input string has changed during simulation.\nIf you continue," +
						" you may get unexpected results.  Do you wish to continue anyways?", "Input Text Changed", JOptionPane.YES_NO_OPTION, 
						JOptionPane.INFORMATION_MESSAGE, null, opts, opts[0]);
				_currentInputString = jTextField1.getText();
			}
			if (answer == 1) {
				//If the last direction pressed was forwards, we need to do one extra
				//call to previous in order to actually move backwards instead of staying
				//at the same spot (which is how ListIterators work).  Set the index
				//of the input string to highlight.
				if (!_backwardClicked) {
					_backwardClicked = true;
					_iter.previous();
				}
				int curIndex = _iter.nextIndex();

				//Get the previous node.  Set it to be current.
				DiagramObject e = _iter.previous();
				e.setCurrent(true);

				//Set the appropriate place of the second slider
				if (!_autoChange) {
					_autoChange = true;
					_simSlide.setValue(_simSlide.getValue() - 100/_sim.size());
					_autoChange = false;
				}

				//Set this old value of the slider so that we can remember the previous place.
				_curr = _simSlide.getValue();

				//Select the correct character in the input string, so the user knows which edge he's taking.
				jTextField1.grabFocus();
				if (curIndex - 1 >= 0)
					jTextField1.select(curIndex - 1, curIndex);
				drawingPanel1.grabFocus();

				//Take the last line of the text area off (remove the last node alert).
				String temp = jTextArea1.getText();
				jTextArea1.setText("");
				String[] tempArray = temp.split("\n");
				for (int i = 0; i < tempArray.length - 1; i ++) {
					jTextArea1.setText(jTextArea1.getText() + (tempArray[i] + "\n"));
				}
			}
			else if (answer == 0) {
				_sim = null;
				_iter = null;
				_simTimer.stop();
				jTextArea1.setText("STOPPED");
				_playPauseBtn.setIcon(new ImageIcon(PLAY_FILEPATH));
				drawingPanel1.clearCurrent();
			}
		}

		//Otherwise, if there is on previous, we are back to the start.
		else {
			jTextField1.select(0, 0);
			_playPauseBtn.setIcon(new ImageIcon(PLAY_FILEPATH));
			jTextArea1.setText("BACK TO START");
			_backwardClicked = false;
		}
		drawingPanel1.repaint();
	}
	
	/**
	 * Gets called when the "Stop" button is clicked.
	 * @param evt		The ActionEvent associated with the stop.
	 */
	private void _stopBtnActionPerformed(java.awt.event.ActionEvent evt) {
		_sim = null;
		_iter = null;
		_simTimer.stop();
		jTextArea1.setText("STOPPED");
		_playPauseBtn.setIcon(new ImageIcon(PLAY_FILEPATH));
		drawingPanel1.clearCurrent();
		drawingPanel1.repaint();
	}
	
	/**
	 * Gets called when the "Play/Pause" button is clicked.
	 * @param evt		The ActionEvent associated with the play.
	 */
	private void _playPauseBtnActionPerformed(java.awt.event.ActionEvent evt) {
		if (!_simTimer.isRunning()) {
			_playPauseBtn.setIcon(new ImageIcon(PAUSE_FILEPATH));
			simulation_move_forward();
			_simTimer.start();
			return;
		}
		else {
			_playPauseBtn.setIcon(new ImageIcon(PLAY_FILEPATH));
			_simTimer.stop();
		}
	}
	
	/**
	 * Gets called when the "Fwd" button is clicked.
	 * @param evt		The ActionEvent associated with the fwd.
	 */
	private void _forwardBtnActionPerformed(java.awt.event.ActionEvent evt) {
		simulation_move_forward();
	}
	
	/**
	 * Gets called when the "Bwd" button is clicked.
	 * @param evt		The ActionEvent associated with the bwd.
	 */
	private void _rewindBtnActionPerformed(java.awt.event.ActionEvent evt) {
		simulation_move_backward();
	}

	/******************************************************************************************************************
	 * SLIDERS STATE CHANGED, TOOLBOX BUTTONS CLICKED																  *
	 ******************************************************************************************************************/
	
	/**
	 * This gets called when the slider value changes that handles scrolling
	 * quickly through simulation.  Eddie made this method, and I can't follow
	 * the logic; he'll have to comment it.
	 */
	private void _simSlideStateChanged(javax.swing.event.ChangeEvent evt) {
		if (_sim == null) {
			return;
		}
		if (_autoChange) {
			return;
		}
		int diff = _simSlide.getValue() - _curr;
		if (diff == 0 && _sim.size() == 0) {
			return;
		}
		if (_sim.size() == 0 && diff > 0) {
			_autoChange = true;
			simulation_move_forward();
			_autoChange = false;
			return;
		}
		if (_sim.size() == 0 && diff > 0) {
			_autoChange = true;
			simulation_move_backward();
			_autoChange = false;
		}
		if (Math.abs(diff) < 100/_sim.size()) {
			return;
		}
		if (diff < 0) {
			while (diff < 0) {
				_autoChange = true;
				simulation_move_backward();
				diff += 100/_sim.size();
				_autoChange = false;
			}
		}
		else if (diff > 0) {
			while (diff > 0) {
				_autoChange = true;
				simulation_move_forward();
				if (_sim == null) {
					_autoChange = false;
					return;
				}
				if (_sim.size() == 0) {
					_simSlide.setValue(0);
					_autoChange = false;
					return;
				}
				diff -= 100/_sim.size();
				_autoChange = false;
			}
		}

		_curr = _simSlide.getValue();
	}
	
	/**
	 * Gets called when the top slider that sets the time interval between
	 * simulation steps gets changed.
	 * @param evt
	 */
	private void _sliderStateChanged(javax.swing.event.ChangeEvent evt) {
		_simTimer.setDelay(_speedSlider.getMaximum() - _speedSlider.getValue());
	}
	
	/**
	 * This gets called when the Doubly sided edge button gets clicked.
	 * @param evt
	 */
	private void _doublyBtnActionPerformed(java.awt.event.ActionEvent evt) {
		for (Edge e : _edgesSelected) {
			e.setDirection(EdgeDirection.DOUBLE);
		}
		_edgeType = EdgeDirection.DOUBLE;
		drawingPanel1.repaint();
	}
	
	/**
	 * This gets called when the Undirected edge button gets clicked.
	 * @param evt
	 */
	private void _undirectedBtnActionPerformed(java.awt.event.ActionEvent evt) {
		for (Edge e : _edgesSelected) {
			e.setDirection(EdgeDirection.NONE);
		}
		_edgeType = EdgeDirection.NONE;
		drawingPanel1.repaint();
	}
	
	/**
	 * This gets called when the Singly directed edge button gets clicked.
	 * @param evt
	 */
	private void _singlyBtnActionPerformed(java.awt.event.ActionEvent evt) {
		for (Edge e : _edgesSelected) {
			e.setDirection(EdgeDirection.SINGLE);
		}
		_edgeType = EdgeDirection.SINGLE;
		drawingPanel1.repaint();
	}

	/******************************************************************************************************************
	 * HELPER FUNCTIONS AND CLASSES																					  *
	 ******************************************************************************************************************/
	
	/**
	 * Listener used for the simulation timer, so that when the timer goes off,
	 * simulation can move forwards.
	 */
	private class SimListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			simulation_move_forward();
		}
	}
	
	/**
	 * Returns 0 if the user does not want to save, but wants to close the window.
	 * Returns 1 if the user does want to save and close the window.
	 * Returns 2 if the user wants to cancel closing the window.
	 * @return		What the user wants to do.
	 */
	private int exitPrompt(){
		String[] opts = {"No", "Yes", "Cancel"};
		int answer = JOptionPane.showOptionDialog(this, "There is unsaved work! Would you like to save before closing" +
				" this tab?", "Closing Unsaved Tab", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opts, opts[1]);
		return answer;
	}
	
	/**
	 * This resets all the selected nodes/edges.
	 */
	private void resetSelected() {
		drawingPanel1.clearSelected();
		_nodesSelected = Collections.synchronizedSet(new HashSet<Node>());
		_edgesSelected = Collections.synchronizedSet(new HashSet<Edge>());
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
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

		/*
		 * Create and display the form
		 */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new MainFrame().setVisible(true);
			}
		});
	}
}

