package backend;

import java.util.Collection;
import java.util.HashSet;
import java.awt.geom.Point2D;
import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D.Double;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

public class Node implements DiagramObject, Cloneable {
	private Point2D.Double _center;
	private double _radius;
	private String _name;
    private JLabel _label;
    private JTextField _area;
	private Collection<Edge> _connected;
	private Color _color;
	private boolean _startState;
	private boolean _endState;
    private Point _offset;
    private boolean _selected;
        
    private java.awt.geom.Ellipse2D.Double _circle;

	public Node(double x, double y) {
		_center = new Point2D.Double(x, y);
		_radius = 30;
		_name = "";
		_connected = new HashSet<Edge>();
		_color = Color.BLACK;
		_startState = false;
		_endState = false;
        _offset = new Point(0,0);
        _selected = true;
        _area = new JTextField();
        _area.setVisible(true);
        _area.setEnabled(true);
        _area.setOpaque(false);
        _area.setSize((int)(1.5*_radius), (int)(_radius));
        _area.getDocument().addDocumentListener(new NodeDocListener());
        _label = new JLabel("hi");
        _label.setVisible(true);
        _label.setOpaque(true);
	}

	public void setCenter(double x, double y){
		_center.setLocation(x, y);
	}


	public Point2D.Double getCenter() {
		return _center;
	}



    public void setOffset(double x, double y){
        _offset.setLocation(x, y);
    }

    public Point getOffset(){
        return _offset;
    }

	public void setRadius(double r){
		_radius = r;

    }
	
	public double getRadius() {
		return _radius;
	}

	public void setLabel(String label) {
		_name = label;
	}

	public String getLabel() {
		return _name;
	}

	public boolean addConnected(Edge e){
		return _connected.add(e);
	}

	public boolean removeConnected(Edge e){
		return _connected.remove(e);
	}

	public Collection<Edge> getConnected() {
		return _connected;
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

    public JTextField getTextField(){
        return _area;
    }

	public Object clone() throws CloneNotSupportedException {
		Node clonedObject = (Node) super.clone();
		clonedObject._center = (Double) _center.clone();
		clonedObject._radius = _radius;
		clonedObject._name = _name;
		clonedObject._connected = new HashSet<Edge>();
		for(Edge e : _connected) {
			clonedObject._connected.add((Edge) e.clone());
		}
		clonedObject._color = (Color) super.clone();
		clonedObject._startState = _startState;
		clonedObject._endState = _endState;
		return clonedObject;
	}
        
    public Ellipse2D.Double getCircle() {
        return _circle;
    }
        
    public Ellipse2D.Double resetCircle() {
        _area.setLocation(new Point((int)(_center.x-_radius), (int)(_center.y)));
        _circle = new Ellipse2D.Double(_center.x-_radius, _center.y-_radius, _radius*2, _radius*2);
        return _circle;
            
    }

    public boolean selected() {
        return _selected;
    }

    public void setSelected(boolean b){
        _selected = b;
    }

    private class NodeDocListener implements DocumentListener{

        public void insertUpdate(DocumentEvent e) {
        }

        public void removeUpdate(DocumentEvent e) {
        }

        public void changedUpdate(DocumentEvent e) {
            try {
                _name = e.getDocument().getText(0, e.getDocument().getLength());
                _label.setText(_name);
            } catch (BadLocationException ex) {
                Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
        
}