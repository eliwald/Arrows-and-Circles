package backend;

import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import javax.swing.JLabel;
import javax.swing.JTextField;

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
    private boolean _selected;

	public Edge(Node s, Node e, Point2D.Double start, Point2D.Double end) {
		_start = s;
		_end = e;
		_area = new JTextField();
        _label = new JLabel();
		_point_start = start;
		_point_end = end;
        _selected = true;
		_direction = EdgeDirection.SINGLE;
		_curve = new Arc2D.Double(Arc2D.OPEN);
		_height = -100000.0;
        this.resetLine();
	}

    public Arc2D resetLine() {

    	// Obtain the length of the chord.
        double cx = (_end.getCenter().getX() - _start.getCenter().getX()) / 2;
        double cy = (_end.getCenter().getY() - _start.getCenter().getY()) / 2;
        double dc = Math.sqrt(cx*cx + cy*cy);
               
        // Obtain the height vector.
        double hx = (-cy) / dc * _height;
        double hy = (cx) / dc * _height;
        
        System.out.println(hx + " AAAAA " + hy);
        
        // Obtain the radius vector and size.
        double rx = cx + hx;
        double ry = cy + hy;
        double dr = Math.sqrt(rx * rx + ry * ry);
        
        // Obtain the center of the arc.
        double ax = _start.getCenter().getX() + rx;
        double ay = _start.getCenter().getY() + ry;
        
        // Change the curve.
        _curve.setArcByCenter(ax, ay, dr, -Math.PI/2, Math.PI/2, Arc2D.OPEN);
        _curve.setAngles(_start.getCenter(), _end.getCenter());

        return _curve;
    }

    public Arc2D getCurve(){
        return _curve;
    }
    
    public void setHeight(double h) {
    	_height = h;
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
}