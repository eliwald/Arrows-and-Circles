package backend;


import java.awt.*;
import java.awt.geom.Arc2D;

import javax.swing.JLabel;
import javax.swing.JTextField;

import frontend.*;

/**
 * The Edge class represents an edge drawn between two nodes on the screen.
 * @author ewald
 *
 */
public class Edge implements DiagramObject {
	
	/*
	 * _start and _end are the Start/End nodes of the edge.
	 * 
	 * _direction is the EdgeDirection (e.g. Singly directed).
	 * 
	 * _area and _label are the label and area drawn next to the edge.
	 * 
	 * _curve is the actual arc of the edge drawn on the canvas.
	 * 
	 * _height and _turn are complex variables that handle drawing the edge.
	 * 
	 * _selected is whether or not this edge is selected.
	 * 
	 * _angle is a complex variable that handles drawing self-looping edges.
	 * 
	 * _offset is used when dragging edges to get the offset from the center
	 * 		to the mouse.
	 * 
	 * _container is the containing DrawingPanel, referenced so that the edge
	 * 		can add and manipulate listeners.
	 * 
	 * _current is whether or not the edge is the current object in simulation.
	 */
	private Node _start;
	private Node _end;
	private EdgeDirection _direction;
	private JTextField _area;
    private JLabel _label;
    private Arc2D _curve;
    private double _height; // from midpoint between two centers to center of the arc
    private boolean _turn = false; // false for negative, true for positive
    private boolean _selected;
    private double _angle; // only applies for self loop
    private double _offset;
    private DrawingPanel _container;
    private boolean _current = false;
    
    //static constants used to draw the edge.
    private static final int ARROW_SIZE = 12;
    private static final int TEXTBOX_HEIGHT = 25;
    private static final int TEXTBOX_WIDTH = 40;
    private static final int TEXTBOX_OFFSET = 25;
    private static final String DEFAULT_STRING = "0";
	private static final int RADIUS_TOLERANCE = 6;
    
	/**
	 * The default constructor is used when we are not creating an edge from opening a file.
	 * @param s			Start Node
	 * @param e			End Node
	 * @param container	The containing DrawingPanel
	 * @param d			The direction of this edge.
	 */
	public Edge(Node s, Node e, DrawingPanel container, EdgeDirection d) {
		_start = s;
		_end = e;
        _container = container;
        _label = new JLabel();
        _selected = true;
        _angle = Math.PI / 4;
        _offset = 0;
		_direction = d;
		_curve = new Arc2D.Double(Arc2D.OPEN);
		_turn = true;
        setAreaAndLabel();
	}
	/**
	 * Constructor for opening a self-looping edge from a file.  Need to call setContainerAndArea after
	 * constructing with this constructor to set the container.
	 * @param start		Start Node
	 * @param end		End Node (will be equal to start)
	 * @param dir		Direction
	 * @param label		Label
	 * @param angle		Angle of the self arc
	 */
	public Edge(Node start, Node end, EdgeDirection dir, String label, double angle) {
		_start = start;
		_end = end;
		_direction = dir;
		_label = new JLabel(label);
		_angle = angle;
		_selected = false;
		_offset = 0;
		_curve = new Arc2D.Double(Arc2D.OPEN);
		_turn = true;
	}
	
	/**
	 * Constructor for opening a non self-looping edge from a file.  Need to call setContainerAndArea after
	 * constructing with this constructor to set the container.
	 * @param start		Start Node
	 * @param end		End Node (will be equal to start)
	 * @param dir		Direction
	 * @param label		Label
	 * @param angle		Angle of the self arc
	 */
	public Edge(Node start, Node end, EdgeDirection dir, String label, double arc_chord_height, int arc_side) {
		_start = start;
		_end = end;
		_direction = dir;
		_label = new JLabel(label);
		_angle = Math.PI / 4;
		_selected = false;
		_offset = 0;
		_height = arc_chord_height;
		if(arc_side > 0) _turn = true;
		else _turn = false;
	}
	
	/**
	 * This method must be called directly after constructing an edge from a file.
	 * This will set the container, area, and label of the edge.
	 */
	public void setContainerAndArea(DrawingPanel container) {
		_container = container;
		setAreaAndLabel();
	}
	/**
	 * This method is called in every constructor to set the default text area and
	 * label.
	 */
	private void setAreaAndLabel() {
        _area = new JTextField();
		_area.setBorder(null);
		
		_curve = new Arc2D.Double(Arc2D.OPEN);
		
        //added support for self loop
        if (_start == _end) 
            _height = 5;
        else 
            _height = -100000.0;
        
        this.resetArc();
		_area.setText(DEFAULT_STRING);
		_area.setVisible(true);
  		_area.setOpaque(false);
 		_area.setSize(100, 20);
		_area.setBackground(new Color(0,0,0,0));
 		_area.setSize(TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
		_area.setHorizontalAlignment(JTextField.CENTER);
		_area.selectAll();
		_area.setEditable(true);
		_area.setEnabled(true);
        _area.addKeyListener(new EnterListener(_container, _area));
		_label = new JLabel(DEFAULT_STRING);
		_area.getDocument().addDocumentListener(new HTMLParser(_label));
		_label.setVisible(true);
		_label.setOpaque(false);
		_label.setSize(100, 20);
		_label.setBackground(new Color(0,0,0,0));
		_label.setSize(TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
		_label.setHorizontalAlignment(JTextField.CENTER);
		_container.add(_label);
		_container.add(_area);
	}
	
	/**
	 * This method gets a self loop, used if this edge's start node and end node are equal.
	 * @param n		The node on which to self loop.
	 * @return		The Arc that we should draw
	 */
    public static Arc2D getSelfLoop(Node n) {
        double cx = n.getRadius() * .6;
        double cy = n.getRadius() * .6;
        double dc = Math.sqrt(cx*cx + cy*cy);

        // Obtain the radius vector and size.
        double rx = (-cy) / dc * 5 + cx;
        double ry = (cx) / dc * 5 + cy;
        double dr = Math.sqrt(rx * rx + ry * ry);

        // Obtain the center of the arc.
        double ax = n.getCenter().getX() + rx;
        double ay = n.getCenter().getY() + ry;

        Arc2D c = new Arc2D.Double();
        c.setArcByCenter(ax, ay, dr, -Math.PI/2, Math.PI/2, Arc2D.OPEN);
        c.setAngles(n.getCenter(), n.getCenter());

        return c;
    }
    
	public Object clone() throws CloneNotSupportedException {
		Edge cloned = (Edge) super.clone();
		cloned.setStartNode(getStartNode());
		cloned.setEndNode(getEndNode());
		cloned.setDirection(getDirection());
		cloned.setAngle(getAngle());
		cloned.setHeight(getHeight());
		cloned.setTurn(_turn);
		return cloned;
	}
    
    /**
     * This method returns the shape that we should draw on the screen to
     * represent the edge.
     * @return		Returns the curve which defines the arc on the screen.
     */
    public Shape resetArc() {

    	// If this is not the self-loop
    	if(_start != _end) {

        	// Obtain the half segment vector from the start to the end.
        	double[] halfSegment = {
        		(_end.getCenter().getX() - _start.getCenter().getX()) / 2,
        		(_end.getCenter().getY() - _start.getCenter().getY()) / 2,
        	};
        	double halfSegmentSize = Math.sqrt(halfSegment[0] * halfSegment[0] + halfSegment[1] * halfSegment[1]);
        	
        	// Obtain the radius vector (from center of the arc to the end)
        	double[] radius = {
        		(-halfSegment[1]) / halfSegmentSize * _height + halfSegment[0],
        		(halfSegment[0]) / halfSegmentSize * _height + halfSegment[1]
        	};
        	double radiusSize = Math.sqrt(radius[0] * radius[0] + radius[1] * radius[1]);
	        
        	// Obtain the center of the arc.
        	double[] arcCenter = {
        		_start.getCenter().getX() + radius[0],
        		_start.getCenter().getY() + radius[1]
        	};

        	// Draw the curve
        	_curve.setArcByCenter(arcCenter[0], arcCenter[1], radiusSize, -Math.PI/2, Math.PI/2, Arc2D.OPEN);
        	if(_turn)
        		_curve.setAngles(_start.getCenter(), _end.getCenter());
	        else
	        	_curve.setAngles(_end.getCenter(), _start.getCenter());
	        
	        // Find the label location
        	double[] label = {
        		(_turn ? -1 : 1) * halfSegment[1] / halfSegmentSize * (radiusSize + (_turn ? 1 : -1) * _height + TEXTBOX_OFFSET) + halfSegment[0],
        		(_turn ? 1 : -1) * halfSegment[0] / halfSegmentSize * (radiusSize + (_turn ? 1 : -1) * _height + TEXTBOX_OFFSET) + halfSegment[1]
        	};
	
	        _area.setLocation((int) (_start.getCenter().getX() + label[0]) - TEXTBOX_WIDTH / 2, 
	        		(int) (_start.getCenter().getY() + label[1]) - TEXTBOX_HEIGHT / 2);
	        _label.setLocation((int) (_start.getCenter().getX() + label[0]) - TEXTBOX_WIDTH / 2, 
	        		(int) (_start.getCenter().getY() + label[1]) - TEXTBOX_HEIGHT / 2);
	        
	        return _curve;
    	}
    	// Drawing self-loop
    	else {
    		// Obtain the center of the arc.
        	double[] arcCenter = {
        		_start.getCenter().getX() + Math.cos(_angle) * _start.getRadius() * Math.sqrt(2),
        		_start.getCenter().getY() + Math.sin(_angle) * _start.getRadius() * Math.sqrt(2)
        	};
        	
        	// Draw the curve
        	_curve.setArcByCenter(arcCenter[0], arcCenter[1], _start.getRadius(), -_angle * 180 / Math.PI - 135, 270, Arc2D.OPEN);
        	
        	// Find the label location
        	double[] label = {
        		Math.cos(_angle) * (_start.getRadius() * (Math.sqrt(2) + 1) + TEXTBOX_OFFSET),
        		Math.sin(_angle) * (_start.getRadius() * (Math.sqrt(2) + 1) + TEXTBOX_OFFSET)
        	};
	
	        _area.setLocation((int) (_start.getCenter().getX() + label[0]) - TEXTBOX_WIDTH / 2, 
	        		(int) (_start.getCenter().getY() + label[1]) - TEXTBOX_HEIGHT / 2);
	        _label.setLocation((int) (_start.getCenter().getX() + label[0]) - TEXTBOX_WIDTH / 2, 
	        		(int) (_start.getCenter().getY() + label[1]) - TEXTBOX_HEIGHT / 2);
        	
	        return _curve;
    	}
    }
    
    /**
     * The one-to-one theta function is used in drawing edges to determine
     * which quadrant the edge should be drawn in.
     * @param x		The x-coordinate in the x-y plane
     * @param y		The y-coordinate in the x-y plane
     * @return		A number between 0 and 4.
     */
    public static double theta(double x, double y) {
    	double theta = y / (Math.abs(x) + Math.abs(y));
        if(x < 0) {
        	theta = 2 - theta;
        }
        else if(y < 0) {
        	theta = theta + 4;
        }
		return theta;
    }
    
    /**
     * This method returns true if the location passed in intersects this arc, and false
     * otherwise.
     * @param x		The x-coordinate of the mouse.
     * @param y		The y-coordinate of the mouse.
     * @return		Whether or not the mouse intersects this edge
     */
    public boolean intersects(double x, double y) {
    	
    	if(_start != _end) {
        	// Obtain the half segment vector from the start to the end.
        	double[] halfSegment = {
        		(_end.getCenter().getX() - _start.getCenter().getX()) / 2,
        		(_end.getCenter().getY() - _start.getCenter().getY()) / 2,
        	};
        	double halfSegmentSize = Math.sqrt(halfSegment[0] * halfSegment[0] + halfSegment[1] * halfSegment[1]);
        	
        	// Obtain the radius vector (from center of the arc to the end)
        	double[] radius = {
        		(-halfSegment[1]) / halfSegmentSize * _height + halfSegment[0],
        		(halfSegment[0]) / halfSegmentSize * _height + halfSegment[1]
        	};
        	double radiusSize = Math.sqrt(radius[0] * radius[0] + radius[1] * radius[1]);
	        
        	// Obtain the center of the arc.
        	double[] arcCenter = {
        		_start.getCenter().getX() + radius[0],
        		_start.getCenter().getY() + radius[1]
        	};
        	
        	// Obtain the vector from the center of the arc to the mouse.
        	double[] mouse = { x - arcCenter[0], y - arcCenter[1] };
        	double mouseSize = Math.sqrt(mouse[0] * mouse[0] + mouse[1] * mouse[1]);
	        
	        // Obtain the virtual angle
	        double thetaMouse = theta(mouse[0], mouse[1]);
	        double thetaP = theta(_start.getCenter().getX() - arcCenter[0], _start.getCenter().getY() - arcCenter[1]);
	        double thetaQ = theta(_end.getCenter().getX() - arcCenter[0], _end.getCenter().getY() - arcCenter[1]);
	        if(_turn) { // needs to reverse
	        	double tmp = thetaP;
	        	thetaP = thetaQ;
	        	thetaQ = tmp;
	        }
	        
	        // Check if mouse is in the range.
	        return (Math.abs(mouseSize - radiusSize) < RADIUS_TOLERANCE && (thetaP < thetaQ && thetaP < thetaMouse && thetaMouse < thetaQ
	        		|| thetaP > thetaQ && (thetaMouse < thetaQ || thetaMouse > thetaP)));
    	}
    	else {
    		// Obtain the center of the arc.
        	double[] arcCenter = {
        		_start.getCenter().getX() + Math.cos(_angle) * _start.getRadius() * Math.sqrt(2),
        		_start.getCenter().getY() + Math.sin(_angle) * _start.getRadius() * Math.sqrt(2)
        	};

        	// Obtain the vector from the center of the arc to the mouse.
        	double[] mouse = { x - arcCenter[0], y - arcCenter[1] };
        	double mouseSize = Math.sqrt(mouse[0] * mouse[0] + mouse[1] * mouse[1]);
	        
	        // Check if mouse is in the range.
	        return (Math.abs(mouseSize - _start.getRadius()) < RADIUS_TOLERANCE);
    	}
    }
    
    /**
     * @return		The curve to draw on the screen.
     */
    public Arc2D getCurve() {
    	return _curve;
    }

    /**
     * @return		The forward arrow to draw on the screen.
     */
    public Shape getForward() {
    	
    	if(_start != _end) {
	    	// Obtain the half segment vector from the start to the end.
	    	double[] halfSegment = {
	    		(_end.getCenter().getX() - _start.getCenter().getX()) / 2,
	    		(_end.getCenter().getY() - _start.getCenter().getY()) / 2,
	    	};
	    	double halfSegmentSize = Math.sqrt(halfSegment[0] * halfSegment[0] + halfSegment[1] * halfSegment[1]);
	    	
	    	// Obtain the radius vector (from center of the arc to the end)
	    	double[] radius = {
	    		(halfSegment[1]) / halfSegmentSize * _height + halfSegment[0],
	    		(-halfSegment[0]) / halfSegmentSize * _height + halfSegment[1]
	    	};
	    	double radiusSize = Math.sqrt(radius[0] * radius[0] + radius[1] * radius[1]);
	    	
	    	// Find the angle to turn around the radius vector to the quasi-tangent vector
	    	double sinTheta = _end.getRadius() / (2 * radiusSize);
	    	double cosTheta = Math.sqrt(1 - sinTheta * sinTheta) * (_turn ? -1 : 1);
	    	
	    	// Find the doubleAngle
	    	double sinTwoTheta = 2 * sinTheta * cosTheta;
	    	double cosTwoTheta = cosTheta * cosTheta - sinTheta * sinTheta;
	    	
	    	// Obtain the quasi-tangent vector
	    	double[] quasiTangent = {
	    		cosTwoTheta * radius[0] + sinTwoTheta * radius[1],
	    		-sinTwoTheta * radius[0] + cosTwoTheta * radius[1]
	    	};
	    	
	    	// Obtain the arrow tip position.
	    	double[] arrowTip = {
	    		_end.getCenter().getX() - radius[0] + quasiTangent[0],
	    		_end.getCenter().getY() - radius[1] + quasiTangent[1] 
	    	};
	    	
	    	// Obtain the arrow base position.
	    	double[] arrowBase = {
	    		(_turn ? -1 : 1) * (quasiTangent[1]) / radiusSize * ARROW_SIZE + arrowTip[0],
	    		(_turn ? 1 : -1) * (quasiTangent[0]) / radiusSize * ARROW_SIZE + arrowTip[1]
	    	};
	    	
	    	// Obtain the arrow left base and right base.
	    	double[] arrowLeft = {
	    		arrowBase[0] + quasiTangent[0] / radiusSize * (ARROW_SIZE / 2),
	    		arrowBase[1] + quasiTangent[1] / radiusSize * (ARROW_SIZE / 2)
	    	};
	    	double[] arrowRight = {
	    		arrowBase[0] - quasiTangent[0] / radiusSize * (ARROW_SIZE / 2),
	    		arrowBase[1] - quasiTangent[1] / radiusSize * (ARROW_SIZE / 2)
	    	};
	    	
	    	// Get all x- and y- coordinates.
	    	int[] xpoints = {(int) arrowLeft[0], (int) arrowRight[0], (int) arrowTip[0]};
	    	int[] ypoints = {(int) arrowLeft[1], (int) arrowRight[1], (int) arrowTip[1]};
	    	
	    	// Draw a triangle and return
	    	Polygon arrow = new Polygon(xpoints, ypoints, 3);
	    	return arrow;
    	}
    	else {
    		double[] arrowTangent = {
        		Math.cos(_angle + Math.PI / 4),
        		Math.sin(_angle + Math.PI / 4)
    		};
    		
    		// Obtain the center of the arc.
        	double[] arrowTip = {
        		_start.getCenter().getX() + Math.cos(_angle - Math.PI / 4) * _start.getRadius(),
        		_start.getCenter().getY() + Math.sin(_angle - Math.PI / 4) * _start.getRadius()
        	};
        	
        	// Obtain the arrow base position.
	    	double[] arrowBase = {
        		_start.getCenter().getX() + Math.cos(_angle - Math.PI / 4) * (_start.getRadius() + ARROW_SIZE),
        		_start.getCenter().getY() + Math.sin(_angle - Math.PI / 4) * (_start.getRadius() + ARROW_SIZE)
	    	};
	    	
	    	// Obtain the arrow left base and right base.
	    	double[] arrowLeft = {
	    		arrowBase[0] + arrowTangent[0] * (ARROW_SIZE / 2),
	    		arrowBase[1] + arrowTangent[1] * (ARROW_SIZE / 2)
	    	};
	    	double[] arrowRight = {
	    		arrowBase[0] - arrowTangent[0] * (ARROW_SIZE / 2),
	    		arrowBase[1] - arrowTangent[1] * (ARROW_SIZE / 2)
	    	};

	    	// Get all x- and y- coordinates.
	    	int[] xpoints = {(int) arrowLeft[0], (int) arrowRight[0], (int) arrowTip[0]};
	    	int[] ypoints = {(int) arrowLeft[1], (int) arrowRight[1], (int) arrowTip[1]};
	    	
	    	// Draw a triangle and return
	    	Polygon arrow = new Polygon(xpoints, ypoints, 3);
	    	return arrow;
    	}
    }
    
    /**
     * @return		The backward arrow to draw on the screen.
     */
    public Shape getBackward() {

    	if(_start != _end) {
	    	// Obtain the half segment vector from the start to the end.
	    	double[] halfSegment = {
	    		(_end.getCenter().getX() - _start.getCenter().getX()) / 2,
	    		(_end.getCenter().getY() - _start.getCenter().getY()) / 2,
	    	};
	    	double halfSegmentSize = Math.sqrt(halfSegment[0] * halfSegment[0] + halfSegment[1] * halfSegment[1]);
	    	
	    	// Obtain the radius vector (from center of the arc to the start)
	    	double[] radius = {
	    		(halfSegment[1]) / halfSegmentSize * _height - halfSegment[0],
	    		(-halfSegment[0]) / halfSegmentSize * _height - halfSegment[1]
	    	};
	    	double radiusSize = Math.sqrt(radius[0] * radius[0] + radius[1] * radius[1]);
	    	
	    	// Find the angle to turn around the radius vector to the quasi-tangent vector
	    	double sinTheta = _start.getRadius() / (2 * radiusSize);
	    	double cosTheta = Math.sqrt(1 - sinTheta * sinTheta) * (_turn ? 1 : -1);
	    	
	    	// Find the doubleAngle
	    	double sinTwoTheta = 2 * sinTheta * cosTheta;
	    	double cosTwoTheta = cosTheta * cosTheta - sinTheta * sinTheta;
	    	
	    	// Obtain the quasi-tangent vector
	    	double[] quasiTangent = {
	    		cosTwoTheta * radius[0] + sinTwoTheta * radius[1],
	    		-sinTwoTheta * radius[0] + cosTwoTheta * radius[1]
	    	};
	    	
	    	// Obtain the arrow tip position.
	    	double[] arrowTip = {
	    		_start.getCenter().getX() - radius[0] + quasiTangent[0],
	    		_start.getCenter().getY() - radius[1] + quasiTangent[1] 
	    	};
	    	
	    	// Obtain the arrow base position.
	    	double[] arrowBase = {
	    		(_turn ? 1 : -1) * (quasiTangent[1]) / radiusSize * ARROW_SIZE + arrowTip[0],
	    		(_turn ? -1 : 1) * (quasiTangent[0]) / radiusSize * ARROW_SIZE + arrowTip[1]
	    	};
	    	
	    	// Obtain the arrow left base and right base.
	    	double[] arrowLeft = {
	    		arrowBase[0] + quasiTangent[0] / radiusSize * (ARROW_SIZE / 2),
	    		arrowBase[1] + quasiTangent[1] / radiusSize * (ARROW_SIZE / 2)
	    	};
	    	double[] arrowRight = {
	    		arrowBase[0] - quasiTangent[0] / radiusSize * (ARROW_SIZE / 2),
	    		arrowBase[1] - quasiTangent[1] / radiusSize * (ARROW_SIZE / 2)
	    	};
	    	
	    	// Get all x- and y- coordinates.
	    	int[] xpoints = {(int) arrowLeft[0], (int) arrowRight[0], (int) arrowTip[0]};
	    	int[] ypoints = {(int) arrowLeft[1], (int) arrowRight[1], (int) arrowTip[1]};
	    	
	    	// Draw a triangle and return
	    	Polygon arrow = new Polygon(xpoints, ypoints, 3);
	    	return arrow;
    	}
    	else {
    		double[] arrowTangent = {
        		Math.cos(_angle - Math.PI / 4),
        		Math.sin(_angle - Math.PI / 4)
    		};
    		
    		// Obtain the center of the arc.
        	double[] arrowTip = {
        		_start.getCenter().getX() + Math.cos(_angle + Math.PI / 4) * _start.getRadius(),
        		_start.getCenter().getY() + Math.sin(_angle + Math.PI / 4) * _start.getRadius()
        	};
        	
        	// Obtain the arrow base position.
	    	double[] arrowBase = {
        		_start.getCenter().getX() + Math.cos(_angle + Math.PI / 4) * (_start.getRadius() + ARROW_SIZE),
        		_start.getCenter().getY() + Math.sin(_angle + Math.PI / 4) * (_start.getRadius() + ARROW_SIZE)
	    	};
	    	
	    	// Obtain the arrow left base and right base.
	    	double[] arrowLeft = {
	    		arrowBase[0] + arrowTangent[0] * (ARROW_SIZE / 2),
	    		arrowBase[1] + arrowTangent[1] * (ARROW_SIZE / 2)
	    	};
	    	double[] arrowRight = {
	    		arrowBase[0] - arrowTangent[0] * (ARROW_SIZE / 2),
	    		arrowBase[1] - arrowTangent[1] * (ARROW_SIZE / 2)
	    	};

	    	// Get all x- and y- coordinates.
	    	int[] xpoints = {(int) arrowLeft[0], (int) arrowRight[0], (int) arrowTip[0]};
	    	int[] ypoints = {(int) arrowLeft[1], (int) arrowRight[1], (int) arrowTip[1]};
	    	
	    	// Draw a triangle and return
	    	Polygon arrow = new Polygon(xpoints, ypoints, 3);
	    	return arrow;
    	}
    }
    
    /**
     * @param h		The height to set for the edge.
     */
    public void setHeight(double h) {
    	_height = h;
    }
    
    /**
     * @param t		The turn to set for the edge (used in drawing).
     */
    public void setTurn(boolean t) {
    	_turn = t;
    }
    
    /**
     * @return		True if edge is selected; false otherwise.
     */
    public boolean isSelected(){
        return _selected;
    }

    /**
     * @param selected		True if the edge should be selected.
     */
    public void setSelected(boolean selected){
        _selected = selected;
    }
    
    /**
     * @return		The text field drawn next to the edge.
     */
	public JTextField getTextField(){
		return _area;
	}
	
	/**
	 * @param label		The textField to set next to the edge.
	 */
	public void setFieldText(JTextField label) {
		_area = label;
	}

	/**
	 * @return		The JLabel drawn next to the edge.
	 */
    public JLabel getLabel(){
        return _label;
    }
    
    /**
     * @param s		The text to set on the JLabel.
     */
    public void setLabel(String s){
        _label.setText(s);
    }
    
    /**
     * @return		The start node.
     */
	public Node getStartNode() {
		return _start;
	}
	
	/**
	 * @return		The end node.
	 */
	public Node getEndNode() {
		return _end;
	}
	
	/**
	 * @param d		The edge direction to set.
	 */
	public void setDirection(EdgeDirection d){
		_direction = d;
	}

	/**
	 * @return		The edge direction.
	 */
	public EdgeDirection getDirection() {
		return _direction;
	}

	/**
	 * @param val		Whether or not this is the current edge in simulation.
	 */
    public void setCurrent(boolean val) {
        _current = val;
    }
    
    /**
     * @return		Whether or not this is the current edge in simulation.
     */
    public boolean getCurrent() {
        return _current;
    }
    
    /**
     * Return the name of this edge.
     */
    public String getName() {
        return ("Edge " + getNodeString() +  ": " + _area.getText());
    }
    
    /**
     * @return		Helper to get name; returns the string of which nodes the edge goes to and from.
     */
	public String getNodeString() {
		return "(" + getStartNode().getLabel().getText() + ", " + getEndNode().getLabel().getText() + ")";
	}
	
	/**
	 * @param offset		Sets the offset of this Edge to the mouse.
	 */	
	public void setOffset(double offset) {
		_offset = offset;
	}
	
	/**
	 * @return		Returns the offset.
	 */
	public double getOffset() {
		return _offset;
	}
	
	/**
	 * @param angle		Sets the angle of this edge (for self looping).
	 */
	public void setAngle(double angle) {
		_angle = angle;
	}
	
	/**
	 * @return		The angle of the edge.
	 */
	public double getAngle() {
		return _angle;
	}
	
	/**
	 * @return		The arc-chord height.
	 */
	public double getHeight() {
		return _height;
	}
	
	public void setStartNode(Node st) {
		_start = st;
	}
	
	public void setEndNode(Node end) {
		_end = end;
	}
	
}