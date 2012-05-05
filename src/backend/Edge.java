package backend;


import java.awt.event.KeyEvent;
import java.awt.geom.Arc2D;

import frontend.DrawingPanel;
import frontend.EnterListener;
import frontend.MyDocListener;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 * 
 * @author ewald
 *
 */
public class Edge implements Cloneable, DiagramObject {
	private Node _start;
	private Node _end;
	private Point2D.Double _point_start;
	private Point2D.Double _point_end;
	private EdgeDirection _direction;
	private JTextField _area;
    private JLabel _label;
    private Arc2D _curve;
    private double _height; // from midpoint between two centers to center of the arc
    private boolean _turn = false; // false for negative, true for positive
    private boolean _selected;
    private DrawingPanel _container;
    private boolean _current = false;
    private static final int ARROW_SIZE = 12;
    private static final int TEXTBOX_HEIGHT = 25;
    private static final int TEXTBOX_WIDTH = 40;
    private static final int TEXTBOX_OFFSET = 25;
    private static final String DEFAULT_STRING = "0";
    

	public Edge(Node s, Node e, Point2D.Double start, Point2D.Double end, DrawingPanel container, EdgeDirection d) {
		_start = s;
		_end = e;
        _container = container;
        _area = new JTextField(){@Override public void
			setBorder(Border border) {}};
        _label = new JLabel();
		_point_start = start;
		_point_end = end;
        _selected = true;
		_direction = d;
		_curve = new Arc2D.Double(Arc2D.OPEN);

        //added support for self loop
        if (s == e) {
            _height = 5;
        }
        else {
            _height = -100000.0;
        }
        _turn = true;
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
		_area.getDocument().addDocumentListener(new MyDocListener(_label));
		_label.setVisible(true);
		_label.setOpaque(false);
		_label.setSize(100, 20);
		_label.setBackground(new Color(0,0,0,0));
		_label.setSize(TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
		_label.setHorizontalAlignment(JTextField.CENTER);
		_container.add(_label);
		_container.add(_area);

	}

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

    public Shape resetArc() {

        double cx = 0;
        double cy = 0;
        
        if (_start == _end) {
            cx = _start.getRadius() * .6;
            cy = _start.getRadius() * .6;
        }

        else {// Obtain the length of the chord.
            cx = (_end.getCenter().getX() - _start.getCenter().getX()) / 2;
            cy = (_end.getCenter().getY() - _start.getCenter().getY()) / 2;
        }
        double dc = Math.sqrt(cx*cx + cy*cy);
        
        // Obtain the radius vector and size.
        double rx = (-cy) / dc * _height + cx;
        double ry = (cx) / dc * _height + cy;
        double dr = Math.sqrt(rx * rx + ry * ry);
       
        // Obtain the center of the arc.
        double ax = _start.getCenter().getX() + rx;
        double ay = _start.getCenter().getY() + ry;
        
        // Location
        double lx, ly;
        
        // Change the curve.
        _curve.setArcByCenter(ax, ay, dr, -Math.PI/2, Math.PI/2, Arc2D.OPEN);
        if(_turn) {
        	_curve.setAngles(_start.getCenter(), _end.getCenter());
        	lx = (-cy) / dc * (dr + _height + TEXTBOX_OFFSET) + cx;
        	ly = (cx) / dc * (dr + _height + TEXTBOX_OFFSET) + cy;
        }
        else {
        	_curve.setAngles(_end.getCenter(), _start.getCenter());
        	lx = (cy) / dc * (dr - _height + TEXTBOX_OFFSET) + cx;
        	ly = (-cx) / dc * (dr - _height + TEXTBOX_OFFSET) + cy;
        }

        _area.setLocation((int) (_start.getCenter().getX() + lx) - TEXTBOX_WIDTH / 2, (int) (_start.getCenter().getY() + ly) - TEXTBOX_HEIGHT / 2);
        _label.setLocation((int) (_start.getCenter().getX() + lx) - TEXTBOX_WIDTH / 2, (int) (_start.getCenter().getY() + ly) - TEXTBOX_HEIGHT / 2);
        
        return _curve;
    }
    
    private static double theta(double x, double y) {
    	double theta = y / (Math.abs(x) + Math.abs(y));
        if(x < 0) {
        	theta = 2 - theta;
        }
        else if(y < 0) {
        	theta = theta + 4;
        }
		return theta;
    }
    
    public boolean intersects(double x, double y) {
    	
    	// In case of the self loop, use the usual intersects 
        if (_start == _end) {
            return _curve.intersects(x - 4, y - 4, 8, 8);
        }

        // Obtain the half vector from the start to the end.
        double cx = (_end.getCenter().getX() - _start.getCenter().getX()) / 2;
        double cy = (_end.getCenter().getY() - _start.getCenter().getY()) / 2;
        double dc = Math.sqrt(cx*cx + cy*cy);
        
        // Obtain the radius vector and size.
        double rx = (-cy) / dc * _height + cx;
        double ry = (cx) / dc * _height + cy;
        double dr = Math.sqrt(rx * rx + ry * ry);
       
        // Obtain the center of the arc.
        double ax = _start.getCenter().getX() + rx;
        double ay = _start.getCenter().getY() + ry;
        
        // Find the distance from the center of the circle to the mouse.
        double mx = x - ax;
        double my = y - ay;
        double dm = Math.sqrt(mx*mx + my*my);
        
        // Obtain the virtual angle
        double thetaMouse = theta(mx, my);
        double thetaP = theta(_start.getCenter().getX() - ax, _start.getCenter().getY() - ay);
        double thetaQ = theta(_end.getCenter().getX() - ax, _end.getCenter().getY() - ay);
        if(_turn) { // needs to reverse
        	double tmp = thetaP;
        	thetaP = thetaQ;
        	thetaQ = tmp;
        }
        
        // Check if mouse is in the range.
        if(Math.abs(dm - dr) < 6 && (thetaP < thetaQ && thetaP < thetaMouse && thetaMouse < thetaQ || 
        		thetaP > thetaQ && (thetaMouse < thetaQ || thetaMouse > thetaP))) {
        	return true;
        }
        else {
        	return false;
        }
    }
    
    public Arc2D getCurve() {
    	return _curve;
    }

    public Shape getForward() {
    	
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
    	double sinTheta = _start.getRadius() / (2 * radiusSize);
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

    public Shape getBackward() {
    	
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
    
    public void setHeight(double h) {
    	_height = h;
    }
    
    public void setTurn(boolean t) {
    	_turn = t;
    }

    public boolean isSelected(){
        return _selected;
    }

    public void setSelected(boolean selected){
        _selected = selected;
    }

	public JTextField getTextField(){
		return _area;
	}

	public void setFieldText(JTextField label) {
		_area = label;
	}

    public JLabel getLabel(){
        return _label;
    }

    public void setLabel(String s){
        _label.setText(s);
    }

	public void setStartNode(Node start) {
		_start = start;
	}

	public Node getStartNode() {
		return _start;
	}

	public void setEndNode(Node end) {
		_end = end;
	}

	public Node getEndNode() {
		return _end;
	}

	public void setStartPoint(Point2D.Double start) {
		_point_start = start;
	}

	public Point2D.Double getStartPoint() {
		return _point_start;
	}

	public void setEndPoint(Point2D.Double end) {
		_point_end = end;
	}

	public Point2D.Double getEndPoint() {
		return _point_end;
	}

	public void setDirection(EdgeDirection d){
		_direction = d;
	}

	public EdgeDirection getDirection() {
		return _direction;
	}

	public Object clone() throws CloneNotSupportedException {
		Edge cloned = (Edge) super.clone();
		cloned.setStartNode((Node) getStartNode().clone());
		cloned.setEndNode((Node) getEndNode().clone());
		cloned.setStartPoint((Point2D.Double) getStartPoint().clone());
		cloned.setEndPoint((Point2D.Double) getEndPoint().clone());
		cloned.setDirection(getDirection());
		return cloned;
	}

    public void setCurrent(boolean val) {
        _current = val;
    }

    public boolean getCurrent() {
        return _current;
    }

    public String getName() {
        return ("Edge " + getNodeString() +  ": " + _area.getText());
    }

	public String getNodeString() {
		return "(" + getStartNode().getLabel().getText() + ", " + getEndNode().getLabel().getText() + ")";
	}

}