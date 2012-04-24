package backend;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Edge implements Cloneable, DiagramObject {
	private Node _start;
	private Node _end;
	private Point2D.Double _point_start;
	private Point2D.Double _point_end;
	private EdgeDirection _direction;
	private String _label;

	public Edge(Point2D.Double start, Point2D.Double end) {
		_label = "";
		_point_start = start;
		_point_end = end;
		_direction = EdgeDirection.SINGLE;
	}

	public Edge(Node s, Node e, Point2D.Double start, Point2D.Double end) {
		_start = s;
		_end = e;
		_label = "";
		_point_start = start;
		_point_end = end;
        this.resetLine();
		_direction = EdgeDirection.SINGLE;
	}

    public Line2D.Double resetLine() {
        double difX = _end.getCenter().x - _start.getCenter().x;
        double difY = _end.getCenter().y - _start.getCenter().y;
        double vecX = difX/Math.sqrt((difX*difX+difY*difY));
        double vecY = difY/Math.sqrt((difX*difX+difY*difY));
        _point_start = new Point2D.Double(_start.getCenter().x+(_start.getRadius()*vecX),_start.getCenter().y+(_start.getRadius()*vecY));
        _point_end = new Point2D.Double(_end.getCenter().x-(_end.getRadius()*vecX),_end.getCenter().y-(_end.getRadius()*vecY));
        Line2D.Double line = new Line2D.Double(_point_start,_point_end);
        return line;
    }

	public void setLabel(String label) {
		_label = label;
	}

	public String getLabel(){
		return _label;
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
		cloned.setLabel(getLabel());
		cloned.setStartNode((Node) getStartNode().clone());
		cloned.setEndNode((Node) getEndNode().clone());
		cloned.setStartPoint((Point2D.Double) getStartPoint().clone());
		cloned.setEndPoint((Point2D.Double) getEndPoint().clone());
		cloned.setDirection(getDirection());
		return cloned;
	}
}