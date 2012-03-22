package backend;

import java.util.Collection;
import java.util.HashSet;
import java.awt.geom.Point2D;
import java.awt.Color;
import java.awt.geom.Ellipse2D;

public class Node {
	private Point2D.Double _center;
	private double _radius;
	private String _label;
	private Collection<Edge> _connected;
	private Color _color;
	private boolean _startState;
	private boolean _endState;
        
        private java.awt.geom.Ellipse2D.Double _circle;

	public Node(double x, double y) {
		_center = new Point2D.Double(x, y);
		_radius = 10;
		_label = "";
		_connected = new HashSet<Edge>();
		_color = Color.BLACK;
		_startState = false;
		_endState = false;
	}

	public void setCenter(double x, double y){
		_center.setLocation(x, y);
	}

	public Point2D.Double getCenter() {
		return _center;
	}

	public void setRadius(double r){
		_radius = r;
	}

	public double getRadius() {
		return _radius;
	}

	public void setLabel(String label) {
		_label = label;
	}

	public String getLabel() {
		return _label;
	}

	public boolean addConnected(Edge e){
		return _connected.add(e);
	}

	public boolean removeConnected(Edge e){
		return _connected.remove(e);
	}

	public void setColor(Color c){
		_color = c;
	}

	public Color getColor() {
		return _color;
	}

	public boolean isStart(){
		return _startState;
	}

	public void setStart(boolean b){
		_startState = b;
	}

	public boolean isEnd() {
		return _endState;
	}

	public void setEnd(boolean b){
		_endState = b;
	}
        
        public Ellipse2D.Double getCircle() {
            return _circle;
        }
        
        public Ellipse2D.Double resetCircle() {
            _circle = new Ellipse2D.Double(_center.x-_radius, _center.y-_radius, _radius*2, _radius*2);
            return _circle;
            
        }
        
}