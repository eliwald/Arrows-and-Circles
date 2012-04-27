package backend;

import frontend.DrawingPanel;

import frontend.EnterListener;
import frontend.MyDocListener;
import java.util.Collection;
import java.util.HashSet;
import java.awt.geom.Point2D;
import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D.Double;
import java.awt.geom.Rectangle2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

public class Node implements DiagramObject, Cloneable {
	private Point2D.Double _center;
	private double _radius;
	private JTextField _area;
	private Collection<Edge> _connected;
	private Color _color;
	private boolean _startState;
	private boolean _endState;
    private Point _offset;
    private boolean _selected;
    private boolean _resizing;
    private boolean _current = false;
	private DrawingPanel _container;
	private JLabel _label;
    private Polygon _startSymbol;
	private java.awt.geom.Ellipse2D.Double _circle;

    public static final double MIN_RADIUS = 20;

	public Node(double x, double y, DrawingPanel container) {
		_container = container;
		_center = new Point2D.Double(x, y);
		_radius = 30;
		_connected = new HashSet<Edge>();
		_color = Color.BLACK;
		_startState = false;
		_endState = false;

		_offset = new Point(0,0);
		_selected = true;
		double hypo = 2*_radius;
		double temp = hypo*hypo;
		double dimension = Math.sqrt(temp/2);
		_area = new JTextField(){@Override public void
			setBorder(Border border) {}};
		String s = "n"+_container.getDiagram().getNodes().size();
		_area.setText(s);
		_area.setVisible(true);
		_area.setOpaque(false);
		_area.setSize((int)(dimension), 12);
		_area.setHorizontalAlignment(JTextField.CENTER);
		_area.selectAll();
		_area.setEditable(true);
		_area.setEnabled(true);
        _area.setBackground(new Color(0,0,0,0));
		_label = new JLabel(s);
		_area.getDocument().addDocumentListener(new MyDocListener(_label));
        _area.addKeyListener(new EnterListener(_container, _area));
		_label.setVisible(true);
		_label.setOpaque(false);
		_label.setSize((int)(dimension), 12);
		_label.setHorizontalAlignment(JTextField.CENTER);

		_container.add(_label);
		_container.add(_area);

        _startSymbol = new Polygon();
        _startSymbol.addPoint((int)(this.getCenter().x - this.getRadius()),(int) (this.getCenter().y));
        _startSymbol.addPoint((int)(this.getCenter().x - this.getRadius() - 20),(int) (this.getCenter().y + 10));
        _startSymbol.addPoint((int)(this.getCenter().x - this.getRadius() - 20),(int) (this.getCenter().y - 10));
    }

    public Polygon getStartSymbol(){
        return _startSymbol;
    }

    public void newStartSymbol(){
        _startSymbol = new Polygon();
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

	public JLabel getLabel(){
		return _label;
	}

	public Object clone() throws CloneNotSupportedException {
		Node clonedObject = (Node) super.clone();
		clonedObject._center = (Double) _center.clone();
		clonedObject._radius = _radius;
		clonedObject._connected = new HashSet<Edge>();
		for(Edge e : _connected) {
			clonedObject._connected.add((Edge) e.clone());
		}
		clonedObject._startState = _startState;
		clonedObject._endState = _endState;
		return clonedObject;
	}



    public Rectangle2D getResize() {
        if (_circle == null) {
            resetCircle();
        }
        return new Rectangle2D.Double(_circle.x + 2*_radius,_circle.y+2*_radius,8,8);
    }

    public boolean resizing() {
        return _resizing;
    }
    public void setResizing(boolean r) {
        _resizing = r;
    }

	public Ellipse2D.Double getCircle() {
		return _circle;
	}

	public Ellipse2D.Double resetCircle() {
		double hypo = 2*_radius;
		double temp = hypo*hypo;
		double dimension = Math.sqrt(temp/2);
		Point p = new Point((int)(_center.x-(dimension/2)), (int)(_center.y-(dimension/2)));
		_area.setSize((int)(dimension), _area.getHeight());
		_label.setSize((int)(dimension), _label.getHeight());
		_area.setLocation(new Point(p.x+2, (int)(_center.y-6)));
		_label.setLocation(new Point(p.x+1, (int)(_center.y-6)));
		_circle = new Ellipse2D.Double(_center.x-_radius, _center.y-_radius, _radius*2, _radius*2);
        _startSymbol = new Polygon();
        _startSymbol.addPoint((int)(this.getCenter().x - this.getRadius()),(int) (this.getCenter().y));
        _startSymbol.addPoint((int)(this.getCenter().x - this.getRadius() - 20),(int) (this.getCenter().y + 10));
        _startSymbol.addPoint((int)(this.getCenter().x - this.getRadius() - 20),(int) (this.getCenter().y - 10));
		return _circle;
	}

	public boolean isSelected() {
		return _selected;
	}

	public void setSelected(boolean b){
		_selected = b;
	}

    public void setCurrent(boolean b) {
        _current = b;
    }

    public boolean getCurrent() {
        return _current;
    }

    public String getName() {
        return ("Node: " + _label.getText());
    }

	

}