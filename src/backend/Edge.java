package backend;

import java.awt.geom.Point2D;

public class Edge {
	private Node _start;
	private Node _end;
	private Point2D.Double _point_start;
	private Point2D.Double _point_end;
	private double weight;
	private double curveAngle;
	private EdgeDirection _direction;
	private String _label;

	public Edge(Point2D.Double start, Point2D.Double end) {
		_label = "";
		_point_start = start;
		_point_end = end;
	}

	public Edge(Node s, Node e, Point2D.Double start, Point2D.Double end) {
		_start = s;
		_end = e;
		_label = "";
		_point_start = start;
		_point_end = end;
	}

	public void setLabel(String label) {
		_label = label;
	}

	public String getLabel(){
		return _label;
	}

	public void setStart(Node start) {
		_start = start;
	}

	public Node getStart() {
		return _start;
	}

	public void setEnd(Node end) {
		_end = end;
	}

	public Node getEnd() {
		return _end;
	}
}