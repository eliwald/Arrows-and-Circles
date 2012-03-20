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

	public Edge(Node s, Node e) {
		_start = s;
		_end = e;
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